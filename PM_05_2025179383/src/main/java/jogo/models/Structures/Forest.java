package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;

public class Forest extends Structures {
    public Forest(Player owner) {
        super("jogo.models.Structures.Forest", ResourceType.WOOD, ResourceType.STONE, 3, 6, owner, ResourceType.STONE, 5);
    }
    public static int getApNeeded() { return 2; }

}
