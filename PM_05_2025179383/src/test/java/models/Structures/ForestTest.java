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
        assertEquals(Forest.getPRODUCTION_TYPE(), forest.getProduction(), "O tipo de produção está errado");
        assertEquals(Forest.getCOST_TYPE(), forest.getCostType(), "O tipo de custo está errado");
        assertEquals(Forest.getEXPENSE(), forest.getExpense(), "A Expense está errada");
        assertEquals(Forest.getPROFIT(), forest.getProfit(), "O Profit está errado");
        assertEquals(Forest.getMATERIAL_TO_UPGRADE(), forest.getUpgradeMaterial(), "O Material para melhorar está errado");
    }

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

    @Test
    void testUpgradeStructureThrowsInsufficientResourcesException() {
        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS, 100);

        assertThrows(InsufficientResourcesException.class, () -> {
            forest.upgradeStructure(1);
        }, "Devia falhar por falta de WOOD para o upgrade.");
    }

    @Test
    void testSetLevelCalculatesExpenseCorrectly() {
        int expenseBefore = forest.getExpense();

        forest.setLevel(3);

        assertEquals(3, forest.getLevel());
        assertEquals(expenseBefore + Forest.getExpenseByLevel() * (forest.getLevel() - 1), forest.getExpense(), "A despesa para o nível 3 foi mal calculada.");
    }

    @Test
    void testGenerateWood() {
        player.clearInventory();
        int initialProfit = forest.getProfit();

        forest.generateResource(1);

        // Verifica se o jogador recebeu a Wood que a floresta produz
        assertEquals(initialProfit, player.getResourceQuantity(ResourceType.WOOD),
                "O jogador devia ter recebido Wood baseado no profit da floresta.");
    }
}