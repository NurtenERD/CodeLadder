package codeladder.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class StructuredFieldDefinition {
    private final String id;
    private final String label;
    private final String prompt;
    private final StructuredFieldInputType inputType;
    private final boolean required;
    private final List<AnswerOption> options;
    private final Set<String> correctOptionIds;
    private final List<Set<String>> acceptedKeywordGroups;
    private final SkillTag skillTag;
    private final String retryFeedback;
    private final String finalFeedback;

    public StructuredFieldDefinition(
            String id,
            String label,
            String prompt,
            StructuredFieldInputType inputType,
            boolean required,
            List<AnswerOption> options,
            Set<String> correctOptionIds,
            List<Set<String>> acceptedKeywordGroups,
            SkillTag skillTag,
            String retryFeedback,
            String finalFeedback
    ) {
        this.id = requireText(id, "id");
        this.label = requireText(label, "label");
        this.prompt = requireText(prompt, "prompt");
        this.inputType = Objects.requireNonNull(inputType, "inputType");
        this.required = required;
        this.options = options == null ? List.of() : new ArrayList<>(options);
        this.correctOptionIds = correctOptionIds == null ? Set.of() : new LinkedHashSet<>(correctOptionIds);
        this.acceptedKeywordGroups = copyKeywordGroups(acceptedKeywordGroups);
        this.skillTag = Objects.requireNonNull(skillTag, "skillTag");
        this.retryFeedback = requireText(retryFeedback, "retryFeedback");
        this.finalFeedback = requireText(finalFeedback, "finalFeedback");
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getPrompt() {
        return prompt;
    }

    public StructuredFieldInputType getInputType() {
        return inputType;
    }

    public boolean isRequired() {
        return required;
    }

    public List<AnswerOption> getOptions() {
        return new ArrayList<>(options);
    }

    public Set<String> getCorrectOptionIds() {
        return new LinkedHashSet<>(correctOptionIds);
    }

    public List<Set<String>> getAcceptedKeywordGroups() {
        return copyKeywordGroups(acceptedKeywordGroups);
    }

    public SkillTag getSkillTag() {
        return skillTag;
    }

    public String getRetryFeedback() {
        return retryFeedback;
    }

    public String getFinalFeedback() {
        return finalFeedback;
    }

    private List<Set<String>> copyKeywordGroups(List<Set<String>> groups) {
        if (groups == null) {
            return List.of();
        }
        List<Set<String>> copy = new ArrayList<>();
        for (Set<String> group : groups) {
            copy.add(group == null ? Set.of() : new LinkedHashSet<>(group));
        }
        return copy;
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is verplicht");
        }
        return value;
    }
}
