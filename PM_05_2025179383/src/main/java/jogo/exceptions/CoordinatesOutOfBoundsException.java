package jogo.exceptions;

public class CoordinatesOutOfBoundsException extends GameException {
    public CoordinatesOutOfBoundsException(int x, int y) {
        super("Coordenadas fora do Mapa!" + "[" + x +"," + y + "]");
    }
}
