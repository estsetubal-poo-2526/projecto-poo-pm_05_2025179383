
package jogo.engine;
import jogo.models.*;
import jogo.models.Structures.*;
import jogo.exceptions.*;

/**
 * Responsible for creating, maintaining, and managing the core game world grid.
 * Holds a fixed-size matrix representing the map tiles where structures can be built.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class WorldMap {

    private final int COLUMN_SIZE = 7;
    private final int LINE_SIZE = 7;
    private final Structures[][] map;

    /**
     * Constructs a new empty WorldMap with a fixed 7x7 grid dimension.
     * All map tiles are initially unallocated (null).
     */
    public WorldMap(){
        this.map = new Structures[COLUMN_SIZE][LINE_SIZE];
    }

    public int getCOLUMN_SIZE() {
        return COLUMN_SIZE;
    }

    public int getLINE_SIZE() {
        return LINE_SIZE;
    }

    /**
     * Places a structure onto the map grid at the specified coordinates.
     * Ensures the coordinates are within bounds and the destination tile is free before insertion.
     *
     * @param structure The Structures instance to be added to the map.
     * @param coordinateX The X-coordinate (column) on the map grid.
     * @param coordinateY The Y-coordinate (line) on the map grid.
     * @throws SpaceAlreadyOccupiedException If there is already a structure at the target location.
     * @throws GameException If the coordinates are outside the map boundaries.
     */
    public void addStructure(Structures structure, int coordinateX, int coordinateY) throws GameException {
        validateCoordinates(coordinateX,coordinateY);

        if (!isNotOccupied(coordinateX, coordinateY)) {
            throw new SpaceAlreadyOccupiedException();
        }

        map[coordinateX][coordinateY] = structure;
    }

    /**
     * Retrieves the structure instance located at the specified map coordinates.
     *
     * @param coordinateX The X-coordinate (column) on the map grid.
     * @param coordinateY The Y-coordinate (line) on the map grid.
     * @return The Structures object at the given position, or null if the tile is empty.
     * @throws GameException If the coordinates are outside the map boundaries.
     */
    public Structures getStructure(int coordinateX, int coordinateY) throws GameException {

        validateCoordinates(coordinateX,coordinateY);

        return map[coordinateX][coordinateY];
    }

    /**
     * Checks whether a specific map coordinate is empty and available for construction.
     *
     * @param coordinateX The X-coordinate (column) on the map grid.
     * @param coordinateY The Y-coordinate (line) on the map grid.
     * @return true if the map tile is unallocated (null); false if it is occupied by a structure.
     * @throws GameException If the specified coordinates are outside the map boundaries.
     */
    public boolean isNotOccupied(int coordinateX, int coordinateY) throws GameException {

        validateCoordinates(coordinateX,coordinateY);

        return map[coordinateX][coordinateY] == null;
    }

    /**
     * Iterates through the entire map grid to trigger resource generation for all existing structures.
     * Each active structure updates its owner's inventory based on its production rates and the active modifier.
     *
     * @param resourceModifier The current resource multiplier applied to the base production of each structure.
     */
    public void generateResources(int resourceModifier){
        for (int i = 0; i < COLUMN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                 if (map[i][j] != null){
                     map[i][j].generateResource(resourceModifier);
                 }
            }
        }
    }

    /**
     * Iterates through the entire map grid to trigger daily resource consumption for all structures.
     * If a structure's level drops to or below zero due to starvation or maintenance failure,
     * it is decommissioned and removed from the world map.
     */
    public void consumeResources() {
        for (int i = 0; i < COLUMN_SIZE; i++) {
            for (int j = 0; j < LINE_SIZE; j++) {
                if (map[i][j] != null) {
                    map[i][j].consumeResources();
                    if (map[i][j].getLevel() <= 0) {
                        map[i][j] = null;
                    }
                }
            }
        }
    }

    /**
     * Validates whether a player is authorized to interact with a specific map coordinate.
     * Checks if the coordinates are within bounds, verifies that a structure exists at the
     * location, and ensures the requesting player is the rightful owner.
     *
     * @param coordinateX The X-coordinate (column) on the map grid.
     * @param coordinateY The Y-coordinate (line) on the map grid.
     * @param player The Player attempting to perform the interaction.
     * @throws StructureDontExistException If the specified coordinates point to an empty tile.
     * @throws UnauthorizedActionException If the structure at the coordinates belongs to the opponent.
     * @throws GameException If the coordinates are out of bounds or any other map validation fails.
     */

    public void canInteract(int coordinateX, int coordinateY, Player player) throws GameException {


        validateCoordinates(coordinateX,coordinateY);
        if (isNotOccupied(coordinateX, coordinateY)) {
            throw new StructureDontExistException();
        }
        if (!map[coordinateX][coordinateY].getOwner().equals(player)) {
            throw new UnauthorizedActionException();
        }
    }

    /**
     * Validates whether the provided grid coordinates fall within the boundaries of the map.
     *
     * @param coordinateX The X-coordinate (column index) to validate.
     * @param coordinateY The Y-coordinate (line index) to validate.
     * @throws CoordinatesOutOfBoundsException If either coordinate is negative or exceeds the map dimensions.
     */

    public void validateCoordinates(int coordinateX, int coordinateY) throws CoordinatesOutOfBoundsException {
        if (coordinateX < 0 || coordinateX >= COLUMN_SIZE || coordinateY < 0 || coordinateY >= LINE_SIZE) {
            throw new CoordinatesOutOfBoundsException(coordinateX, coordinateY);
        }
    }
}
