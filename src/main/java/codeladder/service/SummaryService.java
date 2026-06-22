package codeladder.service;

import codeladder.model.AttemptResult;
import codeladder.model.Exercise;
import codeladder.model.StudentAnswer;
import codeladder.model.SummaryItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SummaryService {

    public List<SummaryItem> buildSummary(List<Exercise> exercises, Collection<StudentAnswer> answers) {
        Map<String, StudentAnswer> answersById = new LinkedHashMap<>();
        for (StudentAnswer answer : answers) {
            answersById.put(answer.getExerciseId(), answer);
        }

        List<SummaryItem> items = new ArrayList<>();
        for (Exercise exercise : exercises) {
            StudentAnswer answer = answersById.get(exercise.getId());
            if (answer == null) {
                items.add(new SummaryItem(
                        exercise.getStepType().getDisplayName(),
                        exercise.getTitle(),
                        "(niet gemaakt)",
                        "Geen feedback beschikbaar.",
                        0
                ));
                continue;
            }

            AttemptResult latestAttempt = answer.getLatestAttempt();
            items.add(new SummaryItem(
                    exercise.getStepType().getDisplayName(),
                    exercise.getTitle(),
                    latestAttempt.getResponse().toDisplayText(exercise),
                    latestAttempt.getFeedback().getMessage(),
                    answer.getAttemptCount()
            ));
        }
        return items;
    }
}
