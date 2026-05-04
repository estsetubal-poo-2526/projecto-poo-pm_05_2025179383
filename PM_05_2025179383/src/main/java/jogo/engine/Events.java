package jogo.engine;

import java.util.Random;

public class Events {

    // 1 -> Normal
    // 0 -> Nulo
    // 2 -> Dobro

    private static int scoreModifier = 1;
    private static int resourceModifier = 1;
    public static Random random = new Random();

    public static int getResourceModifier() {
        return resourceModifier;
    }

    public static int getScoreModifier() {
        return scoreModifier;
    }

    /**
     * Altera os modificadores consoante um valor aleatorio
     */
    public static void triggerEvent() {

        scoreModifier = 1;
        resourceModifier = 1;

        // 15% de ter um evento a cada dia
        if (random.nextInt(100) < 15) {
            int event = random.nextInt(13);

            switch (event) {
                // 1/20 de produzir e ganhar pontuaçao em dobro
                case 0:
                    System.out.println("------- Evento: Estruturas Produzem o dobro de Recursos e de Pontuação!!! -------");
                    resourceModifier = 2;
                    scoreModifier = 2;
                    break;

                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    // 5/13 das estruturas nao produzirem nada
                    System.out.println("------- Evento: Estruturas não produzem Recursos!!! -------");
                    resourceModifier = 0;
                    break;
                // 1/13 das estruturas produzirem o dobro de recursos
                case 6:
                    System.out.println("------- Evento: Estruturas Produzem o dobro de Recursos -------");
                    resourceModifier = 2;
                    break;

                case 7:
                case 8:
                case 9:
                case 10:
                    // 4/13 das estruturas não darem pontuaçao
                    System.out.println("------- Evento: Estruturas não dão Pontuação!!! -------");
                    scoreModifier = 0;
                    break;

                    // 1/13 das estruturas darem o dobro de pontuaçao
                case 11:
                    System.out.println("------- Evento: Estruturas dão o dobro de Pontuação!!! -------");
                    scoreModifier = 2;
                    break;

                    // 1/13 de não ganhar nem produzir nada
                case 12:
                    System.out.println("------ Evento: Greve Geral -------");
                    scoreModifier = 0;
                    resourceModifier = 0;



            }
        }
    }
}
