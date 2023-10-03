import java.util.*;
import java.net.*;
import java.io.*;

/**
* Main game class.
*/
public class JavaCraft {
    /**
    * The value of if the player is in the secret area.
    * <p>
    * Returns true if player is in the secret area, false in any other case.
    * <p>
    * Part of secret door logic.
    */
    private static boolean inSecretArea = false;
    /**
    * The value of if the secret door is unlocked.
    * <p>
    * Returns true if the secret door is unlocked, false in any other case.
    * <p>
    * Part of secret door logic.
    */
    private static boolean secretDoorUnlocked = false;
    /**
    * The value of the unlock mode.
    * <p>
    * Returns true if FIXME: TO ADD, false in any other case.
    * <p>
    * Part of secret door logic.
    */
    private static boolean unlockMode = false;
    /**
    * The Integer value of AIR
    */
    private static final int AIR = 0;
    /**
    * The Integer value of CRAFT_EMERALD_SWORD
    */
    private static final int CRAFT_EMERALD_SWORD = 105;
    /**
    * The Integer value of CRAFT_IRON_SWORD
    */
    private static final int CRAFT_IRON_SWORD = 104;
    /**
    * The Integer value of CRAFT_WOODEN_SWORD
    */
    private static final int CRAFT_WOODEN_SWORD = 103;
    /**
    * The Integer value of CRAFT_IRON_SWORD
    */
    private static final int CRAFT_IRON_INGOT = 102;
    /**
    * The Integer value of CRAFT_STICK
    */
    private static final int CRAFT_STICK = 101;
    /**
    * The Integer value of CRAFT_WOODEN_PLANKS
    */
    private static final int CRAFT_WOODEN_PLANKS = 100;
    /**
    * The Integer value of CRAFTED_IRON_INGOT
    */
    private static final int CRAFTED_EMERALD_SWORD = 205;
    /**
    * The Integer value of CRAFT_IRON_SWORD
    */
    private static final int CRAFTED_IRON_SWORD = 204;
    /**
    * The Integer value of CRAFT_WOODEN_SWORD
    */
    private static final int CRAFTED_WOODEN_SWORD = 203;
    /**
    * The Integer value of CRAFT_IRON_SWORD
    */
    private static final int CRAFTED_IRON_INGOT = 202;
    /**
    * The Integer value of CRAFTED_STICK
    */
    private static final int CRAFTED_STICK = 201;
    /**
    * The Integer value of CRAFTED_WOODEN_PLANKS
    */
    private static final int CRAFTED_WOODEN_PLANKS = 200;
    /**
    * The Integer value of EMPTY_BLOCK
    */
    private static final int EMPTY_BLOCK = 0;
    /**
    * The size of the inventory
    */
    private static final int INVENTORY_SIZE = 100;
    /**
     * The Integer value of COAL_ORE
     */
    private static final int COAL_ORE = 6;
    /**
     * The Integer value of EMERALD_ORE
     */
    private static final int EMERALD_ORE = 5;
    /**
    * The Integer value of IRON_ORE
    */
    private static final int IRON_ORE = 4;
    /**
    * The Integer value of LEAVES
    */
    private static final int LEAVES = 2;
    /**
    * The Integer value of STONE
    */
    private static final int STONE = 3;
    /**
    * The Integer value of WOOD
    */
    private static final int WOOD = 1;
    /**
    * The ANSI color code for BLUE
    */
    private static final String ANSI_BLUE = "\u001B[34m";
    /**
    * The ANSI color code for BROWN
    */
    private static final String ANSI_BROWN = "\u001B[33m";
    /**
    * The ANSI color code for CYAN
    */
    private static final String ANSI_CYAN = "\u001B[36m";
    /**
    * The ANSI color code for GRAY
    */
    private static final String ANSI_GRAY = "\u001B[37m";
    /**
    * The ANSI color code for GREEN
    */
    private static final String ANSI_GREEN = "\u001B[32m";
    /**
     * The ANSI color code for EMERALD_GREEN
     */
    private static final String ANSI_EMERALD_GREEN = "\u001B[32m";
    /**
     * The ANSI color code for GRAY 
     */
    private static final String ANSI_COAL_GRAY = "\u001B[37m";
    /**
    * The ANSI color code for PURPLE
    */
    private static final String ANSI_PURPLE = "\u001B[35m";
    /**
    * The ANSI color code for RED
    */
    private static final String ANSI_RED = "\u001B[31m";
    /**
    * The ANSI color code for RESET
    */
    private static final String ANSI_RESET = "\u001B[0m";
    /**
    * The ANSI color code for WHITE
    */
    private static final String ANSI_WHITE = "\u001B[97m";
    /**
    * The ANSI color code for YELLOW
    */
    private static final String ANSI_YELLOW = "\u001B[33m";
    /**
    * The info on block numbers
    */
    private static final String BLOCK_NUMBERS_INFO = """
            Block Numbers:
            0 - Empty block
            1 - Wood block
            2 - Leaves block
            3 - Stone block
            4 - Iron ore block
            5 - Emerald Block
            6 - Coal
            7 - Wooden Planks (Crafted Item)
            8 - Stick (Crafted Item)
            9 - Iron Ingot (Crafted Item)
            """;
    /**
    * The new world height
    */
    private static int NEW_WORLD_HEIGHT = 15;
    /**
    * The new world width
    */
    private static int NEW_WORLD_WIDTH = 25;
    /**
    * The players X position
    */
    private static int playerX;
    /**
    * The players Y position
    */
    private static int playerY;
    /**
    * The game worlds height
    */
    private static int worldHeight;
    /**
    * The game worlds width
    */
    private static int worldWidth;
    /**
    * The game world
    */
    private static int[][] world;
    /**
    * The players crafted items
    */
    private static List<Integer> craftedItems = new ArrayList<>();
    /**
    * The players inventory
    */
    private static List<Integer> inventory = new ArrayList<>();
    /**
    * The Scanner to read input
    */
    private static Scanner scanner;

