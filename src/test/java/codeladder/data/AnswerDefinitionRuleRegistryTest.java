package codeladder.data;

import codeladder.model.ValidationType;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnswerDefinitionRuleRegistryTest {

    @Test
    void everyConcreteRuleReportsExactlyOneValidationType() {
        List<AnswerDefinitionRule> rules = List.of(
                new TextKeywordDefinitionRule(),
                new SingleChoiceDefinitionRule(),
                new MultiSelectDefinitionRule(),
                new CodeFragmentDefinitionRule(),
                new ReflectionDefinitionRule(),
                new StructuredFieldsDefinitionRule()
        );
        assertEquals(
                List.of(
                        ValidationType.TEXT_KEYWORDS,
                        ValidationType.SINGLE_CHOICE,
                        ValidationType.MULTI_SELECT,
                        ValidationType.CODE_FRAGMENTS,
                        ValidationType.REFLECTION,
                        ValidationType.STRUCTURED_FIELDS
                ),
                rules.stream().map(AnswerDefinitionRule::supportedType).toList()
        );
    }

    @Test
    void duplicateRuleRegistrationIsRejected() {
        ExerciseConfigurationException exception = assertThrows(
                ExerciseConfigurationException.class,
                () -> new AnswerDefinitionRuleRegistry(List.of(new TextKeywordDefinitionRule(), new DuplicateTextRule()))
        );
        assertTrue(exception.getMessage().contains("TEXT_KEYWORDS"));
        assertTrue(exception.getMessage().contains("TextKeywordDefinitionRule"));
        assertTrue(exception.getMessage().contains("DuplicateTextRule"));
    }

    @Test
    void answerDefinitionValidatorContainsNoCentralValidationTypeSwitch() throws Exception {
        String source = Files.readString(Path.of("src/main/java/codeladder/data/ExerciseAnswerDefinitionValidator.java"));
        assertTrue(source.contains("ruleRegistry.find"));
        assertTrue(!source.contains("switch"), "ExerciseAnswerDefinitionValidator mag geen centrale switch bevatten");
    }

    private static final class DuplicateTextRule extends TextKeywordDefinitionRule {
    }
}
