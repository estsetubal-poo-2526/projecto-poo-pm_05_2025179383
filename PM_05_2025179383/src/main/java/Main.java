public class Main {


    public static void main(String[] args){

        Player p1 = new Player("Fabio");

        p1.addResource(Resources.STONE, 10);
        p1.addResource(Resources.WOOD, 3);
        p1.addResource(Resources.STONE,5);

        System.out.println(p1);

        p1.addScore(100);
        System.out.println(p1);


    }
}
