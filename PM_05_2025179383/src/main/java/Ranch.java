public class Ranch extends Structures {
    public Ranch(Player owner) {
        super("Ranch", ResourceType.FOOD, ResourceType.WOOD, 3, 5, owner, ResourceType.STONE, 7);
    }
    public static int getApNeeded() { return 5; }
}