package codeladder.controller;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.StudentAnswer;
import codeladder.model.SummaryItem;
import codeladder.model.ValidationResult;
import codeladder.model.StepType;
import codeladder.service.AnswerValidationService;
import codeladder.service.FeedbackService;
import codeladder.service.LearningRouteService;
import codeladder.service.ProgressService;
import codeladder.service.SummaryService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class AppController {
    private static final double SCENE_WIDTH = 1180;
    private static final double SCENE_HEIGHT = 760;
    private static final double MIN_STAGE_WIDTH = 980;
    private static final double MIN_STAGE_HEIGHT = 680;

    private final Stage stage;
    private final LearningRouteService learningRouteService;
    private final ProgressService progressService;
    private final SummaryService summaryService;
    private final MainDashboardController mainDashboardController;
    private final StartScreenController startScreenController;
    private final ExerciseScreenController exerciseScreenController;
    private final SummaryScreenController summaryScreenController;
    private Scene scene;

    public AppController(Stage stage) {
        this.stage = stage;
        FeedbackService feedbackService = new FeedbackService();
        AnswerValidationService validationService = new AnswerValidationService(feedbackService);
        ExerciseDataProvider dataProvider = new ExerciseDataProvider();
        this.learningRouteService = new LearningRouteService(dataProvider, validationService);
        this.progressService = new ProgressService();
        this.summaryService = new SummaryService();
        this.mainDashboardController = new MainDashboardController(
                learningRouteService.getLearningSteps(),
                learningRouteService.getExercises(),
                this::startAtStep,
                this::startAtExercise
        );
        this.startScreenController = new StartScreenController();
        this.exerciseScreenController = new ExerciseScreenController();
        this.summaryScreenController = new SummaryScreenController();
    }

    public void showStartScreen() {
        ensureScene();
        Parent content = startScreenController.createView(
                this::startLearningRoute,
                this::showAboutDialog
        );
        mainDashboardController.showIntroContent(content, learningRouteService.getTotalExercises());
    }

    private void startLearningRoute() {
        learningRouteService.restart();
        progressService.reset();
        showCurrentExercise();
    }

    private void startAtStep(StepType stepType) {
        boolean found = learningRouteService.jumpToFirstExerciseOfStep(stepType);
        if (!found) {
            showAboutDialog();
            return;
        }

        progressService.reset();
        showCurrentExercise();
    }

    private void startAtExercise(String exerciseId) {
        boolean found = learningRouteService.jumpToExercise(exerciseId);
        if (!found) {
            showAboutDialog();
            return;
        }

        progressService.reset();
        showCurrentExercise();
    }

    private void showCurrentExercise() {
        Exercise currentExercise = learningRouteService.getCurrentExercise();
        if (currentExercise == null) {
            showSummary();
            return;
        }

        StudentAnswer studentAnswer = progressService.getStudentAnswer(currentExercise.getId());
        Parent view = exerciseScreenController.createView(
                currentExercise,
                learningRouteService.getCurrentExerciseNumber(),
                learningRouteService.getTotalExercises(),
                studentAnswer,
                this::handleExerciseSubmission,
                this::moveToPreviousExercise,
                this::moveToNextExercise
        );
        mainDashboardController.showExerciseContent(
                currentExercise,
                learningRouteService.getCurrentExerciseNumber(),
                learningRouteService.getTotalExercises(),
                studentAnswer,
                view
        );
    }

    private void handleExerciseSubmission(ExerciseResponse response) {
        Exercise exercise = learningRouteService.getCurrentExercise();
        if (exercise == null) {
            return;
        }

        int attemptNumber = progressService.getAttemptCount(exercise.getId()) + 1;
        ValidationResult validationResult = learningRouteService.validateCurrentExercise(response, attemptNumber);
        progressService.recordAttempt(exercise, response, validationResult);
        showCurrentExercise();
    }

    private void moveToPreviousExercise() {
        if (learningRouteService.moveToPreviousExercise()) {
            showCurrentExercise();
        }
    }
    private void moveToNextExercise() {
        if (learningRouteService.moveToNextExercise()) {
            showCurrentExercise();
            return;
        }
        showSummary();
    }

    private void showSummary() {
        List<SummaryItem> summaryItems =
                summaryService.buildSummary(learningRouteService.getExercises(), progressService.getAllAnswers());
        Parent view = summaryScreenController.createView(
                summaryItems,
                progressService.getReflection(),
                progressService::saveReflection,
                this::showStartScreen
        );
        mainDashboardController.showSummaryContent(view, learningRouteService.getTotalExercises());
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Over CodeLadder");
        alert.setHeaderText("CodeLadder");
        alert.setContentText(
                "CodeLadder helpt beginnende programmeerstudenten rustig nadenken over opdrachten, classes, "
                        + "attributen, constructors, objecten en methodes. Deze MVP bevat Trede 1 t/m 7 met sessie-opslag, feedback "
                        + "en een eindoverzicht."
        );
        alert.showAndWait();
    }

    private void ensureScene() {
        if (scene != null) {
            stage.show();
            return;
        }

        Parent root = mainDashboardController.createView();
        if (!root.getStyleClass().contains("app-root")) {
            root.getStyleClass().add("app-root");
        }

        scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        String stylesheet = Objects.requireNonNull(
                getClass().getResource("/css/codeladder.css"),
                "Stylesheet '/css/codeladder.css' not found"
        ).toExternalForm();
        if (!scene.getStylesheets().contains(stylesheet)) {
            scene.getStylesheets().add(stylesheet);
        }

        stage.setTitle("CodeLadder");
        stage.setMinWidth(MIN_STAGE_WIDTH);
        stage.setMinHeight(MIN_STAGE_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
