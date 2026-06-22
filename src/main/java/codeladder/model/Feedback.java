package codeladder.model;

public class Feedback {
    private final String message;
    private final FeedbackType type;

    public Feedback(String message, FeedbackType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public FeedbackType getType() {
        return type;
    }
}
