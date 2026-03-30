public class City extends Structures{

    private Player owner;
    private static final int apNeeded = 3;

    public City(Player owner) {
        super("Cidade","ManPower","FOOD", 20, 10, owner, "actionPoints");
    }

    public static int getApNeeded() {
        return apNeeded;
    }
}
