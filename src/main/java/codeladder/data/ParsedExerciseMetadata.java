package codeladder.data;

import codeladder.model.ActivityType;
import codeladder.model.ExerciseType;
import codeladder.model.InteractionType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.SkillTag;
import codeladder.model.SupportLevel;
import codeladder.model.ValidationType;

import java.util.LinkedHashSet;
import java.util.Set;

record ParsedExerciseMetadata(
        ExerciseType exerciseType,
        ActivityType activityType,
        InteractionType interactionType,
        ValidationType validationType,
        ProgrammingPattern programmingPattern,
        SupportLevel supportLevel,
        Set<SkillTag> skillTags
) {
    ParsedExerciseMetadata {
        skillTags = skillTags == null ? Set.of() : new LinkedHashSet<>(skillTags);
    }
}
