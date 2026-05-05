
package jogo.engine;
import jogo.models.*;
import jogo.models.Structures.*;
import jogo.exceptions.*;


public class WorldMap {

    private final int COLUMN_SIZE = 7;
    private final int LINE_SIZE = 7;
    private final Structures[][] map;

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

    public int getCOLUMN_SIZE() {
        return COLUMN_SIZE;
    }

    public int getLINE_SIZE() {
        return LINE_SIZE;
    }

    /**
     *
     * Metodo para adicionar uma estrutura ao mapa
     *
     * @param structure estrutura a ser construida no mapa
     * @param coordinateX coordenada x da estrutura
     * @param coordinateY coordenada y da estrutura
     */

    public void addStructure(Structures structure, int coordinateX, int coordinateY) throws GameException {
        // Valida coordenadas primeiro!
        if (coordinateX < 0 || coordinateX >= COLUMN_SIZE || coordinateY < 0 || coordinateY >= LINE_SIZE) {
            throw new CoordinatesOutOfBoundsException(coordinateX,coordinateY);
        }

        if (!isNotOccupied(coordinateX, coordinateY)) {
            throw new SpaceAlreadyOccupiedException();
        }

        map[coordinateX][coordinateY] = structure;
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
    public Structures getStructure(int coordinateX, int coordinateY) throws GameException {
        if (coordinateX < 0 || coordinateX >= COLUMN_SIZE || coordinateY < 0 || coordinateY >= LINE_SIZE) {
            throw new CoordinatesOutOfBoundsException(coordinateX, coordinateY);
        }
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



    public void generateResources(int resourceModifier){
        for (int i = 0; i < COLUMN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                 if (map[i][j] != null){
                     map[i][j].generateResource(resourceModifier);
                 }
            }
        }
    }

    public void consumeResources() {
        for (int i = 0; i < COLUMN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                if (map[i][j] != null) {
                    String status = map[i][j].consumeResources();
                    System.out.println("[Jogo]: " + status);
                    if (map[i][j].getLevel() <= 0) {
                        map[i][j] = null;
                    }
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
     * @param player      player atual
     * @return true se o player for o dono da estrutura, false caso contrario
     */

    public void canInteract(int coordinateX, int coordinateY, Player player) throws GameException {
        if (coordinateX < 0 || coordinateX >= getCOLUMN_SIZE() || coordinateY < 0 || coordinateY >= getLINE_SIZE()) {
            throw new CoordinatesOutOfBoundsException(coordinateX,coordinateY);
        }
        if (isNotOccupied(coordinateX, coordinateY)) {
            throw new StructureDontExistException();
        }
        if (!map[coordinateX][coordinateY].getOwner().equals(player)) {
            throw new UnauthorizedActionException();
        }
    }

    public String getStructureOwnerName(int coordinateX, int coordinateY) throws GameException {
        if (getStructure(coordinateX,coordinateY) == null) {
            return null;
        }

        return map[coordinateX][coordinateY].getOwner().getName();
    }
}
