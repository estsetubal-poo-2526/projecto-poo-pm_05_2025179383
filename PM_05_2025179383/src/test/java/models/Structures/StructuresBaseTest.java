package models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.Forest;
import jogo.models.Structures.Structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StructuresBaseTest {

    private Player player;
    private Structures structure;

    @BeforeEach
    void setUp() {
        player = new Player("OwnerTest");
        structure = new Forest(player, 1);
    }

    @Test
    void testConsumeResourcesWithSuccess() {
        player.clearInventory();
        player.addResource(ResourceType.STONE, 10);

        String result = structure.consumeResources();

        assertTrue(result.contains("Manutenção paga"), "Deveria confirmar o pagamento.");
    }

    @Test
    void testConsumeResourcesDegradesStructure() {
        player.clearInventory();
        structure.setLevel(3);
        String result = structure.consumeResources();

        assertEquals(2, structure.getLevel(), "A estrutura devia ter degradado para o Nível 2.");
        assertTrue(result.contains("Degradou-se para Nível 2"), "A mensagem de degradação está errada.");
    }

    @Test
    void testConsumeResourcesDestroysStructure() {
        player.clearInventory();

        String result = structure.consumeResources();

        assertEquals(0, structure.getLevel(), "A estrutura devia estar a nível 0 (Destruída).");
        assertTrue(result.contains("foi destruída por falta de pagamento"), "A mensagem de destruição está errada.");
    }
}