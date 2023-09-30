import java.io.*;
import java.util.List;
import java.util.Scanner;

public class GameLoop {

    public static Inventory inventoryManager;
    public static boolean running = true;
    private static boolean unlockMode = false;
    private static boolean secretDoorUnlocked = false;
    private static boolean inSecretArea = false;

    public static void setSecretDoorUnlocked(boolean secretDoorUnlocked) {
        GameLoop.secretDoorUnlocked = secretDoorUnlocked;
    }

    public static void main(String[] args) {
        initGame(25, 15);
        World.generateWorld();
        System.out.println(GameValues.ANSI_GREEN + "Welcome to Simple Minecraft!" + GameValues.ANSI_RESET);
        System.out.println("Instructions:");
        System.out.println(" - Use 'W', 'A', 'S', 'D', or arrow keys to move the player.");
        System.out.println(" - Press 'M' to mine the block at your position and add it to your inventory.");
        System.out.println(" - Press 'P' to place a block from your inventory at your position.");
        System.out.println(" - Press 'C' to view crafting recipes and 'I' to interact with elements in the world.");
        System.out.println(" - Press 'Save' to save the game state and 'Load' to load a saved game state.");
        System.out.println(" - Press 'Exit' to quit the game.");
        System.out.println(" - Type 'Help' to display these instructions again.");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Start the game? (Y/N): ");
        String startGameChoice = scanner.next().toUpperCase();
        if (startGameChoice.equals("Y")) {
            startGame();
        } else {
            System.out.println("Game not started. Goodbye!");
        }
    }

    public static void initGame(int worldWidth, int worldHeight) {
        World.setWorldDimensions(worldWidth, worldHeight);
        Player.setPosition(worldWidth / 2, worldHeight / 2);
        inventoryManager = new Inventory();
    }


    public static void startGame() {
        Scanner scanner = new Scanner(System.in);
        boolean unlockMode = false;
        boolean craftingCommandEntered = false;
        boolean miningCommandEntered = false;
        boolean movementCommandEntered = false;
        boolean openCommandEntered = false;

        while (running) {
            clearScreen();
            displayLegend();
            World.displayWorld(Player.playerX, Player.playerY, inSecretArea);
            inventoryManager.displayInventory();
            System.out.println(GameValues.ANSI_CYAN
                    + "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door"
                    + GameValues.ANSI_RESET);
            String input = scanner.next().toLowerCase();
            InputManager.getInput(input, scanner);

            if (secretDoorUnlocked) {
                clearScreen();
                System.out.println("You have entered the secret area!");
                System.out.println("You are now presented with a game board with a flag!");
                inSecretArea = true;
                //resetWorld();
                System.out.println(Flag.returnASCIIFromImage(ServerInteraction.getCountryAndQuoteFromServer()));
                secretDoorUnlocked = false;
                fillInventory();
                InputManager.waitForEnter();
            }
        }
    }

    private static void fillInventory() {
        inventoryManager.clearInventory();
        for (int blockType = 1; blockType <= 5; blockType++) {
            for (int i = 0; i < GameValues.INVENTORY_SIZE; i++) {
                inventoryManager.addItem(blockType);
            }
        }
    }

    public static void resetWorld() {
        World.generateEmptyWorld("Albania");
        Player.setPosition(World.worldWidth / 2, World.worldHeight / 2);
    }


    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }


    private static int getBlockTypeFromCraftedItem(int craftedItem) {
        switch (craftedItem) {
            case GameValues.CRAFTED_WOODEN_PLANKS:
                return 5;
            case GameValues.CRAFTED_STICK:
                return 6;
            case GameValues.CRAFTED_IRON_INGOT:
                return 7;
            case GameValues.DIAMOND_TOTEM:
                return 8;
            default:
                return -1;
        }
    }


    public static void saveGame(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            // Serialize game state data and write to the file
            outputStream.writeInt(GameValues.NEW_WORLD_WIDTH);
            outputStream.writeInt(GameValues.NEW_WORLD_HEIGHT);
            outputStream.writeObject(World.world);
            outputStream.writeInt(Player.playerX);
            outputStream.writeInt(Player.playerY);
            outputStream.writeObject(inventoryManager.getInventory());
            outputStream.writeObject(inventoryManager.getCraftedItems());
            outputStream.writeBoolean(unlockMode);

            System.out.println("Game state saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error while saving the game state: " + e.getMessage());
        }
        InputManager.waitForEnter();
    }


    public static void loadGame(String fileName) {
        // Implementation for loading the game state from a file goes here
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            // Deserialize game state data from the file and load it into the program
            GameValues.NEW_WORLD_WIDTH = inputStream.readInt();
            GameValues.NEW_WORLD_HEIGHT = inputStream.readInt();
            World.world = (int[][]) inputStream.readObject();
            Player.playerX = inputStream.readInt();
            Player.playerY = inputStream.readInt();
            inventoryManager.setInventory((List<Integer>) inputStream.readObject());
            inventoryManager.setCraftedItems((List<Integer>) inputStream.readObject());
            unlockMode = inputStream.readBoolean();

            System.out.println("Game state loaded from file: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading the game state: " + e.getMessage());
        }
        InputManager.waitForEnter();
    }


    public static void displayLegend() {
        System.out.println(GameValues.ANSI_BLUE + "Legend:");
        System.out.println(GameValues.ANSI_WHITE + "-- - Empty block");
        System.out.println(GameValues.ANSI_BROWN + "\u2592\u2592 - Wood block");
        System.out.println(GameValues.ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
        System.out.println(GameValues.ANSI_GRAY + "\u2593\u2593 - Stone block");
        System.out.println(GameValues.ANSI_WHITE + "\u00B0\u00B0- Iron ore block");
        System.out.println(GameValues.ANSI_YELLOW + "\u00B0\u00B0- Gold ore block");
        System.out.println(GameValues.ANSI_CYAN + "\u00B0\u00B0- Diamond");
        System.out.println(GameValues.ANSI_PURPLE + "P - Player" + GameValues.ANSI_RESET);
        System.out.print("\n\n");
    }


    private static String getBlockColor(int blockType) {
        switch (blockType) {
            case GameValues.AIR:
                return "";
            case GameValues.WOOD:
                return GameValues.ANSI_RED;
            case GameValues.LEAVES:
                return GameValues.ANSI_GREEN;
            case GameValues.STONE:
                return GameValues.ANSI_GRAY;
            case GameValues.IRON_ORE:
                return GameValues.ANSI_WHITE;
            case GameValues.GOLD_ORE:
                return GameValues.ANSI_YELLOW;
            case GameValues.Diamond:
                return GameValues.ANSI_CYAN;
            default:
                return "";
        }
    }


}