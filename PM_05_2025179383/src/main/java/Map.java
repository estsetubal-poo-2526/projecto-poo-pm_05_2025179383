public class Map {

    private final Player p1;
    private final Player p2;
    private final int COLLUN_SIZE = 7;
    private final int LINE_SIZE = 7;
    private Structures[][] map;

    public Map( Player p1, Player p2){
        this.p2 = p2;
        this.p1 = p1;
        this.map = new Structures[COLLUN_SIZE][LINE_SIZE];
    }

    public void printMap(){
        for (int i = 0; i < COLLUN_SIZE; i++){
            for (int j = 0; j < LINE_SIZE; j++){
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void addStructure(Structures c, int s, int d) {
        map[s][d] = c;
    }

    public Player getOwner(int s, int d) {
        return map[s][d].getOwner();
    }

    public void clearMap() {
        for (int i = 0; i < COLLUN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                map[i][j] = null;
            }
        }

    }

    public boolean isOccupied(int s, int d) {
        return map[s][d] != null;
    }
}
