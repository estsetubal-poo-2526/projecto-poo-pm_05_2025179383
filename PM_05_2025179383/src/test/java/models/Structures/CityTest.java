package models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {

    private City city;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("PlayerTest");
        city = new City(player, 1);
    }

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

    @Test
    void testUpgradeStructureWithSuccess() throws InsufficientResourcesException {
        player.clearInventory();
        player.addResource(City.getMATERIAL_TO_UPGRADE(), 100);
        player.addResource(ResourceType.ACTION_POINTS,100);

        int baseApBefore = player.getBaseActionPoints();
        int scoreBefore = player.getScore();

        boolean result = city.upgradeStructure(1);

        assertTrue(result);
        assertEquals(2, city.getLevel(), "O nível devia ter subido para 2.");
        assertEquals(City.getEXPENSE() + City.getEXPENSE_BY_LEVEL(), city.getExpense(), "A despesa devia ter aumentado em 6.");
        assertEquals(baseApBefore + City.getAP_BY_LEVEL() * city.getLevel(), player.getBaseActionPoints(), "O jogador devia ganhar 3 AP máximos no upgrade.");
        assertEquals(scoreBefore + City.getScoreByLevelMultiplier() * city.getLevel(), player.getScore(), "O score devia aumentar em 10");
    }

    @Test
    void testUpgradeStructureThrowsInsufficientResourcesException() {
        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS,100);

        assertThrows(InsufficientResourcesException.class, () -> {
            city.upgradeStructure(1);
        }, "Devia falhar por falta de Stone para o upgrade.");
    }

    @Test
    void testSetLevelCalculatesExpenseCorrectly() {
        city.setLevel(3);
        assertEquals(3, city.getLevel());
        assertEquals(City.getEXPENSE() + City.getEXPENSE_BY_LEVEL() * (city.getLevel() - 1), city.getExpense(), "A despesa para o nível 3 foi mal calculada.");
    }
}