public class Forest extends Structures {
    public Forest(Player owner) {
        super("Forest", ResourceType.WOOD, ResourceType.STONE, 3, 6, owner, ResourceType.STONE, 5);
    }
    public static int getApNeeded() { return 2; }

}
