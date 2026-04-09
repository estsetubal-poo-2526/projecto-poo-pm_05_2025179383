import java.util.Scanner;

/*
@author Fabio
 */



public class Main {
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        final int TOTAL_DAYS = 2;

        Player player1 = new Player("Fabio");
        Player player2 = new Player("Tiago");

        WorldMap map = new WorldMap();

        int day = 0;
        while (day < TOTAL_DAYS){
            System.out.println("---------------------- DIA " + (day + 1) + " ----------------------------------");
            Events.triggerEvent();


            System.out.println("-------------------- Turno de " + player1.getName() + "--------------------------");
            menu(map,player1,scanner);
            player1.resetAC();

            System.out.println("-------------------- Turno de " + player2.getName() + "--------------------------");
            menu(map,player2,scanner);
            player2.resetAC();

            map.generateResources();
            day += 1;
            map.consumeResources();
        }

        System.out.println("----------------- Fim de Jogo ---------------");
        System.out.printf(
                "Pontos de %s : %d\n" +
                "Pontos de %s : %d\n", player1.getName(), player1.getScore(), player2.getName(), player2.getScore()
                        );

        System.out.println("----------------------- Vencedor ------------------");

        if (player1.getScore() == player2.getScore()) {
            System.out.println("Empate!");
        } else {

            Player winner = player1.getScore() > player2.getScore() ? player1 : player2;

            System.out.println(winner);
        }


    }

    /**
     *
     * Menu que permite ao jogador escolher a ação pretendida.
     *
     * @param map mapa em que se esta jogar
     * @param actualPlayer jogador que esta a jogar
     * @param scanner
     *
     *
     */


    public static void menu(WorldMap map, Player actualPlayer, Scanner scanner){

        boolean turnContinues = true;

        // Enquanto o Jogador atual não terminar o turno, continua
        while (turnContinues){
        System.out.println(" 1 - Ver Mapa\n " +
                "2 - Adicionar Estrutura\n " +
                "3 - Ver Informações da Estrutura\n " +
                "4 - Terminar Turno\n " +
                "5 - Visualizar Inventario\n " +
                "6 - Melhorar Estruturas\n" +
                " 7 - Procurar Recursos"
        );

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("---------------------- Mapa ---------------------");
                map.printMap();
                break;

            case 2:


                System.out.println("-------------------- Criação de Estruturas ----------------------");
                System.out.printf("\n" +
                        "Floresta[Pontos de Ação Necessarios: %d | Material para Construir: 3  STONE]\n" +
                        "Mina [Pontos de Ação Necessarios: %d | Material para Construir: 3  WOOD]\n" +
                        "Cidade [Pontos de Ação Necessarios: %d | Material para Construir: 10 STONE]\n" +
                        "Rancho [Pontos de Ação Necessarios: %d | Material para Construir: 3 WOOD]\n", Forest.getApNeeded(),Mine.getApNeeded(),City.getApNeeded(),Ranch.getApNeeded());

                System.out.println("\nInventário Atual: " + actualPlayer.getInventory());

                String createStructure = scanner.next();

                map.printMap();

                int createStructureX = correctCoordinates("x",scanner);
                int createStructureY = correctCoordinates("y",scanner);

                createStructure(map,actualPlayer,createStructure,createStructureX,createStructureY);


                System.out.println(actualPlayer.getActionPoints());
                break;
            case 3:
                System.out.println("--------------- Estrutura --------------------");
                System.out.println();




                int getStructureX = correctCoordinates("x",scanner);
                int getStructureY = correctCoordinates("y",scanner);

                System.out.println(map.getStructure(getStructureX,getStructureY));
                System.out.println(map.getOwner(getStructureX,getStructureY));

                break;


            case 4:
                 System.out.println("-------------------- Turno Terminado -----------------------");
                turnContinues = false;
                break;

            case 5:
                System.out.println("------------------------ Inventario -------------------------");
                System.out.println(actualPlayer.getInventory());
                break;

            case 6:
                System.out.println("--------------- Upgrade --------------------");

                int upgradeX = correctCoordinates("x", scanner);
                int upgradeY = correctCoordinates("y", scanner);

                if (map.canInteract(upgradeX, upgradeY, actualPlayer)) {
                    if (actualPlayer.getActionPoints() >= 10) {
                        if (map.getStructure(upgradeX, upgradeY).upgradeStructure()) {
                            actualPlayer.removeResource(ResourceType.ACTION_POINTS, 10);
                            System.out.println("Upgrade concluído com sucesso! 10 AP consumidos.");
                        }
                    } else {
                        System.out.println("Erro: Pontos de Ação insuficientes (precisas de 10 AP).");
                    }
                } else {
                    System.out.println("Erro: Esta estrutura não te pertence ou está vazia.");
                }
                break;

                // Forma de obter recursos que impede o softlock da falta de recursos
            case 7:
                System.out.println("Escolha o recurso para procurar (1-Wood, 2-Stone, 3-Food):");

                int resType = scanner.nextInt();

                if (actualPlayer.getActionPoints() >= 4) {
                    actualPlayer.removeResource(ResourceType.ACTION_POINTS, 4);

                    int gathered = new java.util.Random().nextInt(3) + 2; // Ganha 2 a 4

                    ResourceType selected = (resType == 1) ? ResourceType.WOOD : (resType == 2) ? ResourceType.STONE : ResourceType.FOOD;

                    actualPlayer.addResource(selected, gathered);

                    System.out.println("Encontraste " + gathered + " de " + selected);
                    System.out.println(actualPlayer.getActionPoints());
                } else {
                    System.out.println("AP insuficiente para procurar recursos.");
                }
                break;


                // Cheat Code para testar as coisas
            case 999:
                actualPlayer.addActionPoints(10000);
                actualPlayer.addResource(ResourceType.WOOD,10000);
                actualPlayer.addResource(ResourceType.STONE,10000);
                actualPlayer.addResource(ResourceType.FOOD,10000);

            }
        }
    }

    /**
     *
     * Metodo auciliar para criar uma estrutura
     *
     * @param map mapa em que se esta a jogar
     * @param player jogador atual
     * @param structure estrutura em texto que tem de ser criada
     * @param x cordenada x para a estrutura ser criada
     * @param y cordenada y para a estrutura ser criada
     */

    public static void createStructure(WorldMap map, Player player, String structure, int x, int y) {
        if (!map.isNotOccupied(x, y)) {
            System.out.println("Espaço Ocupado!");
            return;
        }

        int apCost = CreateStructure.getApCost(structure);
        int matCost = CreateStructure.getMaterialCost(structure);
        ResourceType matType = CreateStructure.getMaterialType(structure);

        if (player.getActionPoints() < apCost) {
            System.out.println("Pontos de Ação Insuficientes");
            return;
        }
        if (player.getResourceQuantity(matType) < matCost) {
            System.out.println("Materiais Insuficientes, são necessários: " + matCost + " " + matType);
            return;
        }

        Structures newStr = CreateStructure.create(structure, player);

        // Verifica se a estrutura é valida
        if (newStr != null) {
            map.addStructure(newStr, x, y);
            player.removeResource(ResourceType.ACTION_POINTS, apCost);
            player.removeResource(matType, matCost);
            System.out.println("Estrutura Criada com Sucesso");
        }
    }


    /**
     * Metodo auciliar que ajuda em ler as coordenadas e garante que esta entre 0 e 6
     *
     * @param axis eixo em que se quer ler as cordenadas
     * @param scanner
     * @return devolve o valor entre 0 e 6
     */

    public static int correctCoordinates(String axis, Scanner scanner) {
        int val;

        do {
            System.out.printf("%s: ", axis);
            val = scanner.nextInt();
        } while ( val < 0 || val > 6);

        return val;
    }
}

