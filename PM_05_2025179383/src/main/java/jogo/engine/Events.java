package jogo.engine;

import java.util.Random;

public class Events {

    private int scoreModifier = 1;
    private int resourceModifier = 1;
    public static Random random = new Random();

    public int getResourceModifier() {
        return resourceModifier;
    }

    public int getScoreModifier() {
        return scoreModifier;
    }


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
                case 2:
                case 3:
                case 4:
                case 5:
                    resourceModifier = ZERO_PRODUCTION;
                    return "Estruturas não produzem Recursos!!!";

                case 6:

                    resourceModifier = DOUBLE_PRODUCTION;
                    return "Estruturas Produzem o dobro de Recursos";

                case 7:
                case 8:
                case 9:
                case 10:
                    scoreModifier = ZERO_PRODUCTION;
                    return "Estruturas não dão Pontuação!!!";

                case 11:

                    scoreModifier = DOUBLE_PRODUCTION;
                    return "Estruturas dão o dobro de Pontuação!!!";

                case 12:
                scoreModifier = ZERO_PRODUCTION;
                resourceModifier = DOUBLE_PRODUCTION;
                return "Greve Geral";


            }
        }
        return "Sem Evento Especial Hoje!";
    }
}
