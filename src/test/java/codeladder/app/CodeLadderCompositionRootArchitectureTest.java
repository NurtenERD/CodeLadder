package codeladder.app;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;

class CodeLadderCompositionRootArchitectureTest {

    @Test
    void compositionRootContainsNoControllerRefPlaceholder() throws IOException {
        String source = Files.readString(
                Path.of("src/main/java/codeladder/app/CodeLadderCompositionRoot.java"),
                StandardCharsets.UTF_8
        );
        assertFalse(source.contains("AppController[]"));
        assertFalse(source.contains("controllerRef"));
        assertFalse(source.contains("AtomicReference"));
    }
}
