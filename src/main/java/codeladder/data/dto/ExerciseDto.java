package codeladder.data.dto;

import java.util.List;
import java.util.Set;

public class ExerciseDto {
    private String id;
    private String stepType;
    private String exerciseType;
    private String title;
    private String instruction;
    private String question;
    private String focusText;
    private List<AnswerOptionDto> options;
    private Set<String> correctOptionIds;
    private Set<String> acceptedKeywords;
    private Set<String> requiredFragments;
    private int minimumRequiredMatches;
    private boolean codeExercise;
    private String successFeedback;
    private String retryFeedback;
    private String finalFeedback;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getFocusText() {
        return focusText;
    }

    public void setFocusText(String focusText) {
        this.focusText = focusText;
    }

    public List<AnswerOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerOptionDto> options) {
        this.options = options;
    }

    public Set<String> getCorrectOptionIds() {
        return correctOptionIds;
    }

    public void setCorrectOptionIds(Set<String> correctOptionIds) {
        this.correctOptionIds = correctOptionIds;
    }

    public Set<String> getAcceptedKeywords() {
        return acceptedKeywords;
    }

    public void setAcceptedKeywords(Set<String> acceptedKeywords) {
        this.acceptedKeywords = acceptedKeywords;
    }

    public Set<String> getRequiredFragments() {
        return requiredFragments;
    }

    public void setRequiredFragments(Set<String> requiredFragments) {
        this.requiredFragments = requiredFragments;
    }

    public int getMinimumRequiredMatches() {
        return minimumRequiredMatches;
    }

    public void setMinimumRequiredMatches(int minimumRequiredMatches) {
        this.minimumRequiredMatches = minimumRequiredMatches;
    }

    public boolean isCodeExercise() {
        return codeExercise;
    }

    public void setCodeExercise(boolean codeExercise) {
        this.codeExercise = codeExercise;
    }

    public String getSuccessFeedback() {
        return successFeedback;
    }

    public void setSuccessFeedback(String successFeedback) {
        this.successFeedback = successFeedback;
    }

    public String getRetryFeedback() {
        return retryFeedback;
    }

    public void setRetryFeedback(String retryFeedback) {
        this.retryFeedback = retryFeedback;
    }

    public String getFinalFeedback() {
        return finalFeedback;
    }

    public void setFinalFeedback(String finalFeedback) {
        this.finalFeedback = finalFeedback;
    }
}
