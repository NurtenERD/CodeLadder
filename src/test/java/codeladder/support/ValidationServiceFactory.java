package codeladder.support;

import codeladder.service.AnswerValidationService;
import codeladder.service.FeedbackService;
import codeladder.service.validation.AnswerValidatorRegistry;
import codeladder.service.validation.CodeFragmentValidator;
import codeladder.service.validation.ExerciseAnswerValidator;
import codeladder.service.validation.MultiSelectValidator;
import codeladder.service.validation.ReflectionValidator;
import codeladder.service.validation.SingleChoiceValidator;
import codeladder.service.validation.StructuredFieldsFeedbackBuilder;
import codeladder.service.validation.StructuredFieldsValidator;
import codeladder.service.validation.TextKeywordValidator;

import java.util.List;

public final class ValidationServiceFactory {

    private ValidationServiceFactory() {
    }

    public static AnswerValidationService create() {
        FeedbackService feedbackService = new FeedbackService();
        List<ExerciseAnswerValidator> validators = List.of(
                new TextKeywordValidator(feedbackService),
                new SingleChoiceValidator(feedbackService),
                new MultiSelectValidator(feedbackService),
                new CodeFragmentValidator(feedbackService),
                new ReflectionValidator(),
                new StructuredFieldsValidator(new StructuredFieldsFeedbackBuilder())
        );
        return new AnswerValidationService(feedbackService, new AnswerValidatorRegistry(validators));
    }
}
