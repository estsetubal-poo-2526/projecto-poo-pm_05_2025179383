public class Florest extends Structures{

    private Player owner;
    private static final int actionPointsNeeded = 2;

    public Florest(Player owner) {
        super("Floresta","WOOD","STONE", 5, 3, owner, "STONE");
    }

    public static int getActionPointsNeeded() {
        return actionPointsNeeded;
    }
}
