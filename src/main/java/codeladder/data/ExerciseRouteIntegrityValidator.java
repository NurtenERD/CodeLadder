package codeladder.data;

import codeladder.data.dto.ExerciseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseRouteIntegrityValidator {

    public void validateIndexFiles(List<String> resourcePaths) {
        if (resourcePaths == null || resourcePaths.isEmpty()) {
            throw new ExerciseConfigurationException("Geen exercise-bestanden gevonden in " + ExerciseJsonLoader.INDEX_PATH);
        }
        Map<String, Integer> seenPaths = new HashMap<>();
        for (int index = 0; index < resourcePaths.size(); index++) {
            String resourcePath = resourcePaths.get(index);
            if (resourcePath == null || resourcePath.isBlank()) {
                throw new ExerciseConfigurationException("Lege resourceverwijzing in " + ExerciseJsonLoader.INDEX_PATH);
            }
            Integer firstIndex = seenPaths.putIfAbsent(resourcePath, index);
            if (firstIndex != null) {
                throw new ExerciseConfigurationException(
                        "Dubbele resourceverwijzing in " + ExerciseJsonLoader.INDEX_PATH
                                + ": " + resourcePath
                                + " (eerste positie " + firstIndex + ", tweede positie " + index + ")"
                );
            }
        }
    }

    public void validateUniqueExerciseIds(List<LoadedExerciseFile> loadedFiles) {
        Map<String, String> firstOccurrences = new HashMap<>();
        for (LoadedExerciseFile loadedFile : loadedFiles) {
            for (ExerciseDto exercise : loadedFile.fileDto().getExercises()) {
                String firstPath = firstOccurrences.putIfAbsent(exercise.getId(), loadedFile.filePath());
                if (firstPath != null) {
                    throw new ExerciseConfigurationException(
                            "Dubbel exercise-id gevonden: "
                                    + exercise.getId()
                                    + ". Eerste vindplaats: "
                                    + firstPath
                                    + ". Tweede vindplaats: "
                                    + loadedFile.filePath()
                    );
                }
            }
        }
    }
}
