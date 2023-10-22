import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class JavaCraft {
    private static final int AIR = 0;
    private static final int WOOD = 1;
    private static final int LEAVES = 2;
    private static final int STONE = 3;
    private static final int IRON_ORE = 4;
    private static final int COPPER_ORE = 5;
    private static final int RED_STONE = 6;
    private static int NEW_WORLD_WIDTH = 27;
    private static int NEW_WORLD_HEIGHT = 15;
    private static int EMPTY_BLOCK = 0;
    private static final int CRAFT_WOODEN_PLANKS = 100;
    private static final int CRAFT_STICK = 101;
    private static final int CRAFT_IRON_INGOT = 102;
    private static final int CRAFTED_WOODEN_PLANKS = 200;
    private static final int CRAFTED_STICK = 201;
    private static final int CRAFTED_IRON_INGOT = 202;
    private static final int CRAFTED_COPPER_INGOT = 203;
    private static final String ANSI_BROWN = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GRAY = "\u001B[37m";
    private static final String ANSI_WHITE = "\u001B[97m";

    private static final String BLOCK_NUMBERS_INFO = "Block Numbers:\n" +
            "0 - Empty block\n" +
            "1 - Wood block\n" +
            "2 - Leaves block\n" +
            "3 - Stone block\n" +
            "4 - Iron ore block\n" +
            "5 - Wooden Planks (Crafted Item)\n" +
            "6 - Stick (Crafted Item)\n" +
            "7 - Iron Ingot (Crafted Item)\n" +
            "8 - Copper ore block\n" +
            "9 - Redstone ore block\n" +
            "10 - Copper Ingot (Crafted Item)";
    private static int[][] world;
    private static int worldWidth;
    private static int worldHeight;
    private static int playerX;
    private static int playerY;
    private static List<Integer> inventory = new ArrayList<>();
    private static List<Integer> craftedItems = new ArrayList<>();
    private static boolean unlockMode = false;
    private static boolean secretDoorUnlocked = false;
    private static boolean inSecretArea = false;
    private static final int INVENTORY_SIZE = 100;

    public static void main(String[] args) {
        initGame(25, 15);
        generateWorld();
        System.out.println(ANSI_GREEN + "Welcome to Simple Minecraft!" + ANSI_RESET);
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
            System.out.println("Game.Game not started. Goodbye!");
        }
    }

    public static void initGame(int worldWidth, int worldHeight) {
        JavaCraft.worldWidth = worldWidth;
        JavaCraft.worldHeight = worldHeight;
        JavaCraft.world = new int[worldWidth][worldHeight];
        playerX = worldWidth / 2;
        playerY = worldHeight / 2;
        inventory = new ArrayList<>();
    }

    public static void generateWorld() {
        Random rand = new Random();
        for (int y = 0; y < worldHeight; y++) {
            for (int x = 0; x < worldWidth; x++) {
                int randValue = rand.nextInt(100);
                if (randValue < 15) {
                    world[x][y] = RED_STONE;
                } else if (randValue < 25) {
                    world[x][y] = COPPER_ORE;
                } else if (randValue < 35) {
                    world[x][y] = WOOD;
                } else if (randValue < 50) {
                    world[x][y] = LEAVES;
                } else if (randValue < 70) {
                    world[x][y] = STONE;
                } else if (randValue < 85) {
                    world[x][y] = IRON_ORE;
                } else {
                    world[x][y] = AIR;
                }
            }
        }
    }

    public static void displayWorld() {
        System.out.println(ANSI_CYAN + "World Map:" + ANSI_RESET);
        System.out.println("╔══" + "═".repeat(worldWidth * 2 - 2) + "╗");
        for (int y = 0; y < worldHeight; y++) {
            System.out.print(ANSI_RESET + "║");
            for (int x = 0; x < worldWidth; x++) {
                if (x == playerX && y == playerY && !inSecretArea) {
                    System.out.print(ANSI_GREEN + "P " + ANSI_RESET);
                } else if (x == playerX && y == playerY && inSecretArea) {
                    System.out.print(ANSI_BLUE + "P " + ANSI_RESET);
                } else {
                    System.out.print(getBlockSymbol(world[x][y]));
                }
            }
            System.out.println(ANSI_RESET + "║");
        }
        System.out.println(ANSI_RESET + "╚══" + "═".repeat(worldWidth * 2 - 2) + "╝");
    }

    private static String getBlockSymbol(int blockType) {
        String blockColor;
        switch (blockType) {
            case AIR:
                return ANSI_RESET + "- ";
            case COPPER_ORE:
                blockColor = ANSI_YELLOW;
                break;
            case WOOD:
            case RED_STONE:
                blockColor = ANSI_RED;
                break;
            case LEAVES:
                blockColor = ANSI_GREEN;
                break;
            case STONE:
                blockColor = ANSI_BLUE;
                break;
            case IRON_ORE:
                blockColor = ANSI_WHITE;
                break;
            default:
                blockColor = ANSI_RESET;
                break;
        }
        return blockColor + getBlockChar(blockType) + " ";
    }

    private static char getBlockChar(int blockType) {
        switch (blockType) {
            case WOOD:
                return '\u2592';
            case COPPER_ORE:
                return '\u00A9';
            case LEAVES:
                return '\u00A7';
            case STONE:
                return '\u2593';
            case IRON_ORE:
                return '\u00B0';
            case RED_STONE:
                return '\u00AE';
            default:
                return '-';
        }
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
            displayWorld();
            displayInventory();
            System.out.println(ANSI_CYAN
                    + "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door"
                    + ANSI_RESET);
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
                System.out.println(ANSI_YELLOW + "Invalid input. Please try again." + ANSI_RESET);
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
        for (int blockType = 1; blockType <= 6; blockType++) {
            for (int i = 0; i < INVENTORY_SIZE; i++) {
                inventory.add(blockType);
            }
        }
    }

    private static void resetWorld() {
        generateAustralianFlag();
        playerX = worldWidth / 2;
        playerY = worldHeight / 2;
    }

    private static void generateEmptyWorld() {
        worldWidth = NEW_WORLD_WIDTH;
        world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
        int redBlock = 1;
        int whiteBlock = 4;
        int blueBlock = 3;
        int stripeWidth = (NEW_WORLD_WIDTH) / 3;

        // flag = South Africa

        // french flag
        // for (int y = 0; y < worldHeight; y++) {
        // for (int x = 0; x < stripeWidth; x++) {
        // world[x][y] = blueBlock;
        // }
        // }

        // for (int y = 0; y < worldHeight; y++) {
        // for (int x = stripeWidth; x < stripeWidth * 2; x++) {
        // world[x][y] = whiteBlock;
        // }
        // }

        // for (int y = 0; y < NEW_WORLD_HEIGHT; y++) {
        // for (int x = stripeWidth * 2; x < NEW_WORLD_WIDTH; x++) {
        // world[x][y] = redBlock;
        // }
        // }

        // Fill the top stripe with red blocks
        // for (int y = 0; y < worldHeight; y++) {
        // for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        // world[x][y] = redBlock;
        // }
        // }
        // }

        // // Fill the middle stripe with white blocks
        // for (int y = stripeHeight; y < stripeHeight * 2; y++) {
        // for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        // world[x][y] = whiteBlock;
        // }
        // }

        // // Fill the bottom stripe with blue blocks
        // for (int y = stripeHeight * 2; y < NEW_WORLD_HEIGHT; y++) {
        // for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        // world[x][y] = blueBlock;
        // }
        // }
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

    private static void generateAustralianFlag() {
        int blackBlock = 7;
        int redBlock = 1;
        int whiteBlock = 4;
        int blueBlock = 3;
        int yellowBlock = 5;
        int greenBlock = 2;
        int stripeHeight = NEW_WORLD_HEIGHT / 3;
        NEW_WORLD_HEIGHT = 30;
        NEW_WORLD_WIDTH = 50;
        worldWidth = NEW_WORLD_WIDTH;
        worldHeight = NEW_WORLD_HEIGHT;

        world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
        int y, x;

        for (x = 0; x < NEW_WORLD_WIDTH; x++) {
            for (y = 0; y < NEW_WORLD_HEIGHT; y++) {

                world[x][y] = blueBlock;

            }
        }

        for (y = 0; y < worldHeight / 2; y++) {
            world[worldWidth / 4 + 5][y] = whiteBlock;
            world[worldWidth / 5 + 4][y] = whiteBlock;
        }

        for (x = 0; x < worldWidth / 2 + 6; x++) {
            world[x][worldHeight / 4 - 2] = whiteBlock;
            world[x][worldHeight / 4 + 1] = whiteBlock;
        }

        y = worldHeight / 2;
        x = 0;
        while (y > 0 && x < 51) {
            world[x][y - 1] = whiteBlock;
            world[x][y + 1] = whiteBlock;
            if (x < 50 && y > 1) {
                world[x + 1][y - 2] = whiteBlock;
            }
            x += 2;
            y--;

        }

        x = 0;
        for (y = 0; y < worldHeight / 2; y++) {
            world[x + 2][y] = whiteBlock;
            world[x][y + 2] = whiteBlock;
            world[x + 3][y] = whiteBlock;
            world[x + 3][y + 3] = whiteBlock;
            x += 2;
        }
        y = 11;
        x = 4;
        while (x < worldWidth / 2 + 2 && y > 0) {
            world[x][y] = whiteBlock;
            world[x][y + 3] = whiteBlock;
            x += 2;
            y--;
        }
        y = worldHeight / 2;
        x = 0;
        while (y > 0 && x < 51) {
            world[x][y] = redBlock;
            world[x][y - 1] = redBlock;
            world[x + 1][y] = redBlock;
            world[x + 1][y - 1] = redBlock;
            x += 2;
            y--;
        }
        x = 0;
        for (y = 0; y < worldHeight / 2; y++) {
            world[x][y] = redBlock;
            world[x + 1][y] = redBlock;
            world[x][y + 1] = redBlock;
            world[x + 1][y + 1] = redBlock;
            x += 2;
        }

        for (y = 0; y < worldHeight / 2; y++) {
            world[worldWidth / 4 + 3][y] = redBlock;
            world[worldWidth / 4 + 4][y] = redBlock;
        }

        for (x = 0; x <= worldWidth / 2 + 5; x++) {
            for (y = worldHeight / 3 - 4; y < worldHeight / 3 - 2; y++) {
                world[x][y] = redBlock;
            }
        }

        world[10][25] = whiteBlock;
        world[11][25] = whiteBlock;
        world[9][25] = whiteBlock;
        world[10][24] = whiteBlock;
        world[10][26] = whiteBlock;
        world[9][26] = whiteBlock;
        world[9][24] = whiteBlock;
        world[11][26] = whiteBlock;
        world[11][24] = whiteBlock;
        world[10][23] = whiteBlock;
        world[10][27] = whiteBlock;
        world[8][23] = whiteBlock;
        world[8][27] = whiteBlock;
        world[12][27] = whiteBlock;
        world[12][23] = whiteBlock;
        world[8][25] = whiteBlock;
        world[12][25] = whiteBlock;
        world[35][25] = whiteBlock;
        world[36][25] = whiteBlock;
        world[34][25] = whiteBlock;
        world[35][24] = whiteBlock;
        world[35][26] = whiteBlock;
        world[32][17] = whiteBlock;
        world[33][17] = whiteBlock;
        world[32][16] = whiteBlock;
        world[32][18] = whiteBlock;
        world[38][20] = whiteBlock;
        world[39][20] = whiteBlock;
        world[37][20] = whiteBlock;
        world[38][21] = whiteBlock;
        world[38][19] = whiteBlock;
        world[45][10] = whiteBlock;
        world[46][10] = whiteBlock;
        world[44][10] = whiteBlock;
        world[45][9] = whiteBlock;
        world[45][11] = whiteBlock;
        world[39][5] = whiteBlock;
        world[38][5] = whiteBlock;
        world[40][5] = whiteBlock;
        world[39][6] = whiteBlock;
        world[39][4] = whiteBlock;
        world[0][16] = blueBlock;
        world[31][worldHeight / 2 - 1] = blueBlock;
        world[worldWidth / 2 + 3][worldHeight / 2 + 1] = blueBlock;
        world[worldWidth / 2 + 4][worldHeight / 2 + 1] = blueBlock;
        for (x = 0; x < worldWidth; x++) {
            world[x][15] = blueBlock;
        }
        for (y = 0; y < worldHeight; y++) {
            world[30][y] = blueBlock;
        }

    }

    private static void lookAround() {
        System.out.println("You look around and see:");
        for (int y = Math.max(0, playerY - 1); y <= Math.min(playerY + 1, worldHeight - 1); y++) {
            for (int x = Math.max(0, playerX - 1); x <= Math.min(playerX + 1, worldWidth - 1); x++) {
                if (x == playerX && y == playerY) {
                    System.out.print(ANSI_GREEN + "P " + ANSI_RESET);
                } else {
                    System.out.print(getBlockSymbol(world[x][y]));
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
        int blockType = world[playerX][playerY];
        if (blockType != AIR) {
            inventory.add(blockType);
            world[playerX][playerY] = AIR;
            System.out.println("Mined " + getBlockName(blockType) + ".");
        } else {
            System.out.println("No block to mine here.");
        }
        waitForEnter();
    }

    public static void placeBlock(int blockType) { // takes the block type to initialize
        if (blockType >= 0 && blockType <= 9) { // check if there is a valid block
                                                // type??????????????????????????????????????????????????????????????????????????????????????????????????
            if (blockType <= 6) { // checks if there is a valid base block type once more
                if (inventory.contains(blockType)) { // checks if there is the valid block type in the inventory of the
                                                     // player
                    inventory.remove(Integer.valueOf(blockType)); // removes a single block of the selected type from
                                                                  // the
                    world[playerX][playerY] = blockType; // takes the position of the player on the map to put the block
                                                         // on
                    System.out.println("Placed " + getBlockName(blockType) + " at your position."); // prints out some
                                                                                                    // confirmation to
                                                                                                    // the player
                } else { // if it isn't in the inventory
                    System.out.println("You don't have " + getBlockName(blockType) + " in your inventory."); // prints
                                                                                                             // out
                                                                                                             // reason
                                                                                                             // for
                                                                                                             // error
                }
            } else { // if it isn't a valid base block type
                int craftedItem = getCraftedItemFromBlockType(blockType); // getCraftedItemFromBlockType() checks the
                                                                          // int code of a crafted item
                if (craftedItems.contains(craftedItem)) { // checks in the inventory if that items exists
                    craftedItems.remove(Integer.valueOf(craftedItem)); // removes one of the item type from the
                                                                       // inventory
                    world[playerX][playerY] = blockType; // gets the location of the player on the map
                    System.out.println("Placed " + getCraftedItemName(craftedItem) + " at your position.");// outputs
                                                                                                           // some
                                                                                                           // confirmation
                                                                                                           // to the
                                                                                                           // player
                } else { // if the item does not appear in the inventory
                    System.out.println("You don't have " + getCraftedItemName(craftedItem) + " in your crafted items."); // outputs
                                                                                                                         // the
                                                                                                                         // reason
                                                                                                                         // for
                                                                                                                         // the
                                                                                                                         // error
                                                                                                                         // to
                                                                                                                         // the
                                                                                                                         // player
                }
            }
        } else { // if the block value that the player gave was not valid
            System.out.println("Invalid block number. Please enter a valid block number."); // outputs reason for the
                                                                                            // error
            System.out.println(BLOCK_NUMBERS_INFO); // gives the player a list of valid blocks and their integer codes
        }
        waitForEnter(); // waits for enter from the user. Quite self explanatory
    }

    private static int getBlockTypeFromCraftedItem(int craftedItem) {
        switch (craftedItem) {
            case CRAFTED_WOODEN_PLANKS:
                return 5;
            case CRAFTED_STICK:
                return 6;
            case CRAFTED_IRON_INGOT:
                return 7;
            case CRAFTED_COPPER_INGOT:
                return 8;
            default:
                return -1;
        }
    }

    private static int getCraftedItemFromBlockType(int blockType) {
        switch (blockType) {
            case 5:
                return CRAFTED_WOODEN_PLANKS;
            case 6:
                return CRAFTED_STICK;
            case 7:
                return CRAFTED_IRON_INGOT;
            case 8:
                return CRAFTED_COPPER_INGOT;
            default:
                return -1;
        }
    }

    public static void displayCraftingRecipes() {
        System.out.println("Crafting Recipes:");
        System.out.println("1. Craft Wooden Planks: 2 Wood");
        System.out.println("2. Craft Stick: 1 Wood");
        System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
        System.out.println("4. Craft Copper Ingot: 3 Copper Ore");
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
            case 4:
                craftCopperIngot();
                break;
            default:
                System.out.println("Invalid recipe number.");
        }
        waitForEnter();
    }

    public static void craftWoodenPlanks() {
        if (inventoryContains(WOOD, 2)) {
            removeItemsFromInventory(WOOD, 2);
            addCraftedItem(CRAFTED_WOODEN_PLANKS);
            System.out.println("Crafted Wooden Planks.");
        } else {
            System.out.println("Insufficient resources to craft Wooden Planks.");
        }
    }

    public static void craftCopperIngot() {
        if (inventoryContains(COPPER_ORE, 3)) {
            removeItemsFromInventory(COPPER_ORE, 3);
            addCraftedItem(CRAFTED_COPPER_INGOT);
            System.out.println("Crafted Copper Ingot.");
        } else {
            System.out.println("Insufficient resources to craft Copper Ingot.");
        }

    }

    public static void craftStick() {
        if (inventoryContains(WOOD)) {
            removeItemsFromInventory(WOOD, 1);
            addCraftedItem(CRAFTED_STICK);
            System.out.println("Crafted Stick.");
        } else {
            System.out.println("Insufficient resources to craft Stick.");
        }
    }

    public static void craftIronIngot() {
        if (inventoryContains(IRON_ORE, 3)) {
            removeItemsFromInventory(IRON_ORE, 3);
            addCraftedItem(CRAFTED_IRON_INGOT);
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
        int blockType = world[playerX][playerY];
        switch (blockType) {
            case WOOD:
                System.out.println("You gather wood from the tree.");
                inventory.add(WOOD);
                break;
            case RED_STONE:
                System.out.println("You mine redstone ore from the ground");
                inventory.add(RED_STONE);
                break;
            case COPPER_ORE:
                System.out.println("You mine copper ore from the ground");
                inventory.add(COPPER_ORE);
                break;
            case LEAVES:
                System.out.println("You gather leaves from the tree.");
                inventory.add(LEAVES);
                break;
            case STONE:
                System.out.println("You gather stones from the ground.");
                inventory.add(STONE);
                break;
            case IRON_ORE:
                System.out.println("You mine iron ore from the ground.");
                inventory.add(IRON_ORE);
                break;
            case AIR:
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
            outputStream.writeInt(NEW_WORLD_WIDTH);
            outputStream.writeInt(NEW_WORLD_HEIGHT);
            outputStream.writeObject(world);
            outputStream.writeInt(playerX);
            outputStream.writeInt(playerY);
            outputStream.writeObject(inventory);
            outputStream.writeObject(craftedItems);
            outputStream.writeBoolean(unlockMode);

            System.out.println("Game.Game state saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error while saving the game state: " + e.getMessage());
        }
        waitForEnter();
    }

    public static void loadGame(String fileName) {
        // Implementation for loading the game state from a file goes here
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            // Deserialize game state data from the file and load it into the program
            NEW_WORLD_WIDTH = inputStream.readInt();
            NEW_WORLD_HEIGHT = inputStream.readInt();
            world = (int[][]) inputStream.readObject();
            playerX = inputStream.readInt();
            playerY = inputStream.readInt();
            inventory = (List<Integer>) inputStream.readObject();
            craftedItems = (List<Integer>) inputStream.readObject();
            unlockMode = inputStream.readBoolean();

            System.out.println("Game.Game state loaded from file: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading the game state: " + e.getMessage());
        }
        waitForEnter();
    }

    private static String getBlockName(int blockType) {
        switch (blockType) {
            case AIR:
                return "Empty Block";
            case WOOD:
                return "Wood";
            case LEAVES:
                return "Leaves";
            case STONE:
                return "Stone";
            case IRON_ORE:
                return "Iron Ore";
            case COPPER_ORE:
                return "Copper Ore";
            case RED_STONE:
                return "Redstone Ore";
            default:
                return "Unknown";
        }
    }

    public static void displayLegend() {
        System.out.println(ANSI_BLUE + "Legend:");
        System.out.println(ANSI_WHITE + "-- - Empty block");
        System.out.println(ANSI_RED + "\u2592\u2592 - Wood block");
        System.out.println(ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
        System.out.println(ANSI_BLUE + "\u2593\u2593 - Stone block");
        System.out.println(ANSI_WHITE + "\u00B0\u00B0- Iron ore block");
        System.out.println(ANSI_YELLOW + "\u00A9\u00A9- Copper ore block");
        System.out.println(ANSI_RED + "\u00AE\u00AE- Redstone ore block");
        System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
    }

    public static void displayInventory() {
        System.out.println("Inventory:");
        if (inventory.isEmpty()) {
            System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
        } else {
            int[] blockCounts = new int[7];// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
            System.out.println(ANSI_YELLOW + "None" + ANSI_RESET);
        } else {
            for (int item : craftedItems) {
                System.out.print(getCraftedItemColor(item) + getCraftedItemName(item) + ", " + ANSI_RESET);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static String getBlockColor(int blockType) {
        switch (blockType) {
            case AIR:
                return "";
            case WOOD:
            case RED_STONE:
                return ANSI_RED;
            case LEAVES:
                return ANSI_GREEN;
            case STONE:
                return ANSI_GRAY;
            case IRON_ORE:
                return ANSI_YELLOW;
            case COPPER_ORE:
                return ANSI_YELLOW;
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
            case CRAFTED_WOODEN_PLANKS:
                return "Wooden Planks";
            case CRAFTED_STICK:
                return "Stick";
            case CRAFTED_IRON_INGOT:
                return "Iron Ingot";
            case CRAFTED_COPPER_INGOT:
                return "Copper Ingot";
            default:
                return "Unknown";
        }
    }

    private static String getCraftedItemColor(int craftedItem) {
        switch (craftedItem) {
            case CRAFTED_WOODEN_PLANKS:
            case CRAFTED_STICK:
            case CRAFTED_IRON_INGOT:
                return ANSI_BROWN;
            case CRAFTED_COPPER_INGOT:
                return ANSI_GREEN;
            default:
                return "";
        }
    }

    public static void getCountryAndQuoteFromServer() {
        try {
            URL url = new URL("https://flag.ashish.nl/get_flag");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            String payload = "{\"group_number\": \"51\", \"group_name\": \"group51\", \"difficulty_level\": \"hard\"}";
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
            System.out.println(json);
            int countryStart = json.indexOf(":\"") + 2;
            int countryEnd = json.indexOf(",", countryStart);
            String country = json.substring(countryStart, countryEnd - 1);
            int quoteStart = json.indexOf("e\":\"") + 4;
            int quoteEnd = json.indexOf("\"}", quoteStart);
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