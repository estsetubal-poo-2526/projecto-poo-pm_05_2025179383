package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;

public class Mine extends Structures {
    public Mine(Player owner) {
        super("jogo.models.Structures.Mine", ResourceType.STONE, ResourceType.WOOD, 3, 7, owner, ResourceType.WOOD, 4);
    }
    public static int getApNeeded() { return 5; }
}