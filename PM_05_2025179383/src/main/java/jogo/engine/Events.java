package jogo.engine;

import java.util.Random;

/**
 * Represents the events that occur throughout the game.
 * Controls the resource production and score modifiers.
 * @author Fabio Cruz
 * @author Tiago Silva
 */

public class Events {

    // 1 -> Normal | 2 -> Double | 0 -> None
    private int scoreModifier = 1;
    private int resourceModifier = 1;
    public static Random random = new Random();

    public int getResourceModifier() {
        return resourceModifier;
    }

    public int getScoreModifier() {
        return scoreModifier;
    }


    /**
     * Modifies the game modifiers based on pre-established random events
     * that have a base probability of occurring.
     * * There is a 45% base chance for an event to trigger.
     * If triggered, the event is selected based on the following distribution:
     * - 1/13: Resource production and score generation are doubled.
     * - 1/13: Resource production is doubled.
     * - 1/13: Structures grant double score upon construction.
     * - 1/13: Structures produce nothing and grant no score for the day.
     * - 4/13: Structures grant no score upon construction.
     * - 5/13: No resource production occurs for the day.
     * * @return A String message describing the event that occurred.
     */
    public String triggerEvent() {

        scoreModifier = 1;
        resourceModifier = 1;

        final int BASE_CHANCE = 45;
        final int DOUBLE_PRODUCTION = 2;
        final int ZERO_PRODUCTION = 0;


        if (random.nextInt(100) < BASE_CHANCE) {
            int event = random.nextInt(13);

            switch (event) {
                case 0:
                    resourceModifier = DOUBLE_PRODUCTION;
                    scoreModifier = DOUBLE_PRODUCTION;
                    return "Estruturas Produzem o dobro de Recursos e de Pontuação!!!";

                case 1:
                    resourceModifier = ZERO_PRODUCTION;
                    return "Estruturas não produzem Recursos!!!";

                case 2:
                    resourceModifier = DOUBLE_PRODUCTION;
                    return "Estruturas Produzem o dobro de Recursos";

                case 3:
                    scoreModifier = ZERO_PRODUCTION;
                    resourceModifier = ZERO_PRODUCTION;
                    return "Greve Geral";

                case 4:
                case 5:
                case 6:
                case 7:
                    scoreModifier = DOUBLE_PRODUCTION;
                    return "Estruturas dão o dobro de Pontuação!!!";

                default:
                    scoreModifier = ZERO_PRODUCTION;
                    return "Estruturas não dão Pontuação!!!";


            }
        }
        return "Sem Evento Especial Hoje!";
    }
}
