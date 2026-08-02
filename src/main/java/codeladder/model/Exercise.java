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
    private final String title;
    private final String instruction;
    private final String question;
    private final String focusText;
    private final String hintText;
    private final CaseStudy caseStudy;
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
            String title,
            String instruction,
            String question,
            String focusText,
            String hintText,
            CaseStudy caseStudy,
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
        this.title = Objects.requireNonNull(title, "title");
        this.instruction = Objects.requireNonNull(instruction, "instruction");
        this.question = Objects.requireNonNull(question, "question");
        this.focusText = focusText == null ? "" : focusText;
        this.hintText = hintText == null ? "" : hintText;
        this.caseStudy = Objects.requireNonNull(caseStudy, "caseStudy");
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
        return title;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getQuestion() {
        return question;
    }

    public String getFocusText() {
        return focusText;
    }

    public String getHintText() {
        return hintText;
    }

    public CaseStudy getCaseStudy() {
        return caseStudy;
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
