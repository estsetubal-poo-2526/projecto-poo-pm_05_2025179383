public class Mine extends Structures{

    private Player owner;
    private static final int actionPointsNeeded = 5;

    public Mine(Player owner) {
        super("Mina","STONE","WOOD", 8, 4, owner, "WOOD");
    }


    public static int getActionPointsNeeded() {
        return actionPointsNeeded;
    }

}
