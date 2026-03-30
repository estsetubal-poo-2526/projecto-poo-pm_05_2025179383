import java.util.Scanner;

public class Main {


    public static void main(String[] args){

        Player p1 = new Player("Fabio");
        Player p2 = new Player("Tiago");

        Map m1 = new Map(p1,p2);



        int day = 0;
        while (day < 7){
            System.out.println("---------------------- DIA " + (day + 1) + " ----------------------------------");
            menu(m1,p1);
            p1.resetAC();

            System.out.println("Turno Acabado");

            menu(m1,p2);
            p2.resetAC();

            m1.generateResources();
            day += 1;
        }


    }

    public static void menu(Map map, Player p1){

        boolean continuee = true;
        while (continuee){
        System.out.println(" 1 - Ver Mapa\n 2 - Adicionar Estrutura\n 3 - Ver Informações da Estrutura\n 4 - Terminar Turno\n 5 - Visualizar Inventario\n 6 - Upgradar Estruturas");

        Scanner s1 =  new Scanner(System.in);

        int c = s1.nextInt();

        switch (c) {
            case 1:
                System.out.println("---------------------- Mapa ---------------------");
                map.printMap();
                break;

            case 2:

                System.out.println("-------------------- Criação de Estruturas ----------------------");
                System.out.println("Floresta | Mina | Cidade | Rancho");

                Scanner s2 = new Scanner(System.in);
                String k = s2.next();

                System.out.print("\nx: ");
                int x = s1.nextInt();
                System.out.print("\ny: ");
                int y = s1.nextInt();



                if (k.equalsIgnoreCase("floresta")) {

                    if (p1.getActionPoints() - Florest.getActionPointsNeeded() >= 0) {
                        p1.removeResource("actionPoint",Florest.getActionPointsNeeded());
                        map.addStructure(new Florest(p1),x,y);

                    }

                } else if (k.equalsIgnoreCase("mina")) {
                    if (p1.getActionPoints() - Mine.getActionPointsNeeded() >= 0) {
                        p1.removeResource("actionPoint",Mine.getActionPointsNeeded());
                        map.addStructure(new Mine(p1),x,y);

                    }
                } else if (k.equalsIgnoreCase("Rancho")) {
                    if (p1.getActionPoints() - Ranch.getActionPointsNeeded() >= 0) {
                        p1.removeResource("actionPoint",Ranch.getActionPointsNeeded());
                        map.addStructure(new Ranch(p1),x,y);

                    }
                } else if (k.equalsIgnoreCase("City")) {
                    if (p1.getActionPoints() - City.getApNeeded() >= 0) {
                        p1.removeResource("actionPoint",City.getApNeeded());
                        map.addStructure(new City(p1),x,y);

                    }

                }
                System.out.println(p1.getActionPoints());
                break;
            case 3:
                System.out.println();

                Scanner s3 = new Scanner(System.in);

                System.out.print("\nx: ");
                int x2 = s3.nextInt();
                System.out.print("\ny: ");
                int y2 = s3.nextInt();

                System.out.println(map.getStructure(x2,y2));
                System.out.println(map.getOwner(x2,y2));

                break;


            case 4:
                continuee = false;
                break;

            case 5:
                System.out.println(p1.getInventory());

        }
        }


        }
}
