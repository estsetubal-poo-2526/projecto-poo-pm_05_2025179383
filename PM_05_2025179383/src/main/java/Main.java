import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        final int TOTAL_DAYS = 7;

        Player player1 = new Player("Fabio");
        Player player2 = new Player("Tiago");

        WorldMap map = new WorldMap();

        int day = 0;
        while (day <= TOTAL_DAYS){
            System.out.println("---------------------- DIA " + (day + 1) + " ----------------------------------");
            System.out.println("-------------------- Turno de " + player1.getName() + "--------------------------");
            menu(map,player1);
            player1.resetAC();

            System.out.println("-------------------- Turno de " + player2.getName() + "--------------------------");
            menu(map,player2);
            player2.resetAC();

            map.generateResources();
            day += 1;
            map.consumeResources();
        }


    }

    public static void menu(WorldMap map, Player actualPlayer){
        Scanner scanner =  new Scanner(System.in);

        boolean turnContinues = true;
        while (turnContinues){
        System.out.println(" 1 - Ver Mapa\n 2 - Adicionar Estrutura\n 3 - Ver Informações da Estrutura\n 4 - Terminar Turno\n 5 - Visualizar Inventario\n 6 - Melhorar Estruturas");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("---------------------- Mapa ---------------------");
                map.printMap();
                break;

            case 2:


                System.out.println("-------------------- Criação de Estruturas ----------------------");
                System.out.printf("\n" +
                        "Floresta %d Material to Upgrade: 5  STONES\n" +
                        "Mina %d Material to Upgrade: 8  WOOD\n" +
                        "Cidade %d Material to Upgrade: 20 STONE\n" +
                        "Rancho %d Material to Upgrade: 8 WOOD\n", Forest.getApNeeded(),Mine.getApNeeded(),City.getApNeeded(),Ranch.getApNeeded());

                System.out.println("\nPontos de Ação atuais: " + actualPlayer.getActionPoints());

                String createStructure = scanner.next();

                System.out.print("\nX: ");
                int createStructureX = correctCoordinates("x",scanner);
                System.out.print("\ny: ");
                int createStructureY = correctCoordinates("y",scanner);

                createStructure(map,actualPlayer,createStructure,createStructureX,createStructureY);


                System.out.println(actualPlayer.getActionPoints());
                break;
            case 3:
                System.out.println("--------------- Estrutura --------------------");
                System.out.println();



                System.out.print("\nx: ");
                int getStructureX = correctCoordinates("x",scanner);
                System.out.print("\ny: ");
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

                System.out.print("\nx: ");
                int upgradeStructureX = correctCoordinates("x",scanner);
                System.out.print("\ny: ");
                int upgradeStructureY = correctCoordinates("y",scanner);

                if (map.canInteract(upgradeStructureX,upgradeStructureY,actualPlayer)) {
                    map.getStructure(upgradeStructureX,upgradeStructureY).upgradeStructure();
                } else {
                    System.out.println("Estrutura não é sua");
                }
            }
        }
    }

        public static void createStructure(WorldMap map, Player player, String structure, int coordinateX, int coordinateY) {

            int cost = CreateStructure.getApCost(structure);

            if (player.getActionPoints() < cost) {
                System.out.println("Pontos Insuficientes");
                return;
            }

            Structures newStructure = CreateStructure.create(structure,player);

            if (newStructure == null) {
                System.out.println("Estrutura Invalida");
                return;
            }

            if (map.addStructure(newStructure,coordinateX,coordinateY)) {
                player.removeResource(ResourceType.ACTION_POINTS,cost);
            }

            if (newStructure.getClass() == City.class){
                player.addActionPoints(3);
            }

            System.out.println("Estrutura Criada com Sucesso");



        }

    public static int correctCoordinates(String axis, Scanner scanner) {
        int val;

        do {
            System.out.printf("\n:%s ", axis);
            val = scanner.nextInt();
        } while ( val < 0 || val > 6);

        return val;
    }
}

