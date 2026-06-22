package codeladder.data.dto;

import java.util.List;

public class ExerciseFileDto {
    private String routeBlockTitle;
    private CaseStudyDto caseStudy;
    private List<ExerciseDto> exercises;

    public String getRouteBlockTitle() {
        return routeBlockTitle;
    }

    public void setRouteBlockTitle(String routeBlockTitle) {
        this.routeBlockTitle = routeBlockTitle;
    }

    public CaseStudyDto getCaseStudy() {
        return caseStudy;
    }

    public void setCaseStudy(CaseStudyDto caseStudy) {
        this.caseStudy = caseStudy;
    }

    public List<ExerciseDto> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseDto> exercises) {
        this.exercises = exercises;
    }
}
