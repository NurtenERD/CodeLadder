package codeladder.data;

import codeladder.data.dto.CaseStudyDto;
import codeladder.data.dto.ExerciseDto;
import codeladder.data.dto.ProgrammingTaskDto;

import java.util.List;

public class ExerciseDefinitionValidator {
    private final ExerciseMetadataDefinitionValidator metadataValidator;
    private final ExerciseAnswerDefinitionValidator answerValidator;
    private final ProgrammingTaskDefinitionValidator programmingTaskValidator;

    public ExerciseDefinitionValidator(EnumValueParser enumValueParser) {
        this(
                new ExerciseMetadataDefinitionValidator(
                        enumValueParser,
                        new ExerciseCompatibilityDefinitionValidator(),
                        new ExerciseMetadataConsistencyValidator()
                ),
                new ExerciseAnswerDefinitionValidator(),
                new ProgrammingTaskDefinitionValidator()
        );
    }

    ExerciseDefinitionValidator(
            ExerciseMetadataDefinitionValidator metadataValidator,
            ExerciseAnswerDefinitionValidator answerValidator,
            ProgrammingTaskDefinitionValidator programmingTaskValidator
    ) {
        this.metadataValidator = metadataValidator;
        this.answerValidator = answerValidator;
        this.programmingTaskValidator = programmingTaskValidator;
    }

    public void validateFile(
            String filePath,
            String routeBlockTitle,
            CaseStudyDto caseStudy,
            ProgrammingTaskDto programmingTask,
            List<ExerciseDto> exercises
    ) {
        ExerciseValidationContext fileContext = new ExerciseValidationContext(filePath, null);
        requireText(routeBlockTitle, "routeBlockTitle", fileContext);
        validateCaseStudy(caseStudy, fileContext);
        if (exercises == null || exercises.isEmpty()) {
            throw fileContext.error("Geen oefeningen gevonden");
        }
        for (ExerciseDto exercise : exercises) {
            validateExercise(exercise, programmingTask, fileContext.withExercise(exercise.getId()));
        }
    }

    private void validateCaseStudy(CaseStudyDto caseStudy, ExerciseValidationContext context) {
        if (caseStudy == null) {
            throw context.missingField("caseStudy");
        }
        requireText(caseStudy.getTitle(), "caseStudy.title", context);
        requireText(caseStudy.getDescription(), "caseStudy.description", context);
    }

    private void validateExercise(ExerciseDto exercise, ProgrammingTaskDto programmingTask, ExerciseValidationContext context) {
        requireText(context.exerciseId(), "id", context);
        requireText(exercise.getStepType(), "stepType", context);
        requireText(exercise.getTitle(), "title", context);
        requireText(exercise.getInstruction(), "instruction", context);
        requireText(exercise.getQuestion(), "question", context);
        requireText(exercise.getSuccessFeedback(), "successFeedback", context);
        requireText(exercise.getRetryFeedback(), "retryFeedback", context);
        requireText(exercise.getFinalFeedback(), "finalFeedback", context);
        ParsedExerciseMetadata parsedMetadata = metadataValidator.validate(exercise.getMetadata(), programmingTask, context);
        answerValidator.validate(exercise, parsedMetadata, context);
        programmingTaskValidator.validate(programmingTask, parsedMetadata, context);
    }

    private void requireText(String value, String fieldName, ExerciseValidationContext context) {
        if (value == null || value.isBlank()) {
            throw context.missingField(fieldName);
        }
    }
}
