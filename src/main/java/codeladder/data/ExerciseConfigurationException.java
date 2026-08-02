package codeladder.data;

public class ExerciseConfigurationException extends RuntimeException {

    public ExerciseConfigurationException(String message) {
        super(message);
    }

    public ExerciseConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
