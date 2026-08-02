package codeladder.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Exercise {
    private final String id;
    private final StepType stepType;
    private final ExerciseMetadata metadata;
    private final ExerciseContent content;
    private final List<AnswerOption> options;
    private final Set<String> correctOptionIds;
    private final Set<String> acceptedKeywords;
    private final Set<String> requiredFragments;
    private final StructuredAnswerDefinition structuredAnswerDefinition;
    private final int minimumRequiredMatches;
    private final String successFeedback;
    private final String retryFeedback;
    private final String finalFeedback;
    private final ProgrammingTask programmingTask;

    public Exercise(
            String id,
            StepType stepType,
            ExerciseMetadata metadata,
            ExerciseContent content,
            List<AnswerOption> options,
            Set<String> correctOptionIds,
            Set<String> acceptedKeywords,
            Set<String> requiredFragments,
            StructuredAnswerDefinition structuredAnswerDefinition,
            int minimumRequiredMatches,
            String successFeedback,
            String retryFeedback,
            String finalFeedback,
            ProgrammingTask programmingTask
    ) {
        this.id = Objects.requireNonNull(id, "id");
        this.stepType = Objects.requireNonNull(stepType, "stepType");
        this.metadata = Objects.requireNonNull(metadata, "metadata");
        this.content = Objects.requireNonNull(content, "content");
        this.options = options == null ? List.of() : new ArrayList<>(options);
        this.correctOptionIds = copySet(correctOptionIds);
        this.acceptedKeywords = copySet(acceptedKeywords);
        this.requiredFragments = copySet(requiredFragments);
        this.structuredAnswerDefinition = structuredAnswerDefinition;
        this.minimumRequiredMatches = minimumRequiredMatches;
        this.successFeedback = Objects.requireNonNull(successFeedback, "successFeedback");
        this.retryFeedback = Objects.requireNonNull(retryFeedback, "retryFeedback");
        this.finalFeedback = Objects.requireNonNull(finalFeedback, "finalFeedback");
        this.programmingTask = programmingTask;
    }

    public String getId() {
        return id;
    }

    public StepType getStepType() {
        return stepType;
    }

    public ExerciseMetadata getMetadata() {
        return metadata;
    }

    public ExerciseContent getContent() {
        return content;
    }

    public ExerciseType getExerciseType() {
        return metadata.getExerciseType();
    }

    public ActivityType getActivityType() {
        return metadata.getActivityType();
    }

    public InteractionType getInteractionType() {
        return metadata.getInteractionType();
    }

    public ValidationType getValidationType() {
        return metadata.getValidationType();
    }

    public ProgrammingPattern getProgrammingPattern() {
        return metadata.getProgrammingPattern();
    }

    public SupportLevel getSupportLevel() {
        return metadata.getSupportLevel();
    }

    public Set<SkillTag> getSkillTags() {
        return metadata.getSkillTags();
    }

    public String getTitle() {
        return content.getTitle();
    }

    public String getInstruction() {
        return content.getInstruction();
    }

    public String getQuestion() {
        return content.getQuestion();
    }

    public String getFocusText() {
        return content.getFocusText();
    }

    public String getHintText() {
        return content.getHintText();
    }

    public CaseStudy getCaseStudy() {
        return content.getCaseStudy();
    }

    public List<AnswerOption> getOptions() {
        return new ArrayList<>(options);
    }

    public Set<String> getCorrectOptionIds() {
        return new LinkedHashSet<>(correctOptionIds);
    }

    public Set<String> getAcceptedKeywords() {
        return new LinkedHashSet<>(acceptedKeywords);
    }

    public Set<String> getRequiredFragments() {
        return new LinkedHashSet<>(requiredFragments);
    }

    public Optional<StructuredAnswerDefinition> getStructuredAnswerDefinition() {
        return Optional.ofNullable(structuredAnswerDefinition);
    }

    public int getMinimumRequiredMatches() {
        return minimumRequiredMatches;
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

    public Optional<ProgrammingTask> getProgrammingTask() {
        return Optional.ofNullable(programmingTask);
    }

    private Set<String> copySet(Set<String> values) {
        return values == null ? Set.of() : new LinkedHashSet<>(values);
    }
}
