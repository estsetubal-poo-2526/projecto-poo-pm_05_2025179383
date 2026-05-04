package jogo.io;

import jogo.models.Player;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public int showPrincipalMenu(int min, int max) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Novo Jogo");
            System.out.println("2 - Carregar Jogo Guardado");
            System.out.println("3 - Sair");
            return readInteger("> ",min,max);


    }

    public int showActionMenu(int min, int max, Player actualPlayer) {
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
        return readInteger("> ", min, max);
    }

    public int showStructuresCreate(int min, int max) {
        System.out.println("Estruturas: ");
        System.out.println("1 - Floresta\n" +
                "2 - Mina\n" +
                "3 - Rancho\n" +
                "4 - Cidade");
        return readInteger("> ",min,max);
    }

    public int showSearchResourcesMenu(int min, int max) {
        System.out.println("Recursos: ");
        System.out.println("1 - Madeira\n" +
                "2 - Pedra\n" +
                "3 - Comida");

        return readInteger("> ", min, max);
    }



    public int readInteger(String msg, int min, int max) {
        int value;
        while (true) {
            System.out.print(msg);

            // 1. Verifica se é um número
            if (!scanner.hasNextInt()) {
                showMsg("Erro: Introduza um número válido.");
                scanner.next(); // Limpa o buffer
                continue;
            }

            value = scanner.nextInt();
            scanner.nextLine();


            if (value >= min && value <= max) {
                return value;
            } else {
                showMsg("Erro: Escolha um número entre " + min + " e " + max + ".");
            }
        }
    }

    public String readString(String msg) {
            System.out.print(msg);
            return scanner.nextLine();
    }
    public void showMsg(String msg){
        System.out.println("[Jogo]: " + msg);
    }
}
