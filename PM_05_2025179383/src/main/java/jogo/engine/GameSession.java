package jogo.engine;

import jogo.models.Player;

public class GameSession {

    private WorldMap map;
    private Player player1;
    private Player player2;
    private Player actualPlayer;
    private Player opponent;

    private Events events;

    private int day;
    private final int TOTAL_DAYS = 10;

    private int scoreModifier;
    private int resourceModifier;

    private boolean playerOneTurn;

    public GameSession() {
        this.map = new WorldMap();
        this.events = new Events();
        this.day = 1;
        this.scoreModifier = 1;
        this.resourceModifier = 1;
        this.playerOneTurn = true;
    }

    public void startNewGame(String player1Name, String player2Name) {
        this.map = new WorldMap();

        this.player1 = new Player(player1Name);
        this.player2 = new Player(player2Name);

        this.actualPlayer = player1;
        this.opponent = player2;

        this.day = 1;
        this.playerOneTurn = true;

        this.events = new Events();
        this.scoreModifier = 1;
        this.resourceModifier = 1;
    }

    public void loadGame(WorldMap map, Player player1, Player player2, int day) {
        this.map = map;

        this.player1 = player1;
        this.player2 = player2;

        this.actualPlayer = player1;
        this.opponent = player2;

        this.day = day;
        this.playerOneTurn = true;

        this.events = new Events();
        this.scoreModifier = 1;
        this.resourceModifier = 1;
    }

    public String startDayEvent() {
        String eventMessage = events.triggerEvent();

        this.scoreModifier = events.getScoreModifier();
        this.resourceModifier = events.getResourceModifier();

        return eventMessage;
    }

    public String endTurn() {
        actualPlayer.resetAP();

        if (playerOneTurn) {
            actualPlayer = player2;
            opponent = player1;
            playerOneTurn = false;

            return null;
        }

        map.generateResources(resourceModifier);
        map.consumeResources();

        day++;

        actualPlayer = player1;
        opponent = player2;
        playerOneTurn = true;

        if (!isGameOver()) {
            return startDayEvent();
        }

        return null;
    }

    public Player getWinner() {
        if (!isGameOver()) {
            return null;
        }

        if (player1.getScore() > player2.getScore()) {
            return player1;
        }

        if (player2.getScore() > player1.getScore()) {
            return player2;
        }

        return null;
    }

    public boolean isGameOver() {
        return day > TOTAL_DAYS;
    }

    public WorldMap getMap() {
        return map;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public Player getOpponent() {
        return opponent;
    }

    public int getDay() {
        return day;
    }

    public int getTOTAL_DAYS() {
        return TOTAL_DAYS;
    }

    public int getScoreModifier() {
        return scoreModifier;
    }

    public int getResourceModifier() {
        return resourceModifier;
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }



}