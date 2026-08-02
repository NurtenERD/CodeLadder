package codeladder.data.dto;

import java.util.List;
import java.util.Set;

public class ProgrammingTaskDto {
    private String inputDescription;
    private String outputDescription;
    private List<String> rules;
    private List<String> edgeCases;
    private List<String> pseudocodeSteps;
    private String javaTemplate;
    private List<ProgrammingTestCaseDto> testCases;
    private Set<String> explanationKeywords;

    public String getInputDescription() {
        return inputDescription;
    }

    public void setInputDescription(String inputDescription) {
        this.inputDescription = inputDescription;
    }

    public String getOutputDescription() {
        return outputDescription;
    }

    public void setOutputDescription(String outputDescription) {
        this.outputDescription = outputDescription;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public List<String> getEdgeCases() {
        return edgeCases;
    }

    public void setEdgeCases(List<String> edgeCases) {
        this.edgeCases = edgeCases;
    }

    public List<String> getPseudocodeSteps() {
        return pseudocodeSteps;
    }

    public void setPseudocodeSteps(List<String> pseudocodeSteps) {
        this.pseudocodeSteps = pseudocodeSteps;
    }

    public String getJavaTemplate() {
        return javaTemplate;
    }

    public void setJavaTemplate(String javaTemplate) {
        this.javaTemplate = javaTemplate;
    }

    public List<ProgrammingTestCaseDto> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<ProgrammingTestCaseDto> testCases) {
        this.testCases = testCases;
    }

    public Set<String> getExplanationKeywords() {
        return explanationKeywords;
    }

    public void setExplanationKeywords(Set<String> explanationKeywords) {
        this.explanationKeywords = explanationKeywords;
    }
}
