package codeladder.controller.exercise;

import codeladder.model.AnswerOption;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MultiSelectInputRenderer implements ExerciseInputRenderer {

    @Override
    public Set<InteractionType> getSupportedTypes() {
        return Set.of(InteractionType.MULTI_SELECT, InteractionType.ERROR_ANALYSIS);
    }

    @Override
    public ExerciseInputView render(Exercise exercise, ExerciseResponse initialResponse) {
        VBox box = new VBox(8);
        box.setFillWidth(true);
        box.setMinWidth(0);
        box.setMaxWidth(Double.MAX_VALUE);
        box.getStyleClass().add("choice-container");
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (AnswerOption option : exercise.getOptions()) {
            CheckBox checkBox = new CheckBox(option.getLabel());
            checkBox.setWrapText(true);
            checkBox.setMinWidth(0);
            checkBox.setMaxWidth(Double.MAX_VALUE);
            checkBox.setUserData(option.getId());
            checkBox.setSelected(initialResponse.getSelectedOptionIds().contains(option.getId()));
            checkBoxes.add(checkBox);
            box.getChildren().add(checkBox);
        }
        return new NodeBackedExerciseInputView(box, () -> readResponse(checkBoxes), box::setDisable);
    }

    private ExerciseResponse readResponse(List<CheckBox> checkBoxes) {
        Set<String> selectedIds = new LinkedHashSet<>();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                selectedIds.add(String.valueOf(checkBox.getUserData()));
            }
        }
        return ExerciseResponse.forSelections(selectedIds);
    }
}
