public class City extends Structures {
    public City(Player owner) {
        super("City", ResourceType.NONE, ResourceType.FOOD, 10, 0, owner, ResourceType.STONE, 15);
        owner.addActionPoints(3);
    }

    public static int getApNeeded() {
        return 3;
    }

    @Override
    public boolean upgradeStructure() {
        if (super.upgradeStructure()) {
            owner.addActionPoints(2 * level); // Bónus específico da cidade
            return true;
        }
        return false;
    }
}