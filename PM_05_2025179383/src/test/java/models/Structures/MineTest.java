package models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.Mine;
import jogo.models.Structures.StructuresType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Mine class.
 *
 * This class tests the creation, upgrade and resource generation
 * behavior of a Mine structure.
 *
 * The tests verify if the mine is created with the correct initial values,
 * if upgrades work correctly, if the correct exception is thrown when
 * resources are missing, and if the mine generates stone for its owner.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class MineTest {

    /**
     * Mine object used in the tests.
     */
    private Mine mine;

    /**
     * Player object used as the owner of the mine.
     */
    private Player player;

    /**
     * Creates a new player and a new Mine before each test.
     *
     * This ensures that every test starts with a clean player
     * and a fresh Mine structure.
     */
    @BeforeEach
    void setUp() {
        player = new Player("PlayerTest");
        mine = new Mine(player, 1);
    }

    /**
     * Tests if a Mine is created with the correct initial values.
     *
     * The test verifies the structure type, level, owner, production type,
     * cost type, expense and profit.
     */
    @Test
    void testCreateValidMine() {
        assertEquals(StructuresType.MINE, mine.getStructureType());
        assertEquals(1, mine.getLevel());
        assertEquals(player, mine.getOwner());
        assertEquals(ResourceType.STONE, Mine.getPRODUCTION_TYPE());
        assertEquals(ResourceType.WOOD, Mine.getCOST_TYPE());
        assertEquals(Mine.getEXPENSE(), mine.getExpense());
        assertEquals(Mine.getPROFIT(), mine.getProfit());
    }

    /**
     * Tests if a Mine can be upgraded successfully.
     *
     * The player receives enough upgrade material and Action Points.
     * After the upgrade, the test verifies if the method returns true,
     * if the level increases, if the player's score increases correctly,
     * and if the mine's profit increases.
     *
     * @throws InsufficientResourcesException if the player does not have enough resources
     */
    @Test
    void testUpgradeMineWithSuccess() throws InsufficientResourcesException {
        player.clearInventory();

        player.addResource(Mine.getMATERIAL_TO_UPGRADE(), 100);
        player.addResource(ResourceType.ACTION_POINTS, 100);

        int scoreBefore = player.getScore();
        boolean result = mine.upgradeStructure(1);

        assertTrue(result);
        assertEquals(2, mine.getLevel(), "O nível devia ser 2.");

        assertEquals(scoreBefore + mine.getLevel() * Mine.getScoreByLevelMultiplier(), player.getScore(),
                "O score devia ter aumentado corretamente.");

        int expectedProfit = Mine.getPROFIT() + Mine.getProfitByLevel();
        assertEquals(expectedProfit, mine.getProfit(), "O lucro devia ter aumentado após o upgrade.");
    }

    /**
     * Tests if upgrading a Mine without enough stone throws
     * InsufficientResourcesException.
     *
     * The player receives wood, but the Mine needs stone as upgrade material.
     */
    @Test
    void testUpgradeMineInsufficientStone() {
        player.clearInventory();
        player.addResource(ResourceType.WOOD, 100);

        assertThrows(InsufficientResourcesException.class, () -> {
            mine.upgradeStructure(1);
        }, "Devia falhar por falta de STONE.");
    }

    /**
     * Tests if the Mine generates stone correctly.
     *
     * The player's inventory is cleared before generating resources.
     * After calling generateResource, the test verifies if the player
     * received stone equal to the Mine's current profit.
     */
    @Test
    void testGenerateStone() {
        player.clearInventory();
        int initialProfit = mine.getProfit();

        mine.generateResource(1);

        assertEquals(initialProfit, player.getResourceQuantity(ResourceType.STONE),
                "O jogador devia ter recebido Stone baseado no profit.");
    }
}