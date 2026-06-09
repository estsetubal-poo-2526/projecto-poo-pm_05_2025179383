package jogo.exceptions;

/**
 * Thrown when the requested grid coordinates fall outside the valid
 * boundaries of the world map.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */

public class CoordinatesOutOfBoundsException extends GameException {

    /**
     * Constructs a new CoordinatesOutOfBoundsException detailing the invalid coordinates.
     *
     * @param x The invalid X-coordinate (column index) that triggered the exception.
     * @param y The invalid Y-coordinate (line index) that triggered the exception.
     */
    public CoordinatesOutOfBoundsException(int x, int y) {
        super("Coordenadas fora do Mapa!" + "[" + x +"," + y + "]");
    }
}
