package ru.itis.flavorful_book.exception;

public class IllegalRecipeArgumentException extends RuntimeException {
    private String titleState;

    private String instructionsState;

    private String activeCookingTimeState;

    private String totalCookingTimeState;

    private String cookingTimeState;

    private String servingsState;

    private boolean shouldThrow;

    public IllegalRecipeArgumentException(String message) {
        super(message);

        titleState = null;
        instructionsState = null;
        activeCookingTimeState = null;
        totalCookingTimeState = null;
        cookingTimeState = null;
        servingsState = null;

        shouldThrow = false;
    }

    public String getTitleState() {
        return titleState;
    }

    public void setTitleState(String titleState) {
        if (titleState != null) {
            this.titleState = titleState;
            shouldThrow = true;
        }
    }

    public String getInstructionsState() {
        return instructionsState;
    }

    public void setInstructionsState(String instructionsState) {
        if (instructionsState != null) {
            this.instructionsState = instructionsState;
            shouldThrow = true;
        }
    }

    public String getActiveCookingTimeState() {
        return activeCookingTimeState;
    }

    public void setActiveCookingTimeState(String activeCookingTimeState) {
        if (activeCookingTimeState != null) {
            this.activeCookingTimeState = activeCookingTimeState;
            shouldThrow = true;
        }
    }

    public String getTotalCookingTimeState() {
        return totalCookingTimeState;
    }

    public void setTotalCookingTimeState(String totalCookingTimeState) {
        if (totalCookingTimeState != null) {
            this.totalCookingTimeState = totalCookingTimeState;
            shouldThrow = true;
        }
    }

    public String getCookingTimeState() {
        return cookingTimeState;
    }

    public void setCookingTimeState(String cookingTimeState) {
        if (cookingTimeState != null) {
            this.cookingTimeState = cookingTimeState;
            shouldThrow = true;
        }
    }

    public String getServingsState() {
        return servingsState;
    }

    public void setServingsState(String servingsState) {
        if (servingsState != null) {
            this.servingsState = servingsState;
            shouldThrow = true;
        }
    }

    public boolean isShouldThrow() {
        return shouldThrow;
    }
}
