package codeladder.model;

public final class ProgrammingTestCase {
    private final String description;
    private final String input;
    private final String expectedOutput;

    public ProgrammingTestCase(String description, String input, String expectedOutput) {
        this.description = description == null ? "" : description;
        this.input = input == null ? "" : input;
        this.expectedOutput = expectedOutput == null ? "" : expectedOutput;
    }

    public String getDescription() {
        return description;
    }

    public String getInput() {
        return input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }
}
