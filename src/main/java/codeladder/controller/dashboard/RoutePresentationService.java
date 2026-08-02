package codeladder.controller.dashboard;

import codeladder.model.Exercise;
import codeladder.model.ExerciseType;
import codeladder.model.LearningStep;
import codeladder.model.SkillTag;
import codeladder.model.StudentAnswer;
import codeladder.model.StepType;

public class RoutePresentationService {

    public String buildRouteGuidance(Exercise exercise, String routeBlockTitle, int currentNumber, int totalExercises) {
        if ("Leerroute afgerond".equals(routeBlockTitle)) {
            return "Je hebt de leerroute afgerond.";
        }
        if (exercise == null) {
            return "Je werkt eerst door de hoofdroute en past daarna dezelfde denkstappen toe in nieuwe contexten.";
        }
        if (isTransferExercise(exercise, routeBlockTitle)) {
            return "Transfer actief. Je past dezelfde denkstappen nu toe in een nieuwe context. "
                    + "Voortgang: oefening " + currentNumber + " van " + totalExercises + ".";
        }
        if (isProgrammingRoute(exercise)) {
            return "Je werkt in een programmeerleerlijn. Eerst analyseer je de opdracht, later pas pseudocode en Java.";
        }
        return "Je werkt eerst door de GymApp-hoofdroute en past daarna dezelfde stappen toe in transferblokken.";
    }

    public String buildStepText(LearningStep step, StepType currentStepType, String routeBlockTitle, Exercise exercise) {
        if (currentStepType == null) {
            return step.getOrderNumber() + ". " + step.getTitle();
        }
        if (isCurrentStep(step, currentStepType) && isTransferExercise(exercise, routeBlockTitle)) {
            return "[Transfer actief] " + step.getOrderNumber() + ". " + step.getTitle();
        }
        if (isCurrentStep(step, currentStepType) && isProgrammingRoute(exercise)) {
            return "[Programmeerroute actief] " + step.getOrderNumber() + ". " + step.getTitle();
        }
        if (isCurrentStep(step, currentStepType)) {
            return "[Actief] " + step.getOrderNumber() + ". " + step.getTitle();
        }
        if (isCompletedStep(step, currentStepType, routeBlockTitle, exercise)) {
            return "[Klaar] " + step.getOrderNumber() + ". " + step.getTitle();
        }
        return step.getOrderNumber() + ". " + step.getTitle();
    }

    public String buildCurrentStepStatus(Exercise exercise) {
        if (isTransferExercise(exercise, exercise.getCaseStudy().getRouteBlockTitle())) {
            return exercise.getStepType().getDisplayName() + " (transfer)";
        }
        if (isProgrammingRoute(exercise)) {
            return exercise.getStepType().getDisplayName() + " (programmeerroute)";
        }
        return exercise.getStepType().getDisplayName();
    }

    public String buildAttemptText(StudentAnswer studentAnswer) {
        if (studentAnswer == null || studentAnswer.getAttemptCount() == 0) {
            return "Nog geen poging";
        }
        return "Poging " + studentAnswer.getLatestAttempt().getAttemptNumber() + " van 2";
    }

    public String buildFeedbackText(StudentAnswer studentAnswer) {
        if (studentAnswer == null || studentAnswer.getAttemptCount() == 0) {
            return "Feedback verschijnt hier na het controleren van een oefening.";
        }
        return studentAnswer.getLatestAttempt().getFeedback().getMessage();
    }

    public boolean isCurrentStep(LearningStep step, StepType currentStepType) {
        return currentStepType != null && step.getStepType() == currentStepType;
    }

    public boolean isCompletedStep(LearningStep step, StepType currentStepType, String routeBlockTitle, Exercise exercise) {
        return currentStepType != null && isGymAppMainRoute(exercise, routeBlockTitle) && step.getOrderNumber() < currentStepType.getOrderNumber();
    }

    private boolean isGymAppMainRoute(Exercise exercise, String routeBlockTitle) {
        return "GymApp-hoofdroute".equals(routeBlockTitle)
                && exercise != null
                && exercise.getExerciseType() == ExerciseType.OOP_LADDER;
    }

    private boolean isTransferExercise(Exercise exercise, String routeBlockTitle) {
        if (exercise == null) {
            return false;
        }
        if (exercise.getSkillTags().contains(SkillTag.TRANSFER)) {
            return true;
        }
        return exercise.getExerciseType() == ExerciseType.OOP_LADDER
                && routeBlockTitle != null
                && !routeBlockTitle.isBlank()
                && !"GymApp-hoofdroute".equals(routeBlockTitle)
                && !"Leerroute afgerond".equals(routeBlockTitle);
    }

    private boolean isProgrammingRoute(Exercise exercise) {
        return exercise != null
                && exercise.getExerciseType() == ExerciseType.PSEUDOCODE_TO_JAVA
                && !exercise.getSkillTags().contains(SkillTag.TRANSFER);
    }
}
