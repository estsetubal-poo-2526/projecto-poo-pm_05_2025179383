package models.Structures;

import jogo.exceptions.GameException;
import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ForestTest {

    private Forest forest;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("PlayerTest");
        forest = new Forest(player, 1);
    }

    @Test
    void testCreateValidCity() {
        assertEquals(Forest.getSTRUCTURE_TYPE(), forest.getStructureType());
        assertEquals(1, forest.getLevel(), "O Nivel deveria começar em 1");
        assertEquals(player, forest.getOwner(), "O dono não está a ser atribuído corretamente");
        assertEquals(City.getPRODUCTION_TYPE(), forest.getProduction(), "O tipo de produção está errado");
        assertEquals(City.getCOST_TYPE(), forest.getCostType(), "O tipo de custo está errado");
        assertEquals(City.getEXPENSE(), forest.getExpense(), "A Expense está errada");
        assertEquals(City.getPROFIT(), forest.getProfit(), "O Profit está errado");
        assertEquals(City.getMATERIAL_TO_UPGRADE(), forest.getUpgradeMaterial(), "O Material para melhorar está errado");
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
        assertEquals(22, city.getExpense(), "A despesa para o nível 3 foi mal calculada.");
    }

}
