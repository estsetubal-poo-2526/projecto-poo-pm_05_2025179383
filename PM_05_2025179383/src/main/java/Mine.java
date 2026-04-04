public class Mine extends Structures{

    private static final int actionPointsNeeded = 5;

    public Mine(Player owner) {
        super("Mina","STONE","WOOD", 8, 4, owner, "WOOD");
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
