public class Main {


    public static void main(String[] args){

        Player p1 = new Player("Fabio");

        p1.addResource("WOOD", 10);
        p1.addResource("STONE", 3);
        p1.addResource("STONE",10000);

        System.out.println(p1);

        p1.addScore(100);
        System.out.println(p1);

        Structures s1 = new Structures("Fazenda", "WOOD", "STONE", 10, 15, p1,"STONE");
        Florest f1 = new Florest(p1);
        Mine m1 = new Mine(p1);

        m1.generateResource();
        m1.upgradeStructure();

        f1.consumeResources();
        f1.upgradeStructure();

        City c1 = new City(p1);
        Ranch r1 = new Ranch(p1);

        c1.generateResource();
        r1.generateResource();
        r1.generateResource();
        r1.generateResource();
        r1.generateResource();
        r1.generateResource();

        for (int i = 0; i < 100; i ++) {
            r1.generateResource();
        }

        c1.consumeResources();

        System.out.println(p1);
        System.out.println(c1);
        System.out.println(f1);
        System.out.println(m1);
        System.out.println(r1);




    }
}
