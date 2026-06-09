package models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the City class.
 *
 * This class tests the creation and upgrade behavior of a City structure.
 * It verifies if the city is created with the correct values,
 * if upgrades work correctly, if resources are consumed properly,
 * and if the level update recalculates the expense value.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class CityTest {

    /**
     * City object used in the tests.
     */
    private City city;

    /**
     * Player object used as the owner of the city.
     */
    private Player player;

    /**
     * Creates a new player and a new City before each test.
     *
     * This ensures that every test starts with a clean player
     * and a fresh City structure.
     */
    @BeforeEach
    void setUp() {
        player = new Player("PlayerTest");
        city = new City(player, 1);
    }

    /**
     * Tests if a City is created with the correct initial values.
     *
     * The test verifies the structure type, level, owner, production type,
     * cost type, expense, profit and upgrade material.
     */
    @Test
    void testCreateValidCity() {
        assertEquals(City.getSTRUCTURE_TYPE(), city.getStructureType());
        assertEquals(1, city.getLevel(), "O Nivel deveria começar em 1");
        assertEquals(player, city.getOwner(), "O dono não está a ser atribuído corretamente");
        assertEquals(City.getPRODUCTION_TYPE(), city.getProduction(), "O tipo de produção está errado");
        assertEquals(City.getCOST_TYPE(), city.getCostType(), "O tipo de custo está errado");
        assertEquals(City.getEXPENSE(), city.getExpense(), "A Expense está errada");
        assertEquals(City.getPROFIT(), city.getProfit(), "O Profit está errado");
        assertEquals(City.getMATERIAL_TO_UPGRADE(), city.getUpgradeMaterial(), "O Material para melhorar está errado");
    }

    /**
     * Tests if a City can be upgraded successfully.
     *
     * The player receives enough upgrade material and Action Points.
     * After the upgrade, the test verifies if the method returns true,
     * if the level increases, if the expense increases,
     * if the player's base Action Points increase,
     * and if the player's score is updated correctly.
     *
     * @throws InsufficientResourcesException if the player does not have enough resources
     */
    @Test
    void testUpgradeStructureWithSuccess() throws InsufficientResourcesException {
        player.clearInventory();
        player.addResource(City.getMATERIAL_TO_UPGRADE(), 100);
        player.addResource(ResourceType.ACTION_POINTS, 100);

        int baseApBefore = player.getBaseActionPoints();
        int scoreBefore = player.getScore();

        boolean result = city.upgradeStructure(1);

        assertTrue(result);
        assertEquals(2, city.getLevel(), "O nível devia ter subido para 2.");
        assertEquals(City.getEXPENSE() + City.getEXPENSE_BY_LEVEL(), city.getExpense(), "A despesa devia ter aumentado em 6.");
        assertEquals(baseApBefore + City.getAP_BY_LEVEL() * city.getLevel(), player.getBaseActionPoints(), "O jogador devia ganhar 3 AP máximos no upgrade.");
        assertEquals(scoreBefore + City.getScoreByLevelMultiplier() * city.getLevel(), player.getScore(), "O score devia aumentar em 10");
    }

    /**
     * Tests if upgrading a City without enough upgrade material throws
     * InsufficientResourcesException.
     *
     * The player receives Action Points but does not receive the required
     * stone needed to upgrade the City.
     */
    @Test
    void testUpgradeStructureThrowsInsufficientResourcesException() {
        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS, 100);

        assertThrows(InsufficientResourcesException.class, () -> {
            city.upgradeStructure(1);
        }, "Devia falhar por falta de Stone para o upgrade.");
    }

    /**
     * Tests if setting the City level recalculates the expense correctly.
     *
     * The City level is manually set to 3.
     * The test verifies if the level was updated and if the expense
     * matches the expected value for that level.
     */
    @Test
    void testSetLevelCalculatesExpenseCorrectly() {
        city.setLevel(3);

        assertEquals(3, city.getLevel());
        assertEquals(City.getEXPENSE() + City.getEXPENSE_BY_LEVEL() * (city.getLevel() - 1), city.getExpense(), "A despesa para o nível 3 foi mal calculada.");
    }
}