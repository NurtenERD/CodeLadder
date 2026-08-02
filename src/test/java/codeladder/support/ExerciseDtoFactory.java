package codeladder.support;

import codeladder.data.dto.CaseStudyDto;
import codeladder.data.dto.ExerciseDto;
import codeladder.data.dto.ExerciseFileDto;
import codeladder.data.dto.ExerciseIndexDto;
import codeladder.data.dto.ExerciseMetadataDto;
import codeladder.model.ActivityType;
import codeladder.model.ExerciseType;
import codeladder.model.InteractionType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.SupportLevel;
import codeladder.model.ValidationType;

import java.util.List;
import java.util.Set;

public final class ExerciseDtoFactory {

    private ExerciseDtoFactory() {
    }

    public static ExerciseDto validExercise(String id, InteractionType interactionType, ValidationType validationType) {
        ExerciseDto dto = new ExerciseDto();
        dto.setId(id);
        dto.setStepType("UNDERSTAND_ASSIGNMENT");
        dto.setMetadata(metadata(id, ExerciseType.OOP_LADDER, interactionType, validationType));
        dto.setTitle("Titel");
        dto.setInstruction("Instructie");
        dto.setQuestion("Vraag");
        dto.setFocusText("");
        dto.setOptions(List.of());
        dto.setCorrectOptionIds(List.of());
        dto.setAcceptedKeywords(List.of("gebruiker"));
        dto.setRequiredFragments(List.of());
        dto.setMinimumRequiredMatches(1);
        dto.setSuccessFeedback("Goed");
        dto.setRetryFeedback("Opnieuw");
        dto.setFinalFeedback("Laatste hint");
        return dto;
    }

    public static ExerciseMetadataDto metadata(
            String seriesId,
            ExerciseType exerciseType,
            InteractionType interactionType,
            ValidationType validationType
    ) {
        ExerciseMetadataDto metadata = new ExerciseMetadataDto();
        metadata.setSeriesId(seriesId);
        metadata.setExerciseType(exerciseType.name());
        metadata.setActivityType(ActivityType.OOP_PRACTICE.name());
        metadata.setInteractionType(interactionType.name());
        metadata.setValidationType(validationType.name());
        metadata.setProgrammingPattern(ProgrammingPattern.NONE.name());
        metadata.setSupportLevel(SupportLevel.NOT_SPECIFIED.name());
        metadata.setSkillTags(Set.of());
        return metadata;
    }

    public static ExerciseFileDto fileDto(String routeBlockTitle, ExerciseDto... exercises) {
        ExerciseFileDto fileDto = new ExerciseFileDto();
        fileDto.setRouteBlockTitle(routeBlockTitle);
        fileDto.setCaseStudy(caseStudy());
        fileDto.setExercises(List.of(exercises));
        return fileDto;
    }

    public static ExerciseIndexDto indexDto(String... files) {
        ExerciseIndexDto indexDto = new ExerciseIndexDto();
        indexDto.setFiles(List.of(files));
        return indexDto;
    }

    public static CaseStudyDto caseStudy() {
        CaseStudyDto caseStudyDto = new CaseStudyDto();
        caseStudyDto.setTitle("Casus");
        caseStudyDto.setDescription("Beschrijving");
        return caseStudyDto;
    }
}