    // FLOWCHART & PSEUDOCODE: Leopold
    /**
    * Main method.
    * <p>
    * This method is called upon execution of the game.
    *
    * @param args The supplied commandline arguments
    */
    public static void main(String[] args) {
        initGame(25, 15);
        generateWorld();
        System.out.println(ANSI_GREEN + "Welcome to Simple Minecraft!" + ANSI_RESET);
        System.out.println("Instructions:");
        System.out.println(" - Use 'W', 'A', 'S', 'D', or arrow keys to move the player.");
        System.out.println(
                " - Press 'M' to mine the block at your position and add it to your inventory.");
        System.out.println(" - Press 'P' to place a block from your inventory at your position.");
        System.out.println(
                " - Press 'C' to view crafting recipes and 'I' to interact with elements in the world.");
        System.out.println(
                " - Press 'Save' to save the game state and 'Load' to load a saved game state.");
        System.out.println(" - Press 'Exit' to quit the game.");
        System.out.println(" - Type 'Help' to display these instructions again.");
        System.out.println();
        JavaCraft.scanner = new Scanner(System.in);
        System.out.print("Start the game? (Y/N): ");
        String startGameChoice = scanner.next().toUpperCase();
        if (startGameChoice.equals("Y")) {
            startGame();
        } else {
            System.out.println("Game not started. Goodbye!");
        }
        scanner.close();
    }
    //hello

    // FLOWCHART & PSEUDOCODE: Tristan
    /**
    * Initializes the game.
    * <p>
    * This method sets JavaCraft.worldWidth, JavaCraft.worldHeight, JavaCraft.world, playerX, playerY and initializes inventory.
    *
    * @param worldWidth  The width of world in blocks
    * @param worldHeight The height of world in blocks
    */
    public static void initGame(int worldWidth, int worldHeight) {
        JavaCraft.worldWidth = worldWidth;
        JavaCraft.worldHeight = worldHeight;
        JavaCraft.world = new int[worldWidth][worldHeight];
        JavaCraft.playerX = worldWidth / 2;
        JavaCraft.playerY = worldHeight / 2;
        JavaCraft.inventory = new ArrayList<>();
    }

