package jogo.engine;

import jogo.exceptions.*;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.CreateStructure;
import jogo.models.Structures.Forest;
import jogo.models.Structures.Structures;
import jogo.models.Structures.StructuresType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GameEngine class.
 *
 * This class tests the main game actions managed by GameEngine,
 * such as creating structures, upgrading structures, searching resources
 * and retrieving structure information from the map.
 *
 * The tests verify both successful actions and expected exceptions
 * when invalid actions are performed.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
class GameEngineTest {

    /**
     * Map used during the tests.
     */
    private WorldMap map;

    /**
     * Player used during the tests.
     */
    private Player player;

    /**
     * Fixed quantity of resources used to give the player enough resources
     * and Action Points during tests.
     */
    private static final int MAKE_RICH_VALUES = 100;

    /**
     * Creates a new map and a new player before each test.
     *
     * This ensures that every test starts with clean objects and that
     * previous tests do not affect the next ones.
     */
    @BeforeEach
    void setUp() {
        map = new WorldMap();
        player = new Player("PlayerTest");
    }

    /**
     * Tests if a structure can be created successfully.
     *
     * The player receives enough resources and Action Points before the test.
     * After creating the structure, the test verifies if the map position
     * becomes occupied and if the correct resources and Action Points
     * were removed from the player.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void testSuccessCreateStructure() throws GameException {
        makePlayerRich(player);

        int apBefore = player.getResourceQuantity(ResourceType.ACTION_POINTS);
        int resourceBefore = player.getResourceQuantity(Forest.getCOST_TYPE());

        assertDoesNotThrow(() -> {
            GameEngine.createStructure(map, player, StructuresType.FOREST, 2, 3, 1);
        }, "A estrutura devia ter sido criada com sucesso.");

        assertFalse(map.isNotOccupied(2, 3), "O Mapa devia estar Ocupado na coordenada [2,3].");

        assertEquals(apBefore - Forest.getApNeeded(), player.getActionPoints(), "Não foi retirada a quantidade correta de AP");

        int constructionCost = CreateStructure.getMaterialCost(StructuresType.FOREST);
        assertEquals(resourceBefore - constructionCost, player.getResourceQuantity(ResourceType.STONE), "Não foi retirada a quantidade correta de Recurso");
    }

    /**
     * Tests if creating a structure outside the map boundaries throws
     * CoordinatesOutOfBoundsException.
     *
     * The test checks both negative coordinates and coordinates equal to
     * or greater than the map size.
     */
    @Test
    void testCreateStructureThrowsCoordinatesOutOfBoundException() {
        makePlayerRich(player);

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.createStructure(map, player, StructuresType.FOREST, -1, -1, 1);
        }, "Devia Ter Lançado a exceção CoordinatesOutOfBounds para indices negativos");

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.createStructure(map, player, StructuresType.FOREST, 7, 3, 1);
        }, "Devia ter lançado CoordinatesOutOfBoundsException para índice igual ou superior ao tamanho.");
    }

    /**
     * Tests if creating a structure in an occupied position throws
     * SpaceAlreadyOccupiedException.
     *
     * @throws GameException if an unexpected game error occurs while preparing the test
     */
    @Test
    void testCreateStructureThrowsSpaceAlreadyOccupiedException() throws GameException {
        makePlayerRich(player);
        map.addStructure(new Forest(player, 1), 2, 3);

        assertThrows(SpaceAlreadyOccupiedException.class, () -> {
            GameEngine.createStructure(map, player, StructuresType.FOREST, 2, 3, 1);
        }, "Devia Ter Lançado SpaceAlreadyOccupiedException porque ja existia estruturas nas coordenadas [2,3]");
    }

    /**
     * Tests if creating a structure without enough resources throws
     * InsufficientResourcesException.
     *
     * The player only receives Action Points, but no construction materials.
     */
    @Test
    void testCreateStructureThrowsInsufficientResourcesException() {
        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS, 100);

        assertThrows(InsufficientResourcesException.class, () -> {
            GameEngine.createStructure(map, player, StructuresType.CITY, 0, 0, 1);
        }, "Devia Ter Lançado InsufficientResourcesException pois o jogador nao tem recursos suficientes");
    }

    /**
     * Tests if creating a structure without enough Action Points throws
     * InsufficientAPException.
     *
     * The player receives construction materials but does not receive
     * enough Action Points.
     */
    @Test
    void testCreateStructureThrowsInsufficientAPException() {
        player.clearInventory();
        player.addResource(ResourceType.STONE, 100);

        assertThrows(InsufficientAPException.class, () -> {
            GameEngine.createStructure(map, player, StructuresType.FOREST, 0, 0, 1);
        }, "Devia Ter Lançado InsufficientAPException pois o jogador nao tem AP suficientes");
    }

    /**
     * Tests if a structure can be upgraded successfully.
     *
     * The test verifies if the upgrade consumes the correct resources,
     * consumes the correct Action Points and increases the structure level.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void testSuccessUpgradeStructure() throws GameException {
        makePlayerRich(player);
        GameEngine.createStructure(map, player, StructuresType.FOREST, 0, 0, 1);

        Structures structure = map.getStructure(0, 0);
        int resourcesBefore = player.getResourceQuantity(structure.getUpgradeMaterial());
        int apBefore = player.getResourceQuantity(ResourceType.ACTION_POINTS);

        int expectedResourcesConsumption = structure.getUpgradeCost();
        int expectedAPCost = structure.getAPCostToUpgrade();

        assertDoesNotThrow(() -> {
            GameEngine.upgradeStructure(map, player, 0, 0, 1);
        }, "Estrutura devia ter sido melhorada com sucesso em [0,0]");

        assertEquals(resourcesBefore - expectedResourcesConsumption, player.getResourceQuantity(structure.getUpgradeMaterial()),
                "O custo em pedra não foi debitado corretamente.");

        assertEquals(apBefore - expectedAPCost, player.getResourceQuantity(ResourceType.ACTION_POINTS),
                "A Logica de Remoção de AP Falhou");

        assertEquals(2, structure.getLevel(), "A estrutura devia estar a nível 2.");
    }

    /**
     * Tests if upgrading a structure outside the map boundaries throws
     * CoordinatesOutOfBoundsException.
     */
    @Test
    void testUpgradeStructureThrowsCoordinatesOutOfBoundException() {
        makePlayerRich(player);

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.upgradeStructure(map, player, -1, -1, 1);
        }, "Devia Ter Lançado a exceção CoordinatesOutOfBounds para indices negativos");

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.upgradeStructure(map, player, 7, 3, 1);
        }, "Devia ter lançado CoordinatesOutOfBoundsException para índice igual ou superior ao tamanho.");
    }

    /**
     * Tests if upgrading a structure without enough Action Points throws
     * InsufficientAPException.
     *
     * @throws GameException if an unexpected game error occurs while preparing the test
     */
    @Test
    void testUpgradeStructureThrowInsufficientAPException() throws GameException {
        makePlayerRich(player);
        GameEngine.createStructure(map, player, StructuresType.FOREST, 0, 0, 1);

        player.clearInventory();
        player.addResource(ResourceType.STONE, 100);

        assertThrows(InsufficientAPException.class, () -> {
            GameEngine.upgradeStructure(map, player, 0, 0, 1);
        }, "Devia ter lançado a InsufficientAPException");
    }

    /**
     * Tests if upgrading a structure without enough upgrade resources throws
     * InsufficientResourcesException.
     *
     * @throws GameException if an unexpected game error occurs while preparing the test
     */
    @Test
    void testUpgradeStructureThrowInsufficientResourcesException() throws GameException {
        makePlayerRich(player);
        GameEngine.createStructure(map, player, StructuresType.FOREST, 0, 0, 1);

        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS, 100);

        assertThrows(InsufficientResourcesException.class, () -> {
            GameEngine.upgradeStructure(map, player, 0, 0, 1);
        }, "Devia ter lançado a InsufficientResourcesException");
    }

    /**
     * Tests if upgrading a position without a structure throws
     * StructureDontExistException.
     */
    @Test
    void testUpgradeStructureThrowsStructureDontExistException() {
        assertThrows(StructureDontExistException.class, () -> {
            GameEngine.upgradeStructure(map, player, 0, 0, 1);
        }, "Devia ter lançado a StructureDontExistException");
    }

    /**
     * Tests if a player cannot upgrade a structure that belongs to another player.
     *
     * Expected result:
     * UnauthorizedActionException is thrown.
     *
     * @throws GameException if an unexpected game error occurs while preparing the test
     */
    @Test
    void testUpgradeStructureThrowsUnauthorizedActionException() throws GameException {
        makePlayerRich(player);
        GameEngine.createStructure(map, player, StructuresType.FOREST, 0, 0, 1);

        Player player2 = new Player("PlayerTest2");
        makePlayerRich(player2);

        assertThrows(UnauthorizedActionException.class, () -> {
            GameEngine.upgradeStructure(map, player2, 0, 0, 1);
        }, "Devia ter lançado a UnauthorizedActionException");
    }

    /**
     * Tests if upgrading a structure increases its statistics correctly.
     *
     * The test verifies if the expense and profit values are updated
     * according to the Forest upgrade values.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void testUpgradeStructureIncreaseStats() throws GameException {
        makePlayerRich(player);

        GameEngine.createStructure(map, player, StructuresType.FOREST, 0, 0, 1);

        Structures structure = map.getStructure(0, 0);

        int beforeUpgradeExpense = structure.getExpense();

        GameEngine.upgradeStructure(map, player, 0, 0, 1);

        int expenseEsperada = beforeUpgradeExpense + Forest.getExpenseByLevel();
        assertEquals(expenseEsperada, structure.getExpense(),
                "A Melhoria não alterou a despesa corretamente.");

        assertEquals(Forest.getPROFIT() + Forest.getProfitByLevel(), structure.getProfit(),
                "A Melhoria não alterou o Lucro Corretamente");
    }

    /**
     * Tests if searching resources works correctly.
     *
     * The test verifies if the player loses the correct amount of Action Points
     * and receives a resource quantity inside the expected random interval.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void testSearchResourcesSuccess() {
        makePlayerRich(player);

        int apBefore = player.getResourceQuantity(ResourceType.ACTION_POINTS);
        int woodBefore = player.getResourceQuantity(ResourceType.WOOD);

        int minResourcesFound = GameEngine.getBonusSearchValue();
        int maxResourcesFound = GameEngine.getBaseSearchValue() + GameEngine.getBonusSearchValue() - 1;

        Integer result = assertDoesNotThrow(() -> {
            return GameEngine.searchResources(player, ResourceType.WOOD);
        }, "A busca por recursos devia ter funcionado.");

        assertNotNull(result);

        assertEquals(apBefore - GameEngine.getApCostSearch(), player.getResourceQuantity(ResourceType.ACTION_POINTS),
                "Não foi retirado o AP correto da procura");

        int woodAfter = player.getResourceQuantity(ResourceType.WOOD);
        int quantityObtained = woodAfter - woodBefore;

        assertTrue(quantityObtained >= minResourcesFound && quantityObtained <= maxResourcesFound,
                "A quantidade de recursos gerada (" + quantityObtained + ") está fora do intervalo" + minResourcesFound + " a " + maxResourcesFound);
    }

    /**
     * Tests the searchResources method multiple times.
     *
     * This test ensures that the random quantity of resources obtained
     * always stays inside the expected interval.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void testSearchResourcesSuccessRepeatedly() throws GameException {
        int minResourcesFound = GameEngine.getBonusSearchValue();
        int maxResourcesFound = GameEngine.getBaseSearchValue() + GameEngine.getBonusSearchValue() - 1;

        for (int i = 0; i < 100; i++) {
            player.clearInventory();
            makePlayerRich(player);

            int woodBefore = player.getResourceQuantity(ResourceType.WOOD);
            GameEngine.searchResources(player, ResourceType.WOOD);
            int quantityObtained = player.getResourceQuantity(ResourceType.WOOD) - woodBefore;

            assertTrue(quantityObtained >= minResourcesFound && quantityObtained <= maxResourcesFound,
                    "Na iteração " + i + ", a quantidade (" + quantityObtained + ") saiu do intervalo de " + minResourcesFound + " a " + maxResourcesFound);
        }
    }

    /**
     * Tests if searching resources without enough Action Points throws
     * InsufficientAPException.
     */
    @Test
    void testSearchResourcesThrowsInsufficientAPException() {
        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS, 3);

        assertThrows(InsufficientAPException.class, () -> {
            GameEngine.searchResources(player, ResourceType.WOOD);
        }, "Devia ter lançado InsufficientAPException");
    }

    /**
     * Tests if searching for an invalid resource type throws GameException.
     *
     * Invalid resource types are NONE and ACTION_POINTS.
     * The test also verifies if the exception message is correct.
     */
    @Test
    void testSearchResourcesThrowsGameExceptionForInvalidType() {
        makePlayerRich(player);

        GameException exNone = assertThrows(GameException.class, () -> {
            GameEngine.searchResources(player, ResourceType.NONE);
        }, "Devia ter lançado GameException para ResourceType.NONE");

        assertEquals("Tipo de recurso inválido.", exNone.getMessage());

        GameException exAp = assertThrows(GameException.class, () -> {
            GameEngine.searchResources(player, ResourceType.ACTION_POINTS);
        }, "Devia ter lançado GameException para ResourceType.ACTION_POINTS");

        assertEquals("Tipo de recurso inválido.", exAp.getMessage());
    }

    /**
     * Tests if getStructureInfo returns the correct structure.
     *
     * The test creates a Forest structure and then verifies if the returned
     * structure is not null, has the correct type and belongs to the correct player.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void testGetStructureInfoSuccess() throws GameException {
        makePlayerRich(player);

        GameEngine.createStructure(map, player, StructuresType.FOREST, 1, 2, 1);

        Structures estrutura = assertDoesNotThrow(() -> {
            return GameEngine.getStructureInfo(map, 1, 2);
        }, "O método devia ter devolvido a estrutura sem lançar exceções.");

        assertNotNull(estrutura, "A estrutura devolvida não devia ser nula.");
        assertEquals(StructuresType.FOREST.toString(), estrutura.getName(),
                "O tipo da estrutura devolvida está errado.");

        assertEquals(player, estrutura.getOwner(), "O dono da estrutura devia ser o player.");
    }

    /**
     * Tests if getting structure information outside the map boundaries throws
     * CoordinatesOutOfBoundsException.
     */
    @Test
    void testGetStructureInfoThrowsCoordinatesOutOfBoundException() {
        makePlayerRich(player);

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.getStructureInfo(map, -1, 1);
        }, "Devia Ter Lançado a exceção CoordinatesOutOfBounds para indice negativo");

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.getStructureInfo(map, 7, 3);
        }, "Devia ter lançado CoordinatesOutOfBoundsException para índice igual ou maior ao tamanho.");
    }

    /**
     * Tests if getting structure information from an empty position throws
     * GameException.
     *
     * The test also verifies if the exception message is correct.
     */
    @Test
    void testGetStructureInfoThrowsExceptionWhenEmpty() throws GameException {
        assertTrue(map.isNotOccupied(4, 4), "A coordenada devia estar vazia.");

        GameException exception = assertThrows(GameException.class, () -> {
            GameEngine.getStructureInfo(map, 4, 4);
        }, "Devia ter lançado GameException porque a coordenada está vazia.");

        assertEquals("Essa Estrutura não Existe!", exception.getMessage(),
                "A mensagem de erro da exceção está errada.");
    }

    /**
     * Gives a player a high amount of every main resource.
     *
     * This helper method is used to prepare tests where the player needs
     * enough resources and Action Points to perform actions.
     *
     * @param player the player that will receive the resources
     */
    private static void makePlayerRich(Player player) {
        player.clearInventory();

        player.addResource(ResourceType.WOOD, MAKE_RICH_VALUES);
        player.addResource(ResourceType.STONE, MAKE_RICH_VALUES);
        player.addResource(ResourceType.ACTION_POINTS, MAKE_RICH_VALUES);
        player.addResource(ResourceType.FOOD, MAKE_RICH_VALUES);
    }
}