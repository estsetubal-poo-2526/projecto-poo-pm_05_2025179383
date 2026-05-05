package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;

public class Mine extends Structures {

    private static final int EXPENSE = 3;
    private static final int PROFIT = 7;
    private static final int SCOREVALUE = 4;

    public Mine(Player owner, int scoreModifier) {
        super("Mine", ResourceType.STONE, ResourceType.WOOD, EXPENSE, PROFIT, owner, ResourceType.WOOD, SCOREVALUE, scoreModifier);
    }
    public static int getApNeeded() { return 5; }

    public static int getExpense() {
        return EXPENSE;
    }

    @Override
    public void generateResource(int resourcesModifier) {
        if (production != ResourceType.NONE) {
            owner.addResource(production, (3 + level * 2) * resourcesModifier);
        }
    }

}