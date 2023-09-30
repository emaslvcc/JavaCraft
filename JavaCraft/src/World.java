import java.util.Random;

public class World {

    public static int worldHeight;
    public static int worldWidth;
    public static int[][] world;

    public static void generateWorld() {
        Random rand = new Random();
        for (int y = 0; y < worldHeight; y++) {
            for (int x = 0; x < worldWidth; x++) {
                int randValue = rand.nextInt(100);
                if (randValue < 5) {
                    world[x][y] = GameValues.Diamond;
                } else if (randValue < 10) {
                    world[x][y] = GameValues.GOLD_ORE;
                } else if (randValue < 15) {
                    world[x][y] = GameValues.IRON_ORE;
                } else if (randValue < 35) {
                    world[x][y] = GameValues.LEAVES;
                } else if (randValue < 40) {
                    world[x][y] = GameValues.WOOD;
                } else if (randValue < 50) {
                    world[x][y] = GameValues.STONE;
                } else {
                    world[x][y] = GameValues.AIR;
                }
            }
        }
    }


    public static void displayWorld(int playerX, int playerY, boolean inSecretArea) {
        System.out.println(GameValues.ANSI_CYAN + "World Map:" + GameValues.ANSI_RESET);
        System.out.println("╔══" + "═".repeat(worldWidth * 2 - 2) + "╗");
        for (int y = 0; y < worldHeight; y++) {
            System.out.print("║");
            for (int x = 0; x < worldWidth; x++) {
                if (x == playerX && y == playerY && !inSecretArea) {
                    System.out.print(GameValues.ANSI_PURPLE + "P " + GameValues.ANSI_RESET);
                } else if (x == playerX && y == playerY) {
                    System.out.print(GameValues.ANSI_PURPLE + "P " + GameValues.ANSI_RESET);
                } else {
                    System.out.print(Blocks.getBlockSymbol(world[x][y]) + GameValues.ANSI_RESET);
                }
            }
            System.out.println("║");
        }
        System.out.println("╚══" + "═".repeat(worldWidth * 2 - 2) + "╝");
    }

    public static void generateEmptyWorld(String country) {


    }


    public static void setWorldDimensions(int worldWidth, int worldHeight) {
        World.worldHeight = worldHeight;
        World.worldWidth = worldWidth;
        world = new int[worldWidth][worldHeight];
    }
}
