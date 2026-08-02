package codeladder.data;

import codeladder.data.dto.ExerciseMetadataDto;
import codeladder.model.ActivityType;
import codeladder.model.ExerciseType;
import codeladder.model.InteractionType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.SkillTag;
import codeladder.model.SupportLevel;
import codeladder.model.ValidationType;

import java.util.LinkedHashSet;
import java.util.Set;

public class ExerciseMetadataDefinitionValidator {
    private final EnumValueParser enumValueParser;
    private final ExerciseCompatibilityDefinitionValidator compatibilityValidator;
    private final ExerciseMetadataConsistencyValidator consistencyValidator;

    public ExerciseMetadataDefinitionValidator(
            EnumValueParser enumValueParser,
            ExerciseCompatibilityDefinitionValidator compatibilityValidator,
            ExerciseMetadataConsistencyValidator consistencyValidator
    ) {
        this.enumValueParser = enumValueParser;
        this.compatibilityValidator = compatibilityValidator;
        this.consistencyValidator = consistencyValidator;
    }

    public ParsedExerciseMetadata validate(ExerciseMetadataDto metadata, codeladder.data.dto.ProgrammingTaskDto programmingTask, ExerciseValidationContext context) {
        if (metadata == null) {
            throw context.missingField("metadata");
        }
        requireText(metadata.getSeriesId(), "metadata.seriesId", context);
        if (metadata.getSkillTags() == null) {
            throw context.missingField("metadata.skillTags");
        }
        ExerciseType exerciseType = parse(ExerciseType.class, "metadata.exerciseType", metadata.getExerciseType(), context);
        ActivityType activityType = parse(ActivityType.class, "metadata.activityType", metadata.getActivityType(), context);
        InteractionType interactionType = parse(InteractionType.class, "metadata.interactionType", metadata.getInteractionType(), context);
        ValidationType validationType = parse(ValidationType.class, "metadata.validationType", metadata.getValidationType(), context);
        ProgrammingPattern programmingPattern = parse(ProgrammingPattern.class, "metadata.programmingPattern", metadata.getProgrammingPattern(), context);
        SupportLevel supportLevel = parse(SupportLevel.class, "metadata.supportLevel", metadata.getSupportLevel(), context);
        Set<SkillTag> skillTags = validateSkillTags(metadata, context);
        ParsedExerciseMetadata parsedMetadata = new ParsedExerciseMetadata(
                exerciseType,
                activityType,
                interactionType,
                validationType,
                programmingPattern,
                supportLevel,
                skillTags
        );
        compatibilityValidator.validate(parsedMetadata, context);
        consistencyValidator.validate(parsedMetadata, programmingTask, context);
        return parsedMetadata;
    }

    private Set<SkillTag> validateSkillTags(ExerciseMetadataDto metadata, ExerciseValidationContext context) {
        Set<SkillTag> skillTags = new LinkedHashSet<>();
        for (String skillTag : metadata.getSkillTags()) {
            skillTags.add(parse(SkillTag.class, "metadata.skillTags", skillTag, context));
        }
        return skillTags;
    }

    private <T extends Enum<T>> T parse(Class<T> enumType, String fieldName, String value, ExerciseValidationContext context) {
        return enumValueParser.parse(enumType, fieldName, value, context.filePath(), context.exerciseId());
    }

    private void requireText(String value, String fieldName, ExerciseValidationContext context) {
        if (value == null || value.isBlank()) {
            throw context.missingField(fieldName);
        }
    }
}
