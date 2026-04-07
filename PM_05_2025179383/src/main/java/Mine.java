public class Mine extends Structures {
    public Mine(Player owner) {
        super("Mine", ResourceType.STONE, ResourceType.WOOD, 8, 4, owner, ResourceType.WOOD);
    }
    public static int getApNeeded() { return 5; }
}