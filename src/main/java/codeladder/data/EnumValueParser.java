package codeladder.data;

public class EnumValueParser {

    public <T extends Enum<T>> T parse(
            Class<T> enumType,
            String fieldName,
            String value,
            String filePath,
            String exerciseId
    ) {
        if (value == null || value.isBlank()) {
            throw missingValue(fieldName, filePath, exerciseId);
        }
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException exception) {
            throw new ExerciseConfigurationException(
                    describe(fieldName, value, filePath, exerciseId),
                    exception
            );
        }
    }

    private ExerciseConfigurationException missingValue(String fieldName, String filePath, String exerciseId) {
        return new ExerciseConfigurationException(
                "Verplicht veld ontbreekt: " + fieldName + " in " + filePath + exerciseSuffix(exerciseId)
        );
    }

    private String describe(String fieldName, String value, String filePath, String exerciseId) {
        return "Ongeldige enumwaarde voor " + fieldName + ": '" + value + "' in " + filePath + exerciseSuffix(exerciseId);
    }

    private String exerciseSuffix(String exerciseId) {
        return exerciseId == null || exerciseId.isBlank() ? "" : " voor oefening " + exerciseId;
    }
}
