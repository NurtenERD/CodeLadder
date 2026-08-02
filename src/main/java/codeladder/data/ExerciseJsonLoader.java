package codeladder.data;

import codeladder.data.dto.ExerciseFileDto;
import codeladder.data.dto.ExerciseIndexDto;
import codeladder.model.CaseStudy;
import codeladder.model.Exercise;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ExerciseJsonLoader {
    static final String INDEX_PATH = "exercises/index.json";

    private final ExerciseResourceReader resourceReader;
    private final ExerciseDefinitionValidator validator;
    private final ExerciseDtoMapper mapper;
    private final ExerciseRouteIntegrityValidator routeIntegrityValidator;

    public ExerciseJsonLoader() {
        this(createObjectMapper());
    }

    ExerciseJsonLoader(ObjectMapper objectMapper) {
        EnumValueParser enumValueParser = new EnumValueParser();
        this.resourceReader = new ExerciseResourceReader(objectMapper);
        this.validator = new ExerciseDefinitionValidator(enumValueParser);
        this.mapper = new ExerciseDtoMapper(
                enumValueParser,
                new ExerciseMetadataMapper(enumValueParser),
                new ProgrammingTaskMapper(),
                new StructuredAnswerDefinitionMapper(enumValueParser)
        );
        this.routeIntegrityValidator = new ExerciseRouteIntegrityValidator();
    }

    ExerciseJsonLoader(
            ExerciseResourceReader resourceReader,
            ExerciseDefinitionValidator validator,
            ExerciseDtoMapper mapper,
            ExerciseRouteIntegrityValidator routeIntegrityValidator
    ) {
        this.resourceReader = resourceReader;
        this.validator = validator;
        this.mapper = mapper;
        this.routeIntegrityValidator = routeIntegrityValidator;
    }

    public List<Exercise> loadExercises() {
        ExerciseIndexDto indexDto = resourceReader.read(INDEX_PATH, ExerciseIndexDto.class);
        routeIntegrityValidator.validateIndexFiles(indexDto.getFiles());
        List<LoadedExerciseFile> loadedFiles = loadFiles(indexDto.getFiles());
        routeIntegrityValidator.validateUniqueExerciseIds(loadedFiles);
        return mapExercises(loadedFiles);
    }

    private List<LoadedExerciseFile> loadFiles(List<String> filePaths) {
        List<LoadedExerciseFile> loadedFiles = new ArrayList<>();
        for (String filePath : filePaths) {
            ExerciseFileDto fileDto = resourceReader.read(filePath, ExerciseFileDto.class);
            validator.validateFile(
                    filePath,
                    fileDto.getRouteBlockTitle(),
                    fileDto.getCaseStudy(),
                    fileDto.getProgrammingTask(),
                    fileDto.getExercises()
            );
            loadedFiles.add(new LoadedExerciseFile(filePath, fileDto));
        }
        return loadedFiles;
    }

    private List<Exercise> mapExercises(List<LoadedExerciseFile> loadedFiles) {
        List<Exercise> exercises = new ArrayList<>();
        for (LoadedExerciseFile loadedFile : loadedFiles) {
            CaseStudy caseStudy = mapper.mapCaseStudy(
                    loadedFile.fileDto().getRouteBlockTitle(),
                    loadedFile.fileDto().getCaseStudy()
            );
            codeladder.model.ProgrammingTask programmingTask =
                    mapper.mapProgrammingTask(loadedFile.fileDto().getProgrammingTask());
            for (codeladder.data.dto.ExerciseDto exerciseDto : loadedFile.fileDto().getExercises()) {
                exercises.add(mapper.mapExercise(
                        exerciseDto,
                        caseStudy,
                        programmingTask,
                        loadedFile.filePath()
                ));
            }
        }
        return exercises;
    }

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }
}
