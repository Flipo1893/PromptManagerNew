const { test, expect } = require('@playwright/test');

const API_URL = 'http://localhost:8082/api/prompts';

test.beforeEach(async ({ page }) => {
  await page.route(API_URL, (route) => route.fulfill({ json: [] }));
  await page.goto('/', { waitUntil: 'domcontentloaded' });
});

test.describe('Header & Navigation', () => {
  test('zeigt Logo und Titel', async ({ page }) => {
    await expect(page.locator('.logo-icon')).toHaveText('PM');
    await expect(page.locator('header h1')).toHaveText('PromptManager');
  });

  test('Hamburger-Menü öffnet sich beim Klick', async ({ page }) => {
    const dropdown = page.locator('#menuDropdown');
    await expect(dropdown).not.toHaveClass(/active/);
    await page.locator('#hamburgerBtn').click();
    await expect(dropdown).toHaveClass(/active/);
  });

  test('Hamburger-Menü schließt sich beim Klick außerhalb', async ({ page }) => {
    await page.locator('#hamburgerBtn').click();
    await expect(page.locator('#menuDropdown')).toHaveClass(/active/);
    await page.locator('main').click();
    await expect(page.locator('#menuDropdown')).not.toHaveClass(/active/);
  });

  test('Hamburger-Menü schließt sich beim zweiten Klick', async ({ page }) => {
    await page.locator('#hamburgerBtn').click();
    await page.locator('#hamburgerBtn').click();
    await expect(page.locator('#menuDropdown')).not.toHaveClass(/active/);
  });

  test('alle KI-Modelle sind im Menü aufgelistet', async ({ page }) => {
    await page.locator('#hamburgerBtn').click();
    const expectedModels = ['ChatGPT', 'Claude.AI', 'DeepSeek', 'Perplexity', 'Gemini'];
    for (const model of expectedModels) {
      await expect(page.locator(`.menu-item[data-ai="${model}"]`)).toBeVisible();
    }
  });
});

test.describe('Sektions-Navigation', () => {
  test('Sektion "Neuer Prompt" ist standardmäßig aktiv', async ({ page }) => {
    await expect(page.locator('#section-create')).toHaveClass(/active/);
    await expect(page.locator('#section-list')).not.toHaveClass(/active/);
  });

  test('Klick auf "Alle" wechselt zur Listen-Sektion', async ({ page }) => {
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="all"]').click();
    await expect(page.locator('#section-list')).toHaveClass(/active/);
    await expect(page.locator('#section-create')).not.toHaveClass(/active/);
  });

  test('Klick auf KI-Modell wechselt zur Listen-Sektion', async ({ page }) => {
    await page.route(`${API_URL}/ChatGPT`, (route) => route.fulfill({ json: [] }));
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="ChatGPT"]').click();
    await expect(page.locator('#section-list')).toHaveClass(/active/);
  });

  test('Klick auf "Neuer Prompt" wechselt zurück zur Create-Sektion', async ({ page }) => {
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="all"]').click();
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-section="create"]').click();
    await expect(page.locator('#section-create')).toHaveClass(/active/);
    await expect(page.locator('#section-list')).not.toHaveClass(/active/);
  });

  test('Menü schließt sich nach Sektionswechsel', async ({ page }) => {
    await page.locator('#hamburgerBtn').click();
    await page.locator('.menu-item[data-ai="all"]').click();
    await expect(page.locator('#menuDropdown')).not.toHaveClass(/active/);
  });
});
