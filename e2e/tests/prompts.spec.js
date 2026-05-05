const { test, expect } = require('@playwright/test');

const API_URL = 'http://localhost:8082/api/prompts';

const SAMPLE_PROMPTS = [
  { id: 1, title: 'Code Review', category: 'Coding', text: 'Bitte überprüfe diesen Code.', aiModel: 'ChatGPT', score: 8 },
  { id: 2, title: 'Blogpost Ideen', category: 'Schreiben', text: 'Generiere Blogpost-Ideen zum Thema KI.', aiModel: 'Claude.AI', score: 5 },
  { id: 3, title: 'Marktanalyse', category: 'Analyse', text: 'Analysiere den aktuellen KI-Markt.', aiModel: 'ChatGPT', score: 7 },
];

test.beforeEach(async ({ page }) => {
  await page.route(API_URL, (route) => {
    if (route.request().method() === 'GET') {
      return route.fulfill({ json: SAMPLE_PROMPTS });
    }
    if (route.request().method() === 'POST') {
      return route.fulfill({ status: 201, json: { id: 99, ...JSON.parse(route.request().postData()), score: 6 } });
    }
  });
  const [response] = await Promise.all([
    page.waitForResponse((res) => res.url().includes('/api/prompts') && res.request().method() === 'GET'),
    page.goto('/'),
  ]);
  await response.json();
});

test.describe('Prompt-Formular', () => {
  test('Formular hat alle Pflichtfelder', async ({ page }) => {
    await expect(page.locator('#promptAI')).toBeVisible();
    await expect(page.locator('#promptTitle')).toBeVisible();
    await expect(page.locator('#promptCategory')).toBeVisible();
    await expect(page.locator('#promptText')).toBeVisible();
    await expect(page.locator('button[type="submit"]')).toBeVisible();
  });

  test('KI-Modell Dropdown hat alle Optionen', async ({ page }) => {
    const select = page.locator('#promptAI');
    for (const model of ['ChatGPT', 'Claude.AI', 'DeepSeek', 'Perplexity', 'Gemini']) {
      await expect(select.locator(`option:has-text("${model}")`)).toHaveCount(1);
    }
  });

  test('Kategorie Dropdown hat alle Optionen', async ({ page }) => {
    const select = page.locator('#promptCategory');
    for (const cat of ['Schreiben', 'Coding', 'Analyse', 'Kreativ', 'Business', 'Sonstiges']) {
      await expect(select.locator(`option:has-text("${cat}")`)).toHaveCount(1);
    }
  });

  test('Formular kann ohne Pflichtfelder nicht abgesendet werden', async ({ page }) => {
    await page.locator('button[type="submit"]').click();
    await expect(page.locator('#promptAI')).toBeFocused();
  });

  test('Prompt erfolgreich speichern zeigt Toast-Nachricht', async ({ page }) => {
    await page.locator('#promptAI').selectOption('ChatGPT');
    await page.locator('#promptTitle').fill('Testprompt');
    await page.locator('#promptCategory').selectOption('Coding');
    await page.locator('#promptText').fill('Das ist ein Testprompt für Playwright.');
    await page.locator('button[type="submit"]').click();
    await expect(page.locator('#toast')).toHaveClass(/show/);
    await expect(page.locator('#toast')).toContainText('Prompt gespeichert');
  });

  test('Formular wird nach dem Speichern zurückgesetzt', async ({ page }) => {
    await page.locator('#promptAI').selectOption('Gemini');
    await page.locator('#promptTitle').fill('Wird gelöscht');
    await page.locator('#promptCategory').selectOption('Kreativ');
    await page.locator('#promptText').fill('Dieser Text soll verschwinden.');
    await page.locator('button[type="submit"]').click();
    await expect(page.locator('#promptTitle')).toHaveValue('');
    await expect(page.locator('#promptText')).toHaveValue('');
    await expect(page.locator('#promptAI')).toHaveValue('');
  });
});

