package codeladder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class AnswerDefinition {
    private final List<AnswerOption> options;
    private final Set<String> correctOptionIds;
    private final Set<String> acceptedKeywords;
    private final Set<String> requiredFragments;
    private final StructuredAnswerDefinition structuredAnswerDefinition;
    private final int minimumRequiredMatches;

    public AnswerDefinition(
            List<AnswerOption> options,
            Set<String> correctOptionIds,
            Set<String> acceptedKeywords,
            Set<String> requiredFragments,
            StructuredAnswerDefinition structuredAnswerDefinition,
            int minimumRequiredMatches
    ) {
        this.options = options == null ? List.of() : new ArrayList<>(options);
        this.correctOptionIds = copySet(correctOptionIds);
        this.acceptedKeywords = copySet(acceptedKeywords);
        this.requiredFragments = copySet(requiredFragments);
        this.structuredAnswerDefinition = structuredAnswerDefinition;
        this.minimumRequiredMatches = minimumRequiredMatches;
    }

    public List<AnswerOption> getOptions() {
        return Collections.unmodifiableList(new ArrayList<>(options));
    }

    public Set<String> getCorrectOptionIds() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(correctOptionIds));
    }

    public Set<String> getAcceptedKeywords() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(acceptedKeywords));
    }

    public Set<String> getRequiredFragments() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(requiredFragments));
    }

    public Optional<StructuredAnswerDefinition> getStructuredAnswerDefinition() {
        return Optional.ofNullable(structuredAnswerDefinition);
    }

    public int getMinimumRequiredMatches() {
        return minimumRequiredMatches;
    }

    private Set<String> copySet(Set<String> values) {
        return values == null ? Set.of() : new LinkedHashSet<>(values);
    }
}
