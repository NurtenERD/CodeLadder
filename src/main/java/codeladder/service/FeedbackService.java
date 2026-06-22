package codeladder.service;

import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.Feedback;
import codeladder.model.FeedbackType;
import codeladder.model.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class FeedbackService {

    public ValidationResult createEmptyAnswerResult(int attemptNumber) {
        boolean allowRetry = attemptNumber < 2;
        String message = allowRetry
                ? "Er staat nog geen echt antwoord. Zet eerst in je eigen woorden op wat je denkt."
                : "Ga verder met een korte notitie voor jezelf: benoem in elk geval wat de app moet doen of onthouden.";
        FeedbackType type = allowRetry ? FeedbackType.GUIDANCE : FeedbackType.FINAL_HINT;
        return new ValidationResult(false, allowRetry, new Feedback(message, type));
    }

    public ValidationResult createTextFeedback(
            Exercise exercise,
            String answerText,
            int matchedRequirements,
            boolean correct,
            int attemptNumber
    ) {
        if (correct) {
            return new ValidationResult(true, false, new Feedback(exercise.getSuccessFeedback(), FeedbackType.SUCCESS));
        }

        boolean allowRetry = attemptNumber < 2;
        String message = allowRetry ? exercise.getRetryFeedback() : exercise.getFinalFeedback();
        if (answerText.isBlank()) {
            message = allowRetry
                    ? "Je keuze is nog leeg. Schrijf eerst kort op wat je uit de opdracht haalt."
                    : "Neem als vuistregel mee: kijk vooral naar gebruikers, acties en informatie die bewaard moet worden.";
        } else if (matchedRequirements == 0 && allowRetry) {
            message = exercise.getRetryFeedback() + " Probeer minstens twee kernwoorden uit de opdracht terug te laten komen.";
        }
        FeedbackType type = allowRetry ? FeedbackType.GUIDANCE : FeedbackType.FINAL_HINT;
        return new ValidationResult(false, allowRetry, new Feedback(message, type));
    }

    public ValidationResult createCodeFeedback(
            Exercise exercise,
            String answerText,
            int matchedRequirements,
            boolean correct,
            int attemptNumber,
            List<String> missingFragments
    ) {
        if (correct) {
            return new ValidationResult(true, false, new Feedback(exercise.getSuccessFeedback(), FeedbackType.SUCCESS));
        }

        boolean allowRetry = attemptNumber < 2;
        String message = allowRetry
                ? buildCodeRetryMessage(exercise, answerText, matchedRequirements, missingFragments)
                : buildCodeFinalMessage(missingFragments);
        FeedbackType type = allowRetry ? FeedbackType.GUIDANCE : FeedbackType.FINAL_HINT;
        return new ValidationResult(false, allowRetry, new Feedback(message, type));
    }

    public ValidationResult createChoiceFeedback(
            Exercise exercise,
            ExerciseResponse response,
            boolean correct,
            int attemptNumber
    ) {
        if (correct) {
            return new ValidationResult(true, false, new Feedback(exercise.getSuccessFeedback(), FeedbackType.SUCCESS));
        }

        boolean allowRetry = attemptNumber < 2;
        String message = allowRetry ? exercise.getRetryFeedback() : exercise.getFinalFeedback();
        if (!response.getSelectedOptionIds().isEmpty() && exercise.getFocusText().contains("naam")) {
            message = allowRetry
                    ? "Naam is meestal informatie van iets anders. Kijk nog eens welke keuze beter past."
                    : "Neem mee: naam is vaak een attribuut van een object, niet een los ding met eigen verantwoordelijkheid.";
        }
        FeedbackType type = allowRetry ? FeedbackType.GUIDANCE : FeedbackType.FINAL_HINT;
        return new ValidationResult(false, allowRetry, new Feedback(message, type));
    }

    public ValidationResult createSelectionFeedback(
            Exercise exercise,
            ExerciseResponse response,
            boolean correct,
            int overlap,
            int attemptNumber
    ) {
        if (correct) {
            return new ValidationResult(true, false, new Feedback(exercise.getSuccessFeedback(), FeedbackType.SUCCESS));
        }

        boolean allowRetry = attemptNumber < 2;
        String message = allowRetry ? exercise.getRetryFeedback() : exercise.getFinalFeedback();
        if (response.getSelectedOptionIds().size() < 2 && allowRetry) {
            message = "Je keuze is nog smal. Kijk of er nog meer woorden of items zijn die logisch bij deze vraag horen.";
        } else if (overlap == 0 && allowRetry) {
            message = "Je keuze is begrijpelijk, maar kijk nog eens naar woorden die gegevens onthouden of gedrag beschrijven.";
        }
        FeedbackType type = allowRetry ? FeedbackType.GUIDANCE : FeedbackType.FINAL_HINT;
        return new ValidationResult(false, allowRetry, new Feedback(message, type));
    }

    private String buildCodeRetryMessage(
            Exercise exercise,
            String answerText,
            int matchedRequirements,
            List<String> missingFragments
    ) {
        if (answerText.isBlank()) {
            return "Je codevak is nog leeg. Zet eerst de classnaam, het attribuut, de constructor of de methode neer die je hier verwacht.";
        }

        if (matchedRequirements == 0) {
            return exercise.getRetryFeedback();
        }

        List<String> hints = new ArrayList<>();
        for (String fragment : missingFragments) {
            String hint = toCodeHint(fragment);
            if (!hints.contains(hint)) {
                hints.add(hint);
            }
            if (hints.size() == 2) {
                break;
            }
        }

        if (hints.isEmpty()) {
            return exercise.getRetryFeedback();
        }
        return String.join(" ", hints);
    }

    private String buildCodeFinalMessage(List<String> missingFragments) {
        if (missingFragments.isEmpty()) {
            return "Neem mee: kijk bij code naar classnaam, private attributen, constructorregels, `new` en puntnotatie.";
        }

        String firstHint = toCodeHint(missingFragments.get(0));
        if (missingFragments.size() == 1) {
            return firstHint;
        }
        return firstHint + " " + toCodeHint(missingFragments.get(1));
    }

    private String toCodeHint(String fragment) {
        if (fragment.startsWith("public class ")) {
            return "Je classnaam is goed om te controleren. Kijk nog eens of je `public class` met de juiste classnaam gebruikt.";
        }
        if (fragment.startsWith("private ")) {
            return "Let op: attributen zet je hier liever private. Controleer of `" + fragment + "` nog aanwezig is.";
        }
        if (fragment.startsWith("public ") && fragment.contains("(") && !fragment.contains("class")) {
            return "Controleer of je constructornaam of methodenaam goed staat en of de haakjes kloppen.";
        }
        if (fragment.startsWith("this.")) {
            return "Je mist waarschijnlijk nog `" + fragment + "`.";
        }
        if (fragment.startsWith("new ")) {
            return "Controleer of je `new` gebruikt bij het maken van een object.";
        }
        if (fragment.contains(" = new ")) {
            return "Controleer of je eerst het type schrijft, daarna de variabelenaam, en daarna `new` gebruikt.";
        }
        if (fragment.contains(".") && fragment.contains("()")) {
            return "Gebruik puntnotatie: `object.methode()`.";
        }
        return "Controleer of `" + fragment + "` nog in je antwoord staat.";
    }
}
