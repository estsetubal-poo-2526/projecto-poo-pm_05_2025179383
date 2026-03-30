public class City extends Structures{

    private Player owner;
    private int apNeeded = 3;

    public City(Player owner) {
        super("Cidade","ManPower","FOOD", 20, 10, owner, "actionPoints");
    }

    public int getApNeeded() {
        return apNeeded;
    }
}
