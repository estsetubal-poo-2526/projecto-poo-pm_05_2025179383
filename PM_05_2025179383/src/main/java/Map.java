public class Map {

    private final Player p1;
    private final Player p2;
    private final int COLLUMN_SIZE = 7;
    private final int LINE_SIZE = 7;
    private Structures[][] map;

    public Map( Player p1, Player p2){
        this.p2 = p2;
        this.p1 = p1;
        this.map = new Structures[COLLUMN_SIZE][LINE_SIZE];
    }

    public void printMap(){
        for (int i = 0; i < COLLUMN_SIZE; i++){
            for (int j = 0; j < LINE_SIZE; j++){
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void addStructure(Structures c, int s, int d) {
       if (isNotOccupied(s,d)) {
           map[s][d] = c;
       } else {
           System.out.println("Espaço Já ocupado");
       }

    }

    public Player getOwner(int s, int d) {
        if (!isNotOccupied(s,d)) {
            return map[s][d].getOwner();
        } else{
            return null;
        }


    }

    public void clearMap() {
        for (int i = 0; i < COLLUMN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                map[i][j] = null;
            }
        }

    }

    public Structures getStructure(int s, int d) {
        return map[s][d];
    }

    public boolean isNotOccupied(int s, int d) {
        return map[s][d] == null;
    }

    public void generateResources(){
        for (int i = 0; i < COLLUMN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                 if (map[i][j] != null){
                     map[i][j].generateResource();
                 }
            }
        }
    }

    public void consumeResources(){
        for (int i = 0; i < COLLUMN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                if (map[i][j] != null){
                    map[i][j].consumeResources();
                }
            }
        }
    }

    public boolean canInteract(int x, int y, Player player) {
        if (x <= COLLUMN_SIZE-1  && x >= 0 && y <= LINE_SIZE-1 && y >= 0) {
            if (!isNotOccupied(x,y)) {
                return map[x][y].getOwner().getName().equals(player.getName());
            }
        }
        return false;
    }
}
