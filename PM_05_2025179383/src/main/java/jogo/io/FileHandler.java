package jogo.io;

import jogo.engine.WorldMap;
import jogo.models.*;
import jogo.models.Structures.*;
import jogo.exceptions.*;
import java.io.*;

public class FileHandler {
    private static final String FILE_NAME = "savegame.csv";

    public static void saveFullGame(WorldMap map, Player p1, Player p2) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println("#PLAYERS");
            savePlayer(writer, p1);
            savePlayer(writer, p2);

            writer.println("#STRUCTURES");
            // Usa os getters do mapa em vez de números fixos!
            for (int i = 0; i < map.getCOLUMN_SIZE(); i++) {
                for (int j = 0; j < map.getLINE_SIZE(); j++) {
                    try {
                        Structures s = map.getStructure(i, j);
                        if (s != null) {
                            writer.println(s.getClass().getSimpleName().toUpperCase() + "," + i + "," + j + "," +
                                    s.getLevel() + "," + s.getOwner().getName());
                        }
                    } catch (GameException ignored) {}
                }
            }
        }
    }

    private static void savePlayer(PrintWriter writer, Player p) {
        writer.println(p.getName() + "," +
                p.getScore() + "," +
                p.getResourceQuantity(ResourceType.WOOD) + "," +
                p.getResourceQuantity(ResourceType.STONE) + "," +
                p.getResourceQuantity(ResourceType.FOOD) + "," +
                p.getResourceQuantity(ResourceType.ACTION_POINTS));
    }

    public static SaveData loadGame() throws GameException {
        WorldMap map = new WorldMap();
        Player p1 = null;
        Player p2 = null;
        String line;
        String section = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("#")) {
                    section = line;
                    continue;
                }

                String[] data = line.split(",");

                if (section.equals("#PLAYERS")) {
                    Player p = new Player(
                            data[0],
                            Integer.parseInt(data[1]),
                            Integer.parseInt(data[2]),
                            Integer.parseInt(data[3]),
                            Integer.parseInt(data[4]),
                            Integer.parseInt(data[5])
                    );

                    if (p1 == null) p1 = p; else p2 = p;

                } else if (section.equals("#STRUCTURES")) {
                    StructuresType type = StructuresType.valueOf(data[0]);
                    int x = Integer.parseInt(data[1]);
                    int y = Integer.parseInt(data[2]);
                    int level = Integer.parseInt(data[3]);
                    Player owner = (p1 != null && p1.getName().equals(data[4])) ? p1 : p2;

                    // Criamos com modificador 1 (neutro) para não inflacionar o score no load
                    Structures s = CreateStructure.create(type, owner, 1);

                    if (s != null) {
                        s.setLevel(level); // Define o nível diretamente sem gastar recursos
                        map.addStructure(s, x, y);
                    }
                }
            }
            return new SaveData(map, p1, p2);
        } catch (IOException | IllegalArgumentException e) {
            throw new GameException("Erro ao processar ficheiro de Save: " + e.getMessage());
        }
    }

    public static class SaveData {
        private WorldMap map;
        private Player p1, p2;
        private SaveData(WorldMap m, Player player1, Player player2) {
            this.map = m; this.p1 = player1; this.p2 = player2;
        }

        public Player getP1() {
            return p1;
        }

        public Player getP2() {
            return p2;
        }

        public WorldMap getMap() {
            return map;
        }
    }
}