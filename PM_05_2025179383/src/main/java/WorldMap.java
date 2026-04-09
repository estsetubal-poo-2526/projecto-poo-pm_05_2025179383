public class WorldMap {

    private final int COLUMN_SIZE = 7;
    private final int LINE_SIZE = 7;
    private Structures[][] map;

    public WorldMap(){
        this.map = new Structures[COLUMN_SIZE][LINE_SIZE];
    }

    /**
     * Imprime o Mapa com tudo, se houver alguma estrutura imprime a estrutura + o nivel
     * caso contrario, imprime null
     */
    public void printMap(){
        for (int i = 0; i < COLUMN_SIZE; i++){
            for (int j = 0; j < LINE_SIZE; j++){
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     *
     * Metodo para adicionar uma estrutura ao mapa
     *
     * @param structure estrutura a ser construida no mapa
     * @param coordinateX coordenada x da estrutura
     * @param coordinateY coordenada y da estrutura
     * @return true se foi possivel construir, false caso contrario
     */

    public boolean addStructure(Structures structure, int coordinateX, int coordinateY) {
       if (isNotOccupied(coordinateX,coordinateY)) {
           map[coordinateX][coordinateY] = structure;
           return true;
       } else {
           System.out.println("Espaço Já ocupado");
           return false;
       }

    }

    /**
     * Metodo para devolver o dono da estrutura
     *
     * @param coordinateX cordenada x
     * @param coordinateY cordenada y
     * @return devolve o player, se não existir dono, devolve null
     */
    public Player getOwner(int coordinateX, int coordinateY) {
        if (!isNotOccupied(coordinateX,coordinateY)) {
            return map[coordinateX][coordinateY].getOwner();
        } else{
            return null;
        }


    }

    /**
     * Limpa o mapa
     */

    public void clearMap() {
        for (int i = 0; i < COLUMN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                map[i][j] = null;
            }
        }

    }

    /**
     *
     * Metodo para obter a estrutura em uma cordenada
     *
     * @param coordinateX cordenadas x
     * @param coordinateY cordenadas y
     * @return a estrutura nas cordenadas
     */
    public Structures getStructure(int coordinateX, int coordinateY) {
        return map[coordinateX][coordinateY];
    }

    /**
     *
     * Metodo para saber se a posicao esta ocupada
     *
     * @param coordinateX cordenada x
     * @param coordinateY cordenada y
     * @return true se estiver ocupada, false se não estiver
     */

    public boolean isNotOccupied(int coordinateX, int coordinateY) {
        return map[coordinateX][coordinateY] == null;
    }



    public void generateResources(){
        for (int i = 0; i < COLUMN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                 if (map[i][j] != null){
                     map[i][j].generateResource();
                 }
            }
        }
    }

    public void consumeResources(){
        for (int i = 0; i < COLUMN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                if (map[i][j] != null){
                    if (!map[i][j].consumeResources()) {
                        map[i][j] = null;
                    };
                }
            }
        }
    }

    /**
     *
     * Verifica se o player pode interagir com a estrutura
     *
     * @param coordinateX cordenada x
     * @param coordinateY cordenada y
     * @param player player atual
     * @return true se o player for o dono da estrutura, false caso contrario
     */

    public boolean canInteract(int coordinateX, int coordinateY, Player player) {
        if (coordinateX <= COLUMN_SIZE -1  && coordinateX >= 0 && coordinateY <= LINE_SIZE-1 && coordinateY >= 0) {
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
