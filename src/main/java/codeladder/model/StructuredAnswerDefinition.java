package codeladder.model;

import java.util.ArrayList;
import java.util.List;

public final class StructuredAnswerDefinition {
    private final List<StructuredFieldDefinition> fields;

    public StructuredAnswerDefinition(List<StructuredFieldDefinition> fields) {
        this.fields = fields == null ? List.of() : new ArrayList<>(fields);
    }

    public List<StructuredFieldDefinition> getFields() {
        return new ArrayList<>(fields);
    }
}