    // FLOWCHART & PSEUDOCODE: Leopold
    /**
    * Generates the world.
    * <p>
    * This method uses randomness to generate a world out of different materials.
    */
    public static void generateWorld() {
        Random rand = new Random();
        for (int y = 0; y < worldHeight; y++) {
            for (int x = 0; x < worldWidth; x++) {
                int randValue = rand.nextInt(100);
                if (randValue < 17) {
                    world[x][y] = WOOD;
                } else if (randValue < 30) {
                    world[x][y] = LEAVES;
                } else if (randValue < 45) {
                    world[x][y] = STONE;
                } else if (randValue < 57) {
                    world[x][y] = IRON_ORE;
                } else if (randValue < 65) {
                    world[x][y] = EMERALD_ORE; 
                } else if (randValue < 75) {
                    world[x][y] = COAL_ORE;
                } else {
                    world[x][y] = AIR;
                }
            }
        }
    }

    // FLOWCHART & PSEUDOCODE: Tristan
    /**
    * Prints the world as ASCII text.
    * <p>
    * This method is responsible for displaying the world.
    */
    public static void displayWorld() {
        System.out.println(ANSI_CYAN + "World Map:" + ANSI_RESET);
        System.out.println("╔══" + "═".repeat(worldWidth * 2 - 2) + "╗");
        for (int y = 0; y < worldHeight; y++) {
            System.out.print("║");
            for (int x = 0; x < worldWidth; x++) {
                if (x == playerX && y == playerY && !inSecretArea) {
                    System.out.print(ANSI_GREEN + "P " + ANSI_RESET);
                } else if (x == playerX && y == playerY && inSecretArea) {
                    System.out.print(ANSI_BLUE + "P " + ANSI_RESET);
                } else {
                    System.out.print(getBlockSymbol(world[x][y]));
                }
            }
            System.out.println("║");
        }
        System.out.println("╚══" + "═".repeat(worldWidth * 2 - 2) + "╝");
    }

    // FLOWCHART & PSEUDOCODE: Tristan
    /**
    * Returns the symbol and color for blockType.
    * <p>
    * This method returns the mapped char and blockColor for blockType.
    *
    * @param blockType The type of block
    * @return String   The mapped symbol and blockColor for blockType
    */
    private static String getBlockSymbol(int blockType) {
        String blockColor;
        switch (blockType) {
            case AIR:
                return ANSI_RESET + "- ";
            case WOOD:
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
            case EMERALD_ORE:
                blockColor = ANSI_EMERALD_GREEN;
                break;
            case COAL_ORE:
                blockColor = ANSI_COAL_GRAY;
                break;    
            default:
                blockColor = ANSI_RESET;
                break;
        }
        return blockColor + getBlockChar(blockType) + " ";
    }

    // FLOWCHART & PSEUDOCODE: Leopold
    /**
    * Returns the symbol for blockType.
    * <p>
    * This method returns the mapped char for blockType.
    *
    * @param blockType The type of block
    * @return char     The mapped symbol for blockType
    */
    private static char getBlockChar(int blockType) {
        switch (blockType) {
            case WOOD:
                return '\u2592';
            case LEAVES:
                return '\u00A7';
            case STONE:
                return '\u2593';
            case IRON_ORE:
                return '\u00B0';
            case EMERALD_ORE:
                return '\u00B0';
            case COAL_ORE:
                return '\u2593';
            default:
                return '-';
        }
    }

