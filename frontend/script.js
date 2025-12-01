document.addEventListener("DOMContentLoaded", () => {
    const API_URL = "http://localhost:8081/api/prompts";

    let allPrompts = [];
    let currentFilter = "all";

    async function loadPrompts() {
        const res = await fetch(API_URL);
        allPrompts = await res.json();
        render();
        updateCounts();
    }

    async function loadPromptsByModel(model) {
        const res = await fetch(`${API_URL}/${model}`);
        allPrompts = await res.json();
        currentFilter = model;
        render();
        updateCounts();
    }

    async function savePromptBackend(prompt) {
        await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(prompt)
        });

        loadPrompts(); // reload UI
    }

    async function deletePromptBackend(id) {
        await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        loadPrompts();
    }

    // -------------------------
    // UI Elemente
    // -------------------------
    const hamburgerBtn = document.getElementById("hamburgerBtn");
    const menuDropdown = document.getElementById("menuDropdown");
    const createSection = document.getElementById("section-create");
    const listSection = document.getElementById("section-list");
    const form = document.getElementById("promptForm");
    const promptsList = document.getElementById("promptsList");
    const filterInfo = document.getElementById("filterInfo");
    const clearFilterBtn = document.getElementById("clearFilter");

    // Hamburger Menü
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

    // Menü: Neuer Prompt
    document.querySelectorAll('.menu-item[data-section="create"]').forEach(item => {
        item.addEventListener("click", () => {
            listSection.classList.remove("active");
            createSection.classList.add("active");
            hamburgerBtn.classList.remove("active");
            menuDropdown.classList.remove("active");
        });
    });

    // KI Filter
    document.querySelectorAll(".menu-item[data-ai]").forEach(item => {
        item.addEventListener("click", () => {
            const model = item.dataset.ai;
            if (model === "all") {
                currentFilter = "all";
                loadPrompts();
            } else {
                loadPromptsByModel(model);
            }

            createSection.classList.remove("active");
            listSection.classList.add("active");
            hamburgerBtn.classList.remove("active");
            menuDropdown.classList.remove("active");
        });
    });

    // Filter entfernen
    clearFilterBtn.addEventListener("click", () => {
        currentFilter = "all";
        loadPrompts();
    });

    // Prompt Formular
    form.addEventListener("submit", e => {
        e.preventDefault();

        const newPrompt = {
            title: document.getElementById("promptTitle").value,
            category: document.getElementById("promptCategory").value,
            text: document.getElementById("promptText").value,
            aiModel: document.getElementById("promptAI").value
        };

        savePromptBackend(newPrompt);
        form.reset();
        alert("✅ Prompt gespeichert!");
    });

    // Prompt löschen
    window.deletePrompt = function (id) {
        deletePromptBackend(id);
    };

    // Prompt kopieren
    window.copyPrompt = function (text) {
        const decoded = decodeURIComponent(text);
        navigator.clipboard.writeText(decoded);
        alert("📋 Prompt kopiert!");
    };

    // Rendern
    function render() {
        const list =
            currentFilter === "all"
                ? allPrompts
                : allPrompts.filter(p => p.aiModel === currentFilter);

        filterInfo.style.display = currentFilter === "all" ? "none" : "flex";
        document.getElementById("filterName").textContent = currentFilter;

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
    }

    // Counter aktualisieren
    function updateCounts() {
        ["all", "ChatGPT", "Claude.AI", "DeepSeek", "Perplexity", "Gemini"].forEach(ai => {
            const el = document.getElementById(`count${ai.replace(".", "")}`);
            if (!el) return;

            if (ai === "all") {
                el.textContent = allPrompts.length;
            } else {
                el.textContent = allPrompts.filter(p => p.aiModel === ai).length;
            }
        });
    }

    loadPrompts(); // Start
    createSection.classList.add("active");
});
