package codeladder.service;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.SkillEvidenceStatus;
import codeladder.model.SkillProgressItem;
import codeladder.support.ValidationServiceFactory;
import codeladder.util.StructuredFieldValueCodec;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillProgressServiceTest {
    private final ExerciseDataProvider provider = new ExerciseDataProvider();
    private final AnswerValidationService validationService = ValidationServiceFactory.create();
    private final ProgressService progressService = new ProgressService();
    private final SkillProgressService skillProgressService = new SkillProgressService();

    @Test
    void supportedExerciseProducesAangetoondMetOndersteuning() {
        record("education-count-passing-01-goal", ExerciseResponse.forSelections(Set.of("passing-count")), 1);
        assertEquals(SkillEvidenceStatus.AANGETOOND_MET_ONDERSTEUNING, status("Doel herkennen"));
    }

    @Test
    void independentExerciseProducesZelfstandigAangetoond() {
        record("education-count-passing-07-independent", ExerciseResponse.forFields(Map.of(
                "goal", "het aantal studenten met minstens 70 punten",
                "input", "een lijst met toetsscores",
                "output", "het aantal geslaagde studenten",
                "rule", "een score telt mee vanaf 70 of hoger",
                "boundary", "70",
                "edgeCase", "lege lijst geeft 0",
                "pattern", "tellen met een voorwaarde"
        )), 1);
        assertEquals(SkillEvidenceStatus.ZELFSTANDIG_AANGETOOND, status("Doel herkennen"));
    }

    @Test
    void transferExerciseProducesTransferAangetoond() {
        record("library-count-overdue-08-transfer", ExerciseResponse.forFields(Map.of(
                "goal", "het aantal boeken dat te laat is",
                "input", "een lijst met te late dagen",
                "output", "het aantal te late boeken",
                "rule", "een boek telt mee als het groter dan 0 is",
                "boundary", "0 telt niet mee",
                "edgeCase", "lege lijst geeft 0",
                "pattern", "tellen met een voorwaarde"
        )), 1);
        assertEquals(SkillEvidenceStatus.TRANSFER_AANGETOOND, status("Transfer toepassen"));
    }

    @Test
    void strongestEvidenceIsPreserved() {
        record("education-count-passing-01-goal", ExerciseResponse.forSelections(Set.of("passing-count")), 1);
        record("education-count-passing-07-independent", ExerciseResponse.forFields(Map.of(
                "goal", "het aantal studenten met minstens 70 punten",
                "input", "een lijst met toetsscores",
                "output", "het aantal geslaagde studenten",
                "rule", "een score telt mee vanaf 70 of hoger",
                "boundary", "70",
                "edgeCase", "lege lijst geeft 0",
                "pattern", "tellen met een voorwaarde"
        )), 1);
        assertEquals(SkillEvidenceStatus.ZELFSTANDIG_AANGETOOND, status("Doel herkennen"));
    }

    @Test
    void incorrectFieldProducesNoEvidenceForThatSkill() {
        record("education-count-passing-05-boundary", ExerciseResponse.forFields(Map.of(
                "selectedGrades", StructuredFieldValueCodec.encodeMultiSelect(Set.of("grade-55", "grade-56")),
                "reason", "onjuist"
        )), 1);
        assertEquals(SkillEvidenceStatus.NOG_NIET_AANGETOOND, status("Grenswaarde herkennen"));
    }

    private void record(String exerciseId, ExerciseResponse response, int attemptNumber) {
        Exercise exercise = provider.getExercises().stream().filter(item -> exerciseId.equals(item.getId())).findFirst().orElseThrow();
        progressService.recordAttempt(exercise, response, validationService.validate(exercise, response, attemptNumber));
    }

    private SkillEvidenceStatus status(String label) {
        Collection<SkillProgressItem> items = skillProgressService.buildSkillProgress(provider.getExercises(), progressService.getAllAnswers());
        return items.stream().filter(item -> label.equals(item.getLabel())).findFirst().orElseThrow().getStatus();
    }
}
