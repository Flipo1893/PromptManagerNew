document.addEventListener("DOMContentLoaded", () => {
  class Prompt {
    constructor(title, category, text, aiModel) {
      this.id = Date.now();
      this.title = title;
      this.category = category;
      this.text = text;
      this.aiModel = aiModel;
    }
  }

  class PromptManager {
    constructor() {
      this.prompts = JSON.parse(localStorage.getItem("prompts") || "[]");
      this.currentFilter = "all";
    }

    save() {
      localStorage.setItem("prompts", JSON.stringify(this.prompts));
    }

    add(p) {
      this.prompts.unshift(p);
      this.save();
    }

    delete(id) {
      this.prompts = this.prompts.filter(p => p.id !== id);
      this.save();
    }

    getFiltered() {
      return this.currentFilter === "all"
        ? this.prompts
        : this.prompts.filter(p => p.aiModel === this.currentFilter);
    }

    count(ai) {
      return ai === "all"
        ? this.prompts.length
        : this.prompts.filter(p => p.aiModel === ai).length;
    }
  }

  const manager = new PromptManager();

  const hamburgerBtn = document.getElementById("hamburgerBtn");
  const menuDropdown = document.getElementById("menuDropdown");
  const createSection = document.getElementById("section-create");
  const listSection = document.getElementById("section-list");
  const form = document.getElementById("promptForm");
  const promptsList = document.getElementById("promptsList");
  const filterInfo = document.getElementById("filterInfo");
  const clearFilterBtn = document.getElementById("clearFilter");

  // === Hamburger Menü ===
  hamburgerBtn.addEventListener("click", e => {
    e.stopPropagation();
    hamburgerBtn.classList.toggle("active");
    menuDropdown.classList.toggle("active");
  });

  document.addEventListener("click", e => {
    if (!hamburgerBtn.contains(e.target) && !menuDropdown.contains(e.target)) {
      hamburgerBtn.classList.remove("active");
      menuDropdown.classList.remove("active");
    }
  });

  // === Menüpunkt: Neuer Prompt ===
  document.querySelectorAll('.menu-item[data-section="create"]').forEach(item => {
    item.addEventListener("click", () => {
      listSection.classList.remove("active");
      createSection.classList.add("active");
      hamburgerBtn.classList.remove("active");
      menuDropdown.classList.remove("active");
    });
  });

  // === KI Filter ===
  document.querySelectorAll(".menu-item[data-ai]").forEach(item => {
    item.addEventListener("click", () => {
      manager.currentFilter = item.dataset.ai;
      createSection.classList.remove("active");
      listSection.classList.add("active");
      hamburgerBtn.classList.remove("active");
      menuDropdown.classList.remove("active");
      render();
    });
  });

  // === Filter entfernen ===
  clearFilterBtn.addEventListener("click", () => {
    manager.currentFilter = "all";
    render();
  });

  // === Prompt Formular ===
  form.addEventListener("submit", e => {
    e.preventDefault();
    const ai = document.getElementById("promptAI").value;
    const title = document.getElementById("promptTitle").value;
    const cat = document.getElementById("promptCategory").value;
    const text = document.getElementById("promptText").value;

    manager.add(new Prompt(title, cat, text, ai));
    form.reset();
    updateCounts();
    alert("✅ Prompt gespeichert!");
  });

  // === Rendern ===
  function render() {
    const list = manager.getFiltered();
    filterInfo.style.display = manager.currentFilter === "all" ? "none" : "flex";
    document.getElementById("filterName").textContent = manager.currentFilter;

    promptsList.innerHTML =
      list.length === 0
        ? `<p style="text-align:center;color:#64748b;">Keine Prompts gefunden.</p>`
        : list
            .map(
              p => `
        <div class="prompt-item">
          <div>
            <div class="prompt-title">${p.title}</div>
            <div class="prompt-meta">${p.aiModel} | ${p.category}</div>
            <div class="prompt-text">${p.text}</div>
          </div>
          <div class="prompt-actions">
            <button class="btn-copy" onclick="copyPrompt('${encodeURIComponent(
              p.text
            )}')">Kopieren</button>
            <button class="btn-delete" onclick="deletePrompt(${p.id})">Löschen</button>
          </div>
        </div>`
            )
            .join("");

    updateCounts();
  }

  // === Prompt löschen ===
  window.deletePrompt = function (id) {
    manager.delete(id);
    render();
  };

  // === Prompt kopieren ===
  window.copyPrompt = function (text) {
    const decoded = decodeURIComponent(text);
    navigator.clipboard.writeText(decoded);
    alert("📋 Prompttext kopiert!");
  };

  // === Zähler aktualisieren ===
  function updateCounts() {
    ["all", "ChatGPT", "Claude.AI", "DeepSeek", "Perplexity", "Gemini"].forEach(ai => {
      const el = document.getElementById(`count${ai.replace(".", "")}`);
      if (el) el.textContent = manager.count(ai);
    });
  }

  updateCounts();
  render();
  createSection.classList.add("active");
});