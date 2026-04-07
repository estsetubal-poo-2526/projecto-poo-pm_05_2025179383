public class City extends Structures {
    public City(Player owner) {
        super("City", ResourceType.FOOD, ResourceType.STONE, 20, 0, owner, ResourceType.STONE);
    }

    public static int getApNeeded() {
        return 3;
    }

    @Override
    public void upgradeStructure() {
        super.upgradeStructure();
        owner.addActionPoints(2 * level); // Bónus específico da cidade
    }
}