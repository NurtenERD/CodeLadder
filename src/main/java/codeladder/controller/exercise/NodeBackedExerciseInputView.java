package codeladder.controller.exercise;

import codeladder.model.ExerciseResponse;
import javafx.scene.Node;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class NodeBackedExerciseInputView implements ExerciseInputView {
    private final Node node;
    private final Supplier<ExerciseResponse> responseSupplier;
    private final Consumer<Boolean> disableHandler;

    public NodeBackedExerciseInputView(
            Node node,
            Supplier<ExerciseResponse> responseSupplier,
            Consumer<Boolean> disableHandler
    ) {
        this.node = node;
        this.responseSupplier = responseSupplier;
        this.disableHandler = disableHandler;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public ExerciseResponse readResponse() {
        return responseSupplier.get();
    }

    @Override
    public void setDisabled(boolean disabled) {
        disableHandler.accept(disabled);
    }
}
