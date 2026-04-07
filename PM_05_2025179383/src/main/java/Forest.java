public class Forest extends Structures {
    public Forest(Player owner) {
        super("Forest", ResourceType.WOOD, ResourceType.STONE, 5, 3, owner, ResourceType.STONE);
    }
    public static int getApNeeded() { return 2; }
}
