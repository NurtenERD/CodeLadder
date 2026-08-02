package codeladder.controller;

import codeladder.controller.dashboard.DashboardLayout;
import codeladder.controller.dashboard.DashboardLayoutBuilder;
import codeladder.controller.dashboard.DashboardStatusBar;
import codeladder.controller.dashboard.LearningRouteSidebar;
import codeladder.controller.dashboard.RoutePresentationService;
import codeladder.model.Exercise;
import codeladder.model.StudentAnswer;
import javafx.scene.Parent;

public class MainDashboardController {
    private final LearningRouteSidebar sidebar;
    private final DashboardStatusBar statusBar;
    private final DashboardLayoutBuilder layoutBuilder;
    private final RoutePresentationService routePresentationService;
    private DashboardLayout layout;

    public MainDashboardController(
            LearningRouteSidebar sidebar,
            DashboardStatusBar statusBar,
            DashboardLayoutBuilder layoutBuilder,
            RoutePresentationService routePresentationService
    ) {
        this.sidebar = sidebar;
        this.statusBar = statusBar;
        this.layoutBuilder = layoutBuilder;
        this.routePresentationService = routePresentationService;
    }

    public Parent createView() {
        if (layout == null) {
            layout = layoutBuilder.build(sidebar.createView(), statusBar.createView());
        }
        sidebar.resetScroll();
        return layout.getRoot();
    }

    public void showIntroContent(Parent content, int totalExercises) {
        layout.showContent(content);
        sidebar.refresh(null, null, "", 0, totalExercises);
        statusBar.update(
                "Nog niet gestart",
                "Kies Start leerroute",
                "0 van " + totalExercises,
                "Nog geen poging",
                "Feedback verschijnt hier na het controleren van een oefening."
        );
    }

    public void showExerciseContent(Exercise exercise, int currentNumber, int totalExercises, StudentAnswer studentAnswer, Parent content) {
        layout.showContent(content);
        sidebar.refresh(exercise, exercise.getStepType(), exercise.getCaseStudy().getRouteBlockTitle(), currentNumber, totalExercises);
        statusBar.update(
                exercise.getCaseStudy().getRouteBlockTitle(),
                routePresentationService.buildCurrentStepStatus(exercise),
                currentNumber + " van " + totalExercises,
                routePresentationService.buildAttemptText(studentAnswer),
                routePresentationService.buildFeedbackText(studentAnswer)
        );
    }

    public void showSummaryContent(Parent content, int totalExercises) {
        layout.showContent(content);
        sidebar.refresh(null, null, "Leerroute afgerond", totalExercises, totalExercises);
        statusBar.update(
                "Leerroute afgerond",
                "Eindoverzicht",
                totalExercises + " van " + totalExercises,
                "Reflectie en herstart beschikbaar",
                "Je ziet hier je antwoorden, feedback en reflectie in dezelfde leeromgeving."
        );
    }
}
