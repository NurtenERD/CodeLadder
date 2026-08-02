package codeladder.controller.dashboard;

import codeladder.model.Exercise;
import codeladder.model.StepType;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class DeveloperTestMenu {
    private final List<Exercise> exercises;
    private DeveloperNavigation navigation;
    private boolean bound;

    public DeveloperTestMenu(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void bindNavigation(DeveloperNavigation navigation) {
        if (bound) {
            throw new IllegalStateException("DeveloperNavigation is al gebonden");
        }
        if (navigation == null) {
            throw new IllegalArgumentException("DeveloperNavigation mag niet null zijn");
        }
        this.navigation = navigation;
        this.bound = true;
    }

    public Parent createView() {
        Label developerTitle = new Label("Developer testmodus");
        developerTitle.getStyleClass().add("route-block-title");
        Label developerText = label("Snel naar een trede of onderwerp om te testen.", "sidebar-subtitle");
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
        contentBox.getChildren().add(startStepButton(stepType));
        for (Exercise exercise : exercises) {
            if (exercise.getStepType() == stepType) {
                contentBox.getChildren().add(exerciseButton(exercise));
            }
        }
        TitledPane titledPane = new TitledPane(stepType.getDisplayName(), contentBox);
        titledPane.setExpanded(false);
        titledPane.setMinWidth(0);
        titledPane.setMaxWidth(Double.MAX_VALUE);
        return titledPane;
    }

    private Button startStepButton(StepType stepType) {
        Button button = new Button("Start Trede " + stepType.getOrderNumber());
        configureButton(button);
        // Late binding is hier passend: de UI bestaat al, maar de concrete AppController wordt pas later aangesloten.
        button.setOnAction(event -> requireNavigation().startAtStep(stepType));
        return button;
    }

    private Button exerciseButton(Exercise exercise) {
        Button button = new Button(buildExerciseText(exercise));
        configureButton(button);
        button.setOnAction(event -> requireNavigation().startAtExercise(exercise.getId()));
        return button;
    }

    private void configureButton(Button button) {
        button.setMinWidth(0);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setWrapText(true);
    }

    private DeveloperNavigation requireNavigation() {
        if (navigation == null) {
            throw new IllegalStateException("DeveloperNavigation is nog niet gebonden");
        }
        return navigation;
    }

    private String buildExerciseText(Exercise exercise) {
        String caseStudyTitle = exercise.getCaseStudy().getTitle();
        return caseStudyTitle == null || caseStudyTitle.isBlank()
                ? exercise.getTitle()
                : caseStudyTitle + " - " + exercise.getTitle();
    }

    private Label label(String text, String styleClass) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMinWidth(0);
        label.setMaxWidth(Double.MAX_VALUE);
        label.getStyleClass().add(styleClass);
        return label;
    }
}
