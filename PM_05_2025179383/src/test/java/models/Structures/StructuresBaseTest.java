package models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.Forest;
import jogo.models.Structures.Structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the base Structures class.
 *
 * This class tests behavior that is common to all structures,
 * using a Forest object as a concrete implementation of Structures.
 *
 * The tests verify if maintenance resources are consumed correctly,
 * if a structure loses level when the player does not have enough resources,
 * and if the structure is destroyed when its level reaches zero.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class StructuresBaseTest {

    /**
     * Player object used as the owner of the structure.
     */
    private Player player;

    /**
     * Structure object used in the tests.
     *
     * A Forest is used because Structures is abstract and cannot be
     * instantiated directly.
     */
    private Structures structure;

    /**
     * Creates a new player and a new structure before each test.
     *
     * This ensures that every test starts with a clean player
     * and a fresh structure.
     */
    @BeforeEach
    void setUp() {
        player = new Player("OwnerTest");
        structure = new Forest(player, 1);
    }

    /**
     * Tests if a structure successfully pays its maintenance cost.
     *
     * The player's inventory is cleared and then enough stone is added
     * to pay the Forest maintenance cost.
     *
     * Expected result:
     * the returned message confirms that maintenance was paid.
     */
    @Test
    void testConsumeResourcesWithSuccess() {
        player.clearInventory();
        player.addResource(ResourceType.STONE, 10);

        String result = structure.consumeResources();

        assertTrue(result.contains("Manutenção paga"), "Deveria confirmar o pagamento.");
    }

    /**
     * Tests if a structure loses one level when the owner does not have
     * enough resources to pay maintenance.
     *
     * The structure level is manually set to 3 and the player's inventory
     * is cleared. Since the player cannot pay the maintenance cost,
     * the structure should degrade to level 2.
     */
    @Test
    void testConsumeResourcesDegradesStructure() {
        player.clearInventory();
        structure.setLevel(3);

        String result = structure.consumeResources();

        assertEquals(2, structure.getLevel(), "A estrutura devia ter degradado para o Nível 2.");
        assertTrue(result.contains("Degradou-se para Nível 2"), "A mensagem de degradação está errada.");
    }

    /**
     * Tests if a structure is destroyed when it cannot pay maintenance
     * while already at level 1.
     *
     * The player's inventory is cleared. Since the structure starts at level 1,
     * failing maintenance should reduce it to level 0 and return a destruction message.
     */
    @Test
    void testConsumeResourcesDestroysStructure() {
        player.clearInventory();

        String result = structure.consumeResources();

        assertEquals(0, structure.getLevel(), "A estrutura devia estar a nível 0 (Destruída).");
        assertTrue(result.contains("foi destruída por falta de pagamento"), "A mensagem de destruição está errada.");
    }
}