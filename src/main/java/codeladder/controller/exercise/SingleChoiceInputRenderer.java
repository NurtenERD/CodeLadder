package codeladder.controller.exercise;

import codeladder.model.AnswerOption;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.Set;

public class SingleChoiceInputRenderer implements ExerciseInputRenderer {

    @Override
    public Set<InteractionType> getSupportedTypes() {
        return Set.of(InteractionType.MULTIPLE_CHOICE);
    }

    @Override
    public ExerciseInputView render(Exercise exercise, ExerciseResponse initialResponse) {
        ToggleGroup toggleGroup = new ToggleGroup();
        VBox box = createChoiceBox();
        for (AnswerOption option : exercise.getOptions()) {
            RadioButton radioButton = new RadioButton(option.getLabel());
            radioButton.setWrapText(true);
            radioButton.setMinWidth(0);
            radioButton.setMaxWidth(Double.MAX_VALUE);
            radioButton.setUserData(option.getId());
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setSelected(initialResponse.getSelectedOptionIds().contains(option.getId()));
            box.getChildren().add(radioButton);
        }
        return new NodeBackedExerciseInputView(box, () -> readResponse(toggleGroup), box::setDisable);
    }

    private VBox createChoiceBox() {
        VBox box = new VBox(8);
        box.setFillWidth(true);
        box.setMinWidth(0);
        box.setMaxWidth(Double.MAX_VALUE);
        box.getStyleClass().add("choice-container");
        return box;
    }

    private ExerciseResponse readResponse(ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() == null) {
            return ExerciseResponse.empty();
        }
        String selectedId = String.valueOf(toggleGroup.getSelectedToggle().getUserData());
        return ExerciseResponse.forSelections(Set.of(selectedId));
    }
}
