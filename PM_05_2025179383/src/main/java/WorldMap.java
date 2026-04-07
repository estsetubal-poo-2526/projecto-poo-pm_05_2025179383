public class WorldMap {

    private final int COLLUMN_SIZE = 7;
    private final int LINE_SIZE = 7;
    private Structures[][] map;

    public WorldMap(){
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

    public boolean addStructure(Structures structure, int coordinateX, int coordinateY) {
       if (isNotOccupied(coordinateX,coordinateY)) {
           map[coordinateX][coordinateY] = structure;
           return true;
       } else {
           System.out.println("Espaço Já ocupado");
           return false;
       }

    }

    public Player getOwner(int coordinateX, int coordinateY) {
        if (!isNotOccupied(coordinateX,coordinateY)) {
            return map[coordinateX][coordinateY].getOwner();
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

    public Structures getStructure(int coordinateX, int coordinateY) {
        return map[coordinateX][coordinateY];
    }

    public boolean isNotOccupied(int coordinateX, int coordinateY) {
        return map[coordinateX][coordinateY] == null;
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

    public boolean canInteract(int coordinateX, int coordinateY, Player player) {
        if (coordinateX <= COLLUMN_SIZE-1  && coordinateX >= 0 && coordinateY <= LINE_SIZE-1 && coordinateY >= 0) {
            if (!isNotOccupied(coordinateX,coordinateY)) {
                return getStructureOwnerName(coordinateX,coordinateY).equals(player.getName());
            }
        }
        return false;
    }

    public String getStructureOwnerName(int coordinateX, int coordinateY) {
        if (getStructure(coordinateX,coordinateY) == null) {
            return null;
        }

        return map[coordinateX][coordinateY].getOwner().getName();
    }
}
