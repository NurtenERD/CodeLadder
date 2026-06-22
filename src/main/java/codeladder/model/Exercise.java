package codeladder.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Exercise {
    private final String id;
    private final StepType stepType;
    private final ExerciseType exerciseType;
    private final String title;
    private final String instruction;
    private final String question;
    private final String focusText;
    private final CaseStudy caseStudy;
    private final List<AnswerOption> options;
    private final Set<String> correctOptionIds;
    private final Set<String> acceptedKeywords;
    private final Set<String> requiredFragments;
    private final int minimumRequiredMatches;
    private final boolean codeExercise;
    private final String successFeedback;
    private final String retryFeedback;
    private final String finalFeedback;

    public Exercise(
            String id,
            StepType stepType,
            ExerciseType exerciseType,
            String title,
            String instruction,
            String question,
            String focusText,
            CaseStudy caseStudy,
            List<AnswerOption> options,
            Set<String> correctOptionIds,
            Set<String> acceptedKeywords,
            Set<String> requiredFragments,
            int minimumRequiredMatches,
            boolean codeExercise,
            String successFeedback,
            String retryFeedback,
            String finalFeedback
    ) {
        this.id = id;
        this.stepType = stepType;
        this.exerciseType = exerciseType;
        this.title = title;
        this.instruction = instruction;
        this.question = question;
        this.focusText = focusText == null ? "" : focusText;
        this.caseStudy = caseStudy;
        this.options = new ArrayList<>(options);
        this.correctOptionIds = new LinkedHashSet<>(correctOptionIds);
        this.acceptedKeywords = new LinkedHashSet<>(acceptedKeywords);
        this.requiredFragments = new LinkedHashSet<>(requiredFragments);
        this.minimumRequiredMatches = minimumRequiredMatches;
        this.codeExercise = codeExercise;
        this.successFeedback = successFeedback;
        this.retryFeedback = retryFeedback;
        this.finalFeedback = finalFeedback;
    }

    public String getId() {
        return id;
    }

    public StepType getStepType() {
        return stepType;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
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

    public int getMinimumRequiredMatches() {
        return minimumRequiredMatches;
    }

    public boolean isCodeExercise() {
        return codeExercise;
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
