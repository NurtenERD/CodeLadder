package codeladder.data;

import codeladder.data.dto.ExerciseFileDto;
import codeladder.data.dto.ExerciseIndexDto;
import codeladder.support.ExerciseDtoFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseJsonLoaderRouteIntegrityTest {

    @Test
    void duplicateExerciseIdAcrossDifferentFilesIsRejected() {
        ExerciseIndexDto indexDto = ExerciseDtoFactory.indexDto("exercises/a.json", "exercises/b.json");
        ExerciseFileDto firstFile = ExerciseDtoFactory.fileDto(
                "Blok A",
                ExerciseDtoFactory.validExercise("dup-id", codeladder.model.InteractionType.OPEN_QUESTION, codeladder.model.ValidationType.TEXT_KEYWORDS)
        );
        ExerciseFileDto secondFile = ExerciseDtoFactory.fileDto(
                "Blok B",
                ExerciseDtoFactory.validExercise("dup-id", codeladder.model.InteractionType.OPEN_QUESTION, codeladder.model.ValidationType.TEXT_KEYWORDS)
        );

        ExerciseJsonLoader loader = createLoader(indexDto, Map.of(
                "exercises/a.json", firstFile,
                "exercises/b.json", secondFile
        ));

        ExerciseConfigurationException exception = assertThrows(ExerciseConfigurationException.class, loader::loadExercises);
        assertTrue(exception.getMessage().contains("dup-id"));
        assertTrue(exception.getMessage().contains("exercises/a.json"));
        assertTrue(exception.getMessage().contains("exercises/b.json"));
    }

    @Test
    void duplicateResourcePathInIndexIsRejected() {
        ExerciseIndexDto indexDto = ExerciseDtoFactory.indexDto("exercises/a.json", "exercises/a.json");
        ExerciseJsonLoader loader = createLoader(indexDto, Map.of(
                "exercises/a.json",
                ExerciseDtoFactory.fileDto("Blok A", ExerciseDtoFactory.validExercise(
                        "id-1",
                        codeladder.model.InteractionType.OPEN_QUESTION,
                        codeladder.model.ValidationType.TEXT_KEYWORDS
                ))
        ));

        ExerciseConfigurationException exception = assertThrows(ExerciseConfigurationException.class, loader::loadExercises);
        assertTrue(exception.getMessage().contains("Dubbele resourceverwijzing"));
        assertTrue(exception.getMessage().contains("exercises/a.json"));
    }

    private ExerciseJsonLoader createLoader(ExerciseIndexDto indexDto, Map<String, ExerciseFileDto> fileDtos) {
        EnumValueParser parser = new EnumValueParser();
        ExerciseDefinitionValidator validator = new ExerciseDefinitionValidator(parser);
        ExerciseDtoMapper mapper = new ExerciseDtoMapper(
                parser,
                new ExerciseMetadataMapper(parser),
                new ProgrammingTaskMapper(),
                new StructuredAnswerDefinitionMapper(parser)
        );
        return new ExerciseJsonLoader(
                new FakeResourceReader(indexDto, fileDtos),
                validator,
                mapper,
                new ExerciseRouteIntegrityValidator()
        );
    }

    private static final class FakeResourceReader extends ExerciseResourceReader {
        private final ExerciseIndexDto indexDto;
        private final Map<String, ExerciseFileDto> fileDtos;

        private FakeResourceReader(ExerciseIndexDto indexDto, Map<String, ExerciseFileDto> fileDtos) {
            super(new ObjectMapper());
            this.indexDto = indexDto;
            this.fileDtos = new HashMap<>(fileDtos);
        }

        @Override
        public <T> T read(String resourcePath, Class<T> targetType) {
            Object value = ExerciseJsonLoader.INDEX_PATH.equals(resourcePath) ? indexDto : fileDtos.get(resourcePath);
            return targetType.cast(value);
        }
    }
}
