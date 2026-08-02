package codeladder.data.dto;

import java.util.List;

public class ExerciseDto {
    private String id;
    private String stepType;
    private ExerciseMetadataDto metadata;
    private String title;
    private String instruction;
    private String question;
    private String focusText;
    private String hintText;
    private List<AnswerOptionDto> options;
    private List<String> correctOptionIds;
    private List<String> acceptedKeywords;
    private List<String> requiredFragments;
    private StructuredAnswerDefinitionDto structuredAnswerDefinition;
    private int minimumRequiredMatches;
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

    public ExerciseMetadataDto getMetadata() {
        return metadata;
    }

    public void setMetadata(ExerciseMetadataDto metadata) {
        this.metadata = metadata;
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

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public List<AnswerOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerOptionDto> options) {
        this.options = options;
    }

    public List<String> getCorrectOptionIds() {
        return correctOptionIds;
    }

    public void setCorrectOptionIds(List<String> correctOptionIds) {
        this.correctOptionIds = correctOptionIds;
    }

    public List<String> getAcceptedKeywords() {
        return acceptedKeywords;
    }

    public void setAcceptedKeywords(List<String> acceptedKeywords) {
        this.acceptedKeywords = acceptedKeywords;
    }

    public List<String> getRequiredFragments() {
        return requiredFragments;
    }

    public void setRequiredFragments(List<String> requiredFragments) {
        this.requiredFragments = requiredFragments;
    }

    public StructuredAnswerDefinitionDto getStructuredAnswerDefinition() {
        return structuredAnswerDefinition;
    }

    public void setStructuredAnswerDefinition(StructuredAnswerDefinitionDto structuredAnswerDefinition) {
        this.structuredAnswerDefinition = structuredAnswerDefinition;
    }

    public int getMinimumRequiredMatches() {
        return minimumRequiredMatches;
    }

    public void setMinimumRequiredMatches(int minimumRequiredMatches) {
        this.minimumRequiredMatches = minimumRequiredMatches;
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
