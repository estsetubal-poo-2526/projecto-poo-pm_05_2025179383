public class Ranch extends Structures{

    private Player owner;
    private final int actionPointsNeeded = 3;

    public Ranch(Player owner) {
        super("Rancho","FOOD","WOOD", 8, 4, owner, "STONE");
    }

    public int getActionPointsNeeded() {
        return actionPointsNeeded;
    }
}
