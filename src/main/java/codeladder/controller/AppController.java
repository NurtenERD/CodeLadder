package codeladder.controller;

import codeladder.data.ExerciseDataProvider;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.StudentAnswer;
import codeladder.model.SummaryItem;
import codeladder.model.ValidationResult;
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

public class AppController {
    private static final double SCENE_WIDTH = 920;
    private static final double SCENE_HEIGHT = 720;

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
        this.mainDashboardController = new MainDashboardController(learningRouteService.getLearningSteps());
        this.startScreenController = new StartScreenController();
        this.exerciseScreenController = new ExerciseScreenController();
        this.summaryScreenController = new SummaryScreenController();
    }

    public void showStartScreen() {
        ensureScene();
        Parent content = startScreenController.createView(this::startLearningRoute, this::showAboutDialog);
        mainDashboardController.showIntroContent(content, learningRouteService.getTotalExercises());
    }

    private void startLearningRoute() {
        learningRouteService.restart();
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
        scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        stage.setTitle("CodeLadder");
        stage.setScene(scene);
        stage.show();
    }
}
