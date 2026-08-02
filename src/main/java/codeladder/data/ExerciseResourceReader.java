package codeladder.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class ExerciseResourceReader {
    private final ObjectMapper objectMapper;

    public ExerciseResourceReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T read(String resourcePath, Class<T> targetType) {
        try (InputStream inputStream = openStream(resourcePath)) {
            return objectMapper.readValue(inputStream, targetType);
        } catch (IOException exception) {
            throw new ExerciseConfigurationException("Kon JSON-bestand niet laden: " + resourcePath, exception);
        }
    }

    private InputStream openStream(String resourcePath) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader() == null
                ? null
                : Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            inputStream = ExerciseJsonLoader.class.getClassLoader().getResourceAsStream(resourcePath);
        }
        if (inputStream == null) {
            inputStream = ExerciseJsonLoader.class.getResourceAsStream("/" + resourcePath);
        }
        if (inputStream == null) {
            throw new ExerciseConfigurationException("Resource ontbreekt: " + resourcePath);
        }
        return inputStream;
    }
}
