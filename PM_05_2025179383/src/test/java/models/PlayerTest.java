package models;

import jogo.models.Player;
import jogo.models.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Player class.
 *
 * This class tests the creation and behavior of Player objects.
 * It verifies if players are correctly created from scratch and from saved data,
 * if invalid names throw exceptions, and if resources, score and Action Points
 * are managed correctly.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class PlayerTest {

    /**
     * Player object used in the tests.
     */
    private Player player;

    /**
     * Creates a new player before each test.
     *
     * This ensures that every test starts with a fresh Player object.
     */
    @BeforeEach
    void setUp() {
        player = new Player("PlayerTest");
    }

    /**
     * Tests if a player is successfully created using the basic constructor.
     *
     * The test verifies if the player is not null, if the name is correct,
     * if the initial score is correct, and if all starting resources have
     * the expected initial values.
     */
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

    /**
     * Tests if a player is successfully created using saved data.
     *
     * This test uses the save constructor to create a player with custom
     * values for name, score, wood, stone, food and Action Points.
     * It then verifies if all values were restored correctly.
     */
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

    /**
     * Tests if the basic constructor throws IllegalArgumentException
     * when the player name is invalid.
     *
     * An empty name should not be accepted.
     */
    @Test
    void playerBasicConstructorThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("");
        }, "Devia ter lançado IllegalArgumentException devido ao nome inválido (vazio).");
    }

    /**
     * Tests if the save constructor throws IllegalArgumentException
     * when the player name is invalid.
     *
     * An empty name should not be accepted, even when loading a player
     * from saved values.
     */
    @Test
    void playerSaveConstructorThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player("", 0, 0, 0, 0, 0);
        }, "Devia ter lançado IllegalArgumentException devido ao nome inválido no save.");
    }

    /**
     * Tests if the player's Action Points are reset correctly.
     *
     * The player receives extra Action Points and then resetAP is called.
     * The current Action Points should return to the player's base Action Points.
     */
    @Test
    void successfullyResetTheAP() {
        player.addResource(ResourceType.ACTION_POINTS, 100);
        player.resetAP();

        assertEquals(player.getBaseActionPoints(), player.getActionPoints(),
                "Não resetou o AP corretamente para o valor base.");
    }

    /**
     * Tests if base Action Points are added correctly.
     *
     * The test verifies if both the current Action Points and the base
     * Action Points increase by the expected value.
     */
    @Test
    void addActionPointsCorrectly() {

        int value = 10;
        int apBefore = player.getActionPoints();
        int baseBefore = player.getBaseActionPoints();

        player.addBaseActionPoints(value);

        assertEquals(apBefore + value, player.getActionPoints(), "Os AP atual não foram incrementados corretamente");
        assertEquals(baseBefore + value, player.getBaseActionPoints(), "O AP base (limite) não foi alterado.");
    }

    /**
     * Tests if score is added correctly to the player.
     *
     * The test stores the score before adding points and then verifies
     * if the score increased by the expected amount.
     */
    @Test
    void addScoreCorrectly() {

        int value = 10;
        int scoreBefore = player.getScore();

        player.addScore(value);

        assertEquals(scoreBefore + value, player.getScore(), "O Score não foi incrementado corretamente");
    }

    /**
     * Tests if resources are added correctly to the player's inventory.
     *
     * The test adds wood to the player and verifies if the quantity
     * increased by the expected value.
     */
    @Test
    void addResourcesCorrectly() {

        int quantityBefore = player.getResourceQuantity(ResourceType.WOOD);
        int value = 10;

        player.addResource(ResourceType.WOOD, value);

        assertEquals(quantityBefore + value, player.getResourceQuantity(ResourceType.WOOD), "A Quantidade não foi adicionada Corretamente");
    }

    /**
     * Tests if resources are removed correctly when the player has enough.
     *
     * The test removes wood from the player and verifies if the operation
     * returns true and if the quantity was reduced correctly.
     */
    @Test
    void removeHavingEnoughResources() {

        int resourcesBefore = player.getResourceQuantity(ResourceType.WOOD);
        int value = 5;

        assertTrue(player.removeResource(ResourceType.WOOD, value), "A Quantidade removida deveria ter sido aceite");
        assertEquals(resourcesBefore - value, player.getResourceQuantity(ResourceType.WOOD), "A quantidade removida não está correta");
    }

    /**
     * Tests if removing resources fails when the player does not have enough.
     *
     * The inventory is cleared before trying to remove wood.
     * The operation should return false.
     */
    @Test
    void removeDontHavingEnoughResources() {

        player.clearInventory();

        assertFalse(player.removeResource(ResourceType.WOOD, 10), "Não deveria ser possivel remover a quantidade");
    }

    /**
     * Tests if the inventory is cleared correctly.
     *
     * After calling clearInventory, all main resources and Action Points
     * should be equal to zero.
     */
    @Test
    void cleaningInventoryCorrectly() {

        player.clearInventory();

        assertEquals(0, player.getResourceQuantity(ResourceType.WOOD));
        assertEquals(0, player.getResourceQuantity(ResourceType.STONE));
        assertEquals(0, player.getResourceQuantity(ResourceType.FOOD));
        assertEquals(0, player.getResourceQuantity(ResourceType.ACTION_POINTS));
    }
}