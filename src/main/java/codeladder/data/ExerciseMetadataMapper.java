package codeladder.data;

import codeladder.data.dto.ExerciseMetadataDto;
import codeladder.model.ActivityType;
import codeladder.model.ExerciseMetadata;
import codeladder.model.ExerciseType;
import codeladder.model.InteractionType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.SkillTag;
import codeladder.model.SupportLevel;
import codeladder.model.ValidationType;

import java.util.LinkedHashSet;
import java.util.Set;

public class ExerciseMetadataMapper {
    private final EnumValueParser enumValueParser;

    public ExerciseMetadataMapper(EnumValueParser enumValueParser) {
        this.enumValueParser = enumValueParser;
    }

    public ExerciseMetadata map(ExerciseMetadataDto dto, String filePath, String exerciseId) {
        Set<SkillTag> skillTags = new LinkedHashSet<>();
        if (dto.getSkillTags() != null) {
            for (String value : dto.getSkillTags()) {
                skillTags.add(enumValueParser.parse(SkillTag.class, "metadata.skillTags", value, filePath, exerciseId));
            }
        }
        return new ExerciseMetadata(
                dto.getSeriesId(),
                enumValueParser.parse(ExerciseType.class, "metadata.exerciseType", dto.getExerciseType(), filePath, exerciseId),
                enumValueParser.parse(ActivityType.class, "metadata.activityType", dto.getActivityType(), filePath, exerciseId),
                enumValueParser.parse(InteractionType.class, "metadata.interactionType", dto.getInteractionType(), filePath, exerciseId),
                enumValueParser.parse(ValidationType.class, "metadata.validationType", dto.getValidationType(), filePath, exerciseId),
                enumValueParser.parse(ProgrammingPattern.class, "metadata.programmingPattern", dto.getProgrammingPattern(), filePath, exerciseId),
                enumValueParser.parse(SupportLevel.class, "metadata.supportLevel", dto.getSupportLevel(), filePath, exerciseId),
                skillTags
        );
    }
}
