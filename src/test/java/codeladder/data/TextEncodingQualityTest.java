package codeladder.data;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

class TextEncodingQualityTest {

    private static final List<String> ROOTS = List.of(
            "src/main/java",
            "src/test/java",
            "src/main/resources"
    );

    private static final List<String> TEXT_EXTENSIONS = List.of(
            ".java",
            ".json",
            ".txt",
            ".md",
            ".properties",
            ".xml"
    );

    private static final List<String> FORBIDDEN_PATTERNS = List.of(
            "\u00C3",
            "\u00C2",
            "\u00E2",
            "\uFFFD",
            "pati" + "\u00C3",
            "\u00C3\u00A9",
            "\u00C3\u00AB",
            "\u00C3\u00A9\u00C3\u00A9n"
    );

    @Test
    void textFilesDoNotContainBrokenEncodingPatterns() throws IOException {
        for (String root : ROOTS) {
            try (Stream<Path> paths = Files.walk(Path.of(root))) {
                for (Path path : paths.filter(Files::isRegularFile).filter(this::isTextFile).toList()) {
                    String text = Files.readString(path, StandardCharsets.UTF_8);
                    for (String pattern : FORBIDDEN_PATTERNS) {
                        if (text.contains(pattern)) {
                            fail("Kapotte encoding gevonden in " + path + " met patroon [" + printablePattern(pattern) + "]");
                        }
                    }
                }
            }
        }
    }

    private boolean isTextFile(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        for (String extension : TEXT_EXTENSIONS) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private String printablePattern(String pattern) {
        return switch (pattern) {
            case "\u00C3" -> "U+00C3";
            case "\u00C2" -> "U+00C2";
            case "\u00E2" -> "U+00E2";
            case "\uFFFD" -> "U+FFFD";
            case "pati\u00C3" -> "pati + U+00C3";
            case "\u00C3\u00A9" -> "U+00C3 U+00A9";
            case "\u00C3\u00AB" -> "U+00C3 U+00AB";
            case "\u00C3\u00A9\u00C3\u00A9n" -> "U+00C3 U+00A9 U+00C3 U+00A9 n";
            default -> pattern;
        };
    }
}
