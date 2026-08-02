package codeladder.support;

import codeladder.model.ActivityType;
import codeladder.model.AnswerOption;
import codeladder.model.CaseStudy;
import codeladder.model.Exercise;
import codeladder.model.ExerciseContent;
import codeladder.model.ExerciseMetadata;
import codeladder.model.ExerciseType;
import codeladder.model.InteractionType;
import codeladder.model.ProgrammingPattern;
import codeladder.model.SupportLevel;
import codeladder.model.ValidationType;

import java.util.List;
import java.util.Set;

import static codeladder.model.StepType.WRITE_SMALL_CLASSES;

public final class ExerciseFixtures {
    private static final CaseStudy CASE_STUDY = new CaseStudy("GymApp-hoofdroute", "GymApp", "Bouw een app voor een sportschool.");

    private ExerciseFixtures() {
    }

    public static Exercise createExercise(
            String id,
            InteractionType interactionType,
            ValidationType validationType,
            List<AnswerOption> options,
            Set<String> correctOptionIds,
            Set<String> acceptedKeywords,
            Set<String> requiredFragments,
            int minimumRequiredMatches,
            String focusText
    ) {
        ExerciseMetadata metadata = new ExerciseMetadata(
                id,
                ExerciseType.OOP_LADDER,
                ActivityType.OOP_PRACTICE,
                interactionType,
                validationType,
                ProgrammingPattern.NONE,
                SupportLevel.NOT_SPECIFIED,
                Set.of()
        );
        return new Exercise(
                id,
                WRITE_SMALL_CLASSES,
                metadata,
                new ExerciseContent(
                        "Testoefening",
                        "Controleer de belangrijkste onderdelen.",
                        "Beantwoord de vraag.",
                        focusText,
                        "",
                        CASE_STUDY
                ),
                options,
                correctOptionIds,
                acceptedKeywords,
                requiredFragments,
                null,
                minimumRequiredMatches,
                "Goed gedaan.",
                "Kijk nog eens goed.",
                "Neem de kernstappen mee.",
                null
        );
    }
}
