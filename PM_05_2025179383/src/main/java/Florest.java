public class Florest extends Structures{

    private static final int actionPointsNeeded = 2;

    public Florest(Player owner) {
        super("Floresta","WOOD","STONE", 5, 3, owner, "STONE");
    }

    public static int getApNeeded() {
        return actionPointsNeeded;
    }

    @Override
    public String getMaterialToUpgrade() {
        return super.getMaterialToUpgrade();
    }

    @Override
    public String getCost() {
        return super.getCost();
    }
}
