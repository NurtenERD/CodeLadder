package codeladder.app;

import codeladder.controller.AppController;
import codeladder.controller.ExerciseScreenController;
import codeladder.controller.MainDashboardController;
import codeladder.controller.StartScreenController;
import codeladder.controller.SummaryScreenController;
import codeladder.controller.dashboard.DashboardLayoutBuilder;
import codeladder.controller.dashboard.DashboardStatusBar;
import codeladder.controller.dashboard.DeveloperTestMenu;
import codeladder.controller.dashboard.LearningRouteSidebar;
import codeladder.controller.dashboard.RoutePresentationService;
import codeladder.controller.exercise.AnalysisFormInputRenderer;
import codeladder.controller.exercise.CategoryChoiceInputRenderer;
import codeladder.controller.exercise.CodeInputRenderer;
import codeladder.controller.exercise.ExerciseActionBarBuilder;
import codeladder.controller.exercise.ExerciseContentBuilder;
import codeladder.controller.exercise.ExerciseFeedbackViewBuilder;
import codeladder.controller.exercise.ExerciseHeaderViewBuilder;
import codeladder.controller.exercise.ExerciseHintProvider;
import codeladder.controller.exercise.ExerciseInputRenderer;
import codeladder.controller.exercise.ExerciseInputRendererRegistry;
import codeladder.controller.exercise.MultiSelectInputRenderer;
import codeladder.controller.exercise.SingleChoiceInputRenderer;
import codeladder.controller.exercise.TextAreaInputRenderer;
import codeladder.controller.exercise.TextFieldInputRenderer;
import codeladder.data.ExerciseDataProvider;
import codeladder.model.ValidationType;
import codeladder.service.AnswerValidationService;
import codeladder.service.FeedbackService;
import codeladder.service.LearningRouteService;
import codeladder.service.ProgressService;
import codeladder.service.SkillProgressService;
import codeladder.service.SummaryService;
import codeladder.service.validation.AnswerValidatorRegistry;
import codeladder.service.validation.CodeFragmentValidator;
import codeladder.service.validation.ExerciseAnswerValidator;
import codeladder.service.validation.MultiSelectValidator;
import codeladder.service.validation.ReflectionValidator;
import codeladder.service.validation.SingleChoiceValidator;
import codeladder.service.validation.StructuredFieldsFeedbackBuilder;
import codeladder.service.validation.StructuredFieldsValidator;
import codeladder.service.validation.TextKeywordValidator;
import javafx.stage.Stage;

import java.util.List;

public class CodeLadderCompositionRoot {

    public AppController createAppController(Stage stage) {
        FeedbackService feedbackService = new FeedbackService();
        AnswerValidationService validationService = new AnswerValidationService(
                feedbackService,
                new AnswerValidatorRegistry(createValidators(feedbackService))
        );
        LearningRouteService learningRouteService = new LearningRouteService(new ExerciseDataProvider(), validationService);
        DeveloperTestMenu developerTestMenu = new DeveloperTestMenu(learningRouteService.getExercises());
        MainDashboardController dashboardController = createDashboardController(learningRouteService, developerTestMenu);
        AppController appController = new AppController(
                stage,
                learningRouteService,
                new ProgressService(),
                new SummaryService(),
                new SkillProgressService(),
                dashboardController,
                new StartScreenController(),
                createExerciseScreenController(),
                new SummaryScreenController()
        );
        developerTestMenu.bindNavigation(appController);
        return appController;
    }

    private MainDashboardController createDashboardController(
            LearningRouteService learningRouteService,
            DeveloperTestMenu developerTestMenu
    ) {
        RoutePresentationService routePresentationService = new RoutePresentationService();
        LearningRouteSidebar sidebar = new LearningRouteSidebar(
                learningRouteService.getLearningSteps(),
                routePresentationService,
                developerTestMenu
        );
        return new MainDashboardController(
                sidebar,
                new DashboardStatusBar(),
                new DashboardLayoutBuilder(),
                routePresentationService
        );
    }

    private ExerciseScreenController createExerciseScreenController() {
        ExerciseHintProvider hintProvider = new ExerciseHintProvider();
        return new ExerciseScreenController(
                new ExerciseInputRendererRegistry(createInputRenderers()),
                new ExerciseHeaderViewBuilder(),
                new ExerciseContentBuilder(),
                new ExerciseFeedbackViewBuilder(),
                new ExerciseActionBarBuilder(hintProvider)
        );
    }

    private List<ExerciseAnswerValidator> createValidators(FeedbackService feedbackService) {
        return List.of(
                new TextKeywordValidator(feedbackService),
                new SingleChoiceValidator(feedbackService),
                new MultiSelectValidator(feedbackService),
                new CodeFragmentValidator(feedbackService),
                new ReflectionValidator(),
                new StructuredFieldsValidator(new StructuredFieldsFeedbackBuilder())
        );
    }

    private List<ExerciseInputRenderer> createInputRenderers() {
        return List.of(
                new TextAreaInputRenderer(),
                new TextFieldInputRenderer(),
                new SingleChoiceInputRenderer(),
                new MultiSelectInputRenderer(),
                new CategoryChoiceInputRenderer(),
                new CodeInputRenderer(),
                new AnalysisFormInputRenderer()
        );
    }
}
