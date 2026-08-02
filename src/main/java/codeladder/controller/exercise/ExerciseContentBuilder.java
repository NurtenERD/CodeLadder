package codeladder.controller.exercise;

import codeladder.model.Exercise;
import codeladder.model.InteractionType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ExerciseContentBuilder {

    public Parent build(Exercise exercise, Node inputNode) {
        VBox caseCard = new VBox(8);
        caseCard.setFillWidth(true);
        caseCard.setMinWidth(0);
        caseCard.setMaxWidth(Double.MAX_VALUE);
        caseCard.getStyleClass().add("case-card");
        caseCard.getChildren().addAll(
                label("Blok: " + exercise.getCaseStudy().getRouteBlockTitle(), "muted-text"),
                label("Casus: " + exercise.getCaseStudy().getTitle(), "section-title"),
                label(exercise.getCaseStudy().getDescription(), "body-text"),
                label(exercise.getQuestion(), "body-text")
        );
        if (!exercise.getFocusText().isBlank()) {
            caseCard.getChildren().add(label(exercise.getFocusText(), "body-text"));
        }
        if (exercise.getInteractionType() == InteractionType.CODE_WRITING) {
            caseCard.getChildren().add(label(
                    "Je hoeft nog geen perfecte Java-code te schrijven. Let vooral op classnaam, attributen, constructor of methode.",
                    "muted-text"
            ));
        }
        if (inputNode instanceof Region inputRegion) {
            inputRegion.setMinWidth(0);
            inputRegion.setMaxWidth(Double.MAX_VALUE);
        }
        VBox answerCard = new VBox(inputNode);
        answerCard.setFillWidth(true);
        answerCard.setMinWidth(0);
        answerCard.setMaxWidth(Double.MAX_VALUE);
        answerCard.getStyleClass().add("answer-card");
        VBox content = new VBox(16, caseCard, answerCard);
        content.setFillWidth(true);
        return content;
    }

    private Label label(String text, String styleClass) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMinWidth(0);
        label.setMaxWidth(Double.MAX_VALUE);
        label.getStyleClass().add(styleClass);
        return label;
    }
}
