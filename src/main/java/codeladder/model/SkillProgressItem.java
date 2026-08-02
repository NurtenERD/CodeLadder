package codeladder.model;

import java.util.Objects;

public final class SkillProgressItem {
    private final String label;
    private final SkillEvidenceStatus status;

    public SkillProgressItem(String label, SkillEvidenceStatus status) {
        this.label = Objects.requireNonNull(label, "label");
        this.status = Objects.requireNonNull(status, "status");
    }

    public String getLabel() {
        return label;
    }

    public SkillEvidenceStatus getStatus() {
        return status;
    }
}
