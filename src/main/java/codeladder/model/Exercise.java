package codeladder.model;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Exercise {
    private final String id;
    private final StepType stepType;
    private final ExerciseMetadata metadata;
    private final ExerciseContent content;
    private final AnswerDefinition answerDefinition;
    private final FeedbackDefinition feedbackDefinition;
    private final ProgrammingTask programmingTask;

    public Exercise(
            String id,
            StepType stepType,
            ExerciseMetadata metadata,
            ExerciseContent content,
            AnswerDefinition answerDefinition,
            FeedbackDefinition feedbackDefinition,
            ProgrammingTask programmingTask
    ) {
        this.id = Objects.requireNonNull(id, "id");
        this.stepType = Objects.requireNonNull(stepType, "stepType");
        this.metadata = Objects.requireNonNull(metadata, "metadata");
        this.content = Objects.requireNonNull(content, "content");
        this.answerDefinition = Objects.requireNonNull(answerDefinition, "answerDefinition");
        this.feedbackDefinition = Objects.requireNonNull(feedbackDefinition, "feedbackDefinition");
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

    public AnswerDefinition getAnswerDefinition() {
        return answerDefinition;
    }

    public FeedbackDefinition getFeedbackDefinition() {
        return feedbackDefinition;
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

    public java.util.List<AnswerOption> getOptions() {
        return answerDefinition.getOptions();
    }

    public Set<String> getCorrectOptionIds() {
        return answerDefinition.getCorrectOptionIds();
    }

    public Set<String> getAcceptedKeywords() {
        return answerDefinition.getAcceptedKeywords();
    }

    public Set<String> getRequiredFragments() {
        return answerDefinition.getRequiredFragments();
    }

    public Optional<StructuredAnswerDefinition> getStructuredAnswerDefinition() {
        return answerDefinition.getStructuredAnswerDefinition();
    }

    public int getMinimumRequiredMatches() {
        return answerDefinition.getMinimumRequiredMatches();
    }

    public String getSuccessFeedback() {
        return feedbackDefinition.getSuccessFeedback();
    }

    public String getRetryFeedback() {
        return feedbackDefinition.getRetryFeedback();
    }

    public String getFinalFeedback() {
        return feedbackDefinition.getFinalFeedback();
    }

    public Optional<ProgrammingTask> getProgrammingTask() {
        return Optional.ofNullable(programmingTask);
    }
}
