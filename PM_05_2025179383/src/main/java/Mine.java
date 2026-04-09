public class Mine extends Structures {
    public Mine(Player owner) {
        super("Mine", ResourceType.STONE, ResourceType.WOOD, 3, 7, owner, ResourceType.WOOD, 4);
    }
    public static int getApNeeded() { return 5; }
}