package codeladder.data;

import codeladder.data.dto.CaseStudyDto;
import codeladder.data.dto.ExerciseDto;
import codeladder.data.dto.ExerciseMetadataDto;
import codeladder.model.ActivityType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.SupportLevel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static codeladder.model.ExerciseType.OOP_LADDER;
import static codeladder.model.InteractionType.OPEN_QUESTION;
import static codeladder.model.ValidationType.TEXT_KEYWORDS;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExerciseDefinitionValidatorDelegationTest {

    @Test
    void validatorDelegatesToMetadataAnswerAndProgrammingValidators() {
        TrackingMetadataValidator metadataValidator = new TrackingMetadataValidator();
        TrackingAnswerValidator answerValidator = new TrackingAnswerValidator();
        TrackingProgrammingTaskValidator programmingValidator = new TrackingProgrammingTaskValidator();
        ExerciseDefinitionValidator validator = new ExerciseDefinitionValidator(
                metadataValidator,
                answerValidator,
                programmingValidator
        );

        validator.validateFile("memory.json", "Blok", caseStudy(), null, List.of(validExercise()));

        assertTrue(metadataValidator.called);
        assertTrue(answerValidator.called);
        assertTrue(programmingValidator.called);
    }

    private ExerciseDto validExercise() {
        ExerciseDto dto = new ExerciseDto();
        dto.setId("exercise-1");
        dto.setStepType("UNDERSTAND_ASSIGNMENT");
        dto.setMetadata(metadata());
        dto.setTitle("Titel");
        dto.setInstruction("Instructie");
        dto.setQuestion("Vraag");
        dto.setAcceptedKeywords(List.of("gebruiker"));
        dto.setMinimumRequiredMatches(1);
        dto.setSuccessFeedback("Goed");
        dto.setRetryFeedback("Opnieuw");
        dto.setFinalFeedback("Hint");
        return dto;
    }

    private ExerciseMetadataDto metadata() {
        ExerciseMetadataDto metadata = new ExerciseMetadataDto();
        metadata.setSeriesId("series-1");
        metadata.setExerciseType(OOP_LADDER.name());
        metadata.setActivityType("OOP_PRACTICE");
        metadata.setInteractionType(OPEN_QUESTION.name());
        metadata.setValidationType(TEXT_KEYWORDS.name());
        metadata.setProgrammingPattern("NONE");
        metadata.setSupportLevel("NOT_SPECIFIED");
        metadata.setSkillTags(Set.of());
        return metadata;
    }

    private CaseStudyDto caseStudy() {
        CaseStudyDto caseStudyDto = new CaseStudyDto();
        caseStudyDto.setTitle("Casus");
        caseStudyDto.setDescription("Beschrijving");
        return caseStudyDto;
    }

    private static final class TrackingMetadataValidator extends ExerciseMetadataDefinitionValidator {
        private boolean called;

        private TrackingMetadataValidator() {
            super(
                    new EnumValueParser(),
                    new ExerciseCompatibilityDefinitionValidator(),
                    new ExerciseMetadataConsistencyValidator()
            );
        }

        @Override
        public ParsedExerciseMetadata validate(
                ExerciseMetadataDto metadata,
                codeladder.data.dto.ProgrammingTaskDto programmingTask,
                ExerciseValidationContext context
        ) {
            called = true;
            return new ParsedExerciseMetadata(
                    OOP_LADDER,
                    ActivityType.OOP_PRACTICE,
                    OPEN_QUESTION,
                    TEXT_KEYWORDS,
                    ProgrammingPattern.NONE,
                    SupportLevel.NOT_SPECIFIED,
                    Set.of()
            );
        }
    }

    private static final class TrackingAnswerValidator extends ExerciseAnswerDefinitionValidator {
        private boolean called;

        @Override
        public void validate(ExerciseDto exercise, ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
            called = true;
        }
    }

    private static final class TrackingProgrammingTaskValidator extends ProgrammingTaskDefinitionValidator {
        private boolean called;

        @Override
        public void validate(codeladder.data.dto.ProgrammingTaskDto programmingTask, ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
            called = true;
        }
    }
}
