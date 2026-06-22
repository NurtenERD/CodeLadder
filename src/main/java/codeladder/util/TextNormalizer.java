package codeladder.util;

public final class TextNormalizer {

    private TextNormalizer() {
    }

    public static String normalizeText(String text) {
        if (text == null) {
            return "";
        }

        return text
                .toLowerCase()
                .replaceAll("[\\p{Punct}]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public static String normalizeCode(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replaceAll("\\s+", "")
                .replace("{", "")
                .replace("}", "")
                .replace(";", "");
    }
}
