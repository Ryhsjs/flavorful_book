package ru.itis.flavorful_book.exceptions;

public class IllegalUserArgumentException extends RuntimeException {
    private String usernameState;

    private String emailState;

    private String passwordState;

    private boolean shouldThrow;

    public IllegalUserArgumentException(String message) {
        super(message);

        usernameState = null;
        emailState = null;
        passwordState = null;

        shouldThrow = false;
    }

    public String getUsernameState() {
        return usernameState;
    }

    public void setUsernameState(String usernameState) {
        if (usernameState != null) {
            this.usernameState = usernameState;
            shouldThrow = true;
        }
    }

    public String getEmailState() {
        return emailState;

    }

    public void setEmailState(String emailState) {
        if (emailState != null) {
            this.emailState = emailState;
            shouldThrow = true;
        }
    }

    public String getPasswordState() {
        return passwordState;
    }

    public void setPasswordState(String passwordState) {
        if (passwordState != null) {
            this.passwordState = passwordState;
            shouldThrow = true;
        }
    }

    public boolean isShouldThrow() {
        return shouldThrow;
    }
}
