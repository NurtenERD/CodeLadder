package codeladder.model;

public enum AnswerCategory {
    CLASS("Object / class-kandidaat"),
    ATTRIBUTE("Attribuut"),
    METHOD("Methode"),
    NOT_RELEVANT("Niet belangrijk");

    private final String displayName;

    AnswerCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
