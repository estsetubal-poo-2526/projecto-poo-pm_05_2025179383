package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;

public class Forest extends Structures {

    private static final int expense = 3;
    private static final int profit = 6;
    private static final int scoreValue = 5;


    public Forest(Player owner, int scoreModifier) {
        super("Forest", ResourceType.WOOD, ResourceType.STONE, expense, profit, owner, ResourceType.STONE, scoreValue,scoreModifier);
    }
    public static int getApNeeded() { return 2; }

    public static int getExpense() {
        return expense;
    }

    @Override
    public void generateResource(int resourcesModifier) {
        if (production != ResourceType.NONE) {
            owner.addResource(production, (5 + level * 2) * resourcesModifier);
        }
    }
}
