package jogo.engine;

import jogo.io.ConsoleUI;
import jogo.io.FileHandler;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.CreateStructure;
import jogo.models.Structures.Structures;
import jogo.models.Structures.StructuresType;

public class GameEngine {

    private static final ConsoleUI consoleUI = new ConsoleUI();

    public static void menuGame() {

        int choice = consoleUI.showPrincipalMenu(1, 3);

        switch (choice) {
            case 1:
                startGame(new WorldMap(), new Player(consoleUI.readString("Jogador 1: ")), new Player(consoleUI.readString("Jogador 2:")));
                break;
            case 2:
                loadSave();
                break;
            case 3:
                consoleUI.showMsg("A encerrar o jogo... ");
                break;
            default:
                consoleUI.showMsg("Opção Invalida! ");
        }
    }

    public static void startGame(WorldMap map, Player player1, Player player2) {

        final int TOTAL_DAYS = 10;
        int day = 0;

        while (day < TOTAL_DAYS) {
            consoleUI.showMsg("---------------------- DIA " + (day + 1) + " ----------------------------------");
            Events.triggerEvent();

            consoleUI.showMsg("-------------------- Turno de " + player1.getName() + " --------------------------");
            if (!menu(map, player1, player2, true)) return;
            player1.resetAC();

            consoleUI.showMsg("-------------------- Turno de " + player2.getName() + " --------------------------");
            if (!menu(map, player2, player1, false)) return;
            player2.resetAC();

            map.generateResources();
            map.consumeResources();
            day++;
        }


        consoleUI.showMsg("----------------- Fim de Jogo ---------------");
        System.out.printf("Pontos de %s : %d\nPontos de %s : %d\n",
                player1.getName(), player1.getScore(), player2.getName(), player2.getScore());

        if (player1.getScore() == player2.getScore()) {
            consoleUI.showMsg("Empate!");
        } else {
            Player winner = player1.getScore() > player2.getScore() ? player1 : player2;
            consoleUI.showMsg("Vencedor: " + winner.getName());
        }
    }

    public static boolean menu(WorldMap map, Player actualPlayer, Player opponent, boolean isPlayerOne) {
        boolean turnContinues = true;

        while (turnContinues) {

            int choice = consoleUI.showActionMenu(1, 9, actualPlayer);

            switch (choice) {
                case 1:
                    map.printMap();
                    break;
                case 2:
                    StructuresType type = getStructureByMenu(consoleUI.showStructuresCreate(1,4));

                    createStructure(map, actualPlayer, type, correctCoordinates("x"), correctCoordinates("y"));
                    break;
                case 3:
                    map.printMap();
                    showStructureInfo(map);
                    break;
                case 4:
                    turnContinues = false;
                    break;
                case 5:
                    consoleUI.showMsg("Inventário: " + actualPlayer.getInventory());
                    break;
                case 6:
                    map.printMap();
                    performUpgrade(map, actualPlayer);
                    break;
                case 7:
                    searchResources(actualPlayer);
                    break;
                case 8:

                    if (isPlayerOne) FileHandler.saveFullGame(map, actualPlayer, opponent);
                    else FileHandler.saveFullGame(map, opponent, actualPlayer);
                    break;
                case 9:
                    System.out.print("Sair sem guardar? (1-Sim / 0-Não): ");
                    if (consoleUI.readInteger("> ", 0, 1) == 1) return false;
                    break;
                case 999:
                    actualPlayer.addActionPoints(1000);
                    break;
            }
        }
        return true;
    }

    private static void showStructureInfo(WorldMap map) {
        int x = correctCoordinates("x");
        int y = correctCoordinates("y");
        Structures s = map.getStructure(x, y);
        if (s != null) {
            consoleUI.showMsg("Estrutura: " + s);
            consoleUI.showMsg("Dono: " + s.getOwner().getName());
        } else {
            consoleUI.showMsg("Espaço vazio.");
        }
    }

    private static void performUpgrade(WorldMap map, Player player) {
        int x = correctCoordinates("x");
        int y = correctCoordinates("y");
        Structures s = map.getStructure(x, y);

        if (s != null) {
            if (map.canInteract(x, y, player)) {
                if (player.getActionPoints() >= 10) {
                    if (s.upgradeStructure()) {
                        player.removeResource(ResourceType.ACTION_POINTS, 10);
                    }
                } else consoleUI.showMsg("AP insuficiente.");
            } else consoleUI.showMsg("Esta estrutura não te pertence.");
        } else consoleUI.showMsg("Não existem estruturas aqui.");
    }

    private static void searchResources(Player player) {
        int res = consoleUI.showSearchResourcesMenu(1,3);

        if (player.getActionPoints() >= 4) {
            player.removeResource(ResourceType.ACTION_POINTS, 4);
            int gathered = new java.util.Random().nextInt(3) + 2;
            ResourceType type = (res == 1) ? ResourceType.WOOD : (res == 2) ? ResourceType.STONE : ResourceType.FOOD;
            player.addResource(type, gathered);
            consoleUI.showMsg("Encontraste " + gathered + " unidades.");
        } else consoleUI.showMsg("AP insuficiente.");
    }

    public static void createStructure(WorldMap map, Player player, StructuresType structure, int x, int y) {
        if (!map.isNotOccupied(x, y)) {
            consoleUI.showMsg("O espaço Já está ocupado!");
            return;
        }

        int apCost = CreateStructure.getApCost(structure);
        int matCost = CreateStructure.getMaterialCost(structure);
        ResourceType matType = CreateStructure.getMaterialType(structure);

        if (player.getActionPoints() >= apCost && player.getResourceQuantity(matType) >= matCost) {
            Structures newStr = CreateStructure.create(structure, player);
            if (newStr != null) {
                map.addStructure(newStr, x, y);
                player.removeResource(ResourceType.ACTION_POINTS, apCost);
                player.removeResource(matType, matCost);
                consoleUI.showMsg("Estrutura criada.");
            }
        } else consoleUI.showMsg("Recursos ou AP insuficientes.");
    }

    public static int correctCoordinates(String axis) {
        return consoleUI.readInteger(axis + "> ", 0, 6);
    }

    private static StructuresType getStructureByMenu(int val) {
        switch (val){
            case 1:
                return StructuresType.FOREST;
            case 2:
                return StructuresType.MINE;
            case 3:
                return StructuresType.RANCH;
            case 4:
                return StructuresType.CITY;
        }

        return null;
    }

    private static void loadSave(){
        FileHandler.SaveData loaded = FileHandler.loadGame();
        if (loaded != null) {
            startGame(loaded.map, loaded.p1, loaded.p2);
        } else {
            consoleUI.showMsg("Erro: Save não Encontrado! ");
        }
    }
}
