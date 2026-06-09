package jogo.io;

import jogo.engine.WorldMap;
import jogo.models.*;
import jogo.models.Structures.*;
import jogo.exceptions.*;

import java.io.*;

public class FileHandler {

    private static final String FILE_NAME = "savegame.csv";

    public static void saveFullGame(WorldMap map, Player p1, Player p2, int day) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {

            writer.println("#GAME");
            writer.println(day);

            writer.println("#PLAYERS");
            savePlayer(writer, p1);
            savePlayer(writer, p2);

            writer.println("#STRUCTURES");

            for (int i = 0; i < map.getCOLUMN_SIZE(); i++) {
                for (int j = 0; j < map.getLINE_SIZE(); j++) {
                    try {
                        Structures s = map.getStructure(i, j);

                        if (s != null) {
                            writer.println(
                                    s.getClass().getSimpleName().toUpperCase() + "," +
                                            i + "," +
                                            j + "," +
                                            s.getLevel() + "," +
                                            s.getOwner().getName()
                            );
                        }

                    } catch (GameException ignored) {
                    }
                }
            }
        }
    }

    private static void savePlayer(PrintWriter writer, Player p) {
        writer.println(
                p.getName() + "," +
                        p.getScore() + "," +
                        p.getResourceQuantity(ResourceType.WOOD) + "," +
                        p.getResourceQuantity(ResourceType.STONE) + "," +
                        p.getResourceQuantity(ResourceType.FOOD) + "," +
                        p.getResourceQuantity(ResourceType.ACTION_POINTS)
        );
    }

    public static SaveData loadGame() throws GameException {
        WorldMap map = new WorldMap();
        Player p1 = null;
        Player p2 = null;
        int day = 1;

        String line;
        String section = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                if (line.startsWith("#")) {
                    section = line;
                    continue;
                }

                String[] data = line.split(",");

                if (section.equals("#GAME")) {
                    day = Integer.parseInt(data[0]);

                } else if (section.equals("#PLAYERS")) {
                    
                    
                    String name = data[0];
                    int score = Integer.parseInt(data[1]);
                    int wood = Integer.parseInt(data[2]);
                    int stone = Integer.parseInt(data[3]);
                    int food = Integer.parseInt(data[4]);
                    int ap = Integer.parseInt(data[5]);

                    
                    
                    Player p = new Player(name, score, wood, stone, food, ap);

                    if (p1 == null) {
                        p1 = p;
                    } else {
                        p2 = p;
                    }

                } else if (section.equals("#STRUCTURES")) {
                    StructuresType type = StructuresType.valueOf(data[0]);
                    int x = Integer.parseInt(data[1]);
                    int y = Integer.parseInt(data[2]);
                    int level = Integer.parseInt(data[3]);

                    Player owner = getOwnerByName(data[4], p1, p2);
                    Structures s = CreateStructure.create(type, owner, 1);

                    if (s != null) {
                        s.setLevel(level);
                        map.addStructure(s, x, y);
                    }
                }
            }

            return new SaveData(map, p1, p2, day);

        } catch (IOException | IllegalArgumentException e) {
            throw new GameException("Erro ao processar ficheiro de Save: " + e.getMessage());
        }
    }

    private static Player getOwnerByName(String name, Player p1, Player p2) throws GameException {
        if (p1 != null && p1.getName().equals(name)) {
            return p1;
        }

        if (p2 != null && p2.getName().equals(name)) {
            return p2;
        }

        throw new GameException("Dono da estrutura não encontrado: " + name);
    }

    public static class SaveData {

        private final WorldMap map;
        private final Player p1;
        private final Player p2;
        private final int day;

        private SaveData(WorldMap map, Player p1, Player p2, int day) {
            this.map = map;
            this.p1 = p1;
            this.p2 = p2;
            this.day = day;
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

        public int getDay() {
            return day;
        }
    }
}