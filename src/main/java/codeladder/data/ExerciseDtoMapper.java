package codeladder.data;

import codeladder.data.dto.AnswerOptionDto;
import codeladder.data.dto.CaseStudyDto;
import codeladder.data.dto.ExerciseDto;
import codeladder.model.AnswerOption;
import codeladder.model.CaseStudy;
import codeladder.model.Exercise;
import codeladder.model.ProgrammingTask;
import codeladder.model.StepType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ExerciseDtoMapper {
    private final EnumValueParser enumValueParser;
    private final ExerciseMetadataMapper metadataMapper;
    private final ProgrammingTaskMapper programmingTaskMapper;
    private final StructuredAnswerDefinitionMapper structuredAnswerDefinitionMapper;

    public ExerciseDtoMapper(
            EnumValueParser enumValueParser,
            ExerciseMetadataMapper metadataMapper,
            ProgrammingTaskMapper programmingTaskMapper,
            StructuredAnswerDefinitionMapper structuredAnswerDefinitionMapper
    ) {
        this.enumValueParser = enumValueParser;
        this.metadataMapper = metadataMapper;
        this.programmingTaskMapper = programmingTaskMapper;
        this.structuredAnswerDefinitionMapper = structuredAnswerDefinitionMapper;
    }

    public CaseStudy mapCaseStudy(String routeBlockTitle, CaseStudyDto caseStudyDto) {
        return new CaseStudy(routeBlockTitle, caseStudyDto.getTitle(), caseStudyDto.getDescription());
    }

    public Exercise mapExercise(ExerciseDto dto, CaseStudy caseStudy, ProgrammingTask programmingTask, String filePath) {
        return new Exercise(
                dto.getId(),
                enumValueParser.parse(StepType.class, "stepType", dto.getStepType(), filePath, dto.getId()),
                metadataMapper.map(dto.getMetadata(), filePath, dto.getId()),
                dto.getTitle(),
                dto.getInstruction(),
                dto.getQuestion(),
                dto.getFocusText(),
                dto.getHintText(),
                caseStudy,
                mapOptions(dto.getOptions()),
                copySet(dto.getCorrectOptionIds()),
                copySet(dto.getAcceptedKeywords()),
                copySet(dto.getRequiredFragments()),
                structuredAnswerDefinitionMapper.map(dto.getStructuredAnswerDefinition(), filePath, dto.getId()),
                dto.getMinimumRequiredMatches(),
                dto.getSuccessFeedback(),
                dto.getRetryFeedback(),
                dto.getFinalFeedback(),
                programmingTask
        );
    }

    public ProgrammingTask mapProgrammingTask(codeladder.data.dto.ProgrammingTaskDto dto) {
        return programmingTaskMapper.map(dto);
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

    private Set<String> copySet(List<String> values) {
        return values == null ? Set.of() : new LinkedHashSet<>(values);
    }
}