    // FLOWCHART & PSEUDOCODE: Leopold
    /**
    * Starts the game.
    * <p>
    * This method handles the following:
    * <ul>
    *     <li>Printing of initial UI, instructions and informational messages</li>
    *     <li>Player input</li>
    *     <li>Secret door logic</li>
    * </ul>
    * <p>
    * Part of secret door logic.
    */
    public static void startGame() {
        JavaCraft.scanner = new Scanner(System.in);
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
            if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("up")
                    || input.equalsIgnoreCase("s") || input.equalsIgnoreCase("down")
                    || input.equalsIgnoreCase("a") || input.equalsIgnoreCase("left")
                    || input.equalsIgnoreCase("d") || input.equalsIgnoreCase("right")) {
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
                if (unlockMode && craftingCommandEntered && miningCommandEntered
                        && movementCommandEntered) {
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

    // FLOWCHART & PSEUDOCODE: Anton
    /**
    * Fills players inventory with all items.
    * <p>
    * This method fills the players inventory with all available blockTypes.
    * <p>
    * Part of secret door logic.
    */
    private static void fillInventory() {
        inventory.clear();
        for (int blockType = 1; blockType <= 6; blockType++) {
            for (int i = 0; i < INVENTORY_SIZE; i++) {
                inventory.add(blockType);
            }
        }
    }

    // FLOWCHART & PSEUDOCODE: Anton
    /**
    * Resets the world to an empty world.
    * <p>
    * This method resets the world to an empty world via generating an empty world and resetting the players position.
    * <p>
    * Part of secret door logic.
    */
    private static void resetWorld() {
        generateEmptyWorld();
        playerX = worldWidth / 2;
        playerY = worldHeight / 2;
    }

    // FLOWCHART & PSEUDOCODE: Sian
    /**
    * Generates an empty world.
    * <p>
    * This method generates an empty world which is part of the secret door logic.
    * <p>
    * Part of secret door logic.
    */
    private static void generateEmptyWorld() {
        world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
        int redBlock = 1;
        int whiteBlock = 4;
        int blueBlock = 3;
        int stripeHeight = NEW_WORLD_HEIGHT / 3; // Divide the height into three equal parts

        // Fill the top stripe with red blocks
        for (int y = 0; y < stripeHeight; y++) {
            for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
                world[x][y] = redBlock;
            }
        }

        // Fill the middle stripe with white blocks
        for (int y = stripeHeight; y < stripeHeight * 2; y++) {
            for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
                world[x][y] = whiteBlock;
            }
        }

        // Fill the bottom stripe with blue blocks
        for (int y = stripeHeight * 2; y < NEW_WORLD_HEIGHT; y++) {
            for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
                world[x][y] = blueBlock;
            }
        }
    }

    // FLOWCHART & PSEUDOCODE: Leopold
    /**
    * Clears the screen.
    * <p>
    * This method clears the screen and uses different logic depending on the OS.
    * <p>
    * <b>Catched Exceptions:</b>
    * <ul>
    *     <li>On IOException: Prints stacktrace when I/O exception of some sort has occurred.</li>
    *     <li>On InterruptedException: Prints stacktrace when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.</li>
    * </ul>
    */
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

    // FLOWCHART & PSEUDOCODE: Anton
    /**
    * Prints all blocks sorrounding the player.
    * <p>
    * This method prints all blocks sorrounding the player. This is meant to make the players life easier.
    */
    private static void lookAround() {
        System.out.println("You look around and see:");
        for (int y = Math.max(0, playerY - 1); y <= Math.min(playerY + 1, worldHeight - 1); y++) {
            for (int x = Math.max(0, playerX - 1); x <= Math.min(playerX + 1,
                    worldWidth - 1); x++) {
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

    // FLOWCHART & PSEUDOCODE: Sian
    /**
    * Moves the player
    * <p>
    * This method moves the player UP/DOWN/LEFT/RIGHT depending on the supplied direction.
    * @param direction The direction the player should be moved towards.
    */
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

    // FLOWCHART & PSEUDOCODE: Tristan
    /**
    * Mines a block.
    * <p>
    * This method mines a block and adds it to the players inventory if it is not AIR.
    */
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

    // FLOWCHART & PSEUDOCODE: Anton
    /**
    * Places a block.
    * <p>
    * This method places a block that is of blockType 0 to 7 and removes it from the players inventory if the players inventory contains that block.
    * @param blockType The type of block
    */
    public static void placeBlock(int blockType) {
        if (blockType >= 0 && blockType <= 9) {
            if (blockType <= 6) {
                if (inventory.contains(blockType)) {
                    inventory.remove(Integer.valueOf(blockType));
                    world[playerX][playerY] = blockType;
                    System.out.println("Placed " + getBlockName(blockType) + " at your position.");
                } else {
                    System.out.println(
                            "You don't have " + getBlockName(blockType) + " in your inventory.");
                }
            } else {
                int craftedItem = getCraftedItemFromBlockType(blockType);
                if (craftedItems.contains(craftedItem)) {
                    craftedItems.remove(Integer.valueOf(craftedItem));
                    world[playerX][playerY] = blockType;
                    System.out.println(
                            "Placed " + getCraftedItemName(craftedItem) + " at your position.");
                } else {
                    System.out.println("You don't have " + getCraftedItemName(craftedItem)
                            + " in your crafted items.");
                }
            }
        } else {
            System.out.println("Invalid block number. Please enter a valid block number.");
            System.out.println(BLOCK_NUMBERS_INFO);
        }
        waitForEnter();
    }

    // FLOWCHART & PSEUDOCODE: Sian
    /**
    * Returns the block type of craftedItem.
    * <p>
    * This method returns the block type of craftedItem.
    * <p>
    * Defaults to -1.
    * @param  craftedItem The crafted item
    * @return int         The block type of craftedItem
    */
    private static int getBlockTypeFromCraftedItem(int craftedItem) {
        switch (craftedItem) {
            case CRAFTED_WOODEN_PLANKS:
                return 7;
            case CRAFTED_STICK:
                return 8;
            case CRAFTED_IRON_INGOT:
                return 9;
            default:
                return -1;
        }
    }

    // FLOWCHART & PSEUDOCODE: Tristan
    /**
    * Returns the crafted item of blockType.
    * <p>
    * This method returns the crafted item of blockType.
    * <p>
    * Defaults to -1.
    * @param  blockType The type of block
    * @return int       The crafted item of blockType
    */
    private static int getCraftedItemFromBlockType(int blockType) {
        switch (blockType) {
            case 7:
                return CRAFTED_WOODEN_PLANKS;
            case 8:
                return CRAFTED_STICK;
            case 9:
                return CRAFTED_IRON_INGOT;
            default:
                return -1;
        }
    }

    // FLOWCHART & PSEUDOCODE: Leopold
    /**
    * Prints crafting recipes.
    * <p>
    * This method prints the available crafting recipes.
    */
    public static void displayCraftingRecipes() {
        System.out.println("Crafting Recipes:");
        System.out.println("1. Craft Wooden Planks: 2 Wood");
        System.out.println("2. Craft Stick: 1 Wood");
        System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    }

    // FLOWCHART & PSEUDOCODE: Leopold
    /**
    * Crafts an item.
    * <p>
    * This method crafts an item from a recipe.
    * <p>
    * Prints message if invalid recipe was supplied.
    * @param recipe The recipe used to craft the item
    */
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

    // FLOWCHART & PSEUDOCODE: Sian
    /**
    * Crafts CRAFTED_WOODEN_PLANKS.
    * <p>
    * This method crafts CRAFTED_WOODEN_PLANKS from 2 WOOD that are taken from the players inventory.
    * <p>
    * Prints message if the player doesn't have the correct items in his inventory.
    */
    public static void craftWoodenPlanks() {
        if (inventoryContains(WOOD, 2)) {
            removeItemsFromInventory(WOOD, 2);
            addCraftedItem(CRAFTED_WOODEN_PLANKS);
            System.out.println("Crafted Wooden Planks.");
        } else {
            System.out.println("Insufficient resources to craft Wooden Planks.");
        }
    }

    // FLOWCHART & PSEUDOCODE: Sian
    /**
    * Crafts CRAFTED_STICK.
    * <p>
    * This method crafts CRAFTED_STICK from 1 WOOD that is taken from the players inventory.
    * <p>
    * Prints message if the player doesn't have the correct items in his inventory.
    */
    public static void craftStick() {
        if (inventoryContains(WOOD)) {
            removeItemsFromInventory(WOOD, 1);
            addCraftedItem(CRAFTED_STICK);
            System.out.println("Crafted Stick.");
        } else {
            System.out.println("Insufficient resources to craft Stick.");
        }
    }

    // FLOWCHART & PSEUDOCODE: Sian
    /**
    * Crafts CRAFTED_IRON_INGOT.
    * <p>
    * This method crafts CRAFTED_IRON_INGOT from 3 IRON_ORE that is taken from the players inventory.
    * <p>
    * Prints message if the player doesn't have the correct items in his inventory.
    */
    public static void craftIronIngot() {
        if (inventoryContains(IRON_ORE, 3)) {
            removeItemsFromInventory(IRON_ORE, 3);
            addCraftedItem(CRAFTED_IRON_INGOT);
            System.out.println("Crafted Iron Ingot.");
        } else {
            System.out.println("Insufficient resources to craft Iron Ingot.");
        }
    }

    // FLOWCHART & PSEUDOCODE: Sian
    /**
    * Queries inventory for an item.
    * <p>
    * This method queries the players inventory for an item.
    * @param item The item to query the inventory for
    * @return boolean true if inventory contains item, false in any other case
    */
    public static boolean inventoryContains(int item) {
        return inventory.contains(item);
    }

    // FLOWCHART & PSEUDOCODE: Tristan
    /**
    * Queries inventory for if it has enough of an item.
    * <p>
    * This method queries the players inventory for an item and if it contains at least as much as the supplied count.
    * @param item  The item to query the inventory for
    * @param count The count that the inventory should contain of the item
    * @return boolean true if inventory contains item at least as many times as the supplied count, false in any other case
    */
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

    // FLOWCHART & PSEUDOCODE: Anton
    /**
    * Removes a count of item from inventory.
    * <p>
    * This method removes a count of an item from the players inventory.
    * @param item  The item to remove from the inventory
    * @param count The count that should be removed from the inventory
    */
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

    // FLOWCHART & PSEUDOCODE: Sian
    /**
    * Adds a crafted item to craftedItems.
    * <p>
    * This method adds a crafted item to craftedItems that are part of the players inventory.
    * @param craftedItem The crafted item
    */
    public static void addCraftedItem(int craftedItem) {
        if (craftedItems == null) {
            craftedItems = new ArrayList<>();
        }
        craftedItems.add(craftedItem);
    }

    // FLOWCHART & PSEUDOCODE: Leopold
    /**
    * Handles interaction with the game world.
    * <p>
    * This method handles interaction with the game world and prints messages for blocks that the player can interact with. It also adds certain blocks to the players inventory if he interacts with them.
    */
    public static void interactWithWorld() {
        int blockType = world[playerX][playerY];
        switch (blockType) {
            case WOOD:
                System.out.println("You gather wood from the tree.");
                inventory.add(WOOD);
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
            case EMERALD_ORE:
                System.out.println("You mine emerald ore from the ground.");
                inventory.add(EMERALD_ORE);
                break;
            case COAL_ORE:
                System.out.println("You mine coal ore from the ground.");
                inventory.add(COAL_ORE);
                break;
            case AIR:
                System.out.println("Nothing to interact with here.");
                break;
            default:
                System.out.println("Unrecognized block. Cannot interact.");
        }
        waitForEnter();
    }

    // FLOWCHART & PSEUDOCODE: Anton
    /**
    * Saves the game.
    * <p>
    * This method saves the game in a file.
    * @param fileName The file name
    * <p>
    * <b>Catched Exceptions:</b>
    * <ul>
    *     <li>On IOException: Prints error with message when I/O exception of some sort has occurred.</li>
    * </ul>
    */
    public static void saveGame(String fileName) {
        try (ObjectOutputStream outputStream =
                new ObjectOutputStream(new FileOutputStream(fileName))) {
            // Serialize game state data and write to the file
            outputStream.writeInt(NEW_WORLD_WIDTH);
            outputStream.writeInt(NEW_WORLD_HEIGHT);
            outputStream.writeObject(world);
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

    // FLOWCHART & PSEUDOCODE: Leopold
    /**
    * Loads the game.
    * <p>
    * This method loads the game from a file.
    * @param fileName The file name
    * <p>
    * <b>Catched Exceptions:</b>
    * <ul>
    *     <li>On IOException: Prints error with message when I/O exception of some sort has occurred.</li>
    *     <li>On ClassNotFoundException: Prints error with message when no definition for the class with the specified name could be found.</li>
    * </ul>
    */
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

            System.out.println("Game state loaded from file: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading the game state: " + e.getMessage());
        }
        waitForEnter();
    }

    // FLOWCHART & PSEUDOCODE: Tristan
    /**
    * Returns human readable block name.
    * <p>
    * This method returns a human readable block name for blockType.
    * <p>
    * Defaults to "Unknown"
    * @param  blockType The type of block
    * @return String    The human readable block name.
    */
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
            case EMERALD_ORE:
                return "Emerald Ore";
            case COAL_ORE:
                return "Coal Ore";
            default:
                return "Unknown";
        }
    }

    // FLOWCHART & PSEUDOCODE: Sian
    /**
    * Prints a legend.
    * <p>
    * This method prints a legend of items on the map.
    */
    public static void displayLegend() {
        System.out.println(ANSI_BLUE + "Legend:");
        System.out.println(ANSI_WHITE + "-- - Empty block");
        System.out.println(ANSI_RED + "\u2592\u2592 - Wood block");
        System.out.println(ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
        System.out.println(ANSI_BLUE + "\u2593\u2593 - Stone block");
        System.out.println(ANSI_WHITE + "\u00B0\u00B0- Iron ore block");
        System.out.println(ANSI_EMERALD_GREEN + "\u00B0\u00B0 - Emerald ore block");
        System.out.println(ANSI_COAL_GRAY + "\u2593\u2593 - Coal ore block");
        System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
    }

    // FLOWCHART & PSEUDOCODE: Tristan
    /**
    * Prints players inventory.
    * <p>
    * This method prints the players inventory including craftedItems.
    */
    public static void displayInventory() {
        System.out.println("Inventory:");
        if (inventory.isEmpty()) {
            System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
        } else {
            int[] blockCounts = new int[7];
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
                System.out.print(
                        getCraftedItemColor(item) + getCraftedItemName(item) + ", " + ANSI_RESET);
            }
            System.out.println();
        }
        System.out.println();
    }

    // FLOWCHART & PSEUDOCODE: Anton
    /**
    * Returns block color.
    * <p>
    * This method returns the blocks color.
    * <p>
    * Defaults to empty String
    * @param  blockType The type of block
    * @return String    The human readable name of craftedItem
    */
    private static String getBlockColor(int blockType) {
        switch (blockType) {
            case AIR:
                return "";
            case WOOD:
                return ANSI_RED;
            case LEAVES:
                return ANSI_GREEN;
            case STONE:
                return ANSI_GRAY;
            case IRON_ORE:
                return ANSI_YELLOW;
            case EMERALD_ORE:
                return ANSI_EMERALD_GREEN;
            case COAL_ORE:
                return ANSI_COAL_GRAY;
            default:
                return "";
        }
    }

    // FLOWCHART & PSEUDOCODE: Anton
    /**
    * Waits for input ENTER.
    * <p>
    * This method waits for player to input ENTER.
    */
    private static void waitForEnter() {
        System.out.println("Press Enter to continue...");
        JavaCraft.scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    // FLOWCHART & PSEUDOCODE: Tristan
    /**
    * Returns human readble item name.
    * <p>
    * This method returns a human readable item name for craftedItem.
    * @param  craftedItem The crafted item 
    * @return String      The human readable name of craftedItem
    */
    private static String getCraftedItemName(int craftedItem) {
        switch (craftedItem) {
            case CRAFTED_WOODEN_PLANKS:
                return "Wooden Planks";
            case CRAFTED_STICK:
                return "Stick";
            case CRAFTED_IRON_INGOT:
                return "Iron Ingot";
            default:
                return "Unknown";
        }
    }

    // FLOWCHART & PSEUDOCODE: Anton
    /**
    * Returns item color.
    * <p>
    * This method returns the items color.
    * <p>
    * Defaults to empty String
    * @param  craftedItem The crafted item 
    * @return String      The human readable name of craftedItem
    */
    private static String getCraftedItemColor(int craftedItem) {
        switch (craftedItem) {
            case CRAFTED_WOODEN_PLANKS:
            case CRAFTED_STICK:
            case CRAFTED_IRON_INGOT:
                return ANSI_BROWN;
            default:
                return "";
        }
    }

    // FLOWCHART & PSEUDOCODE: Leopold
    /**
    * Gets country and quote from server.
    * <p>
    * This method gets country and quote from server via a POST request.
    * <p>
    * <b>Catched Exceptions:</b>
    * <ul>
    *     <li>On Exception: Prints an error for any encountered exception.</li>
    * </ul>
    */
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
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));
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
