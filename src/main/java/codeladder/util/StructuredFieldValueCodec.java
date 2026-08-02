package codeladder.util;

import java.util.LinkedHashSet;
import java.util.Set;

public final class StructuredFieldValueCodec {
    private static final String SEPARATOR = "\u001F";

    private StructuredFieldValueCodec() {
    }

    public static String encodeMultiSelect(Set<String> selectedIds) {
        return selectedIds == null || selectedIds.isEmpty() ? "" : String.join(SEPARATOR, selectedIds);
    }

    public static Set<String> decodeMultiSelect(String encoded) {
        Set<String> selectedIds = new LinkedHashSet<>();
        if (encoded == null || encoded.isBlank()) {
            return selectedIds;
        }
        for (String value : encoded.split(SEPARATOR, -1)) {
            if (!value.isBlank()) {
                selectedIds.add(value);
            }
        }
        return selectedIds;
    }
}
