package jogo.engine;

import jogo.models.Player;
import jogo.models.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameSessionTest {

    private GameSession session;

    @BeforeEach
    void setUp() {
        session = new GameSession();
        session.startNewGame("Fabio", "Computer");
    }

    @Test
    void testStartNewGameInitializesCorrectly() {
        assertNotNull(session.getMap(), "O mapa deve ser inicializado.");
        assertEquals("Fabio", session.getPlayer1().getName());
        assertEquals("Computer", session.getPlayer2().getName());
        assertEquals(session.getPlayer1(), session.getActualPlayer(), "O jogo deve começar no Player 1.");
        assertEquals(1, session.getDay(), "O jogo deve começar no dia 1.");
        assertTrue(session.isPlayerOneTurn(), "Deve ser o turno do Player 1.");
    }

    @Test
    void testLoadGameRestoresState() {
        WorldMap customMap = new WorldMap();
        Player p1 = new Player("Custom1");
        Player p2 = new Player("Custom2");
        int savedDay = 5;

        session.loadGame(customMap, p1, p2, savedDay);

        assertEquals(customMap, session.getMap());
        assertEquals(p1, session.getPlayer1());
        assertEquals(p2, session.getPlayer2());
        assertEquals(savedDay, session.getDay());
        assertTrue(session.isPlayerOneTurn(), "Por defeito, o load coloca o turno no Player 1.");
    }

    @Test
    void testStartDayEventUpdatesModifiers() {
        // Dispara o evento e verifica se os modificadores internos são atualizados com valores válidos (0, 1 ou 2)
        String msg = session.startDayEvent();
        assertNotNull(msg, "O evento deve devolver uma mensagem informativa.");

        int resMod = session.getResourceModifier();
        int scoreMod = session.getScoreModifier();

        assertTrue(resMod == 0 || resMod == 1 || resMod == 2, "Modificador de recursos inválido.");
        assertTrue(scoreMod == 0 || scoreMod == 1 || scoreMod == 2, "Modificador de score inválido.");
    }

    @Test
    void testEndTurnFromPlayerOneToPlayerTwo() {
        Player p1 = session.getActualPlayer();

        // Simula gasto de AP para testar o resetAP no final do turno
        p1.removeResource(ResourceType.ACTION_POINTS, 5);

        String resultMessage = session.endTurn();

        assertNull(resultMessage, "Mudar do Player 1 para o Player 2 não deve disparar mensagens de novo dia.");
        assertEquals(session.getPlayer2(), session.getActualPlayer(), "O jogador atual deve passar a ser o Player 2.");
        assertEquals(p1.getBaseActionPoints(), p1.getActionPoints(), "Os Action Points do Player 1 deviam ter sido repostos.");
        assertFalse(session.isPlayerOneTurn(), "Já não deve ser o turno do Player 1.");
        assertEquals(1, session.getDay(), "O dia não deve mudar enquanto o Player 2 não jogar.");
    }

    @Test
    void testEndTurnFromPlayerTwoTriggersNewDay() {
        // Passa o turno do Player 1 para o Player 2
        session.endTurn();

        int dayBefore = session.getDay();

        // Player 2 termina o turno dele -> Fecha o dia
        String eventMessage = session.endTurn();

        assertEquals(dayBefore + 1, session.getDay(), "O dia deve ter incrementado.");
        assertEquals(session.getPlayer1(), session.getActualPlayer(), "O turno deve voltar para o Player 1.");
        assertTrue(session.isPlayerOneTurn(), "Deve voltar a ser o turno do Player 1.");
        assertNotNull(eventMessage, "A transição de dia deve despoletar um evento diário.");
    }

    @Test
    void testGameOverAndWinnerDetermination() {
        // Vamos forçar o avanço dos dias completando os turnos consecutivamente
        // Cada dia precisa de 2 chamadas ao endTurn() (p1 e p2)
        int totalTurnsToLoop = session.getTOTAL_DAYS() * 2;

        for (int i = 0; i < totalTurnsToLoop; i++) {
            session.endTurn();
        }

        assertTrue(session.isGameOver(), "O jogo deveria estar terminado após os turnos regulamentares.");
        assertNull(session.endTurn(), "Chamar endTurn com o jogo terminado não deve processar mais dias.");

        // Força pontuações para testar o cálculo do vencedor
        session.getPlayer1().addScore(50);
        session.getPlayer2().addScore(30);
        assertEquals(session.getPlayer1(), session.getWinner(), "O Player 1 devia ser o vencedor com mais pontos.");

        session.getPlayer2().addScore(30); // Player 2 passa a ter 60
        assertEquals(session.getPlayer2(), session.getWinner(), "O Player 2 devia ser o vencedor após a ultrapassagem.");

        session.getPlayer1().addScore(10); // Empate a 60
        assertNull(session.getWinner(), "Em caso de empate, o vencedor deve retornar null.");
    }
}