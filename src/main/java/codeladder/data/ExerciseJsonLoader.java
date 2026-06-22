package codeladder.data;

import codeladder.data.dto.AnswerOptionDto;
import codeladder.data.dto.CaseStudyDto;
import codeladder.data.dto.ExerciseDto;
import codeladder.data.dto.ExerciseFileDto;
import codeladder.data.dto.ExerciseIndexDto;
import codeladder.model.AnswerOption;
import codeladder.model.CaseStudy;
import codeladder.model.Exercise;
import codeladder.model.ExerciseType;
import codeladder.model.StepType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ExerciseJsonLoader {
    static final String INDEX_PATH = "exercises/index.json";

    private final ObjectMapper objectMapper;
    private final ExerciseDefinitionValidator validator;

    public ExerciseJsonLoader() {
        this(createObjectMapper(), new ExerciseDefinitionValidator());
    }

    ExerciseJsonLoader(ObjectMapper objectMapper, ExerciseDefinitionValidator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public List<Exercise> loadExercises() {
        ExerciseIndexDto indexDto = loadIndex();
        if (indexDto.getFiles() == null || indexDto.getFiles().isEmpty()) {
            throw new IllegalStateException("Geen exercise-bestanden gevonden in " + INDEX_PATH);
        }

        List<Exercise> exercises = new ArrayList<>();
        for (String filePath : indexDto.getFiles()) {
            ExerciseFileDto fileDto = loadExerciseFile(filePath);
            validator.validateFile(filePath, fileDto.getRouteBlockTitle(), fileDto.getCaseStudy(), fileDto.getExercises());

            CaseStudy caseStudy = mapCaseStudy(fileDto.getRouteBlockTitle(), fileDto.getCaseStudy());
            for (ExerciseDto exerciseDto : fileDto.getExercises()) {
                exercises.add(mapToExercise(exerciseDto, caseStudy, filePath));
            }
        }
        return exercises;
    }

    private ExerciseIndexDto loadIndex() {
        return readResource(INDEX_PATH, ExerciseIndexDto.class);
    }

    private ExerciseFileDto loadExerciseFile(String path) {
        return readResource(path, ExerciseFileDto.class);
    }

    private <T> T readResource(String resourcePath, Class<T> targetType) {
        try (InputStream inputStream = getResourceAsStream(resourcePath)) {
            return objectMapper.readValue(inputStream, targetType);
        } catch (IOException exception) {
            throw new IllegalStateException("Kon JSON-bestand niet laden: " + resourcePath, exception);
        }
    }

    private InputStream getResourceAsStream(String resourcePath) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader() == null
                ? null
                : Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            inputStream = ExerciseJsonLoader.class.getClassLoader().getResourceAsStream(resourcePath);
        }
        if (inputStream == null) {
            inputStream = ExerciseJsonLoader.class.getResourceAsStream("/" + resourcePath);
        }
        if (inputStream == null) {
            throw new IllegalStateException("Resource ontbreekt: " + resourcePath);
        }
        return inputStream;
    }

    private CaseStudy mapCaseStudy(String routeBlockTitle, CaseStudyDto caseStudyDto) {
        return new CaseStudy(routeBlockTitle, caseStudyDto.getTitle(), caseStudyDto.getDescription());
    }

    private Exercise mapToExercise(ExerciseDto dto, CaseStudy caseStudy, String filePath) {
        return new Exercise(
                dto.getId(),
                parseStepType(dto.getStepType(), dto.getId(), filePath),
                parseExerciseType(dto.getExerciseType(), dto.getId(), filePath),
                dto.getTitle(),
                dto.getInstruction(),
                dto.getQuestion(),
                dto.getFocusText(),
                caseStudy,
                mapOptions(dto.getOptions()),
                toOrderedSet(dto.getCorrectOptionIds()),
                toOrderedSet(dto.getAcceptedKeywords()),
                toOrderedSet(dto.getRequiredFragments()),
                dto.getMinimumRequiredMatches(),
                dto.isCodeExercise(),
                dto.getSuccessFeedback(),
                dto.getRetryFeedback(),
                dto.getFinalFeedback()
        );
    }

    private List<AnswerOption> mapOptions(List<AnswerOptionDto> options) {
        if (options == null || options.isEmpty()) {
            return List.of();
        }

        List<AnswerOption> mapped = new ArrayList<>();
        for (AnswerOptionDto option : options) {
            mapped.add(new AnswerOption(option.getId(), option.getLabel()));
        }
        return mapped;
    }

    private Set<String> toOrderedSet(Set<String> values) {
        if (values == null || values.isEmpty()) {
            return Set.of();
        }
        return new LinkedHashSet<>(values);
    }

    private StepType parseStepType(String value, String exerciseId, String filePath) {
        try {
            return StepType.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Ongeldige stepType '" + value + "' in " + filePath + " voor oefening " + exerciseId, exception);
        }
    }

    private ExerciseType parseExerciseType(String value, String exerciseId, String filePath) {
        try {
            return ExerciseType.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Ongeldige exerciseType '" + value + "' in " + filePath + " voor oefening " + exerciseId, exception);
        }
    }

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }
}
