package codeladder.model;

import java.util.Objects;

public final class FeedbackDefinition {
    private final String successFeedback;
    private final String retryFeedback;
    private final String finalFeedback;

    public FeedbackDefinition(String successFeedback, String retryFeedback, String finalFeedback) {
        this.successFeedback = Objects.requireNonNull(successFeedback, "successFeedback");
        this.retryFeedback = Objects.requireNonNull(retryFeedback, "retryFeedback");
        this.finalFeedback = Objects.requireNonNull(finalFeedback, "finalFeedback");
    }

    public String getSuccessFeedback() {
        return successFeedback;
    }

    public String getRetryFeedback() {
        return retryFeedback;
    }

    public String getFinalFeedback() {
        return finalFeedback;
    }
}
