package models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.Ranch;
import jogo.models.Structures.StructuresType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RanchTest {

    private Ranch ranch;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("PlayerTest");
        ranch = new Ranch(player, 1);
    }

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

    @Test
    void testUpgradeStructureThrowsInsufficientResourcesException() {
        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS, 100);

        assertThrows(InsufficientResourcesException.class, () -> {
            ranch.upgradeStructure(1);
        }, "Devia falhar por falta de STONE para o upgrade.");
    }

    @Test
    void testSetLevelCalculatesExpenseCorrectly() {
        int expenseBefore = ranch.getExpense();

        ranch.setLevel(3);

        assertEquals(3, ranch.getLevel());
        assertEquals(expenseBefore + Ranch.getExpenseByLevel() * (ranch.getLevel() - 1), ranch.getExpense(), "A despesa para o nível 3 foi mal calculada.");
    }

    @Test
    void testGenerateFood() {
        player.clearInventory();
        int initialProfit = ranch.getProfit();

        ranch.generateResource(1);

        assertEquals(initialProfit, player.getResourceQuantity(ResourceType.FOOD),
                "O jogador devia ter recebido Food baseado no profit do rancho.");
    }
}