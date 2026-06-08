package jogo.models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;

public class Ranch extends Structures {

    private static final int BASE_EXPENSE = 3;
    private static final int BASE_PROFIT = 6;
    private static final int BASE_SCORE_VALUE = 2;
    private static final int BASE_AP_NEEDED = 3;
    private static final int INITIAL_LEVEL = 1;

    private static final int EXPENSE_BY_LEVEL = 2;
    private static final int PROFIT_BY_LEVEL = 5;
    private static final int UPGRADE_COST_MULTIPLIER = 5;
    private static final int SCORE_BY_LEVEL_MULTIPLIER = 5;

    private static final StructuresType STRUCTURE_TYPE = StructuresType.RANCH;
    private static final ResourceType PRODUCTION = ResourceType.FOOD;
    private static final ResourceType COST_MATERIAL = ResourceType.WOOD;
    private static final ResourceType MATERIAL_TO_UPGRADE = ResourceType.STONE;

    public Ranch(Player owner, int scoreModifier) {
        super(
                STRUCTURE_TYPE,
                PRODUCTION,
                COST_MATERIAL,
                BASE_EXPENSE,
                BASE_PROFIT,
                owner,
                MATERIAL_TO_UPGRADE,
                INITIAL_LEVEL,
                BASE_SCORE_VALUE,
                scoreModifier
        );
    }

    @Override
    public boolean upgradeStructure(int scoreModifier) throws InsufficientResourcesException {
        int upgradeCost = getUpgradeCost();

        if (!owner.removeResource(UPGRADE_MATERIAL, upgradeCost)) {
            throw new InsufficientResourcesException();
        }

        level++;
        expense += EXPENSE_BY_LEVEL;
        profit += PROFIT_BY_LEVEL;

        owner.addScore(level * SCORE_BY_LEVEL_MULTIPLIER * scoreModifier);

        return true;
    }

    @Override
    public void generateResource(int resourcesModifier) {
        if (PRODUCTION != ResourceType.NONE) {
            owner.addResource(PRODUCTION, (profit) * resourcesModifier);
        }
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
        this.expense = BASE_EXPENSE + EXPENSE_BY_LEVEL * (level - 1);
        this.profit = BASE_PROFIT + PROFIT_BY_LEVEL * (level - 1);
    }

    @Override
    public int getFutureExpense() {
        return expense + EXPENSE_BY_LEVEL;
    }

    @Override
    public int getFutureProfit() {
        return profit + PROFIT_BY_LEVEL;
    }

    @Override
    public int getUpgradeCost() {
        return level * UPGRADE_COST_MULTIPLIER;
    }

    public static int getApNeeded() {
        return BASE_AP_NEEDED;
    }

    public static int getEXPENSE() {
        return BASE_EXPENSE;
    }

    public static int getPROFIT() {
        return BASE_PROFIT;
    }

    public static int getSCOREVALUE() {
        return BASE_SCORE_VALUE;
    }

    public static ResourceType getPRODUCTION_TYPE() {
        return PRODUCTION;
    }

    public static ResourceType getCOST_TYPE() {
        return COST_MATERIAL;
    }

    public static ResourceType getMATERIAL_TO_UPGRADE() {
        return MATERIAL_TO_UPGRADE;
    }

    @Override
    public int getAPCostToUpgrade() {
        return BASE_AP_NEEDED * (level + 1);
    }

    public static int getScoreByLevelMultiplier() {
        return SCORE_BY_LEVEL_MULTIPLIER;
    }

    public static int getExpenseByLevel() {
        return EXPENSE_BY_LEVEL;
    }
}