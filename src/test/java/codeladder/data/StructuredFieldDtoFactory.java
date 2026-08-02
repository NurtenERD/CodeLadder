package codeladder.data;

import codeladder.data.dto.AnswerOptionDto;
import codeladder.data.dto.StructuredAnswerDefinitionDto;
import codeladder.data.dto.StructuredFieldDefinitionDto;
import codeladder.model.SkillTag;

import java.util.List;

final class StructuredFieldDtoFactory {

    private StructuredFieldDtoFactory() {
    }

    static StructuredAnswerDefinitionDto analysisForm(String fieldId) {
        StructuredFieldDefinitionDto field = new StructuredFieldDefinitionDto();
        field.setId(fieldId);
        field.setLabel("Doel");
        field.setPrompt("Wat is het doel?");
        field.setInputType("TEXT");
        field.setRequired(true);
        field.setAcceptedKeywordGroups(List.of(List.of("aantal")));
        field.setSkillTag(SkillTag.GOAL.name());
        field.setRetryFeedback("Kijk opnieuw");
        field.setFinalFeedback("Het doel is het aantal.");

        StructuredAnswerDefinitionDto definition = new StructuredAnswerDefinitionDto();
        definition.setFields(List.of(field));
        return definition;
    }

    static StructuredFieldDefinitionDto textField(String id, String skillTag) {
        StructuredFieldDefinitionDto field = new StructuredFieldDefinitionDto();
        field.setId(id);
        field.setLabel("Label " + id);
        field.setPrompt("Prompt " + id);
        field.setInputType("TEXT");
        field.setRequired(true);
        field.setAcceptedKeywordGroups(List.of(List.of("woord")));
        field.setSkillTag(skillTag);
        field.setRetryFeedback("Retry " + id);
        field.setFinalFeedback("Final " + id);
        return field;
    }

    static StructuredFieldDefinitionDto singleChoiceField(String id) {
        StructuredFieldDefinitionDto field = baseChoiceField(id, "SINGLE_CHOICE");
        field.setCorrectOptionIds(List.of("a"));
        return field;
    }

    static StructuredFieldDefinitionDto multiSelectField(String id) {
        StructuredFieldDefinitionDto field = baseChoiceField(id, "MULTI_SELECT");
        field.setCorrectOptionIds(List.of("a"));
        return field;
    }

    private static StructuredFieldDefinitionDto baseChoiceField(String id, String inputType) {
        StructuredFieldDefinitionDto field = new StructuredFieldDefinitionDto();
        field.setId(id);
        field.setLabel("Label " + id);
        field.setPrompt("Prompt " + id);
        field.setInputType(inputType);
        field.setRequired(true);
        field.setOptions(List.of(option("a"), option("b")));
        field.setSkillTag(SkillTag.GOAL.name());
        field.setRetryFeedback("Retry " + id);
        field.setFinalFeedback("Final " + id);
        return field;
    }

    private static AnswerOptionDto option(String id) {
        AnswerOptionDto option = new AnswerOptionDto();
        option.setId(id);
        option.setLabel("Optie " + id);
        return option;
    }
}
