import java.util.Random;

public class World {

    private static int worldHeight;
    public static int worldWidth;
    public static int [][]world;

    public static void generateWorld() {
        Random rand = new Random();
        for (int y = 0; y < worldHeight; y++) {
            for (int x = 0; x < worldWidth; x++) {
                int randValue = rand.nextInt(100);
                if (randValue < 20) {
                    world[x][y] = GameValues.WOOD;
                } else if (randValue < 35) {
                    world[x][y] = GameValues.LEAVES;
                } else if (randValue < 50) {
                    world[x][y] = GameValues.STONE;
                } else if (randValue < 70) {
                    world[x][y] = GameValues.IRON_ORE;
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
                    System.out.print(GameValues.ANSI_GREEN + "P " + GameValues.ANSI_RESET);
                } else if (x == playerX && y == playerY) {
                    System.out.print(GameValues.ANSI_BLUE + "P " + GameValues.ANSI_RESET);
                } else {
                    System.out.print(Blocks.getBlockSymbol(world[x][y]));
                }
            }
            System.out.println("║");
        }
        System.out.println("╚══" + "═".repeat(worldWidth * 2 - 2) + "╝");
    }

    public static void generateEmptyWorld() {
        world = new int[GameValues.NEW_WORLD_WIDTH][GameValues.NEW_WORLD_HEIGHT];
        int redBlock = 1;
        int whiteBlock = 4;
        int blueBlock = 3;
        int stripeHeight = GameValues.NEW_WORLD_HEIGHT / 3; // Divide the height into three equal parts

        // Fill the top stripe with red blocks
        for (int y = 0; y < stripeHeight; y++) {
            for (int x = 0; x < GameValues.NEW_WORLD_WIDTH; x++) {
                world[x][y] = redBlock;
            }
        }

        // Fill the middle stripe with white blocks
        for (int y = stripeHeight; y < stripeHeight * 2; y++) {
            for (int x = 0; x < GameValues.NEW_WORLD_WIDTH; x++) {
                world[x][y] = whiteBlock;
            }
        }

        // Fill the bottom stripe with blue blocks
        for (int y = stripeHeight * 2; y < GameValues.NEW_WORLD_HEIGHT; y++) {
            for (int x = 0; x < GameValues.NEW_WORLD_WIDTH; x++) {
                world[x][y] = blueBlock;
            }
        }
    }


    public static void setWorldDimensions(int worldWidth,int worldHeight) {
        World.worldHeight = worldHeight;
        World.worldWidth = worldWidth;
        world = new int[worldWidth][worldHeight];
    }
}
