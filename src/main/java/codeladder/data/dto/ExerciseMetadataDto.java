package codeladder.data.dto;

import java.util.Set;

public class ExerciseMetadataDto {
    private String seriesId;
    private String exerciseType;
    private String activityType;
    private String interactionType;
    private String validationType;
    private String programmingPattern;
    private String supportLevel;
    private Set<String> skillTags;

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public String getValidationType() {
        return validationType;
    }

    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }

    public String getProgrammingPattern() {
        return programmingPattern;
    }

    public void setProgrammingPattern(String programmingPattern) {
        this.programmingPattern = programmingPattern;
    }

    public String getSupportLevel() {
        return supportLevel;
    }

    public void setSupportLevel(String supportLevel) {
        this.supportLevel = supportLevel;
    }

    public Set<String> getSkillTags() {
        return skillTags;
    }

    public void setSkillTags(Set<String> skillTags) {
        this.skillTags = skillTags;
    }
}
