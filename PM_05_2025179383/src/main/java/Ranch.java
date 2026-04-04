public class Ranch extends Structures{

    private static final int actionPointsNeeded = 3;

    public Ranch(Player owner) {
        super("Rancho","FOOD","WOOD", 8, 4, owner, "STONE");
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
