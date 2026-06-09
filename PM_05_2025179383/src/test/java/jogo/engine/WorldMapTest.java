package jogo.engine;

import jogo.exceptions.*;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the WorldMap class.
 *
 * This class tests the main behavior of the game map,
 * including adding structures, checking occupied spaces,
 * validating coordinates, checking player interaction permissions,
 * generating resources and consuming resources.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class WorldMapTest {

    /**
     * WorldMap object used in the tests.
     */
    private WorldMap map;

    /**
     * Player object used as the owner of structures during the tests.
     */
    private Player player1;

    /**
     * Creates a new player and a new empty map before each test.
     *
     * This ensures that every test starts with a clean map
     * and a fresh player.
     */
    @BeforeEach
    void setUp() {
        player1 = new Player("Player1");
        map = new WorldMap();
    }

    /**
     * Tests if a structure can be added to the map without throwing exceptions.
     *
     * The test adds a Forest to position 0,0 and then verifies
     * if the structure stored in that position is really a Forest.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void addStructuresWithoutThrows() throws GameException {

        assertDoesNotThrow(() -> {
            map.addStructure(new Forest(player1, 1), 0, 0);
        }, "Não deveria lançar nenhuma exceção");

        assertEquals(Forest.class, map.getStructure(0, 0).getClass(), "Deveria ser da Classe FOREST");
    }

    /**
     * Tests if adding a structure to an already occupied position throws
     * SpaceAlreadyOccupiedException.
     *
     * @throws GameException if an unexpected game error occurs while preparing the test
     */
    @Test
    void addStructuresThrowingSpaceAlreadyOccupiedException() throws GameException {

        map.addStructure(new Forest(player1, 1), 0, 0);

        assertThrows(SpaceAlreadyOccupiedException.class, () -> {
            map.addStructure(new Mine(new Player("Player2"), 1), 0, 0);
        }, "Deveria Ter lançado SpaceAlreadyOccupiedException");
    }

    /**
     * Tests if adding a structure outside the map boundaries throws
     * CoordinatesOutOfBoundsException.
     */
    @Test
    void addStructuresOutsideTheMap() {

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            map.addStructure(new Forest(player1, 1), -1, 8);
        }, "Deveria ter Lançado CoordinatesOutOfBoundsException");
    }

    /**
     * Tests if getting a structure inside the map works correctly.
     *
     * A Forest is added to the map and then retrieved from the same position.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void getStructuresInsideTheMap() throws GameException {

        map.addStructure(new Forest(player1, 1), 0, 0);

        assertDoesNotThrow(() -> {
            map.getStructure(0, 0);
        }, "Deveria devolver a Estrutura");
    }

    /**
     * Tests if getting a structure from an empty position inside the map
     * returns null.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void getNullStructuresInsideTheMap() throws GameException {

        assertNull(map.getStructure(0, 0), "Deveria ser null");
    }

    /**
     * Tests if getting a structure outside the map boundaries throws
     * CoordinatesOutOfBoundsException.
     */
    @Test
    void getStructuresInfoOutsideMap() {
        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            map.getStructure(9, -1);
        }, "Deveria ter Lançado CoordinatesOutOfBoundsException");
    }

    /**
     * Tests if a map position is correctly detected as occupied.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void spaceIsOccupied() throws GameException {
        map.addStructure(new Forest(new Player("Player"), 1), 0, 0);

        assertFalse(map.isNotOccupied(0, 0), "O espaço deveria estar ocupado");
    }

    /**
     * Tests if an empty map position is correctly detected as not occupied.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void spaceIsNotOccupied() throws GameException {

        assertTrue(map.isNotOccupied(0, 0), "O espaço não deveria estar ocupado");
    }

    /**
     * Tests if checking occupation outside the map boundaries throws
     * CoordinatesOutOfBoundsException.
     *
     * @throws GameException if an unexpected game error occurs while preparing the test
     */
    @Test
    void spaceIsOccupiedOutsideMap() throws GameException {
        map.addStructure(new Forest(new Player("Player"), 1), 0, 0);

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            map.isNotOccupied(-2, 10);
        }, "Deveria ter lançado CoordinatesOutOfBoundsException");
    }

    /**
     * Tests if the owner of a structure can interact with it.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void canInteract() throws GameException {

        map.addStructure(new Forest(player1, 1), 0, 0);

        assertDoesNotThrow(() -> {
            map.canInteract(0, 0, player1);
        }, "O jogador deveria conseguir interagir");
    }

    /**
     * Tests if interacting with an empty map position throws
     * StructureDontExistException.
     */
    @Test
    void canInteractThrowsStructureDontExistException() {

        assertThrows(StructureDontExistException.class, () -> {
            map.canInteract(0, 0, player1);
        }, "Não deveria conseguir interagir com null");
    }

    /**
     * Tests if a player cannot interact with a structure that belongs
     * to another player.
     *
     * Expected result:
     * UnauthorizedActionException is thrown.
     *
     * @throws GameException if an unexpected game error occurs while preparing the test
     */
    @Test
    void canInteractThrowsUnauthorizedActionException() throws GameException {
        Player player2 = new Player("Player2");

        map.addStructure(new Forest(player2, 1), 0, 0);

        assertThrows(UnauthorizedActionException.class, () -> {
            map.canInteract(0, 0, player1);
        }, "Não deveria conseguir interagir com a estrutura que não lhe pertence");
    }

    /**
     * Tests if interacting outside the map boundaries throws
     * CoordinatesOutOfBoundsException.
     */
    @Test
    void canInteractOutsideMap() {

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            map.canInteract(-2, 10, player1);
        }, "Não deveria conseguir interagir fora do mapa");
    }

    /**
     * Tests if structures generate resources correctly.
     *
     * A Forest is added to the map and then resource generation is triggered.
     * The test verifies if the player's wood quantity increased.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void testGenerateResources() throws GameException {
        map.addStructure(new Forest(player1, 1), 0, 0);

        int woodQuantityBefore = player1.getResourceQuantity(ResourceType.WOOD);

        map.generateResources(1);

        int woodQuantityAfter = player1.getResourceQuantity(ResourceType.WOOD);

        assertTrue(woodQuantityAfter > woodQuantityBefore, "Não gerou os recursos corretamente");
    }

    /**
     * Tests if consuming resources does not damage a structure when
     * the player has enough resources.
     *
     * The test checks if the structure level stays the same.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void testConsumeResourcesWithoutDamagingTheStructure() throws GameException {

        map.addStructure(new Forest(player1, 1), 0, 0);

        int levelBefore = map.getStructure(0, 0).getLevel();

        map.consumeResources();

        int levelAfter = map.getStructure(0, 0).getLevel();

        assertEquals(levelAfter, levelBefore, "O Nivel não deveria diminuir");
    }

    /**
     * Tests if consuming resources damages a structure when the player
     * does not have enough resources.
     *
     * The structure is first upgraded to level 2.
     * Then the player's inventory is cleared.
     * After consuming resources, the structure level should decrease by 1.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void testConsumeResourcesDamagingTheStructure() throws GameException {

        map.addStructure(new Forest(player1, 1), 0, 0);
        player1.addResource(Forest.getMATERIAL_TO_UPGRADE(), 100);
        player1.addResource(ResourceType.ACTION_POINTS, 100);
        GameEngine.upgradeStructure(map, player1, 0, 0, 1);

        int levelBefore = map.getStructure(0, 0).getLevel();

        player1.clearInventory();

        map.consumeResources();
        int levelAfter = map.getStructure(0, 0).getLevel();

        assertTrue(levelAfter < levelBefore, "O Nivel não diminuiu");
        assertEquals(levelAfter + 1, levelBefore, "O Nivel não foi diminuido corretamente");
    }

    /**
     * Tests if consuming resources destroys a structure when its level
     * reaches zero.
     *
     * The player's inventory is cleared, forcing the structure to lose level.
     * Since the structure starts at level 1, it should be removed from the map.
     *
     * @throws GameException if an unexpected game error occurs
     */
    @Test
    void testConsumeResourcesDestroyingTheStructure() throws GameException {

        map.addStructure(new Forest(player1, 1), 0, 0);
        player1.clearInventory();
        map.consumeResources();

        assertNull(map.getStructure(0, 0));
    }
}