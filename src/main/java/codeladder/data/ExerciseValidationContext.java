package codeladder.data;

record ExerciseValidationContext(String filePath, String exerciseId) {

    ExerciseValidationContext withExercise(String id) {
        return new ExerciseValidationContext(filePath, id);
    }

    ExerciseConfigurationException missingField(String fieldName) {
        return new ExerciseConfigurationException(fieldName + " ontbreekt in " + filePath + suffix());
    }

    ExerciseConfigurationException invalidValue(String fieldName, String value) {
        return new ExerciseConfigurationException(
                "Ongeldige waarde voor " + fieldName + ": '" + value + "' in " + filePath + suffix()
        );
    }

    ExerciseConfigurationException error(String message) {
        return new ExerciseConfigurationException(message + " in " + filePath + suffix());
    }

    private String suffix() {
        return exerciseId == null || exerciseId.isBlank() ? "" : " voor oefening " + exerciseId;
    }
}
