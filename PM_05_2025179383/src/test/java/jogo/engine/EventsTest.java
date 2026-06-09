package jogo.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Events class.
 *
 * This class tests the different random events that can occur in the game.
 * It verifies if the resource and score modifiers are correctly changed
 * according to the event that is triggered.
 *
 * The random behavior is controlled by replacing the random object in Events
 * with a mocked Random object. This allows each test to force a specific result.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
class EventsTest {

    /**
     * Events object used in the tests.
     */
    private Events events;

    /**
     * Creates a new Events object before each test.
     *
     * This makes sure that each test starts with a fresh object.
     */
    @BeforeEach
    void setUp() {
        events = new Events();
    }

    /**
     * Tests when no special event is triggered.
     *
     * The first random value is 45.
     * Since the event only happens when the value is lower than 45,
     * no event should occur.
     *
     * Expected result:
     * resource modifier stays 1 and score modifier stays 1.
     */
    @Test
    void testNoEventTriggered() {

        Events.random = new Random() {
            @Override
            public int nextInt(int bound) {
                if (bound == 100) return 45;
                return 0;
            }
        };

        String result = events.triggerEvent();

        assertEquals("Sem Evento Especial Hoje!", result);
        assertEquals(1, events.getResourceModifier(), "Recursos deviam ser base (1)");
        assertEquals(1, events.getScoreModifier(), "Pontuação devia ser base (1)");
    }

    /**
     * Tests the event where both resource production and score are doubled.
     *
     * The mocked random values force the event with index 0.
     *
     * Expected result:
     * resource modifier becomes 2 and score modifier becomes 2.
     */
    @Test
    void testEventDoubleEverything() {

        mockRandom(10, 0);

        String result = events.triggerEvent();

        assertEquals("Estruturas Produzem o dobro de Recursos e de Pontuação!!!", result);
        assertEquals(2, events.getResourceModifier());
        assertEquals(2, events.getScoreModifier());
    }

    /**
     * Tests the event where structures do not produce resources.
     *
     * The mocked random values force the event with index 1.
     *
     * Expected result:
     * resource modifier becomes 0 and score modifier stays 1.
     */
    @Test
    void testEventZeroProduction() {

        mockRandom(10, 1);

        String result = events.triggerEvent();

        assertEquals("Estruturas não produzem Recursos!!!", result);
        assertEquals(0, events.getResourceModifier());
        assertEquals(1, events.getScoreModifier());
    }

    /**
     * Tests the event where structures produce double resources.
     *
     * The mocked random values force the event with index 2.
     *
     * Expected result:
     * resource modifier becomes 2 and score modifier stays 1.
     */
    @Test
    void testEventDoubleProduction() {

        mockRandom(10, 2);

        String result = events.triggerEvent();

        assertEquals("Estruturas Produzem o dobro de Recursos", result);
        assertEquals(2, events.getResourceModifier());
        assertEquals(1, events.getScoreModifier());
    }

    /**
     * Tests the event where structures do not give score.
     *
     * The mocked random values force an event from the default case.
     *
     * Expected result:
     * resource modifier stays 1 and score modifier becomes 0.
     */
    @Test
    void testEventZeroScore() {
        mockRandom(10, 10);

        String result = events.triggerEvent();

        assertEquals("Estruturas não dão Pontuação!!!", result);
        assertEquals(1, events.getResourceModifier());
        assertEquals(0, events.getScoreModifier());
    }

    /**
     * Tests the event where structures give double score.
     *
     * The mocked random values force the event with index 4.
     *
     * Expected result:
     * resource modifier stays 1 and score modifier becomes 2.
     */
    @Test
    void testEventDoubleScore() {

        mockRandom(10, 4);

        String result = events.triggerEvent();

        assertEquals("Estruturas dão o dobro de Pontuação!!!", result);
        assertEquals(1, events.getResourceModifier());
        assertEquals(2, events.getScoreModifier());
    }

    /**
     * Tests the general strike event.
     *
     * The mocked random values force the event with index 3.
     *
     * Expected result:
     * resource modifier becomes 0 and score modifier becomes 0.
     */
    @Test
    void testEventGeneralStrike() {

        mockRandom(10, 3);

        String result = events.triggerEvent();

        assertEquals("Greve Geral", result);
        assertEquals(0, events.getResourceModifier());
        assertEquals(0, events.getScoreModifier());
    }

    /**
     * Tests if the modifiers are reset every time triggerEvent is called.
     *
     * First, an event is forced to change the modifiers.
     * Then, a second call forces no event to occur.
     * The modifiers should return to their default values.
     *
     * Expected result:
     * resource modifier returns to 1 and score modifier returns to 1.
     */
    @Test
    void testModifiersAreResetOnEachCall() {

        mockRandom(10, 0);
        events.triggerEvent();

        assertEquals(2, events.getResourceModifier());

        mockRandom(80, 0);
        events.triggerEvent();

        assertEquals(1, events.getResourceModifier(), "Os modificadores deviam ter reiniciado para 1");
        assertEquals(1, events.getScoreModifier(), "Os modificadores deviam ter reiniciado para 1");
    }

    /**
     * Replaces the random object used by Events with a mocked Random object.
     *
     * The first call to nextInt returns firstReturn.
     * This value is used to decide if an event occurs.
     *
     * The second call to nextInt returns secondReturn.
     * This value is used to choose the specific event.
     *
     * @param firstReturn value returned on the first call to nextInt
     * @param secondReturn value returned on the second and following calls to nextInt
     */
    private void mockRandom(int firstReturn, int secondReturn) {
        Events.random = new Random() {
            private int callCount = 0;

            @Override
            public int nextInt(int bound) {
                callCount++;
                if (callCount == 1) {
                    return firstReturn;
                }
                return secondReturn;
            }
        };
    }
}