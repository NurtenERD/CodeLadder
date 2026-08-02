package codeladder.service;

import codeladder.model.AttemptResult;
import codeladder.model.Exercise;
import codeladder.model.ExerciseType;
import codeladder.model.FieldValidationOutcome;
import codeladder.model.SkillEvidenceStatus;
import codeladder.model.SkillProgressItem;
import codeladder.model.SkillTag;
import codeladder.model.StudentAnswer;
import codeladder.model.SupportLevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SkillProgressService {
    private static final Map<SkillTag, String> LABELS = Map.of(
            SkillTag.GOAL, "Doel herkennen",
            SkillTag.INPUT, "Invoer herkennen",
            SkillTag.OUTPUT, "Uitvoer herkennen",
            SkillTag.RULE, "Regel herkennen",
            SkillTag.BOUNDARY, "Grenswaarde herkennen",
            SkillTag.EDGE_CASE, "Randgevallen herkennen",
            SkillTag.PATTERN_RECOGNITION, "Programmeerpatroon herkennen",
            SkillTag.TRANSFER, "Transfer toepassen"
    );

    public List<SkillProgressItem> buildSkillProgress(List<Exercise> exercises, Collection<StudentAnswer> answers) {
        Map<String, StudentAnswer> answersById = new LinkedHashMap<>();
        for (StudentAnswer answer : answers) {
            answersById.put(answer.getExerciseId(), answer);
        }
        Map<SkillTag, SkillEvidenceStatus> statuses = createDefaultStatuses();
        for (Exercise exercise : exercises) {
            if (exercise.getExerciseType() != ExerciseType.PSEUDOCODE_TO_JAVA) {
                continue;
            }
            StudentAnswer answer = answersById.get(exercise.getId());
            if (answer == null || answer.getAttempts().isEmpty()) {
                continue;
            }
            promoteStatuses(statuses, exercise, answer.getLatestAttempt());
        }
        return toItems(statuses);
    }

    private void promoteStatuses(Map<SkillTag, SkillEvidenceStatus> statuses, Exercise exercise, AttemptResult attempt) {
        SkillEvidenceStatus evidence = evidenceFor(exercise);
        boolean transferExercise = exercise.getSkillTags().contains(SkillTag.TRANSFER);
        if (!attempt.getDetails().getFieldOutcomes().isEmpty()) {
            promoteFieldEvidence(statuses, attempt, evidence, transferExercise);
            return;
        }
        if (!attempt.isCorrect()) {
            return;
        }
        for (SkillTag skillTag : exercise.getSkillTags()) {
            statuses.put(skillTag, strongest(statuses.get(skillTag), evidence));
        }
    }

    private void promoteFieldEvidence(
            Map<SkillTag, SkillEvidenceStatus> statuses,
            AttemptResult attempt,
            SkillEvidenceStatus evidence,
            boolean transferExercise
    ) {
        Map<SkillTag, Boolean> correctnessBySkill = new LinkedHashMap<>();
        boolean anyCorrectField = false;
        for (FieldValidationOutcome outcome : attempt.getDetails().getFieldOutcomes().values()) {
            anyCorrectField = anyCorrectField || outcome.isCorrect();
            correctnessBySkill.merge(outcome.getSkillTag(), outcome.isCorrect(), Boolean::logicalAnd);
        }
        for (Map.Entry<SkillTag, Boolean> entry : correctnessBySkill.entrySet()) {
            if (entry.getValue()) {
                statuses.put(entry.getKey(), strongest(statuses.get(entry.getKey()), evidence));
            }
        }
        if (transferExercise && anyCorrectField) {
            statuses.put(SkillTag.TRANSFER, SkillEvidenceStatus.TRANSFER_AANGETOOND);
        }
    }

    private SkillEvidenceStatus evidenceFor(Exercise exercise) {
        if (exercise.getSupportLevel() == SupportLevel.INDEPENDENT && exercise.getSkillTags().contains(SkillTag.TRANSFER)) {
            return SkillEvidenceStatus.TRANSFER_AANGETOOND;
        }
        if (exercise.getSupportLevel() == SupportLevel.INDEPENDENT) {
            return SkillEvidenceStatus.ZELFSTANDIG_AANGETOOND;
        }
        return SkillEvidenceStatus.AANGETOOND_MET_ONDERSTEUNING;
    }

    private Map<SkillTag, SkillEvidenceStatus> createDefaultStatuses() {
        Map<SkillTag, SkillEvidenceStatus> statuses = new LinkedHashMap<>();
        for (SkillTag skillTag : List.of(
                SkillTag.GOAL,
                SkillTag.INPUT,
                SkillTag.OUTPUT,
                SkillTag.RULE,
                SkillTag.BOUNDARY,
                SkillTag.EDGE_CASE,
                SkillTag.PATTERN_RECOGNITION,
                SkillTag.TRANSFER
        )) {
            statuses.put(skillTag, SkillEvidenceStatus.NOG_NIET_AANGETOOND);
        }
        return statuses;
    }

    private List<SkillProgressItem> toItems(Map<SkillTag, SkillEvidenceStatus> statuses) {
        List<SkillProgressItem> items = new ArrayList<>();
        for (Map.Entry<SkillTag, SkillEvidenceStatus> entry : statuses.entrySet()) {
            items.add(new SkillProgressItem(LABELS.get(entry.getKey()), entry.getValue()));
        }
        return items;
    }

    private SkillEvidenceStatus strongest(SkillEvidenceStatus current, SkillEvidenceStatus candidate) {
        return rank(candidate) > rank(current) ? candidate : current;
    }

    private int rank(SkillEvidenceStatus status) {
        return switch (status) {
            case NOG_NIET_AANGETOOND -> 0;
            case AANGETOOND_MET_ONDERSTEUNING -> 1;
            case ZELFSTANDIG_AANGETOOND -> 2;
            case TRANSFER_AANGETOOND -> 3;
        };
    }
}
