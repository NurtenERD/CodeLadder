package codeladder.model;

public enum SkillEvidenceStatus {
    NOG_NIET_AANGETOOND,
    AANGETOOND_MET_ONDERSTEUNING,
    ZELFSTANDIG_AANGETOOND,
    TRANSFER_AANGETOOND;

    public String getDisplayName() {
        return switch (this) {
            case NOG_NIET_AANGETOOND -> "Nog niet aangetoond";
            case AANGETOOND_MET_ONDERSTEUNING -> "Aangetoond met ondersteuning";
            case ZELFSTANDIG_AANGETOOND -> "Zelfstandig aangetoond";
            case TRANSFER_AANGETOOND -> "Transfer aangetoond";
        };
    }
}
