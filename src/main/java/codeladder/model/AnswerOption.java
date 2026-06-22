package codeladder.model;

public class AnswerOption {
    private final String id;
    private final String label;

    public AnswerOption(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
