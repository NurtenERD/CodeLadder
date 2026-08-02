package codeladder.data;

import codeladder.data.dto.ProgrammingTaskDto;
import codeladder.data.dto.ProgrammingTestCaseDto;
import codeladder.model.ProgrammingTask;
import codeladder.model.ProgrammingTestCase;

import java.util.ArrayList;
import java.util.List;

public class ProgrammingTaskMapper {

    public ProgrammingTask map(ProgrammingTaskDto dto) {
        if (dto == null) {
            return null;
        }
        return new ProgrammingTask(
                dto.getInputDescription(),
                dto.getOutputDescription(),
                dto.getRules(),
                dto.getEdgeCases(),
                dto.getPseudocodeSteps(),
                dto.getJavaTemplate(),
                mapTestCases(dto.getTestCases()),
                dto.getExplanationKeywords()
        );
    }

    private List<ProgrammingTestCase> mapTestCases(List<ProgrammingTestCaseDto> values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        List<ProgrammingTestCase> mapped = new ArrayList<>();
        for (ProgrammingTestCaseDto value : values) {
            mapped.add(new ProgrammingTestCase(value.getDescription(), value.getInput(), value.getExpectedOutput()));
        }
        return mapped;
    }
}
