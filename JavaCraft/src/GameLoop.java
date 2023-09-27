import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class GameLoop {




    
    private static int worldWidth;
    private static int worldHeight;
    private static int playerX;
    private static int playerY;
    private static List<Integer> inventory = new ArrayList<>();
    private static List<Integer> craftedItems = new ArrayList<>();
    private static boolean unlockMode = false;
    private static boolean secretDoorUnlocked = false;
    private static boolean inSecretArea = false;

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
        World.setWorldDimensions(worldWidth,worldHeight);
        playerX = worldWidth / 2;
        playerY = worldHeight / 2;
        inventory = new ArrayList<>();
    }




    public static void startGame() {
        Scanner scanner = new Scanner(System.in);
        boolean unlockMode = false;
        boolean craftingCommandEntered = false;
        boolean miningCommandEntered = false;
        boolean movementCommandEntered = false;
        boolean openCommandEntered = false;
        while (true) {
            clearScreen();
            displayLegend();
            World.displayWorld(playerX,playerY,inSecretArea);
            displayInventory();
            System.out.println(GameValues.ANSI_CYAN
                    + "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door"
                    + GameValues.ANSI_RESET);
            String input = scanner.next().toLowerCase();
            if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("up") ||
                    input.equalsIgnoreCase("s") || input.equalsIgnoreCase("down") ||
                    input.equalsIgnoreCase("a") || input.equalsIgnoreCase("left") ||
                    input.equalsIgnoreCase("d") || input.equalsIgnoreCase("right")) {
                if (unlockMode) {
                    movementCommandEntered = true;
                }
                movePlayer(input);
            } else if (input.equalsIgnoreCase("m")) {
                if (unlockMode) {
                    miningCommandEntered = true;
                }
                mineBlock();
            } else if (input.equalsIgnoreCase("p")) {
                displayInventory();
                System.out.print("Enter the block type to place: ");
                int blockType = scanner.nextInt();
                placeBlock(blockType);
            } else if (input.equalsIgnoreCase("c")) {
                displayCraftingRecipes();
                System.out.print("Enter the recipe number to craft: ");
                int recipe = scanner.nextInt();
                craftItem(recipe);
            } else if (input.equalsIgnoreCase("i")) {
                interactWithWorld();
            } else if (input.equalsIgnoreCase("save")) {
                System.out.print("Enter the file name to save the game state: ");
                String fileName = scanner.next();
                saveGame(fileName);
            } else if (input.equalsIgnoreCase("load")) {
                System.out.print("Enter the file name to load the game state: ");
                String fileName = scanner.next();
                loadGame(fileName);
            } else if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the game. Goodbye!");
                break;
            } else if (input.equalsIgnoreCase("look")) {
                lookAround();
            } else if (input.equalsIgnoreCase("unlock")) {
                unlockMode = true;
            } else if (input.equalsIgnoreCase("getflag")) {
                getCountryAndQuoteFromServer();
                waitForEnter();
            } else if (input.equalsIgnoreCase("open")) {
                if (unlockMode && craftingCommandEntered && miningCommandEntered && movementCommandEntered) {
                    secretDoorUnlocked = true;
                    resetWorld();
                    System.out.println("Secret door unlocked!");
                    waitForEnter();
                } else {
                    System.out.println("Invalid passkey. Try again!");
                    waitForEnter();
                    unlockMode = false;
                    craftingCommandEntered = false;
                    miningCommandEntered = false;
                    movementCommandEntered = false;
                    openCommandEntered = false;
                }
            } else {
                System.out.println(GameValues.ANSI_YELLOW + "Invalid input. Please try again." + GameValues.ANSI_RESET);
            }
            if (unlockMode) {
                if (input.equalsIgnoreCase("c")) {
                    craftingCommandEntered = true;
                } else if (input.equalsIgnoreCase("m")) {
                    miningCommandEntered = true;
                } else if (input.equalsIgnoreCase("open")) {
                    openCommandEntered = true;
                }
            }
            if (secretDoorUnlocked) {
                clearScreen();
                System.out.println("You have entered the secret area!");
                System.out.println("You are now presented with a game board with a flag!");
                inSecretArea = true;
                resetWorld();
                secretDoorUnlocked = false;
                fillInventory();
                waitForEnter();
            }
        }
    }

    private static void fillInventory() {
        inventory.clear();
        for (int blockType = 1; blockType <= 4; blockType++) {
            for (int i = 0; i < GameValues.INVENTORY_SIZE; i++) {
                inventory.add(blockType);
            }
        }
    }

    private static void resetWorld() {
        World.generateEmptyWorld();
        playerX = worldWidth / 2;
        playerY = worldHeight / 2;
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

    private static void lookAround() {
        System.out.println("You look around and see:");
        for (int y = Math.max(0, playerY - 1); y <= Math.min(playerY + 1, worldHeight - 1); y++) {
            for (int x = Math.max(0, playerX - 1); x <= Math.min(playerX + 1, worldWidth - 1); x++) {
                if (x == playerX && y == playerY) {
                    System.out.print(GameValues.ANSI_GREEN + "P " + GameValues.ANSI_RESET);
                } else {
                    System.out.print(Blocks.getBlockSymbol(World.world[x][y]));
                }
            }
            System.out.println();
        }
        System.out.println();
        waitForEnter();
    }

    public static void movePlayer(String direction) {
        switch (direction.toUpperCase()) {
            case "W":
            case "UP":
                if (playerY > 0) {
                    playerY--;
                }
                break;
            case "S":
            case "DOWN":
                if (playerY < worldHeight - 1) {
                    playerY++;
                }
                break;
            case "A":
            case "LEFT":
                if (playerX > 0) {
                    playerX--;
                }
                break;
            case "D":
            case "RIGHT":
                if (playerX < worldWidth - 1) {
                    playerX++;
                }
                break;
            default:
                break;
        }
    }

    public static void mineBlock() {
        int blockType = World.world[playerX][playerY];
        if (blockType != GameValues.AIR) {
            inventory.add(blockType);
            World.world[playerX][playerY] = GameValues.AIR;
            System.out.println("Mined " + getBlockName(blockType) + ".");
        } else {
            System.out.println("No block to mine here.");
        }
        waitForEnter();
    }

    public static void placeBlock(int blockType) {
        if (blockType >= 0 && blockType <= 7) {
            if (blockType <= 4) {
                if (inventory.contains(blockType)) {
                    inventory.remove(Integer.valueOf(blockType));
                    World.world[playerX][playerY] = blockType;
                    System.out.println("Placed " + getBlockName(blockType) + " at your position.");
                } else {
                    System.out.println("You don't have " + getBlockName(blockType) + " in your inventory.");
                }
            } else {
                int craftedItem = getCraftedItemFromBlockType(blockType);
                if (craftedItems.contains(craftedItem)) {
                    craftedItems.remove(Integer.valueOf(craftedItem));
                    World.world[playerX][playerY] = blockType;
                    System.out.println("Placed " + getCraftedItemName(craftedItem) + " at your position.");
                } else {
                    System.out.println("You don't have " + getCraftedItemName(craftedItem) + " in your crafted items.");
                }
            }
        } else {
            System.out.println("Invalid block number. Please enter a valid block number.");
            System.out.println(GameValues.BLOCK_NUMBERS_INFO);
        }
        waitForEnter();
    }

    private static int getBlockTypeFromCraftedItem(int craftedItem) {
        switch (craftedItem) {
            case GameValues.CRAFTED_WOODEN_PLANKS:
                return 5;
            case GameValues.CRAFTED_STICK:
                return 6;
            case GameValues.CRAFTED_IRON_INGOT:
                return 7;
            default:
                return -1;
        }
    }

    private static int getCraftedItemFromBlockType(int blockType) {
        switch (blockType) {
            case 5:
                return GameValues.CRAFTED_WOODEN_PLANKS;
            case 6:
                return GameValues.CRAFTED_STICK;
            case 7:
                return GameValues.CRAFTED_IRON_INGOT;
            default:
                return -1;
        }
    }

    public static void displayCraftingRecipes() {
        System.out.println("Crafting Recipes:");
        System.out.println("1. Craft Wooden Planks: 2 Wood");
        System.out.println("2. Craft Stick: 1 Wood");
        System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    }

    public static void craftItem(int recipe) {
        switch (recipe) {
            case 1:
                craftWoodenPlanks();
                break;
            case 2:
                craftStick();
                break;
            case 3:
                craftIronIngot();
                break;
            default:
                System.out.println("Invalid recipe number.");
        }
        waitForEnter();
    }

    public static void craftWoodenPlanks() {
        if (inventoryContains(GameValues.WOOD, 2)) {
            removeItemsFromInventory(GameValues.WOOD, 2);
            addCraftedItem(GameValues.CRAFTED_WOODEN_PLANKS);
            System.out.println("Crafted Wooden Planks.");
        } else {
            System.out.println("Insufficient resources to craft Wooden Planks.");
        }
    }

    public static void craftStick() {
        if (inventoryContains(GameValues.WOOD)) {
            removeItemsFromInventory(GameValues.WOOD, 1);
            addCraftedItem(GameValues.CRAFTED_STICK);
            System.out.println("Crafted Stick.");
        } else {
            System.out.println("Insufficient resources to craft Stick.");
        }
    }

    public static void craftIronIngot() {
        if (inventoryContains(GameValues.IRON_ORE, 3)) {
            removeItemsFromInventory(GameValues.IRON_ORE, 3);
            addCraftedItem(GameValues.CRAFTED_IRON_INGOT);
            System.out.println("Crafted Iron Ingot.");
        } else {
            System.out.println("Insufficient resources to craft Iron Ingot.");
        }
    }

    public static boolean inventoryContains(int item) {
        return inventory.contains(item);
    }

    public static boolean inventoryContains(int item, int count) {
        int itemCount = 0;
        for (int i : inventory) {
            if (i == item) {
                itemCount++;
                if (itemCount == count) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void removeItemsFromInventory(int item, int count) {
        int removedCount = 0;
        Iterator<Integer> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            if (i == item) {
                iterator.remove();
                removedCount++;
                if (removedCount == count) {
                    break;
                }
            }
        }
    }

    public static void addCraftedItem(int craftedItem) {
        if (craftedItems == null) {
            craftedItems = new ArrayList<>();
        }
        craftedItems.add(craftedItem);
    }

    public static void interactWithWorld() {
        int blockType = World.world[playerX][playerY];
        switch (blockType) {
            case GameValues.WOOD:
                System.out.println("You gather wood from the tree.");
                inventory.add(GameValues.WOOD);
                break;
            case GameValues.LEAVES:
                System.out.println("You gather leaves from the tree.");
                inventory.add(GameValues.LEAVES);
                break;
            case GameValues.STONE:
                System.out.println("You gather stones from the ground.");
                inventory.add(GameValues.STONE);
                break;
            case GameValues.IRON_ORE:
                System.out.println("You mine iron ore from the ground.");
                inventory.add(GameValues.IRON_ORE);
                break;
            case GameValues.AIR:
                System.out.println("Nothing to interact with here.");
                break;
            default:
                System.out.println("Unrecognized block. Cannot interact.");
        }
        waitForEnter();
    }

    public static void saveGame(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            // Serialize game state data and write to the file
            outputStream.writeInt(GameValues.NEW_WORLD_WIDTH);
            outputStream.writeInt(GameValues.NEW_WORLD_HEIGHT);
            outputStream.writeObject(World.world);
            outputStream.writeInt(playerX);
            outputStream.writeInt(playerY);
            outputStream.writeObject(inventory);
            outputStream.writeObject(craftedItems);
            outputStream.writeBoolean(unlockMode);

            System.out.println("Game state saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error while saving the game state: " + e.getMessage());
        }
        waitForEnter();
    }


    public static void loadGame(String fileName) {
        // Implementation for loading the game state from a file goes here
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            // Deserialize game state data from the file and load it into the program
            GameValues.NEW_WORLD_WIDTH = inputStream.readInt();
            GameValues.NEW_WORLD_HEIGHT = inputStream.readInt();
            World.world = (int[][]) inputStream.readObject();
            playerX = inputStream.readInt();
            playerY = inputStream.readInt();
            inventory = (List<Integer>) inputStream.readObject();
            craftedItems = (List<Integer>) inputStream.readObject();
            unlockMode = inputStream.readBoolean();

            System.out.println("Game state loaded from file: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading the game state: " + e.getMessage());
        }
        waitForEnter();
    }

    private static String getBlockName(int blockType) {
        switch (blockType) {
            case GameValues.AIR:
                return "Empty Block";
            case GameValues.WOOD:
                return "Wood";
            case GameValues.LEAVES:
                return "Leaves";
            case GameValues.STONE:
                return "Stone";
            case GameValues.IRON_ORE:
                return "Iron Ore";
            default:
                return "Unknown";
        }
    }

    public static void displayLegend() {
        System.out.println(GameValues.ANSI_BLUE + "Legend:");
        System.out.println(GameValues.ANSI_WHITE + "-- - Empty block");
        System.out.println(GameValues.ANSI_RED + "\u2592\u2592 - Wood block");
        System.out.println(GameValues.ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
        System.out.println(GameValues.ANSI_BLUE + "\u2593\u2593 - Stone block");
        System.out.println(GameValues.ANSI_WHITE + "\u00B0\u00B0- Iron ore block");
        System.out.println(GameValues.ANSI_BLUE + "P - Player" + GameValues.ANSI_RESET);
    }

    public static void displayInventory() {
        System.out.println("Inventory:");
        if (inventory.isEmpty()) {
            System.out.println(GameValues.ANSI_YELLOW + "Empty" + GameValues.ANSI_RESET);
        } else {
            int[] blockCounts = new int[5];
            for (int i = 0; i < inventory.size(); i++) {
                int block = inventory.get(i);
                blockCounts[block]++;
            }
            for (int blockType = 1; blockType < blockCounts.length; blockType++) {
                int occurrences = blockCounts[blockType];
                if (occurrences > 0) {
                    System.out.println(getBlockName(blockType) + " - " + occurrences);
                }
            }
        }
        System.out.println("Crafted Items:");
        if (craftedItems == null || craftedItems.isEmpty()) {
            System.out.println(GameValues.ANSI_YELLOW + "None" + GameValues.ANSI_RESET);
        } else {
            for (int item : craftedItems) {
                System.out.print(getCraftedItemColor(item) + getCraftedItemName(item) + ", " + GameValues.ANSI_RESET);
            }
            System.out.println();
        }
        System.out.println();
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
                return GameValues.ANSI_YELLOW;
            default:
                return "";
        }
    }

    private static void waitForEnter() {
        System.out.println("Press Enter to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    private static String getCraftedItemName(int craftedItem) {
        switch (craftedItem) {
            case GameValues.CRAFTED_WOODEN_PLANKS:
                return "Wooden Planks";
            case GameValues.CRAFTED_STICK:
                return "Stick";
            case GameValues.CRAFTED_IRON_INGOT:
                return "Iron Ingot";
            default:
                return "Unknown";
        }
    }

    private static String getCraftedItemColor(int craftedItem) {
        switch (craftedItem) {
            case GameValues.CRAFTED_WOODEN_PLANKS:
            case GameValues.CRAFTED_STICK:
            case GameValues.CRAFTED_IRON_INGOT:
                return GameValues.ANSI_BROWN;
            default:
                return "";
        }
    }

    public static void getCountryAndQuoteFromServer() {
        try {
            URL url = new URL(" ");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            String payload = " ";
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(payload);
            writer.flush();
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            int countryStart = json.indexOf(" ") + 11;
            int countryEnd = json.indexOf(" ", countryStart);
            String country = json.substring(countryStart, countryEnd);
            int quoteStart = json.indexOf(" ") + 9;
            int quoteEnd = json.indexOf(" ", quoteStart);
            String quote = json.substring(quoteStart, quoteEnd);
            quote = quote.replace(" ", " ");
            System.out.println(" " + country);
            System.out.println(" " + quote);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error connecting to the server");
        }
    }
}