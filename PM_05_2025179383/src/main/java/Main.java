import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        Player p1 = new Player("Fabio");
        Player p2 = new Player("Tiago");

        Map m1 = new Map(p1,p2);



        int day = 0;
        while (day < 7){
            System.out.println("---------------------- DIA " + (day + 1) + " ----------------------------------");
            System.out.println("-------------------- Turno de " + p1.getName() + "--------------------------");
            menu(m1,p1);
            p1.resetAC();

            System.out.println("-------------------- Turno de " + p2.getName() + "--------------------------");
            menu(m1,p2);
            p2.resetAC();

            m1.generateResources();
            day += 1;
            m1.consumeResources();
        }


    }

    public static void menu(Map map, Player p1){
        Scanner s1 =  new Scanner(System.in);

        boolean continuee = true;
        while (continuee){
        System.out.println(" 1 - Ver Mapa\n 2 - Adicionar Estrutura\n 3 - Ver Informações da Estrutura\n 4 - Terminar Turno\n 5 - Visualizar Inventario\n 6 - Melhorar Estruturas");

        int c = s1.nextInt();

        switch (c) {
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
                        "Rancho %d Material to Upgrade: 8 WOOD\n", Florest.getApNeeded(),Mine.getApNeeded(),City.getApNeeded(),Ranch.getApNeeded());

                System.out.println("\nPontos de Ação atuais: " + p1.getActionPoints());

                String k = s1.next();

                System.out.print("\nx: ");
                int x = correctCordinates("x",s1);
                System.out.print("\ny: ");
                int y = correctCordinates("y",s1);

                createStructure(map,p1,k,x,y);


                System.out.println(p1.getActionPoints());
                break;
            case 3:
                System.out.println("--------------- Estrutura --------------------");
                System.out.println();



                System.out.print("\nx: ");
                int x2 = correctCordinates("x",s1);
                System.out.print("\ny: ");
                int y2 = correctCordinates("y",s1);

                System.out.println(map.getStructure(x2,y2));
                System.out.println(map.getOwner(x2,y2));

                break;


            case 4:
                 System.out.println("-------------------- Turno Terminado -----------------------");
                continuee = false;
                break;

            case 5:
                System.out.println("------------------------ Inventario -------------------------");
                System.out.println(p1.getInventory());
                break;

            case 6:

                System.out.println("--------------- Upgrade --------------------");

                System.out.print("\nx: ");
                int x3 = correctCordinates("x",s1);
                System.out.print("\ny: ");
                int y3 = correctCordinates("y",s1);

                if (map.canInteract(x3,y3,p1)) {
                    map.getStructure(x3,y3).upgradeStructure();
                } else {
                    System.out.println("Estrutura não é sua");
                }


        }
        }


        }

        public static void createStructure(Map map, Player player,String structure, int x, int y) {
            int custo = 0;
            Structures nova = null;

            if (structure.equalsIgnoreCase("floresta")) {
                custo = Florest.getApNeeded();
                nova = new Florest(player);
            } else if (structure.equalsIgnoreCase("mina")) {
                custo = Mine.getApNeeded();
                nova = new Mine(player);
            } else if (structure.equalsIgnoreCase("Rancho")) {
                custo = Ranch.getApNeeded();
                nova = new Ranch(player);
            } else if (structure.equalsIgnoreCase("City")) {
                custo = City.getApNeeded();
                nova = new City(player);
            }

            if (nova != null && player.getActionPoints() >= custo) {
                player.removeResource("actionPoint", custo);
                map.addStructure(nova, x, y);
                if (structure.equalsIgnoreCase("City")) {
                    player.addActionPoints(3);
                }
            } else {
                System.out.println("Erro: Pontos insuficientes ou tipo inválido.");
            }
    }

    public static int correctCordinates(String axis, Scanner scanner) {
        int val;

        do {
            System.out.printf("\n:%s ", axis);
            val = scanner.nextInt();
        } while ( val < 0 || val > 6);

        return val;
    }
}

