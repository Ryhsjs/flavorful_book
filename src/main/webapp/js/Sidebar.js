class Sidebar {
    constructor() {
        this.sidebarButton = document.getElementById("sidebar-button");
        this.sidebar = document.getElementById("sidebar");
        this.searchButton = document.getElementById("search-button");
        this.mainContainer = document.getElementById("main-container");
        this.ingredients = document.getElementById("ingredients-section");
        this.ingredientsSelect = document.getElementById("ingredients-search");
        this.categories = document.getElementById("categories-section");
        this.categoriesSelect = document.getElementById("categories-search");

        this.activeTimeStart = document.getElementById("activeCookingTimeStart");
        this.activeTimeEnd = document.getElementById("activeCookingTimeEnd");
        this.totalTimeStart = document.getElementById("totalCookingTimeStart");
        this.totalTimeEnd = document.getElementById("totalCookingTimeEnd");

        this.selectedIngredients = new Set();
        this.selectedCategories = new Set();
        this.init()
    }

    init() {
        this.sidebarButton.addEventListener("click", () => {
            this.switchSideBar();
        });
        this.ingredientsSelect.addEventListener("change", (e) => {
            const selectedOption = e.target.options[e.target.selectedIndex];
            if (selectedOption.value) {
                this.addIngredient(selectedOption.value, selectedOption.textContent);
                e.target.selectedIndex = 0;
            }
        });

        this.categoriesSelect.addEventListener("change", (e) => {
            const selectedOption = e.target.options[e.target.selectedIndex];
            if (selectedOption.value) {
                this.addCategory(selectedOption.value, selectedOption.textContent);
                e.target.selectedIndex = 0;
            }
        });

        this.searchButton.addEventListener("click", () => {
            this.search();
        });
    }

    switchSideBar() {
        if (this.sidebar.classList.contains("active")) {
            this.sidebar.classList.remove("active")
            this.mainContainer.classList.remove("active");
        } else {
            this.sidebar.classList.add("active");
            this.mainContainer.classList.add("active")
        }
    }

    addIngredient(id, displayText) {
        if (!id || this.selectedIngredients.has(id)) return;

        this.selectedIngredients.add(id);

        const newField = this.createNewField(displayText);

        this.ingredients.appendChild(newField);

        newField.querySelector(".del-button").addEventListener("click", () => {
            this.removeIngredient(id, newField);
        });
    }

    addCategory(id, displayText) {
        if (!id || this.selectedCategories.has(id)) return;

        this.selectedCategories.add(id);

        const newField = this.createNewField(displayText);
        this.categories.appendChild(newField);

        newField.querySelector(".del-button").addEventListener("click", () => {
            this.removeCategory(id, newField);
        });
    }

    createNewField(displayText) {
        const newField = document.createElement("div");
        newField.className = "block-container";
        newField.innerHTML = `
            <div class="block">
                ${displayText}
            </div>
                <button class="del-button" type="button">&times;</button>
        `;

        return newField;
    }

    removeIngredient(id, element) {
        this.selectedIngredients.delete(id);
        element.remove();
    }

    removeCategory(id, element) {
        this.selectedCategories.delete(id);
        element.remove();
    }

    search() {
        const requestData = {
            ingredientIds: Array.from(this.selectedIngredients),
            categoryIds: Array.from(this.selectedCategories)
        };

        const params = new URLSearchParams();


        requestData.ingredientIds.forEach(id => {
            params.append("ingredientId", id);
        });

        requestData.categoryIds.forEach(id => {
            params.append("categoryId", id);
        });

        if (this.activeTimeStart.value) {
            params.append('activeCookingTimeStart', this.activeTimeStart.value);
        }
        if (this.activeTimeEnd.value) {
            params.append('activeCookingTimeEnd', this.activeTimeEnd.value);
        }

        if (this.totalTimeStart.value) {
            params.append('totalCookingTimeStart', this.totalTimeStart.value);
        }
        if (this.totalTimeEnd.value) {
            params.append('totalCookingTimeEnd', this.totalTimeEnd.value);
        }

        window.location.href = (BASE_URL + `/recipes?${params.toString()}`);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new Sidebar();
});