document.addEventListener("DOMContentLoaded", () => {
    const API_URL = "http://localhost:8082/api/prompts";
    let allPrompts = [];
    let currentFilter = "all";

    function showToast(msg) {
        const t = document.getElementById("toast");
        t.textContent = msg;
        t.classList.add("show");
        setTimeout(() => t.classList.remove("show"), 2200);
    }

    async function loadPrompts() {
        const res = await fetch(API_URL);
        allPrompts = await res.json();
        render();
        updateCounts();
    }

    async function loadByModel(model) {
        const res = await fetch(`${API_URL}/${model}`);
        allPrompts = await res.json();
        currentFilter = model;
        render();
        updateCounts();
    }

    async function savePrompt(prompt) {
        await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(prompt),
        });
        loadPrompts();
    }

    async function deletePromptBackend(id) {
        await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        loadPrompts();
    }

    // ── DOM refs ──
    const hamburgerBtn  = document.getElementById("hamburgerBtn");
    const menuDropdown  = document.getElementById("menuDropdown");
    const createSection = document.getElementById("section-create");
    const listSection   = document.getElementById("section-list");
    const form          = document.getElementById("promptForm");
    const promptsList   = document.getElementById("promptsList");
    const filterInfo    = document.getElementById("filterInfo");

    hamburgerBtn.addEventListener("click", (e) => {
        e.stopPropagation();
        hamburgerBtn.classList.toggle("active");
        menuDropdown.classList.toggle("active");
    });

    document.addEventListener("click", (e) => {
        if (!hamburgerBtn.contains(e.target) && !menuDropdown.contains(e.target)) {
            hamburgerBtn.classList.remove("active");
            menuDropdown.classList.remove("active");
        }
    });

    document.querySelectorAll('.menu-item[data-section="create"]').forEach((el) =>
        el.addEventListener("click", () => {
            listSection.classList.remove("active");
            createSection.classList.add("active");
            hamburgerBtn.classList.remove("active");
            menuDropdown.classList.remove("active");
        })
    );

    document.querySelectorAll(".menu-item[data-ai]").forEach((el) =>
        el.addEventListener("click", () => {
            const model = el.dataset.ai;
            if (model === "all") {
                currentFilter = "all";
                loadPrompts();
            } else {
                loadByModel(model);
            }
            createSection.classList.remove("active");
            listSection.classList.add("active");
            hamburgerBtn.classList.remove("active");
            menuDropdown.classList.remove("active");
        })
    );

    document.getElementById("clearFilter").addEventListener("click", () => {
        currentFilter = "all";
        loadPrompts();
    });

    form.addEventListener("submit", (e) => {
        e.preventDefault();
        savePrompt({
            title:    document.getElementById("promptTitle").value,
            category: document.getElementById("promptCategory").value,
            text:     document.getElementById("promptText").value,
            aiModel:  document.getElementById("promptAI").value,
        });
        form.reset();
        showToast("✓ Prompt gespeichert");
    });

    window.deletePrompt = (id) => {
        deletePromptBackend(id);
        showToast("Prompt gelöscht");
    };

    window.copyPrompt = (text) => {
        navigator.clipboard.writeText(decodeURIComponent(text));
        showToast("📋 Kopiert");
    };

    function render() {
        const list =
            currentFilter === "all"
                ? allPrompts
                : allPrompts.filter((p) => p.aiModel === currentFilter);

        filterInfo.classList.toggle("visible", currentFilter !== "all");
        document.getElementById("filterName").textContent = currentFilter;

        if (list.length === 0) {
            promptsList.innerHTML = `
        <div class="empty">
          <div class="empty-icon">◈</div>
          <p>Keine Prompts gefunden.</p>
        </div>`;
            return;
        }

        promptsList.innerHTML = list
            .map(
                (p) => `
      <div class="prompt-item">
        <div class="prompt-title">${p.title}</div>
        <div class="prompt-meta">
          <span class="tag model">${p.aiModel}</span>
          <span class="tag">${p.category}</span>
        </div>
        <div class="prompt-text">${p.text}</div>
        <div class="prompt-footer">
          <div class="score-badge"><span class="dot"></span> Score: ${p.score ?? 0}</div>
          <div class="prompt-actions">
            <button class="btn-copy" onclick="copyPrompt('${encodeURIComponent(p.text)}')">Kopieren</button>
            <button class="btn-delete" onclick="deletePrompt(${p.id})">Löschen</button>
          </div>
        </div>
      </div>`
            )
            .join("");
    }

    function updateCounts() {
        ["all", "ChatGPT", "Claude.AI", "DeepSeek", "Perplexity", "Gemini"].forEach((ai) => {
            const el = document.getElementById(`count${ai.replace(".", "")}`);
            if (!el) return;
            el.textContent =
                ai === "all"
                    ? allPrompts.length
                    : allPrompts.filter((p) => p.aiModel === ai).length;
        });
    }

    loadPrompts();
    createSection.classList.add("active");
});