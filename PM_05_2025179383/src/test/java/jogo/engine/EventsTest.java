package jogo.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class EventsTest {

    private Events events;

    @BeforeEach
    void setUp() {
        events = new Events();
    }

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

    @Test
    void testEventDoubleEverything() {

        mockRandom(10, 0);

        String result = events.triggerEvent();

        assertEquals("Estruturas Produzem o dobro de Recursos e de Pontuação!!!", result);
        assertEquals(2, events.getResourceModifier());
        assertEquals(2, events.getScoreModifier());
    }

    @Test
    void testEventZeroProduction() {

        mockRandom(10, 1);

        String result = events.triggerEvent();

        assertEquals("Estruturas não produzem Recursos!!!", result);
        assertEquals(0, events.getResourceModifier());
        assertEquals(1, events.getScoreModifier());
    }

    @Test
    void testEventDoubleProduction() {

        mockRandom(10, 6);

        String result = events.triggerEvent();

        assertEquals("Estruturas Produzem o dobro de Recursos", result);
        assertEquals(2, events.getResourceModifier());
        assertEquals(1, events.getScoreModifier());
    }

    @Test
    void testEventZeroScore() {
        mockRandom(10, 10);

        String result = events.triggerEvent();

        assertEquals("Estruturas não dão Pontuação!!!", result);
        assertEquals(1, events.getResourceModifier());
        assertEquals(0, events.getScoreModifier());
    }

    @Test
    void testEventDoubleScore() {

        mockRandom(10, 11);

        String result = events.triggerEvent();

        assertEquals("Estruturas dão o dobro de Pontuação!!!", result);
        assertEquals(1, events.getResourceModifier());
        assertEquals(2, events.getScoreModifier());
    }

    @Test
    void testEventGeneralStrike() {

        mockRandom(10, 12);

        String result = events.triggerEvent();

        assertEquals("Greve Geral", result);
        assertEquals(2, events.getResourceModifier());
        assertEquals(0, events.getScoreModifier());
    }

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
