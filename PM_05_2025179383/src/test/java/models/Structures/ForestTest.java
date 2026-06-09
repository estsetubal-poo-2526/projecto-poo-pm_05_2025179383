package models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Forest class.
 *
 * This class tests the creation, upgrade and resource generation
 * behavior of a Forest structure.
 *
 * The tests verify if the forest is created with the correct initial values,
 * if upgrades work correctly, if the correct exception is thrown when
 * resources are missing, if setting the level recalculates values correctly,
 * and if the forest generates wood for its owner.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class ForestTest {

    /**
     * Forest object used in the tests.
     */
    private Forest forest;

    /**
     * Player object used as the owner of the forest.
     */
    private Player player;

    /**
     * Creates a new player and a new Forest before each test.
     *
     * This ensures that every test starts with a clean player
     * and a fresh Forest structure.
     */
    @BeforeEach
    void setUp() {
        player = new Player("PlayerTest");
        forest = new Forest(player, 1);
    }

    /**
     * Tests if a Forest is created with the correct initial values.
     *
     * The test verifies the structure type, level, owner, production type,
     * cost type, expense, profit and upgrade material.
     */
    @Test
    void testCreateValidCity() {
        assertEquals(Forest.getSTRUCTURE_TYPE(), forest.getStructureType());
        assertEquals(1, forest.getLevel(), "O Nivel deveria começar em 1");
        assertEquals(player, forest.getOwner(), "O dono não está a ser atribuído corretamente");
        assertEquals(Forest.getPRODUCTION_TYPE(), forest.getProduction(), "O tipo de produção está errado");
        assertEquals(Forest.getCOST_TYPE(), forest.getCostType(), "O tipo de custo está errado");
        assertEquals(Forest.getEXPENSE(), forest.getExpense(), "A Expense está errada");
        assertEquals(Forest.getPROFIT(), forest.getProfit(), "O Profit está errado");
        assertEquals(Forest.getMATERIAL_TO_UPGRADE(), forest.getUpgradeMaterial(), "O Material para melhorar está errado");
    }

    /**
     * Tests if a Forest can be upgraded successfully.
     *
     * The player receives enough upgrade material and Action Points.
     * After the upgrade, the test verifies if the method returns true,
     * if the level increases, if the expense increases,
     * and if the player's score increases according to the upgrade formula.
     *
     * @throws InsufficientResourcesException if the player does not have enough resources
     */
    @Test
    void testUpgradeStructureWithSuccess() throws InsufficientResourcesException {
        player.clearInventory();
        player.addResource(Forest.getMATERIAL_TO_UPGRADE(), 100);
        player.addResource(ResourceType.ACTION_POINTS, 100);

        int scoreBefore = player.getScore();

        boolean result = forest.upgradeStructure(1);

        assertTrue(result);
        assertEquals(2, forest.getLevel(), "O nível devia ter subido para 2.");
        assertEquals(Forest.getEXPENSE() + Forest.getExpenseByLevel(), forest.getExpense(), "A despesa devia ter aumentado.");

        int expectedScoreBonus = forest.getLevel() * Forest.getScoreByLevelMultiplier();
        assertEquals(scoreBefore + expectedScoreBonus, player.getScore(), "O score devia aumentar corretamente baseado na fórmula de upgrade.");
    }

    /**
     * Tests if upgrading a Forest without enough upgrade material throws
     * InsufficientResourcesException.
     *
     * The player receives Action Points but does not receive the required
     * wood needed to upgrade the Forest.
     */
    @Test
    void testUpgradeStructureThrowsInsufficientResourcesException() {
        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS, 100);

        assertThrows(InsufficientResourcesException.class, () -> {
            forest.upgradeStructure(1);
        }, "Devia falhar por falta de WOOD para o upgrade.");
    }

    /**
     * Tests if setting the Forest level recalculates the expense correctly.
     *
     * The Forest level is manually set to 3.
     * The test verifies if the level was updated and if the expense
     * matches the expected value for that level.
     */
    @Test
    void testSetLevelCalculatesExpenseCorrectly() {
        int expenseBefore = forest.getExpense();

        forest.setLevel(3);

        assertEquals(3, forest.getLevel());
        assertEquals(expenseBefore + Forest.getExpenseByLevel() * (forest.getLevel() - 1), forest.getExpense(), "A despesa para o nível 3 foi mal calculada.");
    }

    /**
     * Tests if the Forest generates wood correctly.
     *
     * The player's inventory is cleared before generating resources.
     * After calling generateResource, the test verifies if the player
     * received the amount of wood equal to the Forest's current profit.
     */
    @Test
    void testGenerateWood() {
        player.clearInventory();
        int initialProfit = forest.getProfit();

        forest.generateResource(1);

        assertEquals(initialProfit, player.getResourceQuantity(ResourceType.WOOD),
                "O jogador devia ter recebido Wood baseado no profit da floresta.");
    }
}