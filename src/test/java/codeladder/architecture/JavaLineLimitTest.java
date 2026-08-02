package codeladder.architecture;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

class JavaLineLimitTest {
    private static final int MAX_LINES = 200;
    private static final List<Path> ROOTS = List.of(Path.of("src/main/java"), Path.of("src/test/java"));

    @Test
    void javaFilesStayWithinPhysicalLineLimit() throws IOException {
        for (Path root : ROOTS) {
            try (Stream<Path> paths = Files.walk(root)) {
                for (Path path : paths.filter(Files::isRegularFile).filter(file -> file.toString().endsWith(".java")).toList()) {
                    long lines = Files.lines(path).count();
                    if (lines > MAX_LINES) {
                        fail("Java-bestand overschrijdt limiet: " + path + " heeft " + lines + " regels; maximum is " + MAX_LINES);
                    }
                }
            }
        }
    }
}
