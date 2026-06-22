package codeladder.model;

public enum StepType {
    UNDERSTAND_ASSIGNMENT(
            1,
            "Trede 1 - Opdracht begrijpen",
            "Opdracht begrijpen",
            "Lees eerst rustig wat de app moet doen en welke informatie daarbij hoort."
    ),
    OBJECTS_ATTRIBUTES_METHODS(
            2,
            "Trede 2 - Objecten, attributen en methodes",
            "Objecten, attributen en methodes",
            "Onderzoek wat een ding is, wat informatie is, wat gedrag is en wat nog niet belangrijk is."
    ),
    CHOOSE_CLASSES(
            3,
            "Trede 3 - Classes kiezen",
            "Classes kiezen",
            "Bepaal welke objecten echte classes worden en welke verantwoordelijkheid daarbij hoort."
    ),
    WRITE_SMALL_CLASSES(
            4,
            "Trede 4 - Kleine classes schrijven",
            "Kleine classes schrijven",
            "Oefen met een eenvoudige class, private attributen en een kleine methode."
    ),
    CONSTRUCTORS(
            5,
            "Trede 5 - Constructors maken",
            "Constructors maken",
            "Oefen met constructors, parameters en het koppelen van waarden aan attributen."
    ),
    CREATE_AND_CALL_OBJECTS(
            6,
            "Trede 6 - Objecten maken en methodes aanroepen",
            "Objecten en methode-aanroepen",
            "Oefen met new, variabelen, objecten en puntnotatie."
    ),
    CLASS_RESPONSIBILITIES(
            7,
            "Trede 7 - Data, methodes en verantwoordelijkheden verdelen",
            "Verantwoordelijkheden verdelen",
            "Bepaal welke class welke data, methode en taak logisch hoort te hebben."
    );

    private final int orderNumber;
    private final String displayName;
    private final String shortTitle;
    private final String description;

    StepType(int orderNumber, String displayName, String shortTitle, String description) {
        this.orderNumber = orderNumber;
        this.displayName = displayName;
        this.shortTitle = shortTitle;
        this.description = description;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public String getDescription() {
        return description;
    }
}
