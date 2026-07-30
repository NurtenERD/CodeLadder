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
        progressLabel.getStyleClass().add("progress-badge");

        Label stepLabel = new Label(exercise.getStepType().getDisplayName());
        stepLabel.setWrapText(true);
        stepLabel.setMinWidth(0);
        stepLabel.setMaxWidth(Double.MAX_VALUE);
        stepLabel.getStyleClass().add("section-title");

        Label stepDescriptionLabel = new Label(exercise.getStepType().getDescription());
        stepDescriptionLabel.setWrapText(true);
        stepDescriptionLabel.setMinWidth(0);
        stepDescriptionLabel.setMaxWidth(Double.MAX_VALUE);
        stepDescriptionLabel.getStyleClass().add("muted-text");

        Label titleLabel = new Label(exercise.getTitle());
        titleLabel.setWrapText(true);
        titleLabel.setMinWidth(0);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.getStyleClass().add("page-title");

        Label instructionLabel = new Label(exercise.getInstruction());
        instructionLabel.setWrapText(true);
        instructionLabel.setMinWidth(0);
        instructionLabel.setMaxWidth(Double.MAX_VALUE);
        instructionLabel.getStyleClass().add("body-text");

        VBox headerCard = new VBox(8, progressLabel, stepLabel, stepDescriptionLabel, titleLabel, instructionLabel);
        headerCard.setFillWidth(true);
        headerCard.setMinWidth(0);
        headerCard.setMaxWidth(Double.MAX_VALUE);
        headerCard.getStyleClass().add("exercise-card");

        Label blockTitleLabel = new Label("Blok: " + exercise.getCaseStudy().getRouteBlockTitle());
        blockTitleLabel.setWrapText(true);
        blockTitleLabel.setMinWidth(0);
        blockTitleLabel.setMaxWidth(Double.MAX_VALUE);
        blockTitleLabel.getStyleClass().add("muted-text");

        Label caseTitleLabel = new Label("Casus: " + exercise.getCaseStudy().getTitle());
        caseTitleLabel.setWrapText(true);
        caseTitleLabel.setMinWidth(0);
        caseTitleLabel.setMaxWidth(Double.MAX_VALUE);
        caseTitleLabel.getStyleClass().add("section-title");

        Label caseDescriptionLabel = new Label(exercise.getCaseStudy().getDescription());
        caseDescriptionLabel.setWrapText(true);
        caseDescriptionLabel.setMinWidth(0);
        caseDescriptionLabel.setMaxWidth(Double.MAX_VALUE);
        caseDescriptionLabel.getStyleClass().add("body-text");

        Label questionLabel = new Label(exercise.getQuestion());
        questionLabel.setWrapText(true);
        questionLabel.setMinWidth(0);
        questionLabel.setMaxWidth(Double.MAX_VALUE);
        questionLabel.getStyleClass().add("body-text");

        VBox caseCard = new VBox(8, blockTitleLabel, caseTitleLabel, caseDescriptionLabel, questionLabel);
        caseCard.setFillWidth(true);
        caseCard.setMinWidth(0);
        caseCard.setMaxWidth(Double.MAX_VALUE);
        caseCard.getStyleClass().add("case-card");

        if (!exercise.getFocusText().isBlank()) {
            Label focusLabel = new Label(exercise.getFocusText());
            focusLabel.setWrapText(true);
            focusLabel.setMinWidth(0);
            focusLabel.setMaxWidth(Double.MAX_VALUE);
            focusLabel.getStyleClass().add("body-text");
            caseCard.getChildren().add(focusLabel);
        }

        if (exercise.getExerciseType() == ExerciseType.CODE_WRITING) {
            Label helperLabel = new Label(
                    "Je hoeft nog geen perfecte Java-code te schrijven. Let vooral op classnaam, attributen, constructor of methode."
            );
            helperLabel.setWrapText(true);
            helperLabel.setMinWidth(0);
            helperLabel.setMaxWidth(Double.MAX_VALUE);
            helperLabel.getStyleClass().add("muted-text");
            caseCard.getChildren().add(helperLabel);
        }

        ExerciseResponse initialResponse = studentAnswer == null ? ExerciseResponse.empty() : studentAnswer.getLatestResponse();
        InputSection inputSection = buildInputSection(exercise, initialResponse);

        if (inputSection.node() instanceof Region inputRegion) {
            inputRegion.setMinWidth(0);
            inputRegion.setMaxWidth(Double.MAX_VALUE);
        }

        VBox answerCard = new VBox(inputSection.node());
        answerCard.setFillWidth(true);
        answerCard.setMinWidth(0);
        answerCard.setMaxWidth(Double.MAX_VALUE);
        answerCard.getStyleClass().add("answer-card");

        Label hintLabel = new Label();
        hintLabel.setWrapText(true);
        hintLabel.setMinWidth(0);
        hintLabel.setMaxWidth(Double.MAX_VALUE);
        hintLabel.getStyleClass().add("body-text");

        VBox hintCard = new VBox(hintLabel);
        hintCard.setFillWidth(true);
        hintCard.setMinWidth(0);
        hintCard.setMaxWidth(Double.MAX_VALUE);
        hintCard.getStyleClass().add("hint-card");
        hintCard.setVisible(false);
        hintCard.setManaged(false);

        boolean readyForNext = studentAnswer != null && studentAnswer.isReadyForNext();
        inputSection.setDisabled(readyForNext);
        int attemptCount = studentAnswer == null ? 0 : studentAnswer.getAttemptCount();

        Button previousButton = new Button("Terug");
        previousButton.getStyleClass().add("ghost-button");
        previousButton.setOnAction(event -> onPrevious.run());
        previousButton.setDisable(currentNumber <= 1);

        Button submitButton = new Button(attemptCount == 0 ? "Controleer" : "Controleer opnieuw");
        submitButton.getStyleClass().add("primary-button");
        submitButton.setOnAction(event -> onSubmit.accept(inputSection.readResponse()));
        submitButton.setDisable(readyForNext);
        submitButton.setVisible(!readyForNext);
        submitButton.setManaged(!readyForNext);

        Button hintButton = new Button("Hint");
        hintButton.getStyleClass().add("hint-button");
        boolean showHintButton = shouldShowHintButton(exercise) && !readyForNext;

        hintButton.setVisible(showHintButton);
        hintButton.setManaged(showHintButton);

        hintButton.setOnAction(event -> {
            hintLabel.setText(buildHintText(exercise));
            hintCard.setVisible(true);
            hintCard.setManaged(true);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button nextButton = new Button("Volgende");
        nextButton.getStyleClass().add("primary-button");
        nextButton.setOnAction(event -> onNext.run());
        nextButton.setVisible(readyForNext);
        nextButton.setManaged(readyForNext);

        HBox buttonBar = new HBox(10, previousButton, submitButton, hintButton, spacer, nextButton);
        buttonBar.setMinWidth(0);
        buttonBar.setMaxWidth(Double.MAX_VALUE);

        VBox contentBox = new VBox(18);
        contentBox.setFillWidth(true);
        contentBox.setMinWidth(0);
        contentBox.setMaxWidth(Double.MAX_VALUE);
        contentBox.setPadding(new Insets(0));
        contentBox.getChildren().addAll(headerCard, caseCard, answerCard, hintCard);

        if (studentAnswer != null) {
            AttemptResult latestAttempt = studentAnswer.getLatestAttempt();
            Label feedbackTitle = new Label("Feedback");
            feedbackTitle.getStyleClass().add("section-title");

            Label feedbackLabel = new Label(latestAttempt.getFeedback().getMessage());
            feedbackLabel.setWrapText(true);
            feedbackLabel.setMinWidth(0);
            feedbackLabel.setMaxWidth(Double.MAX_VALUE);
            feedbackLabel.getStyleClass().add("body-text");

            Label attemptLabel = new Label("Poging " + latestAttempt.getAttemptNumber() + " van 2");
            attemptLabel.getStyleClass().add("attempt-badge");

            VBox feedbackCard = new VBox(8, feedbackTitle, feedbackLabel, attemptLabel);
            feedbackCard.setFillWidth(true);
            feedbackCard.setMinWidth(0);
            feedbackCard.setMaxWidth(Double.MAX_VALUE);
            feedbackCard.getStyleClass().add("feedback-card");
            contentBox.getChildren().add(feedbackCard);
        }

        contentBox.getChildren().add(buttonBar);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(false);
        scrollPane.getStyleClass().add("content-scroll");
        return scrollPane;
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
        textArea.setMinWidth(0);
        textArea.setMaxWidth(Double.MAX_VALUE);
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
        textArea.setMinWidth(0);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.getStyleClass().add("code-area");
        return new InputSection(
                textArea,
                () -> ExerciseResponse.forText(textArea.getText()),
                disabled -> textArea.setDisable(disabled)
        );
    }

    private InputSection buildTextFieldSection(ExerciseResponse initialResponse) {
        TextField textField = new TextField(initialResponse.getTextAnswer());
        textField.setMinWidth(0);
        textField.setMaxWidth(Double.MAX_VALUE);
        return new InputSection(
                textField,
                () -> ExerciseResponse.forText(textField.getText()),
                disabled -> textField.setDisable(disabled)
        );
    }

    private InputSection buildRadioSection(List<AnswerOption> options, ExerciseResponse initialResponse) {
        ToggleGroup toggleGroup = new ToggleGroup();
        VBox box = new VBox(8);
        box.setFillWidth(true);
        box.setMinWidth(0);
        box.setMaxWidth(Double.MAX_VALUE);
        box.getStyleClass().add("choice-container");
        for (AnswerOption option : options) {
            RadioButton radioButton = new RadioButton(option.getLabel());
            radioButton.setWrapText(true);
            radioButton.setMinWidth(0);
            radioButton.setMaxWidth(Double.MAX_VALUE);
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
        box.setFillWidth(true);
        box.setMinWidth(0);
        box.setMaxWidth(Double.MAX_VALUE);
        box.getStyleClass().add("choice-container");
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (AnswerOption option : options) {
            CheckBox checkBox = new CheckBox(option.getLabel());
            checkBox.setWrapText(true);
            checkBox.setMinWidth(0);
            checkBox.setMaxWidth(Double.MAX_VALUE);
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
        flowPane.setMinWidth(0);
        flowPane.setMaxWidth(Double.MAX_VALUE);
        flowPane.getStyleClass().add("choice-container");

        for (AnswerOption option : options) {
            ToggleButton button = new ToggleButton(option.getLabel());
            button.setWrapText(true);
            button.setMinWidth(0);
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

    private boolean shouldShowHintButton(Exercise exercise) {
        return exercise.getExerciseType() != ExerciseType.OPEN_QUESTION
                && exercise.getExerciseType() != ExerciseType.REFLECTION;
    }

    private String buildHintText(Exercise exercise) {
        return switch (exercise.getExerciseType()) {
            case MULTIPLE_CHOICE ->
                    "Lees de vraag nog eens rustig. Kijk welk antwoord het beste past bij de code of opdracht.";

            case MULTI_SELECT, ERROR_ANALYSIS ->
                    "Let op: er kunnen meerdere signalen in de vraag zitten. Zoek eerst wat duidelijk klopt of juist fout gaat.";

            case CATEGORY_CHOICE ->
                    "Kijk naar de rol van het woord. Is het een class, attribuut, methode, object of verantwoordelijkheid?";

            case FILL_IN_THE_BLANK ->
                    "Lees de zin vÃ³Ã³r en na de lege plek. Vaak staat daar al welke term logisch past.";

            case CODE_WRITING ->
                    "Begin klein. Schrijf eerst de classnaam of methodekop. Daarna pas de rest.";

            default ->
                    "Lees de opdracht opnieuw en zoek eerst de belangrijkste woorden.";
        };
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
