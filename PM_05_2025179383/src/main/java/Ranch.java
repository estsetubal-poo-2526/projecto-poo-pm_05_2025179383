public class Ranch extends Structures {
    public Ranch(Player owner) {
        super("Ranch", ResourceType.FOOD, ResourceType.WOOD, 8, 4, owner, ResourceType.STONE);
    }
    public static int getApNeeded() { return 5; }
}