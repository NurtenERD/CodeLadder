package codeladder.data.dto;

import java.util.List;

public class StructuredFieldDefinitionDto {
    private String id;
    private String label;
    private String prompt;
    private String inputType;
    private boolean required = true;
    private List<AnswerOptionDto> options;
    private List<String> correctOptionIds;
    private List<List<String>> acceptedKeywordGroups;
    private String skillTag;
    private String retryFeedback;
    private String finalFeedback;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
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

    public List<List<String>> getAcceptedKeywordGroups() {
        return acceptedKeywordGroups;
    }

    public void setAcceptedKeywordGroups(List<List<String>> acceptedKeywordGroups) {
        this.acceptedKeywordGroups = acceptedKeywordGroups;
    }

    public String getSkillTag() {
        return skillTag;
    }

    public void setSkillTag(String skillTag) {
        this.skillTag = skillTag;
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
