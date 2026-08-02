package codeladder.data;

import codeladder.data.dto.AnswerOptionDto;
import codeladder.data.dto.StructuredAnswerDefinitionDto;
import codeladder.data.dto.StructuredFieldDefinitionDto;
import codeladder.model.AnswerOption;
import codeladder.model.SkillTag;
import codeladder.model.StructuredAnswerDefinition;
import codeladder.model.StructuredFieldDefinition;
import codeladder.model.StructuredFieldInputType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class StructuredAnswerDefinitionMapper {
    private final EnumValueParser enumValueParser;

    public StructuredAnswerDefinitionMapper(EnumValueParser enumValueParser) {
        this.enumValueParser = enumValueParser;
    }

    public StructuredAnswerDefinition map(StructuredAnswerDefinitionDto dto, String filePath, String exerciseId) {
        if (dto == null) {
            return null;
        }
        List<StructuredFieldDefinition> fields = new ArrayList<>();
        if (dto.getFields() != null) {
            for (StructuredFieldDefinitionDto fieldDto : dto.getFields()) {
                fields.add(mapField(fieldDto, filePath, exerciseId));
            }
        }
        return new StructuredAnswerDefinition(fields);
    }

    private StructuredFieldDefinition mapField(StructuredFieldDefinitionDto dto, String filePath, String exerciseId) {
        return new StructuredFieldDefinition(
                dto.getId(),
                dto.getLabel(),
                dto.getPrompt(),
                enumValueParser.parse(StructuredFieldInputType.class, "structuredField.inputType", dto.getInputType(), filePath, exerciseId),
                dto.isRequired(),
                mapOptions(dto.getOptions()),
                copySet(dto.getCorrectOptionIds()),
                mapKeywordGroups(dto.getAcceptedKeywordGroups()),
                enumValueParser.parse(SkillTag.class, "structuredField.skillTag", dto.getSkillTag(), filePath, exerciseId),
                dto.getRetryFeedback(),
                dto.getFinalFeedback()
        );
    }

    private List<AnswerOption> mapOptions(List<AnswerOptionDto> options) {
        if (options == null || options.isEmpty()) {
            return List.of();
        }
        List<AnswerOption> mapped = new ArrayList<>();
        for (AnswerOptionDto option : options) {
            mapped.add(new AnswerOption(option.getId(), option.getLabel()));
        }
        return mapped;
    }

    private List<Set<String>> mapKeywordGroups(List<List<String>> groups) {
        if (groups == null || groups.isEmpty()) {
            return List.of();
        }
        List<Set<String>> mapped = new ArrayList<>();
        for (List<String> group : groups) {
            mapped.add(group == null ? Set.of() : new LinkedHashSet<>(group));
        }
        return mapped;
    }

    private Set<String> copySet(List<String> values) {
        return values == null ? Set.of() : new LinkedHashSet<>(values);
    }
}
