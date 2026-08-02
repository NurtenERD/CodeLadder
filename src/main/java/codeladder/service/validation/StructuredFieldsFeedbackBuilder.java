package codeladder.service.validation;

import codeladder.model.Exercise;
import codeladder.model.Feedback;
import codeladder.model.FeedbackType;
import codeladder.model.FieldValidationOutcome;
import codeladder.model.StructuredFieldDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StructuredFieldsFeedbackBuilder {

    public Feedback success(Exercise exercise) {
        return new Feedback(exercise.getSuccessFeedback() + " Je hebt de opdrachtanalyse aangetoond.", FeedbackType.SUCCESS);
    }

    public Feedback failure(Exercise exercise, Map<String, FieldValidationOutcome> outcomes, int attemptNumber) {
        boolean allowRetry = attemptNumber < 2;
        List<String> correctFields = new ArrayList<>();
        List<String> incorrectFields = new ArrayList<>();
        for (StructuredFieldDefinition field : exercise.getStructuredAnswerDefinition().orElseThrow().getFields()) {
            FieldValidationOutcome outcome = outcomes.get(field.getId());
            if (outcome != null && outcome.isCorrect()) {
                correctFields.add(field.getLabel());
            } else {
                incorrectFields.add((allowRetry ? field.getRetryFeedback() : field.getFinalFeedback()));
            }
        }
        StringBuilder message = new StringBuilder();
        if (!correctFields.isEmpty()) {
            message.append("Al goed: ").append(String.join(", ", correctFields)).append(".");
        }
        if (!incorrectFields.isEmpty()) {
            if (!message.isEmpty()) {
                message.append("\n");
            }
            message.append(allowRetry ? "Bekijk opnieuw:" : "Modelantwoord voor de nog foutieve velden:");
            for (String value : incorrectFields) {
                message.append("\n- ").append(value);
            }
        }
        return new Feedback(message.toString(), allowRetry ? FeedbackType.GUIDANCE : FeedbackType.FINAL_HINT);
    }
}
