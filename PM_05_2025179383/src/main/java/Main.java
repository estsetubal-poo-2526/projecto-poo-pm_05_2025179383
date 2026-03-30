import java.util.Scanner;

public class Main {


    public static void main(String[] args){

        Player actualPlayer;
        Player p1 = new Player("Fabio");
        Player p2 = new Player("Tiago");

        Map m1 = new Map(p1,p2);



        int day = 0;
        while (day < 7){

            menu(m1,p1,p2);







            day += 1;
        }


    }

    public static void menu(Map map, Player p1, Player p2){
        Scanner s1 = new Scanner(System.in);

        Player currentPlayer = p1;
        while (p1.getActionPoints() > 0 && p2.getActionPoints() > 0) {
        System.out.println(" 1 - Mostrar o Mapa\n 2 - Criar Estrutura\n 3 - Terminar Turno\n 4 - Mostrar Player\n 5 - Mostrar informação de Estrutura");

        int i = s1.nextInt();
        switch (i) {
            case 1:
                map.printMap();
                break;
            case 2:
                System.out.println("Floresta | Mina | Rancho | Cidade");
                String structure = s1.next();

                System.out.println("x: ");
                int x = s1.nextInt();

                System.out.println("y: ");
                int y = s1.nextInt();

                addStructure(x, y, structure, map, currentPlayer);

                break;
            case 3:
                currentPlayer = p2;
                break;
            case 4:
                System.out.println(currentPlayer);
                break;
            case 5:
                System.out.println("x: ");
                int x2 = s1.nextInt();

                System.out.println("y: ");
                int y2 = s1.nextInt();
                System.out.println(map.getStructure(x2,y2));
                System.out.println(map.getOwner(x2,y2));
                break;
            default:
                System.out.println("?");


        }
        }
    }

    public static void addStructure(int x, int y, String s, Map map,Player currentPlayer) {
        if (s.equalsIgnoreCase("floresta")) {
            map.addStructure(new Florest(currentPlayer),x,y);
        } else if (s.equalsIgnoreCase("mina")) {
            map.addStructure(new Mine(currentPlayer),x,y);
        } else if (s.equalsIgnoreCase("Rancho")) {
            map.addStructure(new Ranch(currentPlayer),x,y);
        } else if (s.equalsIgnoreCase("City")) {
            map.addStructure(new City(currentPlayer),x,y);
        }
    }
}
