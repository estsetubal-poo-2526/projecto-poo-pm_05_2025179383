package models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.Mine;
import jogo.models.Structures.StructuresType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MineTest {

    private Mine mine;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("PlayerTest");
        mine = new Mine(player, 1);
    }

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

    @Test
    void testUpgradeMineInsufficientStone() {
        player.clearInventory();
        player.addResource(ResourceType.WOOD, 100);

        assertThrows(InsufficientResourcesException.class, () -> {
            mine.upgradeStructure(1);
        }, "Devia falhar por falta de STONE.");
    }

    @Test
    void testGenerateStone() {
        player.clearInventory();
        int initialProfit = mine.getProfit();

        mine.generateResource(1);

        assertEquals(initialProfit, player.getResourceQuantity(ResourceType.STONE),
                "O jogador devia ter recebido Stone baseado no profit.");
    }
}