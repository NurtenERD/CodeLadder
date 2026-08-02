package codeladder.controller.exercise;

import codeladder.data.ExerciseConfigurationException;
import codeladder.model.Exercise;
import codeladder.model.ExerciseResponse;
import codeladder.model.InteractionType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ExerciseInputRendererRegistry {
    private final Map<InteractionType, ExerciseInputRenderer> renderers = new EnumMap<>(InteractionType.class);

    public ExerciseInputRendererRegistry(List<ExerciseInputRenderer> renderers) {
        if (renderers == null) {
            throw new IllegalArgumentException("Rendererregistraties mogen niet null zijn");
        }
        for (ExerciseInputRenderer renderer : renderers) {
            if (renderer == null) {
                throw new ExerciseConfigurationException("Null rendererregistratie in ExerciseInputRendererRegistry");
            }
            for (InteractionType type : renderer.getSupportedTypes()) {
                register(type, renderer);
            }
        }
    }

    public ExerciseInputView render(Exercise exercise, ExerciseResponse initialResponse) {
        InteractionType type = exercise.getInteractionType();
        ExerciseInputRenderer renderer = renderers.get(type);
        if (renderer == null) {
            throw new ExerciseConfigurationException(
                    "Geen renderer geregistreerd voor interactionType " + type + " bij oefening " + exercise.getId()
            );
        }
        return renderer.render(exercise, initialResponse);
    }

    private void register(InteractionType type, ExerciseInputRenderer renderer) {
        if (type == null) {
            throw new ExerciseConfigurationException("Null interactionType in ExerciseInputRendererRegistry");
        }
        ExerciseInputRenderer existingRenderer = renderers.putIfAbsent(type, renderer);
        if (existingRenderer != null) {
            throw new ExerciseConfigurationException(
                    "Dubbele rendererregistratie voor interactionType "
                            + type
                            + ": "
                            + existingRenderer.getClass().getSimpleName()
                            + " en "
                            + renderer.getClass().getSimpleName()
            );
        }
    }
}
