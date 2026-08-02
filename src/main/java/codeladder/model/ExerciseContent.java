package codeladder.model;

import java.util.Objects;

public final class ExerciseContent {
    private final String title;
    private final String instruction;
    private final String question;
    private final String focusText;
    private final String hintText;
    private final CaseStudy caseStudy;

    public ExerciseContent(
            String title,
            String instruction,
            String question,
            String focusText,
            String hintText,
            CaseStudy caseStudy
    ) {
        this.title = Objects.requireNonNull(title, "title");
        this.instruction = Objects.requireNonNull(instruction, "instruction");
        this.question = Objects.requireNonNull(question, "question");
        this.focusText = focusText == null ? "" : focusText;
        this.hintText = hintText == null ? "" : hintText;
        this.caseStudy = Objects.requireNonNull(caseStudy, "caseStudy");
    }

    public String getTitle() {
        return title;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getQuestion() {
        return question;
    }

    public String getFocusText() {
        return focusText;
    }

    public String getHintText() {
        return hintText;
    }

    public CaseStudy getCaseStudy() {
        return caseStudy;
    }
}
