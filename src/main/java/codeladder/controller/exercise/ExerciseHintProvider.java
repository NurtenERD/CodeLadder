package codeladder.controller.exercise;

import codeladder.data.ExerciseConfigurationException;
import codeladder.model.Exercise;
import codeladder.model.InteractionType;

public class ExerciseHintProvider {

    public boolean shouldShowHintButton(Exercise exercise) {
        InteractionType type = exercise.getInteractionType();
        return type != InteractionType.OPEN_QUESTION && type != InteractionType.REFLECTION;
    }

    public String buildHintText(Exercise exercise) {
        if (!exercise.getHintText().isBlank()) {
            return exercise.getHintText();
        }
        return switch (exercise.getInteractionType()) {
            case MULTIPLE_CHOICE ->
                    "Lees de vraag nog eens rustig. Kijk welk antwoord het beste past bij de code of opdracht.";
            case MULTI_SELECT, ERROR_ANALYSIS ->
                    "Let op: er kunnen meerdere signalen in de vraag zitten. Zoek eerst wat duidelijk klopt of juist fout gaat.";
            case CATEGORY_CHOICE ->
                    "Kijk naar de rol van het woord. Is het een class, attribuut, methode, object of verantwoordelijkheid?";
            case FILL_IN_THE_BLANK ->
                    "Lees de zin vóór en na de lege plek. Vaak staat daar al welke term logisch past.";
            case CODE_WRITING ->
                    "Begin klein. Schrijf eerst de classnaam of methodekop. Daarna pas de rest.";
            case ANALYSIS_FORM ->
                    "Werk rustig per veld: doel, invoer, uitvoer, regel, grenswaarde, randgeval en patroon.";
            case OPEN_QUESTION, REFLECTION ->
                    "Lees de opdracht opnieuw en zoek eerst de belangrijkste woorden.";
            case ORDER_ITEMS ->
                    unsupported(exercise);
        };
    }

    private String unsupported(Exercise exercise) {
        throw new ExerciseConfigurationException(
                "Niet ondersteunde hint/interactie voor interactionType "
                        + exercise.getInteractionType()
                        + " bij oefening "
                        + exercise.getId()
        );
    }
}
