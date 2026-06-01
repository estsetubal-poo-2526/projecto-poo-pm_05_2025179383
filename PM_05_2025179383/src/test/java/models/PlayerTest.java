package models;

import jogo.models.Player;
import jogo.models.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("PlayerTest");
    }

    @Test
    void successfullyCreatePlayerFromScratch() {

        assertNotNull(player, "O jogador instanciado no setUp não devia ser nulo.");
        assertEquals("PlayerTest", player.getName(), "O nome do jogador não corresponde ao instanciado.");
        assertEquals(Player.getInitialScore(), player.getScore(), "O Jogador começou com o Score Incorreto");
        assertEquals(Player.getInitialItems(), player.getResourceQuantity(ResourceType.WOOD), "O jogador começou com o valor incorreto de WOOD");
        assertEquals(Player.getInitialItems(), player.getResourceQuantity(ResourceType.STONE), "O jogador começou com o valor incorreto de STONE");
        assertEquals(Player.getInitialItems(), player.getResourceQuantity(ResourceType.FOOD), "O jogador começou com o valor incorreto de FOOD");
        assertEquals(Player.getInitialActionPoints(), player.getResourceQuantity(ResourceType.ACTION_POINTS), "O jogador começou com o valor incorreto de ACTION POINTS");
    }

    @Test
    void successfullyCreatePlayerFromSave() {
        String name = "PlayerTest3";
        int score = 100;
        int wood = 10;
        int stone = 13;
        int food = 5;
        int actionPoints = 3;

        final Player[] player2Container = new Player[1];

        assertDoesNotThrow(() -> {
            player2Container[0] = new Player(name, score, wood, stone, food, actionPoints);
        }, "O construtor de carregamento de save falhou.");

        Player player2 = player2Container[0];

        assertNotNull(player2, "O jogador carregado não devia ser nulo.");
        assertEquals(name, player2.getName(), "O nome do jogador não foi restaurado corretamente.");
        assertEquals(score, player2.getScore(), "O Jogador começou com o Score Incorreto");
        assertEquals(wood, player2.getResourceQuantity(ResourceType.WOOD), "O jogador começou com o valor incorreto de WOOD");
        assertEquals(stone, player2.getResourceQuantity(ResourceType.STONE), "O jogador começou com o valor incorreto de STONE");
        assertEquals(food, player2.getResourceQuantity(ResourceType.FOOD), "O jogador começou com o valor incorreto de FOOD");
        assertEquals(actionPoints, player2.getResourceQuantity(ResourceType.ACTION_POINTS), "O jogador começou com o valor incorreto de ACTION POINTS");
    }

    @Test
    void playerBasicConstructorThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("");
        }, "Devia ter lançado IllegalArgumentException devido ao nome inválido (vazio).");
    }

    @Test
    void playerSaveConstructorThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("", 0, 0, 0, 0, 0);
        }, "Devia ter lançado IllegalArgumentException devido ao nome inválido no save.");
    }

    @Test
    void successfullyResetTheAP() {
        player.addResource(ResourceType.ACTION_POINTS, 100);
        player.resetAP();

        assertEquals(player.getBaseActionPoints(), player.getActionPoints(),
                "Não resetou o AP corretamente para o valor base.");
    }

    @Test
    void addActionPointsCorrectly(){

        int value = 10;
        int apBefore = player.getActionPoints();
        int baseBefore = player.getBaseActionPoints();

        player.addBaseActionPoints(value);

        assertEquals(apBefore + value, player.getActionPoints(), "Os AP atual não foram incrementados corretamente");
        assertEquals(baseBefore + value, player.getBaseActionPoints(), "O AP base (limite) não foi alterado.");

    }

    @Test
    void addScoreCorrectly() {

        int value = 10;
        int scoreBefore = player.getScore();

        player.addScore(value);

        assertEquals(scoreBefore + value, player.getScore(), "O Score não foi incrementado corretamente");

    }

    @Test
    void addResourcesCorrectly(){

        int quantityBefore = player.getResourceQuantity(ResourceType.WOOD);
        int value = 10;

        player.addResource(ResourceType.WOOD,value);

        assertEquals(quantityBefore + value, player.getResourceQuantity(ResourceType.WOOD), "A Quantidade não foi adicionada Corretamente");
    }

    @Test
    void removeHavingEnoughResources(){

        int resourcesBefore = player.getResourceQuantity(ResourceType.WOOD);
        int value = 5;

        assertTrue(player.removeResource(ResourceType.WOOD,value),"A Quantidade removida deveria ter sido aceite");
        assertEquals(resourcesBefore - value, player.getResourceQuantity(ResourceType.WOOD), "A quantidade removida não está correta");

    }

    @Test
    void removeDontHavingEnoughResources(){

        player.clearInventory();

        assertFalse(player.removeResource(ResourceType.WOOD,10),"Não deveria ser possivel remover a quantidade");

    }

    @Test
    void cleaningInventoryCorrectly(){

        player.clearInventory();

        assertEquals(0,player.getResourceQuantity(ResourceType.WOOD));
        assertEquals(0,player.getResourceQuantity(ResourceType.STONE));
        assertEquals(0,player.getResourceQuantity(ResourceType.FOOD));
        assertEquals(0,player.getResourceQuantity(ResourceType.ACTION_POINTS));

    }

}