package codeladder.controller;

import codeladder.model.Exercise;
import codeladder.model.LearningStep;
import codeladder.model.StudentAnswer;
import codeladder.model.StepType;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainDashboardController {
    private final List<LearningStep> steps;

    private BorderPane root;
    private VBox stepListBox;
    private StackPane centerPane;
    private Label routeBlockValueLabel;
    private Label routeGuidanceLabel;
    private Label currentStepValueLabel;
    private Label progressValueLabel;
    private Label attemptValueLabel;
    private Label feedbackValueLabel;

    public MainDashboardController(List<LearningStep> steps) {
        this.steps = steps;
    }

    public Parent createView() {
        if (root == null) {
            root = createRootLayout();
        }
        return root;
    }

    public void showIntroContent(Parent content, int totalExercises) {
        centerPane.getChildren().setAll(content);
        refreshStepOverview(null, "", 0, totalExercises);
        updateStatus(
                "Nog niet gestart",
                "Kies Start leerroute",
                "0 van " + totalExercises,
                "Nog geen poging",
                "Feedback verschijnt hier na het controleren van een oefening."
        );
    }

    public void showExerciseContent(
            Exercise exercise,
            int currentNumber,
            int totalExercises,
            StudentAnswer studentAnswer,
            Parent content
    ) {
        centerPane.getChildren().setAll(content);
        refreshStepOverview(
                exercise.getStepType(),
                exercise.getCaseStudy().getRouteBlockTitle(),
                currentNumber,
                totalExercises
        );
        updateStatus(
                exercise.getCaseStudy().getRouteBlockTitle(),
                buildCurrentStepStatus(exercise),
                currentNumber + " van " + totalExercises,
                buildAttemptText(studentAnswer),
                buildFeedbackText(studentAnswer)
        );
    }

    public void showSummaryContent(Parent content, int totalExercises) {
        centerPane.getChildren().setAll(content);
        refreshStepOverview(null, "Leerroute afgerond", totalExercises, totalExercises);
        updateStatus(
                "Leerroute afgerond",
                "Eindoverzicht",
                totalExercises + " van " + totalExercises,
                "Reflectie en herstart beschikbaar",
                "Je ziet hier je antwoorden, feedback en reflectie in dezelfde leeromgeving."
        );
    }

    private BorderPane createRootLayout() {
        BorderPane layout = new BorderPane();
        layout.setTop(createHeader());
        layout.setLeft(createSidebar());
        layout.setCenter(createCenterPane());
        layout.setBottom(createStatusBar());
        return layout;
    }

    private Parent createHeader() {
        Label titleLabel = new Label("CodeLadder");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Stap voor stap leren denken, ontwerpen en coderen.");
        subtitleLabel.setWrapText(true);

        VBox headerBox = new VBox(4, titleLabel, subtitleLabel);
        headerBox.setPadding(new Insets(18, 20, 14, 20));
        return headerBox;
    }

    private Parent createSidebar() {
        Label sidebarTitle = new Label("Leerroute");
        sidebarTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label sidebarIntro = new Label("De treden blijven zichtbaar terwijl je de route doorloopt.");
        sidebarIntro.setWrapText(true);

        Label routeBlockLabel = new Label("Huidig routeblok");
        routeBlockLabel.setStyle("-fx-font-weight: bold;");

        routeBlockValueLabel = new Label("Nog niet gestart");
        routeBlockValueLabel.setWrapText(true);

        routeGuidanceLabel = new Label("Je werkt eerst door de GymApp-hoofdroute en past daarna dezelfde stappen toe in transferblokken.");
        routeGuidanceLabel.setWrapText(true);

        stepListBox = new VBox(8);

        VBox sidebarContent = new VBox(
                12,
                sidebarTitle,
                sidebarIntro,
                routeBlockLabel,
                routeBlockValueLabel,
                routeGuidanceLabel,
                stepListBox
        );
        sidebarContent.setPadding(new Insets(18));

        ScrollPane scrollPane = new ScrollPane(sidebarContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(280);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");
        return scrollPane;
    }

    private Parent createCenterPane() {
        centerPane = new StackPane();
        centerPane.setPadding(new Insets(0, 20, 0, 0));
        return centerPane;
    }

    private Parent createStatusBar() {
        Label currentStepLabel = new Label("Huidige trede:");
        currentStepLabel.setStyle("-fx-font-weight: bold;");
        currentStepValueLabel = new Label("Nog niet gestart");

        Label progressLabel = new Label("Voortgang:");
        progressLabel.setStyle("-fx-font-weight: bold;");
        progressValueLabel = new Label("0 van 0");

        Label attemptLabel = new Label("Poging:");
        attemptLabel.setStyle("-fx-font-weight: bold;");
        attemptValueLabel = new Label("Nog geen poging");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topRow = new HBox(
                8,
                currentStepLabel,
                currentStepValueLabel,
                new Label("|"),
                progressLabel,
                progressValueLabel,
                new Label("|"),
                attemptLabel,
                attemptValueLabel,
                spacer
        );

        Label feedbackLabel = new Label("Feedback:");
        feedbackLabel.setStyle("-fx-font-weight: bold;");
        feedbackValueLabel = new Label("Feedback verschijnt hier na het controleren van een oefening.");
        feedbackValueLabel.setWrapText(true);

        VBox statusBox = new VBox(8, topRow, feedbackLabel, feedbackValueLabel);
        statusBox.setPadding(new Insets(14, 20, 18, 20));
        return statusBox;
    }

    private void refreshStepOverview(
            StepType currentStepType,
            String routeBlockTitle,
            int currentNumber,
            int totalExercises
    ) {
        stepListBox.getChildren().clear();
        routeGuidanceLabel.setText(buildRouteGuidance(routeBlockTitle, currentNumber, totalExercises));

        for (LearningStep step : steps) {
            Label stepLabel = new Label(buildStepText(step, currentStepType, routeBlockTitle));
            stepLabel.setWrapText(true);

            if (isCurrentStep(step, currentStepType)) {
                stepLabel.setStyle("-fx-font-weight: bold;");
            } else if (isCompletedStep(step, currentStepType, routeBlockTitle)) {
                stepLabel.setStyle("-fx-text-fill: #3c6e3c;");
            }

            Label descriptionLabel = new Label(step.getDescription());
            descriptionLabel.setWrapText(true);

            VBox stepBox = new VBox(3, stepLabel, descriptionLabel);
            stepListBox.getChildren().add(stepBox);
        }
    }

    private String buildStepText(LearningStep step, StepType currentStepType, String routeBlockTitle) {
        if (currentStepType == null) {
            return step.getOrderNumber() + ". " + step.getTitle();
        }
        if (isCurrentStep(step, currentStepType) && isTransferBlock(routeBlockTitle)) {
            return "[Transfer actief] " + step.getOrderNumber() + ". " + step.getTitle();
        }
        if (isCurrentStep(step, currentStepType)) {
            return "[Actief] " + step.getOrderNumber() + ". " + step.getTitle();
        }
        if (isCompletedStep(step, currentStepType, routeBlockTitle)) {
            return "[Klaar] " + step.getOrderNumber() + ". " + step.getTitle();
        }
        return step.getOrderNumber() + ". " + step.getTitle();
    }

    private boolean isCompletedStep(LearningStep step, StepType currentStepType, String routeBlockTitle) {
        return currentStepType != null
                && isGymAppMainRoute(routeBlockTitle)
                && step.getOrderNumber() < currentStepType.getOrderNumber();
    }

    private boolean isCurrentStep(LearningStep step, StepType currentStepType) {
        return currentStepType != null && step.getStepType() == currentStepType;
    }

    private boolean isGymAppMainRoute(String routeBlockTitle) {
        return "GymApp-hoofdroute".equals(routeBlockTitle);
    }

    private boolean isTransferBlock(String routeBlockTitle) {
        return routeBlockTitle != null
                && !routeBlockTitle.isBlank()
                && !isGymAppMainRoute(routeBlockTitle)
                && !"Leerroute afgerond".equals(routeBlockTitle);
    }

    private String buildRouteGuidance(String routeBlockTitle, int currentNumber, int totalExercises) {
        if (isTransferBlock(routeBlockTitle)) {
            return "GymApp-hoofdroute afgerond. Je past de stappen nu toe in een nieuwe context. "
                    + "Voortgang: oefening " + currentNumber + " van " + totalExercises + ".";
        }
        if ("Leerroute afgerond".equals(routeBlockTitle)) {
            return "Je hebt de hoofdroute en transferblokken afgerond.";
        }
        return "Je werkt eerst door de GymApp-hoofdroute en past daarna dezelfde stappen toe in transferblokken.";
    }

    private String buildCurrentStepStatus(Exercise exercise) {
        if (isTransferBlock(exercise.getCaseStudy().getRouteBlockTitle())) {
            return exercise.getStepType().getDisplayName() + " (transfer)";
        }
        return exercise.getStepType().getDisplayName();
    }

    private String buildAttemptText(StudentAnswer studentAnswer) {
        if (studentAnswer == null || studentAnswer.getAttemptCount() == 0) {
            return "Nog geen poging";
        }
        return "Poging " + studentAnswer.getLatestAttempt().getAttemptNumber() + " van 2";
    }

    private String buildFeedbackText(StudentAnswer studentAnswer) {
        if (studentAnswer == null || studentAnswer.getAttemptCount() == 0) {
            return "Feedback verschijnt hier na het controleren van een oefening.";
        }
        return studentAnswer.getLatestAttempt().getFeedback().getMessage();
    }

    private void updateStatus(
            String routeBlock,
            String currentStep,
            String progress,
            String attemptText,
            String feedbackText
    ) {
        routeBlockValueLabel.setText(routeBlock);
        currentStepValueLabel.setText(currentStep);
        progressValueLabel.setText(progress);
        attemptValueLabel.setText(attemptText);
        feedbackValueLabel.setText(feedbackText);
    }
}
