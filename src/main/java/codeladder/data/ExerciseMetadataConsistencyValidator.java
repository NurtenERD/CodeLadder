package codeladder.data;

import codeladder.data.dto.ProgrammingTaskDto;
import codeladder.model.ActivityType;
import codeladder.model.ExerciseType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.SkillTag;
import codeladder.model.SupportLevel;

public class ExerciseMetadataConsistencyValidator {

    public void validate(ParsedExerciseMetadata metadata, ProgrammingTaskDto programmingTask, ExerciseValidationContext context) {
        if (metadata.exerciseType() == ExerciseType.OOP_LADDER) {
            validateOopLadder(metadata, context);
            return;
        }
        validateProgrammingRoute(metadata, programmingTask, context);
    }

    private void validateOopLadder(ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        require(metadata.activityType() == ActivityType.OOP_PRACTICE, "OOP_LADDER vereist ActivityType.OOP_PRACTICE", context);
        require(metadata.programmingPattern() == ProgrammingPattern.NONE, "OOP_LADDER vereist ProgrammingPattern.NONE", context);
        require(metadata.supportLevel() == SupportLevel.NOT_SPECIFIED, "OOP_LADDER vereist SupportLevel.NOT_SPECIFIED", context);
    }

    private void validateProgrammingRoute(
            ParsedExerciseMetadata metadata,
            ProgrammingTaskDto programmingTask,
            ExerciseValidationContext context
    ) {
        require(metadata.activityType() != ActivityType.OOP_PRACTICE, "PSEUDOCODE_TO_JAVA mag geen OOP_PRACTICE gebruiken", context);
        require(metadata.programmingPattern() != ProgrammingPattern.NONE, "PSEUDOCODE_TO_JAVA vereist een programmeerpatroon", context);
        require(metadata.supportLevel() != SupportLevel.NOT_SPECIFIED, "PSEUDOCODE_TO_JAVA vereist een supportLevel", context);
        require(!metadata.skillTags().isEmpty(), "PSEUDOCODE_TO_JAVA vereist minimaal één SkillTag", context);
        require(programmingTask != null, "PSEUDOCODE_TO_JAVA zonder ProgrammingTask", context);
        validateTransfer(metadata, context);
    }

    private void validateTransfer(ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        if (!metadata.skillTags().contains(SkillTag.TRANSFER)) {
            return;
        }
        require(metadata.supportLevel() == SupportLevel.INDEPENDENT, "TRANSFER vereist SupportLevel.INDEPENDENT", context);
        require(metadata.skillTags().size() > 1, "TRANSFER vereist ook een inhoudelijke SkillTag", context);
    }

    private void require(boolean condition, String message, ExerciseValidationContext context) {
        if (!condition) {
            throw context.error(message);
        }
    }
}
