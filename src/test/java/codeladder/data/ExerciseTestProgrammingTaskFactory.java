package codeladder.data;

import codeladder.data.dto.ProgrammingTaskDto;
import codeladder.data.dto.ProgrammingTestCaseDto;

import java.util.List;
import java.util.Set;

final class ExerciseTestProgrammingTaskFactory {

    private ExerciseTestProgrammingTaskFactory() {
    }

    static ProgrammingTaskDto create() {
        ProgrammingTaskDto dto = new ProgrammingTaskDto();
        dto.setInputDescription("Lijst");
        dto.setOutputDescription("Aantal");
        dto.setRules(List.of("regel"));
        dto.setEdgeCases(List.of("leeg"));
        dto.setPseudocodeSteps(List.of());
        dto.setJavaTemplate("");
        ProgrammingTestCaseDto testCase = new ProgrammingTestCaseDto();
        testCase.setDescription("voorbeeld");
        testCase.setInput("[]");
        testCase.setExpectedOutput("0");
        dto.setTestCases(List.of(testCase));
        dto.setExplanationKeywords(Set.of("lijst"));
        return dto;
    }
}
