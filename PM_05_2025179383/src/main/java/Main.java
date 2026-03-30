public class Main {


    public static void main(String[] args){

        Player p1 = new Player("Fabio");
        Player p2 = new Player("Tiago");

        Map m1 = new Map(p1,p2);

        m1.printMap();

        Florest f1 = new Florest(p1);

        m1.addStructure(f1,2,4);

        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= 6; j++){
                m1.addStructure(f1,i,j);
            }
        }

        m1.printMap();

        System.out.println(m1.getOwner(5,3));

        m1.clearMap();
        m1.printMap();

        System.out.println(m1.isOccupied(2,3));

        m1.addStructure(f1,2,3);

        System.out.println(m1.isOccupied(2,3));


    }
}
