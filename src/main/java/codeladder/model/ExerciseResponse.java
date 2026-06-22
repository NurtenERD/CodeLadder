package codeladder.model;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringJoiner;

public class ExerciseResponse {
    private final String textAnswer;
    private final Set<String> selectedOptionIds;

    private ExerciseResponse(String textAnswer, Set<String> selectedOptionIds) {
        this.textAnswer = textAnswer == null ? "" : textAnswer.trim();
        this.selectedOptionIds = new LinkedHashSet<>(selectedOptionIds);
    }

    public static ExerciseResponse empty() {
        return new ExerciseResponse("", Set.of());
    }

    public static ExerciseResponse forText(String textAnswer) {
        return new ExerciseResponse(textAnswer, Set.of());
    }

    public static ExerciseResponse forSelections(Set<String> selectedOptionIds) {
        return new ExerciseResponse("", selectedOptionIds);
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public Set<String> getSelectedOptionIds() {
        return new LinkedHashSet<>(selectedOptionIds);
    }

    public boolean isEmpty() {
        return textAnswer.isBlank() && selectedOptionIds.isEmpty();
    }

    public String toDisplayText(Exercise exercise) {
        if (!textAnswer.isBlank()) {
            return textAnswer;
        }

        if (selectedOptionIds.isEmpty()) {
            return "(geen antwoord)";
        }

        StringJoiner joiner = new StringJoiner(", ");
        for (AnswerOption option : exercise.getOptions()) {
            if (selectedOptionIds.contains(option.getId())) {
                joiner.add(option.getLabel());
            }
        }
        return joiner.toString();
    }
}
