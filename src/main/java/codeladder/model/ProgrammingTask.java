package codeladder.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class ProgrammingTask {
    private final String inputDescription;
    private final String outputDescription;
    private final List<String> rules;
    private final List<String> edgeCases;
    private final List<String> pseudocodeSteps;
    private final String javaTemplate;
    private final List<ProgrammingTestCase> testCases;
    private final Set<String> explanationKeywords;

    public ProgrammingTask(
            String inputDescription,
            String outputDescription,
            List<String> rules,
            List<String> edgeCases,
            List<String> pseudocodeSteps,
            String javaTemplate,
            List<ProgrammingTestCase> testCases,
            Set<String> explanationKeywords
    ) {
        this.inputDescription = inputDescription == null ? "" : inputDescription;
        this.outputDescription = outputDescription == null ? "" : outputDescription;
        this.rules = copyList(rules);
        this.edgeCases = copyList(edgeCases);
        this.pseudocodeSteps = copyList(pseudocodeSteps);
        this.javaTemplate = javaTemplate == null ? "" : javaTemplate;
        this.testCases = testCases == null ? List.of() : new ArrayList<>(testCases);
        this.explanationKeywords = explanationKeywords == null ? Set.of() : new LinkedHashSet<>(explanationKeywords);
    }

    public String getInputDescription() {
        return inputDescription;
    }

    public String getOutputDescription() {
        return outputDescription;
    }

    public List<String> getRules() {
        return new ArrayList<>(rules);
    }

    public List<String> getEdgeCases() {
        return new ArrayList<>(edgeCases);
    }

    public List<String> getPseudocodeSteps() {
        return new ArrayList<>(pseudocodeSteps);
    }

    public String getJavaTemplate() {
        return javaTemplate;
    }

    public List<ProgrammingTestCase> getTestCases() {
        return new ArrayList<>(testCases);
    }

    public Set<String> getExplanationKeywords() {
        return new LinkedHashSet<>(explanationKeywords);
    }

    private List<String> copyList(List<String> values) {
        return values == null ? List.of() : new ArrayList<>(values);
    }
}
