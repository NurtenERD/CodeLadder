package codeladder.data.dto;

import java.util.List;

public class StructuredAnswerDefinitionDto {
    private List<StructuredFieldDefinitionDto> fields;

    public List<StructuredFieldDefinitionDto> getFields() {
        return fields;
    }

    public void setFields(List<StructuredFieldDefinitionDto> fields) {
        this.fields = fields;
    }
}
