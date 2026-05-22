package jogo.models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;

public class City extends Structures {


    private static final int BASE_EXPENSE = 10;
    private static final int BASE_PROFIT = 0;
    private static final int BASE_SCORE_VALUE = 15;
    private static final int BASE_AP_NEEDED = 5;
    private static final int INITIAL_LEVEL = 1;

    private static final int EXPENSE_BY_LEVEL = 6;
    private static final int AP_BY_LEVEL = 3;
    private static final int UPGRADE_COST_MULTIPLIER = 5;
    private static final int SCORE_BY_LEVEL_MULTIPLIER = 5;

    private static final StructuresType STRUCTURE_TYPE = StructuresType.CITY;
    private static final ResourceType PRODUCTION = ResourceType.NONE;
    private static final ResourceType COST_MATERIAL = ResourceType.FOOD;
    private static final ResourceType MATERIAL_TO_UPGRADE = ResourceType.STONE;

    public City(Player owner, int scoreModifier) {
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

        owner.addActionPoints(AP_BY_LEVEL);
    }

    @Override
    public boolean upgradeStructure(int scoreModifier) throws InsufficientResourcesException {
        int upgradeCost = level * UPGRADE_COST_MULTIPLIER;

        if (!owner.removeResource(UPGRADE_MATERIAL, upgradeCost)) {
            throw new InsufficientResourcesException();
        }

        level++;
        expense += EXPENSE_BY_LEVEL;

        owner.addScore(level * SCORE_BY_LEVEL_MULTIPLIER * scoreModifier);
        owner.addActionPoints(AP_BY_LEVEL * 2);

        return true;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
        expense = BASE_EXPENSE + EXPENSE_BY_LEVEL * (level - 1);
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
}