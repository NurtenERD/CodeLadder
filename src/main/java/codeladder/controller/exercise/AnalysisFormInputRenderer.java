package codeladder.controller.exercise;

import codeladder.model.AnswerOption;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;
import codeladder.model.StructuredFieldDefinition;
import codeladder.model.StructuredFieldInputType;
import codeladder.util.StructuredFieldValueCodec;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class AnalysisFormInputRenderer implements ExerciseInputRenderer {
    @Override
    public Set<InteractionType> getSupportedTypes() {
        return Set.of(InteractionType.ANALYSIS_FORM);
    }

    @Override
    public ExerciseInputView render(Exercise exercise, ExerciseResponse initialResponse) {
        VBox form = new VBox(14);
        form.setFillWidth(true);
        form.setMinWidth(0);
        form.setMaxWidth(Double.MAX_VALUE);
        form.getStyleClass().add("analysis-form");
        Map<String, Supplier<String>> readers = new LinkedHashMap<>();
        for (StructuredFieldDefinition field : exercise.getStructuredAnswerDefinition().orElseThrow().getFields()) {
            form.getChildren().add(buildField(field, initialResponse.getFieldValues(), readers));
        }
        return new NodeBackedExerciseInputView(form, () -> ExerciseResponse.forFields(readValues(readers)), form::setDisable);
    }

    private VBox buildField(
            StructuredFieldDefinition field,
            Map<String, String> initialValues,
            Map<String, Supplier<String>> readers
    ) {
        VBox fieldBox = new VBox(8);
        fieldBox.setFillWidth(true);
        fieldBox.setMinWidth(0);
        fieldBox.setMaxWidth(Double.MAX_VALUE);
        fieldBox.getStyleClass().add("analysis-field");
        fieldBox.getChildren().add(label(field.getLabel(), "field-label"));
        fieldBox.getChildren().add(label(field.getPrompt(), "field-prompt"));
        String initialValue = initialValues.getOrDefault(field.getId(), "");
        Region input = switch (field.getInputType()) {
            case TEXT -> textInput(field, initialValue, readers);
            case SINGLE_CHOICE -> singleChoiceInput(field, initialValue, readers);
            case MULTI_SELECT -> multiSelectInput(field, initialValue, readers);
        };
        fieldBox.getChildren().add(input);
        return fieldBox;
    }

    private Region textInput(StructuredFieldDefinition field, String initialValue, Map<String, Supplier<String>> readers) {
        TextField textField = new TextField(initialValue);
        textField.setMinWidth(0);
        textField.setMaxWidth(Double.MAX_VALUE);
        readers.put(field.getId(), textField::getText);
        return textField;
    }

    private Region singleChoiceInput(StructuredFieldDefinition field, String initialValue, Map<String, Supplier<String>> readers) {
        ToggleGroup toggleGroup = new ToggleGroup();
        VBox box = optionBox();
        for (AnswerOption option : field.getOptions()) {
            RadioButton radioButton = new RadioButton(option.getLabel());
            radioButton.setUserData(option.getId());
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setSelected(option.getId().equals(initialValue));
            styleOptionControl(radioButton);
            box.getChildren().add(radioButton);
        }
        readers.put(field.getId(), () -> toggleGroup.getSelectedToggle() == null ? "" : String.valueOf(toggleGroup.getSelectedToggle().getUserData()));
        return box;
    }

    private Region multiSelectInput(StructuredFieldDefinition field, String initialValue, Map<String, Supplier<String>> readers) {
        Set<String> selectedIds = StructuredFieldValueCodec.decodeMultiSelect(initialValue);
        List<CheckBox> checkBoxes = new ArrayList<>();
        VBox box = optionBox();
        for (AnswerOption option : field.getOptions()) {
            CheckBox checkBox = new CheckBox(option.getLabel());
            checkBox.setUserData(option.getId());
            checkBox.setSelected(selectedIds.contains(option.getId()));
            styleOptionControl(checkBox);
            checkBoxes.add(checkBox);
            box.getChildren().add(checkBox);
        }
        readers.put(field.getId(), () -> StructuredFieldValueCodec.encodeMultiSelect(readSelections(checkBoxes)));
        return box;
    }

    private VBox optionBox() {
        VBox box = new VBox(8);
        box.setFillWidth(true);
        box.setMinWidth(0);
        box.setMaxWidth(Double.MAX_VALUE);
        box.getStyleClass().add("field-options");
        return box;
    }

    private void styleOptionControl(javafx.scene.control.Labeled control) {
        control.setWrapText(true);
        control.setMinWidth(0);
        control.setMaxWidth(Double.MAX_VALUE);
    }

    private Map<String, String> readValues(Map<String, Supplier<String>> readers) {
        Map<String, String> values = new LinkedHashMap<>();
        for (Map.Entry<String, Supplier<String>> entry : readers.entrySet()) {
            values.put(entry.getKey(), entry.getValue().get());
        }
        return values;
    }

    private Set<String> readSelections(List<CheckBox> checkBoxes) {
        Set<String> selectedIds = new LinkedHashSet<>();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                selectedIds.add(String.valueOf(checkBox.getUserData()));
            }
        }
        return selectedIds;
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
