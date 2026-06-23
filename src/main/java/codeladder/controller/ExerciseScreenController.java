package codeladder.controller;

import codeladder.model.AnswerOption;
import codeladder.model.AttemptResult;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.ExerciseType;
import codeladder.model.StudentAnswer;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExerciseScreenController {

    public Parent createView(
            Exercise exercise,
            int currentNumber,
            int totalExercises,
            StudentAnswer studentAnswer,
            Consumer<ExerciseResponse> onSubmit,
            Runnable onPrevious,
            Runnable onNext
    ) {
        Label progressLabel = new Label("Oefening " + currentNumber + " van " + totalExercises);
        Label stepLabel = new Label(exercise.getStepType().getDisplayName());
        stepLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label stepDescriptionLabel = new Label(exercise.getStepType().getDescription());
        stepDescriptionLabel.setWrapText(true);

        Label titleLabel = new Label(exercise.getTitle());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label instructionLabel = new Label(exercise.getInstruction());
        instructionLabel.setWrapText(true);

        Label blockTitleLabel = new Label("Blok: " + exercise.getCaseStudy().getRouteBlockTitle());
        blockTitleLabel.setStyle("-fx-font-weight: bold;");

        Label caseTitleLabel = new Label("Casus: " + exercise.getCaseStudy().getTitle());
        caseTitleLabel.setStyle("-fx-font-weight: bold;");

        Label caseDescriptionLabel = new Label(exercise.getCaseStudy().getDescription());
        caseDescriptionLabel.setWrapText(true);

        Label questionLabel = new Label(exercise.getQuestion());
        questionLabel.setWrapText(true);

        VBox contentBox = new VBox(12);
        contentBox.getChildren().addAll(
                progressLabel,
                stepLabel,
                stepDescriptionLabel,
                titleLabel,
                instructionLabel,
                blockTitleLabel,
                caseTitleLabel,
                caseDescriptionLabel,
                questionLabel
        );

        if (!exercise.getFocusText().isBlank()) {
            Label focusLabel = new Label(exercise.getFocusText());
            focusLabel.setStyle("-fx-font-weight: bold;");
            focusLabel.setWrapText(true);
            contentBox.getChildren().add(focusLabel);
        }

        if (exercise.getExerciseType() == ExerciseType.CODE_WRITING) {
            Label helperLabel = new Label(
                    "Je hoeft nog geen perfecte Java-code te schrijven. Let vooral op classnaam, attributen, constructor of methode."
            );
            helperLabel.setWrapText(true);
            contentBox.getChildren().add(helperLabel);
        }

        ExerciseResponse initialResponse = studentAnswer == null ? ExerciseResponse.empty() : studentAnswer.getLatestResponse();
        InputSection inputSection = buildInputSection(exercise, initialResponse);
        contentBox.getChildren().add(inputSection.node());

        if (studentAnswer != null) {
            AttemptResult latestAttempt = studentAnswer.getLatestAttempt();
            Label feedbackTitle = new Label("Feedback");
            feedbackTitle.setStyle("-fx-font-weight: bold;");

            Label feedbackLabel = new Label(latestAttempt.getFeedback().getMessage());
            feedbackLabel.setWrapText(true);
            contentBox.getChildren().addAll(feedbackTitle, feedbackLabel);

            Label attemptLabel = new Label("Poging " + latestAttempt.getAttemptNumber() + " van 2");
            contentBox.getChildren().add(attemptLabel);
        }

        boolean readyForNext = studentAnswer != null && studentAnswer.isReadyForNext();
        inputSection.setDisabled(readyForNext);
        int attemptCount = studentAnswer == null ? 0 : studentAnswer.getAttemptCount();

        Button previousButton = new Button("Terug");
        previousButton.setOnAction(event -> onPrevious.run());
        previousButton.setDisable(currentNumber <= 1);

        Button submitButton = new Button(attemptCount == 0 ? "Controleer" : "Controleer opnieuw");
        submitButton.setOnAction(event -> onSubmit.accept(inputSection.readResponse()));
        submitButton.setDisable(readyForNext);
        submitButton.setVisible(!readyForNext);
        submitButton.setManaged(!readyForNext);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button nextButton = new Button("Volgende");
        nextButton.setOnAction(event -> onNext.run());
        nextButton.setVisible(readyForNext);
        nextButton.setManaged(readyForNext);

        HBox buttonBar = new HBox(10, previousButton, submitButton, spacer, nextButton);
        contentBox.getChildren().add(buttonBar);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);

        VBox root = new VBox(scrollPane);
        root.setPadding(new Insets(20));
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        return root;
    }

    private InputSection buildInputSection(Exercise exercise, ExerciseResponse initialResponse) {
        return switch (exercise.getExerciseType()) {
            case OPEN_QUESTION, REFLECTION -> buildTextAreaSection(initialResponse);
            case FILL_IN_THE_BLANK -> buildTextFieldSection(initialResponse);
            case MULTIPLE_CHOICE -> buildRadioSection(exercise.getOptions(), initialResponse);
            case MULTI_SELECT, ERROR_ANALYSIS -> buildCheckBoxSection(exercise.getOptions(), initialResponse);
            case CATEGORY_CHOICE -> buildCategorySection(exercise.getOptions(), initialResponse);
            case CODE_WRITING -> buildCodeTextAreaSection(initialResponse);
        };
    }

    private InputSection buildTextAreaSection(ExerciseResponse initialResponse) {
        TextArea textArea = new TextArea(initialResponse.getTextAnswer());
        textArea.setWrapText(true);
        textArea.setPrefRowCount(6);
        return new InputSection(
                textArea,
                () -> ExerciseResponse.forText(textArea.getText()),
                disabled -> textArea.setDisable(disabled)
        );
    }

    private InputSection buildCodeTextAreaSection(ExerciseResponse initialResponse) {
        TextArea textArea = new TextArea(initialResponse.getTextAnswer());
        textArea.setWrapText(true);
        textArea.setPrefRowCount(12);
        return new InputSection(
                textArea,
                () -> ExerciseResponse.forText(textArea.getText()),
                disabled -> textArea.setDisable(disabled)
        );
    }

    private InputSection buildTextFieldSection(ExerciseResponse initialResponse) {
        TextField textField = new TextField(initialResponse.getTextAnswer());
        return new InputSection(
                textField,
                () -> ExerciseResponse.forText(textField.getText()),
                disabled -> textField.setDisable(disabled)
        );
    }

    private InputSection buildRadioSection(List<AnswerOption> options, ExerciseResponse initialResponse) {
        ToggleGroup toggleGroup = new ToggleGroup();
        VBox box = new VBox(8);
        for (AnswerOption option : options) {
            RadioButton radioButton = new RadioButton(option.getLabel());
            radioButton.setUserData(option.getId());
            radioButton.setToggleGroup(toggleGroup);
            if (initialResponse.getSelectedOptionIds().contains(option.getId())) {
                radioButton.setSelected(true);
            }
            box.getChildren().add(radioButton);
        }

        return new InputSection(
                box,
                () -> {
                    if (toggleGroup.getSelectedToggle() == null) {
                        return ExerciseResponse.empty();
                    }
                    String selectedId = String.valueOf(toggleGroup.getSelectedToggle().getUserData());
                    return ExerciseResponse.forSelections(Set.of(selectedId));
                },
                disabled -> box.setDisable(disabled)
        );
    }

    private InputSection buildCheckBoxSection(List<AnswerOption> options, ExerciseResponse initialResponse) {
        VBox box = new VBox(8);
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (AnswerOption option : options) {
            CheckBox checkBox = new CheckBox(option.getLabel());
            checkBox.setUserData(option.getId());
            checkBox.setSelected(initialResponse.getSelectedOptionIds().contains(option.getId()));
            checkBoxes.add(checkBox);
            box.getChildren().add(checkBox);
        }

        return new InputSection(
                box,
                () -> {
                    Set<String> selectedIds = new LinkedHashSet<>();
                    for (CheckBox checkBox : checkBoxes) {
                        if (checkBox.isSelected()) {
                            selectedIds.add(String.valueOf(checkBox.getUserData()));
                        }
                    }
                    return ExerciseResponse.forSelections(selectedIds);
                },
                disabled -> box.setDisable(disabled)
        );
    }

    private InputSection buildCategorySection(List<AnswerOption> options, ExerciseResponse initialResponse) {
        ToggleGroup toggleGroup = new ToggleGroup();
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.setVgap(10);

        for (AnswerOption option : options) {
            ToggleButton button = new ToggleButton(option.getLabel());
            button.setUserData(option.getId());
            button.setToggleGroup(toggleGroup);
            if (initialResponse.getSelectedOptionIds().contains(option.getId())) {
                button.setSelected(true);
            }
            flowPane.getChildren().add(button);
        }

        return new InputSection(
                flowPane,
                () -> {
                    if (toggleGroup.getSelectedToggle() == null) {
                        return ExerciseResponse.empty();
                    }
                    String selectedId = String.valueOf(toggleGroup.getSelectedToggle().getUserData());
                    return ExerciseResponse.forSelections(Set.of(selectedId));
                },
                disabled -> flowPane.setDisable(disabled)
        );
    }

    private record InputSection(
            javafx.scene.Node node,
            Supplier<ExerciseResponse> responseSupplier,
            Consumer<Boolean> disableHandler
    ) {
        ExerciseResponse readResponse() {
            return responseSupplier.get();
        }

        void setDisabled(boolean disabled) {
            disableHandler.accept(disabled);
        }
    }
}
