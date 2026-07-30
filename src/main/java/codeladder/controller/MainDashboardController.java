package codeladder.controller;

import codeladder.model.Exercise;
import codeladder.model.LearningStep;
import codeladder.model.StudentAnswer;
import codeladder.model.StepType;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class MainDashboardController {
    private static final double SIDEBAR_WIDTH = 300;

    private final List<LearningStep> steps;
    private final List<Exercise> exercises;
    private final Consumer<StepType> onDeveloperStartAtStep;
    private final Consumer<String> onDeveloperStartAtExercise;

    private BorderPane root;
    private ScrollPane sidebarScrollPane;
    private VBox stepListBox;
    private StackPane centerPane;
    private Label routeBlockValueLabel;
    private Label routeGuidanceLabel;
    private Label routeBlockStatusValueLabel;
    private Label currentStepValueLabel;
    private Label progressValueLabel;
    private Label attemptValueLabel;
    private Label feedbackValueLabel;

    public MainDashboardController(
            List<LearningStep> steps,
            List<Exercise> exercises,
            Consumer<StepType> onDeveloperStartAtStep,
            Consumer<String> onDeveloperStartAtExercise
    ) {
        this.steps = steps;
        this.exercises = exercises;
        this.onDeveloperStartAtStep = onDeveloperStartAtStep;
        this.onDeveloperStartAtExercise = onDeveloperStartAtExercise;
    }

    public Parent createView() {
        if (root == null) {
            root = createRootLayout();
        }
        if (sidebarScrollPane != null) {
            sidebarScrollPane.setVvalue(0.0);
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
        layout.getStyleClass().add("dashboard-shell");
        layout.setTop(createHeader());
        layout.setLeft(createSidebar());
        layout.setCenter(createCenterPane());
        layout.setBottom(createStatusBar());
        return layout;
    }

    private Parent createHeader() {
        Label titleLabel = new Label("CodeLadder");
        titleLabel.getStyleClass().add("app-title");

        Label subtitleLabel = new Label("Stap voor stap leren denken, ontwerpen en coderen.");
        subtitleLabel.setWrapText(true);
        subtitleLabel.getStyleClass().add("app-subtitle");

        VBox headerBox = new VBox(4, titleLabel, subtitleLabel);
        headerBox.setPadding(new Insets(18, 20, 14, 20));
        headerBox.getStyleClass().add("top-bar");
        return headerBox;
    }

    private Parent createSidebar() {
        Label sidebarTitle = new Label("Leerroute");
        sidebarTitle.getStyleClass().add("sidebar-title");

        Label sidebarIntro = new Label("De treden blijven zichtbaar terwijl je de route doorloopt.");
        configureSidebarLabel(sidebarIntro);
        sidebarIntro.getStyleClass().add("sidebar-subtitle");

        Label routeBlockLabel = new Label("Huidig routeblok");
        configureSidebarLabel(routeBlockLabel);
        routeBlockLabel.getStyleClass().add("route-block-title");

        routeBlockValueLabel = new Label("Nog niet gestart");
        configureSidebarLabel(routeBlockValueLabel);
        routeBlockValueLabel.getStyleClass().add("route-block-value");

        VBox routeBlockBox = new VBox(routeBlockLabel, routeBlockValueLabel);
        routeBlockBox.setFillWidth(true);
        routeBlockBox.setMinWidth(0);
        routeBlockBox.setMaxWidth(Double.MAX_VALUE);
        routeBlockBox.getStyleClass().add("route-block-card");

        routeGuidanceLabel = new Label("Je werkt eerst door de GymApp-hoofdroute en past daarna dezelfde stappen toe in transferblokken.");
        configureSidebarLabel(routeGuidanceLabel);
        routeGuidanceLabel.getStyleClass().add("sidebar-subtitle");

        stepListBox = new VBox();
        stepListBox.setFillWidth(true);
        stepListBox.setMinWidth(0);
        stepListBox.setMaxWidth(Double.MAX_VALUE);
        stepListBox.getStyleClass().add("step-list");

        VBox sidebarContent = new VBox(
                12,
                sidebarTitle,
                sidebarIntro,
                routeBlockBox,
                routeGuidanceLabel,
                stepListBox,
                createDeveloperStepSelection()
        );
        sidebarContent.setFillWidth(true);
        sidebarContent.setMinWidth(0);
        sidebarContent.setMaxWidth(Double.MAX_VALUE);
        sidebarContent.setPadding(new Insets(18));

        sidebarScrollPane = new ScrollPane(sidebarContent);
        sidebarScrollPane.setFitToWidth(true);
        sidebarScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sidebarScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sidebarScrollPane.setPannable(false);
        sidebarScrollPane.setMinWidth(SIDEBAR_WIDTH);
        sidebarScrollPane.setPrefWidth(SIDEBAR_WIDTH);
        sidebarScrollPane.setMaxWidth(SIDEBAR_WIDTH);
        sidebarScrollPane.setVvalue(0.0);
        sidebarScrollPane.getStyleClass().add("sidebar");
        return sidebarScrollPane;
    }

    private Parent createDeveloperStepSelection() {
        Label developerTitle = new Label("Developer testmodus");
        developerTitle.getStyleClass().add("route-block-title");

        Label developerText = new Label("Snel naar een trede of onderwerp om te testen.");
        configureSidebarLabel(developerText);
        developerText.getStyleClass().add("sidebar-subtitle");

        Accordion stepAccordion = new Accordion();
        stepAccordion.setMinWidth(0);
        stepAccordion.setMaxWidth(Double.MAX_VALUE);

        for (StepType stepType : StepType.values()) {
            stepAccordion.getPanes().add(createDeveloperStepPane(stepType));
        }

        VBox developerBox = new VBox(8, developerTitle, developerText, stepAccordion);
        developerBox.setFillWidth(true);
        developerBox.setMinWidth(0);
        developerBox.setMaxWidth(Double.MAX_VALUE);
        developerBox.setPadding(new Insets(12, 0, 0, 0));
        return developerBox;
    }

    private TitledPane createDeveloperStepPane(StepType stepType) {
        VBox contentBox = new VBox(6);
        contentBox.setFillWidth(true);
        contentBox.setMinWidth(0);
        contentBox.setMaxWidth(Double.MAX_VALUE);

        Button startStepButton = new Button("Start Trede " + stepType.getOrderNumber());
        startStepButton.setMinWidth(0);
        startStepButton.setMaxWidth(Double.MAX_VALUE);
        startStepButton.setWrapText(true);
        startStepButton.setOnAction(event -> onDeveloperStartAtStep.accept(stepType));
        contentBox.getChildren().add(startStepButton);

        for (Exercise exercise : exercises) {
            if (exercise.getStepType() != stepType) {
                continue;
            }

            Button exerciseButton = new Button(buildDeveloperExerciseText(exercise));
            exerciseButton.setMinWidth(0);
            exerciseButton.setMaxWidth(Double.MAX_VALUE);
            exerciseButton.setWrapText(true);
            exerciseButton.setOnAction(event -> onDeveloperStartAtExercise.accept(exercise.getId()));
            contentBox.getChildren().add(exerciseButton);
        }

        TitledPane titledPane = new TitledPane(
                stepType.getDisplayName(),
                contentBox
        );
        titledPane.setExpanded(false);
        titledPane.setMinWidth(0);
        titledPane.setMaxWidth(Double.MAX_VALUE);
        return titledPane;
    }

    private String buildDeveloperExerciseText(Exercise exercise) {
        String caseStudyTitle = exercise.getCaseStudy().getTitle();
        if (caseStudyTitle == null || caseStudyTitle.isBlank()) {
            return exercise.getTitle();
        }
        return caseStudyTitle + " - " + exercise.getTitle();
    }

    private Parent createCenterPane() {
        centerPane = new StackPane();
        centerPane.setMinWidth(0);
        centerPane.setPadding(new Insets(0, 20, 0, 0));
        centerPane.getStyleClass().add("content-area");
        return centerPane;
    }

    private Parent createStatusBar() {
        Label routeBlockStatusLabel = new Label("Routeblok:");
        routeBlockStatusLabel.getStyleClass().add("status-caption");
        routeBlockStatusValueLabel = new Label("Nog niet gestart");
        routeBlockStatusValueLabel.setWrapText(true);
        routeBlockStatusValueLabel.setMinWidth(0);
        routeBlockStatusValueLabel.setMaxWidth(Double.MAX_VALUE);
        routeBlockStatusValueLabel.getStyleClass().add("status-value");
        VBox routeBlockSection = new VBox(routeBlockStatusLabel, routeBlockStatusValueLabel);
        routeBlockSection.setMinWidth(0);
        routeBlockSection.setMaxWidth(Double.MAX_VALUE);
        routeBlockSection.getStyleClass().add("status-section");
        HBox.setHgrow(routeBlockSection, Priority.ALWAYS);

        Label currentStepLabel = new Label("Huidige trede:");
        currentStepLabel.getStyleClass().add("status-caption");
        currentStepValueLabel = new Label("Nog niet gestart");
        currentStepValueLabel.setWrapText(true);
        currentStepValueLabel.setMinWidth(0);
        currentStepValueLabel.setMaxWidth(Double.MAX_VALUE);
        currentStepValueLabel.getStyleClass().add("status-value");
        VBox currentStepSection = new VBox(currentStepLabel, currentStepValueLabel);
        currentStepSection.setMinWidth(0);
        currentStepSection.setMaxWidth(Double.MAX_VALUE);
        currentStepSection.getStyleClass().add("status-section");
        HBox.setHgrow(currentStepSection, Priority.ALWAYS);

        Label progressLabel = new Label("Voortgang:");
        progressLabel.getStyleClass().add("status-caption");
        progressValueLabel = new Label("0 van 0");
        progressValueLabel.getStyleClass().add("status-value");
        VBox progressSection = new VBox(progressLabel, progressValueLabel);
        progressSection.getStyleClass().add("status-section");

        Label attemptLabel = new Label("Poging:");
        attemptLabel.getStyleClass().add("status-caption");
        attemptValueLabel = new Label("Nog geen poging");
        attemptValueLabel.setWrapText(true);
        attemptValueLabel.setMinWidth(0);
        attemptValueLabel.setMaxWidth(Double.MAX_VALUE);
        attemptValueLabel.getStyleClass().add("status-value");
        VBox attemptSection = new VBox(attemptLabel, attemptValueLabel);
        attemptSection.setMinWidth(0);
        attemptSection.setMaxWidth(Double.MAX_VALUE);
        attemptSection.getStyleClass().add("status-section");
        HBox.setHgrow(attemptSection, Priority.SOMETIMES);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topRow = new HBox(
                18,
                routeBlockSection,
                currentStepSection,
                progressSection,
                attemptSection,
                spacer
        );

        Label feedbackLabel = new Label("Feedback:");
        feedbackLabel.getStyleClass().add("status-caption");
        feedbackValueLabel = new Label("Feedback verschijnt hier na het controleren van een oefening.");
        feedbackValueLabel.setWrapText(true);
        feedbackValueLabel.setMinWidth(0);
        feedbackValueLabel.setMaxWidth(Double.MAX_VALUE);
        feedbackValueLabel.getStyleClass().add("status-value");

        VBox feedbackSection = new VBox(feedbackLabel, feedbackValueLabel);
        feedbackSection.setMinWidth(0);
        feedbackSection.setMaxWidth(Double.MAX_VALUE);
        feedbackSection.getStyleClass().add("status-section");

        VBox statusBox = new VBox(topRow, feedbackSection);
        statusBox.getStyleClass().add("status-bar");
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
            configureSidebarLabel(stepLabel);
            stepLabel.getStyleClass().add("step-title");

            Label descriptionLabel = new Label(step.getDescription());
            configureSidebarLabel(descriptionLabel);
            descriptionLabel.getStyleClass().add("step-description");

            VBox stepBox = new VBox(stepLabel, descriptionLabel);
            stepBox.setFillWidth(true);
            stepBox.setMinWidth(0);
            stepBox.setMaxWidth(Double.MAX_VALUE);
            stepBox.getStyleClass().add("step-item");

            if (isCurrentStep(step, currentStepType)) {
                stepBox.getStyleClass().add("step-item-active");
            } else if (isCompletedStep(step, currentStepType, routeBlockTitle)) {
                stepBox.getStyleClass().add("step-item-completed");
            }

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

    private void configureSidebarLabel(Label label) {
        label.setWrapText(true);
        label.setMinWidth(0);
        label.setMaxWidth(Double.MAX_VALUE);
    }

    private void updateStatus(
            String routeBlock,
            String currentStep,
            String progress,
            String attemptText,
            String feedbackText
    ) {
        routeBlockValueLabel.setText(routeBlock);
        routeBlockStatusValueLabel.setText(routeBlock);
        currentStepValueLabel.setText(currentStep);
        progressValueLabel.setText(progress);
        attemptValueLabel.setText(attemptText);
        feedbackValueLabel.setText(feedbackText);
    }
}