test.describe('Prompt-Liste', () => {
  test.beforeEach(async ({ page }) => {
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="all"]').click();
  });

  test('alle gespeicherten Prompts werden angezeigt', async ({ page }) => {
    await expect(page.locator('.prompt-item')).toHaveCount(3);
  });

  test('Prompt-Titel wird korrekt angezeigt', async ({ page }) => {
    await expect(page.locator('.prompt-item').first().locator('.prompt-title')).toHaveText('Code Review');
  });

  test('KI-Modell-Tag wird angezeigt', async ({ page }) => {
    await expect(page.locator('.prompt-item').first().locator('.tag.model')).toHaveText('ChatGPT');
  });

  test('Kategorie-Tag wird angezeigt', async ({ page }) => {
    await expect(page.locator('.prompt-item').first().locator('.tag:not(.model)')).toHaveText('Coding');
  });

  test('Score-Badge wird angezeigt', async ({ page }) => {
    await expect(page.locator('.prompt-item').first().locator('.score-badge')).toContainText('Score: 8');
  });

  test('leere Liste zeigt Empty-State', async ({ page }) => {
    await page.route(API_URL, (route) => route.fulfill({ json: [] }));
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-section="create"]').click();
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="all"]').click();
    await expect(page.locator('.empty')).toBeVisible();
    await expect(page.locator('.empty p')).toHaveText('Keine Prompts gefunden.');
  });
});

test.describe('Prompt löschen', () => {
  test.beforeEach(async ({ page }) => {
    await page.route(`${API_URL}/1`, (route) => {
      if (route.request().method() === 'DELETE') return route.fulfill({ status: 204 });
    });
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="all"]').click();
  });

  test('Löschen-Button zeigt Toast', async ({ page }) => {
    await page.locator('.prompt-item').first().locator('.btn-delete').click();
    await expect(page.locator('#toast')).toHaveClass(/show/);
    await expect(page.locator('#toast')).toContainText('Prompt gelöscht');
  });
});

test.describe('Prompt-Filterung', () => {
  test('Zähler im Menü zeigen korrekte Anzahl', async ({ page }) => {
    await expect(page.locator('#countall')).toHaveText('3');
    await expect(page.locator('#countChatGPT')).toHaveText('2');
    await expect(page.locator('#countClaudeAI')).toHaveText('1');
    await expect(page.locator('#countDeepSeek')).toHaveText('0');
  });

  test('Filter nach ChatGPT zeigt nur ChatGPT-Prompts', async ({ page }) => {
    await page.route(`${API_URL}/ChatGPT`, (route) =>
      route.fulfill({ json: SAMPLE_PROMPTS.filter((p) => p.aiModel === 'ChatGPT') })
    );
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="ChatGPT"]').click();
    await expect(page.locator('.prompt-item')).toHaveCount(2);
    await expect(page.locator('.tag.model').first()).toHaveText('ChatGPT');
  });

  test('Filter-Info wird beim Filtern angezeigt', async ({ page }) => {
    await page.route(`${API_URL}/Claude.AI`, (route) =>
      route.fulfill({ json: SAMPLE_PROMPTS.filter((p) => p.aiModel === 'Claude.AI') })
    );
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="Claude.AI"]').click();
    await expect(page.locator('#filterInfo')).toHaveClass(/visible/);
    await expect(page.locator('#filterName')).toHaveText('Claude.AI');
  });

  test('"Filter entfernen" zeigt alle Prompts', async ({ page }) => {
    await page.route(`${API_URL}/ChatGPT`, (route) =>
      route.fulfill({ json: SAMPLE_PROMPTS.filter((p) => p.aiModel === 'ChatGPT') })
    );
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="ChatGPT"]').click();
    await expect(page.locator('.prompt-item')).toHaveCount(2);
    await page.locator('#clearFilter').click();
    await expect(page.locator('.prompt-item')).toHaveCount(3);
    await expect(page.locator('#filterInfo')).not.toHaveClass(/visible/);
  });
});

test.describe('Prompt kopieren', () => {
  test.beforeEach(async ({ page }) => {
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="all"]').click();
  });

  test('Kopieren-Button zeigt Toast', async ({ page, browserName }) => {
    if (browserName === 'chromium') {
      await page.context().grantPermissions(['clipboard-read', 'clipboard-write']);
    }
    await page.locator('.prompt-item').first().locator('.btn-copy').click();
    await expect(page.locator('#toast')).toHaveClass(/show/);
    await expect(page.locator('#toast')).toContainText('Kopiert');
  });
});
