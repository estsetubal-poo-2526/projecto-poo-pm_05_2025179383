package jogo.engine;

import java.util.Scanner;
import java.util.InputMismatchException;

import jogo.models.*;
import jogo.models.Structures.*;
import jogo.io.*;

public class GameEngine {

    public static void menuGame() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Novo Jogo");
            System.out.println("2 - Carregar Jogo Guardado");
            System.out.println("3 - Sair");
            System.out.print("> ");

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Introduz um Numero");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    startGame(new WorldMap(), new Player("Fabio"), new Player("Tiago"));
                    break;
                case 2:
                    FileHandler.SaveData loaded = FileHandler.loadGame();
                    if (loaded != null) {
                        startGame(loaded.map, loaded.p1, loaded.p2);
                    } else {
                        System.out.println("Erro: Não foi possível carregar o ficheiro.");
                    }
                    break;
                case 3:
                    System.out.println("A fechar o jogo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (choice != 3);
    }

    public static void startGame(WorldMap map, Player player1, Player player2) {
        Scanner scanner = new Scanner(System.in);
        final int TOTAL_DAYS = 10;
        int day = 0;

        while (day < TOTAL_DAYS) {
            System.out.println("\n---------------------- DIA " + (day + 1) + " ----------------------------------");

            Events.triggerEvent();

            System.out.println("-------------------- Turno de " + player1.getName() + " --------------------------");
            if (!menu(map, player1, player2, scanner, true)) return;
            player1.resetAC();

            System.out.println("-------------------- Turno de " + player2.getName() + " --------------------------");
            if (!menu(map, player2, player1, scanner, false)) return;
            player2.resetAC();

            map.generateResources();
            map.consumeResources();
            day++;
        }


        System.out.println("\n----------------- Fim de Jogo ---------------");
        System.out.printf("Pontos de %s : %d\nPontos de %s : %d\n",
                player1.getName(), player1.getScore(), player2.getName(), player2.getScore());

        if (player1.getScore() == player2.getScore()) {
            System.out.println("Empate!");
        } else {
            Player winner = player1.getScore() > player2.getScore() ? player1 : player2;
            System.out.println("Vencedor: " + winner.getName());
        }
    }

    public static boolean menu(WorldMap map, Player actualPlayer, Player opponent, Scanner scanner, boolean isPlayerOne) {
        boolean turnContinues = true;

        while (turnContinues) {
            System.out.println("\n--- Ações de " + actualPlayer.getName() + " (AP: " + actualPlayer.getActionPoints() + ") ---");
            System.out.println("" +
                    " 1 - Ver Mapa\n" +
                    " 2 - Construir Estruturas\n" +
                    " 3 - Informações de Estrutura\n" +
                    " 4 - Terminar Turno\n" +
                    " 5 - Ver Inventário\n" +
                    " 6 - Melhorar Estrutura\n" +
                    " 7 - Encontrar Recursos\n" +
                    " 8 - Guardar Jogo\n" +
                    " 9 - Sair do Jogo");
            System.out.print("> ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Introduz um número de 1 a 9.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    map.printMap();
                    break;
                case 2:
                    System.out.print("Tipo (Floresta/Mina/Cidade/Rancho): ");
                    String type = scanner.next();
                    createStructure(map, actualPlayer, type, correctCoordinates("x", scanner), correctCoordinates("y", scanner));
                    break;
                case 3:
                    map.printMap();
                    showStructureInfo(map, scanner);
                    break;
                case 4:
                    turnContinues = false;
                    break;
                case 5:
                    System.out.println("Inventário: " + actualPlayer.getInventory());
                    break;
                case 6:
                    map.printMap();
                    performUpgrade(map, actualPlayer, scanner);
                    break;
                case 7:
                    searchResources(actualPlayer, scanner);
                    break;
                case 8:
                    // Mantém a ordem original dos jogadores no ficheiro
                    if (isPlayerOne) FileHandler.saveFullGame(map, actualPlayer, opponent);
                    else FileHandler.saveFullGame(map, opponent, actualPlayer);
                    break;
                case 9:
                    System.out.print("Sair sem guardar? (1-Sim / 0-Não): ");
                    if (scanner.nextInt() == 1) return false;
                    break;
                case 999:
                    actualPlayer.addActionPoints(1000);
                    break;
            }
        }
        return true;
    }

    private static void showStructureInfo(WorldMap map, Scanner scanner) {
        int x = correctCoordinates("x", scanner);
        int y = correctCoordinates("y", scanner);
        Structures s = map.getStructure(x, y);
        if (s != null) {
            System.out.println("Estrutura: " + s);
            System.out.println("Dono: " + s.getOwner().getName());
        } else {
            System.out.println("Espaço vazio.");
        }
    }

    private static void performUpgrade(WorldMap map, Player player, Scanner scanner) {
        int x = correctCoordinates("x", scanner);
        int y = correctCoordinates("y", scanner);
        Structures s = map.getStructure(x, y);

        if (s != null) {
            if (map.canInteract(x, y, player)) {
                if (player.getActionPoints() >= 10) {
                    if (s.upgradeStructure()) {
                        player.removeResource(ResourceType.ACTION_POINTS, 10);
                    }
                } else System.out.println("AP insuficiente.");
            } else System.out.println("Esta estrutura não te pertence.");
        } else System.out.println("Não existem estruturas aqui.");
    }

    private static void searchResources(Player player, Scanner scanner) {
        int res = -1;
        while (res < 1 || res > 3) {
            try {
                System.out.print("1-Wood, 2-Stone, 3-Food: ");
                res = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Número inválido.");
                scanner.nextLine();
            }
        }

        if (player.getActionPoints() >= 4) {
            player.removeResource(ResourceType.ACTION_POINTS, 4);
            int gathered = new java.util.Random().nextInt(3) + 2;
            ResourceType type = (res == 1) ? ResourceType.WOOD : (res == 2) ? ResourceType.STONE : ResourceType.FOOD;
            player.addResource(type, gathered);
            System.out.println("Encontraste " + gathered + " unidades.");
        } else System.out.println("AP insuficiente.");
        scanner.nextLine();
    }

    public static void createStructure(WorldMap map, Player player, String structure, int x, int y) {
        if (!map.isNotOccupied(x, y)) {
            System.out.println("O espaço Já está ocupado!");
            return;
        }
        String type = structure.substring(0, 1).toUpperCase() + structure.substring(1).toLowerCase();

        int apCost = CreateStructure.getApCost(type);
        int matCost = CreateStructure.getMaterialCost(type);
        ResourceType matType = CreateStructure.getMaterialType(type);

        if (player.getActionPoints() >= apCost && player.getResourceQuantity(matType) >= matCost) {
            Structures newStr = CreateStructure.create(type, player);
            if (newStr != null) {
                map.addStructure(newStr, x, y);
                player.removeResource(ResourceType.ACTION_POINTS, apCost);
                player.removeResource(matType, matCost);
                System.out.println("Estrutura criada.");
            }
        } else System.out.println("Recursos ou AP insuficientes.");
    }

    public static int correctCoordinates(String axis, Scanner scanner) {
        int val = -1;
        while (true) {
            try {
                System.out.print(axis + " (0-6): ");
                val = scanner.nextInt();
                if (val >= 0 && val <= 6) return val;
                System.out.println("Entre 0 e 6, por favor.");
            } catch (InputMismatchException e) {
                System.out.println("Número inválido.");
                scanner.nextLine();
            }
        }
    }
}