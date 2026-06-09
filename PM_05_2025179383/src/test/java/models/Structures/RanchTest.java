package models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.Ranch;
import jogo.models.Structures.StructuresType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Ranch class.
 *
 * This class tests the creation, upgrade and resource generation
 * behavior of a Ranch structure.
 *
 * The tests verify if the ranch is created with the correct initial values,
 * if upgrades work correctly, if the correct exception is thrown when
 * resources are missing, if setting the level recalculates the expense,
 * and if the ranch generates food for its owner.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class RanchTest {

    /**
     * Ranch object used in the tests.
     */
    private Ranch ranch;

    /**
     * Player object used as the owner of the ranch.
     */
    private Player player;

    /**
     * Creates a new player and a new Ranch before each test.
     *
     * This ensures that every test starts with a clean player
     * and a fresh Ranch structure.
     */
    @BeforeEach
    void setUp() {
        player = new Player("PlayerTest");
        ranch = new Ranch(player, 1);
    }

    /**
     * Tests if a Ranch is created with the correct initial values.
     *
     * The test verifies the structure type, level, owner, production type,
     * cost type, expense, profit and upgrade material.
     */
    @Test
    void testCreateValidRanch() {
        assertEquals(StructuresType.RANCH, ranch.getStructureType());
        assertEquals(1, ranch.getLevel(), "O Nível deveria começar em 1.");
        assertEquals(player, ranch.getOwner(), "O dono não está a ser atribuído corretamente.");
        assertEquals(Ranch.getPRODUCTION_TYPE(), ranch.getProduction(), "O tipo de produção está errado.");
        assertEquals(Ranch.getCOST_TYPE(), ranch.getCostType(), "O tipo de custo está errado.");
        assertEquals(Ranch.getEXPENSE(), ranch.getExpense(), "A Expense inicial está errada.");
        assertEquals(Ranch.getPROFIT(), ranch.getProfit(), "O Profit inicial está errado.");
        assertEquals(Ranch.getMATERIAL_TO_UPGRADE(), ranch.getUpgradeMaterial(), "O Material para melhorar está errado.");
    }

    /**
     * Tests if a Ranch can be upgraded successfully.
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
        player.addResource(Ranch.getMATERIAL_TO_UPGRADE(), 100);
        player.addResource(ResourceType.ACTION_POINTS, 100);

        int scoreBefore = player.getScore();

        boolean result = ranch.upgradeStructure(1);

        assertTrue(result);
        assertEquals(2, ranch.getLevel(), "O nível devia ter subido para 2.");
        assertEquals(Ranch.getEXPENSE() + Ranch.getExpenseByLevel(), ranch.getExpense(), "A despesa devia ter aumentado.");

        int expectedScoreBonus = ranch.getLevel() * Ranch.getScoreByLevelMultiplier();
        assertEquals(scoreBefore + expectedScoreBonus, player.getScore(), "O score foi mal calculado no upgrade.");
    }

    /**
     * Tests if upgrading a Ranch without enough upgrade material throws
     * InsufficientResourcesException.
     *
     * The player receives Action Points but does not receive the required
     * stone needed to upgrade the Ranch.
     */
    @Test
    void testUpgradeStructureThrowsInsufficientResourcesException() {
        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS, 100);

        assertThrows(InsufficientResourcesException.class, () -> {
            ranch.upgradeStructure(1);
        }, "Devia falhar por falta de STONE para o upgrade.");
    }

    /**
     * Tests if setting the Ranch level recalculates the expense correctly.
     *
     * The Ranch level is manually set to 3.
     * The test verifies if the level was updated and if the expense
     * matches the expected value for that level.
     */
    @Test
    void testSetLevelCalculatesExpenseCorrectly() {
        int expenseBefore = ranch.getExpense();

        ranch.setLevel(3);

        assertEquals(3, ranch.getLevel());
        assertEquals(expenseBefore + Ranch.getExpenseByLevel() * (ranch.getLevel() - 1), ranch.getExpense(), "A despesa para o nível 3 foi mal calculada.");
    }

    /**
     * Tests if the Ranch generates food correctly.
     *
     * The player's inventory is cleared before generating resources.
     * After calling generateResource, the test verifies if the player
     * received food equal to the Ranch's current profit.
     */
    @Test
    void testGenerateFood() {
        player.clearInventory();
        int initialProfit = ranch.getProfit();

        ranch.generateResource(1);

        assertEquals(initialProfit, player.getResourceQuantity(ResourceType.FOOD),
                "O jogador devia ter recebido Food baseado no profit do rancho.");
    }
}