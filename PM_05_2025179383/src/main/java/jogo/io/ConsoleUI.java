package jogo.io;

import jogo.models.Player;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner;


    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE_BOLD = "\u001B[1;37m";

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public int showPrincipalMenu(int min, int max) {
        System.out.println(WHITE_BOLD + "\n=== MENU PRINCIPAL ===" + RESET);
        System.out.println("1 - Novo Jogo");
        System.out.println("2 - Carregar Jogo Guardado");
        System.out.println("3 - Sair");
        return readInteger("> ", min, max);
    }

    public int showActionMenu(int min, int max, Player actualPlayer) {
        System.out.println("\n--- Ações de " + GREEN + actualPlayer.getName() + RESET +
                " (AP: " + YELLOW + actualPlayer.getActionPoints() + RESET + ") ---");
        System.out.println(" 1 - Ver Mapa\n" +
                " 2 - Construir Estruturas\n" +
                " 3 - Informações de Estrutura\n" +
                " 4 - Terminar Turno\n" +
                " 5 - Ver Inventário\n" +
                " 6 - Melhorar Estrutura\n" +
                " 7 - Encontrar Recursos\n" +
                " 8 - Guardar Jogo\n" +
                " 9 - Sair");
        return readInteger("> ", min, max);
    }

    public void showMsg(String msg) {
        // Se a mensagem contiver "Erro" ou "Falha", pomos a vermelho, senão usamos ciano
        String color = (msg.toLowerCase().contains("erro") || msg.toLowerCase().contains("falha")) ? RED : CYAN;
        System.out.println(color + "[Jogo]: " + msg + RESET);
    }

    public int showStructuresCreate(int min, int max) {
        System.out.println(BLUE + "Estruturas Disponíveis:" + RESET);
        System.out.println("1 - Floresta\n" +
                "2 - Mina\n" +
                "3 - Rancho\n" +
                "4 - Cidade");
        return readInteger("> ", min, max);
    }

    public int showSearchResourcesMenu(int min, int max) {
        System.out.println(BLUE + "Recursos para procurar:" + RESET);
        System.out.println("1 - Madeira\n" +
                "2 - Pedra\n" +
                "3 - Comida");
        return readInteger("> ", min, max);
    }

    public String readString(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }

    public int readInteger(String msg, int min, int max) {
        int value;
        while (true) {
            System.out.print(msg);

            if (!scanner.hasNextInt()) {
                showMsg("Erro: Introduza um número válido.");
                scanner.next(); // Limpa o "lixo" do buffer
                continue;
            }

            value = scanner.nextInt();
            scanner.nextLine(); // Limpa o resto da linha

            // O teu "cheat" de 999 continua aqui, infelizmente
            if ((value >= min && value <= max) || value == 999) {
                return value;
            } else {
                showMsg("Erro: Escolha um número entre " + min + " e " + max + ".");
            }
        }
    }
}