package codeladder.model;

import java.util.Objects;

public final class FieldValidationOutcome {
    private final String fieldId;
    private final SkillTag skillTag;
    private final boolean correct;

    public FieldValidationOutcome(String fieldId, SkillTag skillTag, boolean correct) {
        this.fieldId = Objects.requireNonNull(fieldId, "fieldId");
        this.skillTag = Objects.requireNonNull(skillTag, "skillTag");
        this.correct = correct;
    }

    public String getFieldId() {
        return fieldId;
    }

    public SkillTag getSkillTag() {
        return skillTag;
    }

    public boolean isCorrect() {
        return correct;
    }
}
