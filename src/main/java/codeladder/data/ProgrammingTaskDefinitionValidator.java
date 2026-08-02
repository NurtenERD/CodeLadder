package codeladder.data;

import codeladder.data.dto.ExerciseDto;
import codeladder.data.dto.ProgrammingTaskDto;
import codeladder.data.dto.ProgrammingTestCaseDto;
import codeladder.model.ExerciseType;

import java.util.List;
import java.util.Set;

public class ProgrammingTaskDefinitionValidator {

    public void validate(ProgrammingTaskDto programmingTask, ParsedExerciseMetadata metadata, ExerciseValidationContext context) {
        if (metadata.exerciseType() == ExerciseType.PSEUDOCODE_TO_JAVA && programmingTask == null) {
            throw context.error("PSEUDOCODE_TO_JAVA zonder ProgrammingTask");
        }
        if (programmingTask == null) {
            return;
        }
        requireText(programmingTask.getInputDescription(), "programmingTask.inputDescription", context);
        requireText(programmingTask.getOutputDescription(), "programmingTask.outputDescription", context);
        requireCollection(programmingTask.getRules(), "programmingTask.rules", context);
        requireCollection(programmingTask.getEdgeCases(), "programmingTask.edgeCases", context);
        requireCollection(programmingTask.getPseudocodeSteps(), "programmingTask.pseudocodeSteps", context);
        requireCollection(programmingTask.getTestCases(), "programmingTask.testCases", context);
        requireSet(programmingTask.getExplanationKeywords(), "programmingTask.explanationKeywords", context);
        validateTestCases(programmingTask.getTestCases(), context);
    }

    private void validateTestCases(List<ProgrammingTestCaseDto> testCases, ExerciseValidationContext context) {
        for (ProgrammingTestCaseDto testCase : testCases) {
            requireText(testCase.getDescription(), "programmingTask.testCase.description", context);
            requireText(testCase.getExpectedOutput(), "programmingTask.testCase.expectedOutput", context);
        }
    }

    private void requireCollection(List<?> values, String fieldName, ExerciseValidationContext context) {
        if (values == null) {
            throw context.missingField(fieldName);
        }
    }

    private void requireSet(Set<?> values, String fieldName, ExerciseValidationContext context) {
        if (values == null) {
            throw context.missingField(fieldName);
        }
    }

    private void requireText(String value, String fieldName, ExerciseValidationContext context) {
        if (value == null || value.isBlank()) {
            throw context.missingField(fieldName);
        }
    }
}
