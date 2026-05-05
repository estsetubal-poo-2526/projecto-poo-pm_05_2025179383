package jogo.engine;

import java.util.Random;

public class Events {


    // 1 -> Normal
    // 0 -> Nulo
    // 2 -> Dobro

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
     * Altera os modificadores consoante um valor aleatorio
     */
    public String triggerEvent() {

        scoreModifier = 1;
        resourceModifier = 1;

        // 15% de ter um evento a cada dia
        if (random.nextInt(100) < 45) {
            int event = random.nextInt(13);

            switch (event) {
                // 1/13 de produzir e ganhar pontuaçao em dobro
                case 0:
                    resourceModifier = 2;
                    scoreModifier = 2;
                    return "------- Evento: Estruturas Produzem o dobro de Recursos e de Pontuação!!! -------";

                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    // 5/13 das estruturas nao produzirem nada
                    resourceModifier = 0;
                    return "------- Evento: Estruturas não produzem Recursos!!! -------";

                // 1/13 das estruturas produzirem o dobro de recursos
                case 6:

                    resourceModifier = 2;
                    return "------- Evento: Estruturas Produzem o dobro de Recursos -------";

                case 7:
                case 8:
                case 9:
                case 10:
                    // 4/13 das estruturas não darem pontuaçao
                    scoreModifier = 0;
                    return "------- Evento: Estruturas não dão Pontuação!!! -------";

                // 1/13 das estruturas darem o dobro de pontuaçao
                case 11:

                    scoreModifier = 2;
                    return "------- Evento: Estruturas dão o dobro de Pontuação!!! -------";

                // 1/13 de não ganhar nem produzir nada
                case 12:
                scoreModifier = 0;
                resourceModifier = 0;
                return "------ Evento: Greve Geral -------";


            }
        }
        return "Sem Evento Especial Hoje!";
    }
}
