package codeladder.controller.exercise;

import codeladder.model.AnswerOption;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;

import java.util.Set;

public class CategoryChoiceInputRenderer implements ExerciseInputRenderer {

    @Override
    public Set<InteractionType> getSupportedTypes() {
        return Set.of(InteractionType.CATEGORY_CHOICE);
    }

    @Override
    public ExerciseInputView render(Exercise exercise, ExerciseResponse initialResponse) {
        ToggleGroup toggleGroup = new ToggleGroup();
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setMinWidth(0);
        flowPane.setMaxWidth(Double.MAX_VALUE);
        flowPane.getStyleClass().add("choice-container");
        for (AnswerOption option : exercise.getOptions()) {
            ToggleButton button = new ToggleButton(option.getLabel());
            button.setWrapText(true);
            button.setMinWidth(0);
            button.setUserData(option.getId());
            button.setToggleGroup(toggleGroup);
            button.setSelected(initialResponse.getSelectedOptionIds().contains(option.getId()));
            flowPane.getChildren().add(button);
        }
        return new NodeBackedExerciseInputView(flowPane, () -> readResponse(toggleGroup), flowPane::setDisable);
    }

    private ExerciseResponse readResponse(ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() == null) {
            return ExerciseResponse.empty();
        }
        String selectedId = String.valueOf(toggleGroup.getSelectedToggle().getUserData());
        return ExerciseResponse.forSelections(Set.of(selectedId));
    }
}
