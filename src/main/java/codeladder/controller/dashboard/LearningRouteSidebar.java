package codeladder.controller.dashboard;

import codeladder.model.Exercise;
import codeladder.model.LearningStep;
import codeladder.model.StepType;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class LearningRouteSidebar {
    private static final double SIDEBAR_WIDTH = 300;

    private final List<LearningStep> steps;
    private final RoutePresentationService routePresentationService;
    private final DeveloperTestMenu developerTestMenu;
    private ScrollPane scrollPane;
    private VBox stepListBox;
    private Label routeBlockValueLabel;
    private Label routeGuidanceLabel;

    public LearningRouteSidebar(
            List<LearningStep> steps,
            RoutePresentationService routePresentationService,
            DeveloperTestMenu developerTestMenu
    ) {
        this.steps = steps;
        this.routePresentationService = routePresentationService;
        this.developerTestMenu = developerTestMenu;
    }

    public Parent createView() {
        stepListBox = new VBox();
        stepListBox.getStyleClass().add("step-list");
        routeBlockValueLabel = label("Nog niet gestart", "route-block-value");
        routeGuidanceLabel = label("Je werkt eerst door de hoofdroute en past daarna dezelfde denkstappen toe in nieuwe contexten.", "sidebar-subtitle");
        VBox sidebarContent = new VBox(
                12,
                title("Leerroute", "sidebar-title"),
                label("De treden blijven zichtbaar terwijl je de route doorloopt.", "sidebar-subtitle"),
                routeBlockBox(),
                routeGuidanceLabel,
                stepListBox,
                developerTestMenu.createView()
        );
        sidebarContent.setFillWidth(true);
        sidebarContent.setPadding(new Insets(18));
        scrollPane = new ScrollPane(sidebarContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(false);
        scrollPane.setMinWidth(SIDEBAR_WIDTH);
        scrollPane.setPrefWidth(SIDEBAR_WIDTH);
        scrollPane.setMaxWidth(SIDEBAR_WIDTH);
        scrollPane.getStyleClass().add("sidebar");
        return scrollPane;
    }

    public void refresh(Exercise exercise, StepType currentStepType, String routeBlockTitle, int currentNumber, int totalExercises) {
        routeBlockValueLabel.setText(routeBlockTitle == null || routeBlockTitle.isBlank() ? "Nog niet gestart" : routeBlockTitle);
        routeGuidanceLabel.setText(routePresentationService.buildRouteGuidance(exercise, routeBlockTitle, currentNumber, totalExercises));
        stepListBox.getChildren().clear();
        for (LearningStep step : steps) {
            VBox stepBox = new VBox(
                    label(routePresentationService.buildStepText(step, currentStepType, routeBlockTitle, exercise), "step-title"),
                    label(step.getDescription(), "step-description")
            );
            stepBox.getStyleClass().add("step-item");
            if (routePresentationService.isCurrentStep(step, currentStepType)) {
                stepBox.getStyleClass().add("step-item-active");
            } else if (routePresentationService.isCompletedStep(step, currentStepType, routeBlockTitle, exercise)) {
                stepBox.getStyleClass().add("step-item-completed");
            }
            stepListBox.getChildren().add(stepBox);
        }
    }

    public void resetScroll() {
        if (scrollPane != null) {
            scrollPane.setVvalue(0.0);
        }
    }

    private VBox routeBlockBox() {
        VBox routeBlockBox = new VBox(label("Huidig routeblok", "route-block-title"), routeBlockValueLabel);
        routeBlockBox.getStyleClass().add("route-block-card");
        return routeBlockBox;
    }

    private Label title(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
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
