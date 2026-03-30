public class Mine extends Structures{

    private Player owner;
    private final int actionPointsNeeded = 5;

    public Mine(Player owner) {
        super("Mina","STONE","WOOD", 8, 4, owner, "WOOD");
    }


    public int getActionPointsNeeded() {
        return actionPointsNeeded;
    }

}
