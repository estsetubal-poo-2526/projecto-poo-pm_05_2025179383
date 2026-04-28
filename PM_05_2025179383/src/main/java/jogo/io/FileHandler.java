package jogo.io;

import jogo.engine.WorldMap;
import jogo.models.*;
import jogo.models.Structures.*;

import java.io.*;

public class FileHandler {
    private static final String FILE_NAME = "savegame.csv";

    public static void saveFullGame(WorldMap map, Player p1, Player p2) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            // Jogadores
            writer.println("#PLAYERS");
            savePlayer(writer, p1);
            savePlayer(writer, p2);

            // Estruturas
            writer.println("#STRUCTURES");
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    Structures s = map.getStructure(i, j);
                    if (s != null) {

                        writer.println(s.getClass().getSimpleName() + "," + i + "," + j + "," +
                                s.getLevel() + "," + s.getOwner().getName());
                    }
                }
            }
            System.out.println("Jogo Guardado");
        } catch (IOException e) {
            System.out.println("Ocorreu um Erro: " + e.getMessage());
        }
    }

    private static void savePlayer(PrintWriter writer, Player p) {
        // Nome, Score, BaseAP, Wood, Stone, Food, CurrentAP
        writer.println(p.getName() + "," +
                p.getScore() + "," +
                p.getBaseAp() + "," +
                p.getResourceQuantity(ResourceType.WOOD) + "," +
                p.getResourceQuantity(ResourceType.STONE) + "," +
                p.getResourceQuantity(ResourceType.FOOD) + "," +
                p.getResourceQuantity(ResourceType.ACTION_POINTS));
    }

    public static SaveData loadGame() {
        WorldMap map = new WorldMap();
        Player p1 = null;
        Player p2 = null;
        String line;
        String section = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    section = line;
                    continue;
                }

                String[] data = line.split(",");

                if (section.equals("#PLAYERS")) {
                    // Nome, Score, BaseAP, Wood, Stone, Food, CurrentAP
                    Player p = new Player(data[0]);
                    p.addScore(Integer.parseInt(data[1]));
                    // Precisas de um método no jogo.models.Player para definir o BaseAP ou ajustá-lo
                    p.addActionPoints(Integer.parseInt(data[2]) - 10);
                    p.addResource(ResourceType.WOOD, Integer.parseInt(data[3]) - 10);
                    p.addResource(ResourceType.STONE, Integer.parseInt(data[4]) - 10);
                    p.addResource(ResourceType.FOOD, Integer.parseInt(data[5]) - 10);
                    // Define o AP atual
                    p.removeResource(ResourceType.ACTION_POINTS, p.getActionPoints());
                    p.addResource(ResourceType.ACTION_POINTS, Integer.parseInt(data[6]));

                    if (p1 == null) p1 = p; else p2 = p;

                } else if (section.equals("#STRUCTURES")) {
                    // Tipo, X, Y, Nivel, Dono
                    String type = data[0];
                    int x = Integer.parseInt(data[1]);
                    int y = Integer.parseInt(data[2]);
                    int level = Integer.parseInt(data[3]);
                    String ownerName = data[4];

                    Player owner = (p1.getName().equals(ownerName)) ? p1 : p2;
                    Structures s = CreateStructure.create(type, owner);


                    if (s != null) {
                        s.setLevel(level); // Força o nível guardado
                        map.addStructure(s, x, y);
                    }

                }
            }
            System.out.println("Jogo carregado. Tenta não perder desta vez.");
            return new SaveData(map, p1, p2);
        } catch (Exception e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
            return null;
        }
    }

    // Classe auxiliar para transportar os dados
    public static class SaveData {
        public WorldMap map;
        public Player p1, p2;
        public SaveData(WorldMap m, Player player1, Player player2) {
            this.map = m; this.p1 = player1; this.p2 = player2;
        }
    }
}