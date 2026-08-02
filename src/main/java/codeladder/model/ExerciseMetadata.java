package codeladder.model;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class ExerciseMetadata {
    private final String seriesId;
    private final ExerciseType exerciseType;
    private final ActivityType activityType;
    private final InteractionType interactionType;
    private final ValidationType validationType;
    private final ProgrammingPattern programmingPattern;
    private final SupportLevel supportLevel;
    private final Set<SkillTag> skillTags;

    public ExerciseMetadata(
            String seriesId,
            ExerciseType exerciseType,
            ActivityType activityType,
            InteractionType interactionType,
            ValidationType validationType,
            ProgrammingPattern programmingPattern,
            SupportLevel supportLevel,
            Set<SkillTag> skillTags
    ) {
        this.seriesId = requireText(seriesId, "seriesId");
        this.exerciseType = Objects.requireNonNull(exerciseType, "exerciseType");
        this.activityType = Objects.requireNonNull(activityType, "activityType");
        this.interactionType = Objects.requireNonNull(interactionType, "interactionType");
        this.validationType = Objects.requireNonNull(validationType, "validationType");
        this.programmingPattern = Objects.requireNonNull(programmingPattern, "programmingPattern");
        this.supportLevel = Objects.requireNonNull(supportLevel, "supportLevel");
        this.skillTags = skillTags == null ? Set.of() : new LinkedHashSet<>(skillTags);
    }

    public String getSeriesId() {
        return seriesId;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public ValidationType getValidationType() {
        return validationType;
    }

    public ProgrammingPattern getProgrammingPattern() {
        return programmingPattern;
    }

    public SupportLevel getSupportLevel() {
        return supportLevel;
    }

    public Set<SkillTag> getSkillTags() {
        return new LinkedHashSet<>(skillTags);
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is verplicht");
        }
        return value;
    }
}
