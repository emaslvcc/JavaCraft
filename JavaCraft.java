import java.util.*;
import java.net.*;
import java.io.*;

public class JavaCraft {
    private static final int AIR = 0;
    private static final int WOOD = 1;
    private static final int LEAVES = 2;
    private static final int STONE = 3;
    private static final int IRON_ORE = 4;
    //NEW STUFF
    private static final int GOLD_ORE = 5;
    private static final int DIAMOND_ORE = 6;
    private static int NEW_WORLD_WIDTH = 25;
    private static int NEW_WORLD_HEIGHT = 15;
    private static int EMPTY_BLOCK = 0;
    private static final int CRAFT_WOODEN_PLANKS = 100;
    private static final int CRAFT_STICK = 101;
    private static final int CRAFT_IRON_INGOT = 102;
    private static final int CRAFT_GOLD_INGOT = 103;
    private static final int CRAFT_DIAMOND_INGOT = 104;
    private static final int CRAFT_IRON_PICKAXE = 105;
    private static final int CRAFTED_WOODEN_PLANKS = 200;
    private static final int CRAFTED_STICK = 201;
    private static final int CRAFTED_IRON_INGOT = 202;
    //NEW
    private static final int CRAFTED_GOLD_INGOT = 203;
    private static final int CRAFTED_DIAMOND_INGOT = 204;
    private static final int CRAFTED_IRON_PICKAXE = 205;
    private static final String ANSI_BROWN = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
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
            "5 - Gold ore block\n" +
            "6 - Diamond ore block\n" +
            "7 - Wooden Planks (Crafted Item)\n" +
            "8 - Stick (Crafted Item)\n" +
            "9 - Iron Ingot (Crafted Item)\n" +
            "10 - Iron Pickaxe (Crafted Item)\n" +
            "11 - Gold Ingot (Crafted Item)\n" +
            "12 - Diamond Ingot (Crafted Item)\n";
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
            System.out.println("Game not started. Goodbye!");
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
                if (randValue < 20) {
                    world[x][y] = WOOD;
                } else if (randValue < 30) {
                    world[x][y] = LEAVES;
                } else if (randValue < 40) {
                    world[x][y] = STONE;
                } else if (randValue < 60) {
                    world[x][y] = IRON_ORE;
                } else if (randValue < 70) {
                    world[x][y] = GOLD_ORE;
                } else if (randValue < 80) {
                    world[x][y] = DIAMOND_ORE;
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
            case GOLD_ORE:
                blockColor = ANSI_YELLOW;
                break;
            case DIAMOND_ORE:
                blockColor = ANSI_BLACK;
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
            case LEAVES:
                return '\u00A7';
            case STONE:
                return '\u2593';
            case IRON_ORE:
                return '\u00B0';
            case GOLD_ORE:
                return '\u0E51';
            case DIAMOND_ORE:
                return '\u09F0';
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
        generateEmptyWorld();
        playerX = worldWidth / 2;
        playerY = worldHeight / 2;
    }

    private static void changeColor(int y, int x, int color) {
        world[x * 2][y * 2] = color;
        world[x * 2 + 1][y * 2] = color;
        world[x * 2][y * 2 + 1] = color;
        world[x * 2 + 1][y * 2 + 1] = color;
    }

    private static void generateEmptyWorld() {
        NEW_WORLD_WIDTH = 50;
        NEW_WORLD_HEIGHT = 30;
        worldHeight = NEW_WORLD_HEIGHT;
        worldWidth = NEW_WORLD_WIDTH;
        world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
        int greenBlock = LEAVES;
        int yellowBlock = GOLD_ORE;
        int redBlock = WOOD;
        int blueBlock = STONE;
        int whiteBlock = IRON_ORE;
        int blackBlock = DIAMOND_ORE;

        //1
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 2) {
                changeColor(0, j, greenBlock);
            }
            else if(j < 3) {
                changeColor(0, j, whiteBlock);
            }
            else {
                changeColor(0, j, redBlock);
            }
        }

        //2
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 3) {
                changeColor(1, j, greenBlock);
            }
            else if(j < 4) {
                changeColor(1, j, whiteBlock);
            }
            else {
                changeColor(1, j, redBlock);
            }
        }

        //3
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 1) {
                changeColor(2, j, yellowBlock);
            }
            else if(j < 4) {
                changeColor(2, j, greenBlock);
            }
            else if(j < 5) {
                changeColor(2, j, whiteBlock);
            }
            else {
                changeColor(2, j, redBlock);
            }
        }

        //4
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 1) {
                changeColor(3, j, yellowBlock);
            }
            else if(j < 5) {
                changeColor(3, j, greenBlock);
            }
            else if(j < 6) {
                changeColor(3, j, whiteBlock);
            }
            else {
                changeColor(3, j, redBlock);
            }
        }

        //5
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 2) {
                changeColor(4, j, blackBlock);
            }
            else if(j < 3) {
                changeColor(4, j, yellowBlock);
            }
            else if(j < 6) {
                changeColor(4, j, greenBlock);
            }
            else if(j < 7) {
                changeColor(4, j, whiteBlock);
            }
            else {
                changeColor(4, j, redBlock);
            }
        }

        //6
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 3) {
                changeColor(5, j, blackBlock);
            }
            else if(j < 4) {
                changeColor(5, j, yellowBlock);
            }
            else if(j < 7) {
                changeColor(5, j, greenBlock);
            }
            else {
                changeColor(5, j, whiteBlock);
            }
        }

        //7
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 4) {
                changeColor(6, j, blackBlock);
            }
            else if(j < 5) {
                changeColor(6, j, yellowBlock);
            }
            else {
                changeColor(6, j, greenBlock);
            }
        }

        //8
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 5) {
                changeColor(7, j, blackBlock);
            }
            else if(j < 6) {
                changeColor(7, j, yellowBlock);
            }
            else {
                changeColor(7, j, greenBlock);
            }
        }

        //9
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 4) {
                changeColor(8, j, blackBlock);
            }
            else if(j < 5) {
                changeColor(8, j, yellowBlock);
            }
            else {
                changeColor(8, j, greenBlock);
            }
        }

        //10
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 3) {
                changeColor(9, j, blackBlock);
            }
            else if(j < 4) {
                changeColor(9, j, yellowBlock);
            }
            else if (j < 7) {
                changeColor(9, j, greenBlock);
            }
            else {
                changeColor(9, j, whiteBlock);
            }
        }

        //11
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 2) {
                changeColor(10, j, blackBlock);
            }
            else if(j < 3) {
                changeColor(10, j, yellowBlock);
            }
            else if (j < 6) {
                changeColor(10, j, greenBlock);
            }
            else if (j < 7) {
                changeColor(10, j, whiteBlock);
            }
            else {
                changeColor(10, j, blueBlock);
            }
        }

        //12
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 1) {
                changeColor(11, j, blackBlock);
            }
            else if(j < 2) {
                changeColor(11, j, yellowBlock);
            }
            else if (j < 5) {
                changeColor(11, j, greenBlock);
            }
            else if (j < 6) {
                changeColor(11, j, whiteBlock);
            }
            else {
                changeColor(11, j, blueBlock);
            }
        }

        //13
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 1) {
                changeColor(12, j, yellowBlock);
            }
            else if(j < 4) {
                changeColor(12, j, greenBlock);
            }
            else if (j < 5) {
                changeColor(12, j, whiteBlock);
            }
            else {
                changeColor(12, j, blueBlock);
            }
        }

        //14
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 3) {
                changeColor(13, j, greenBlock);
            }
            else if (j < 4) {
                changeColor(13, j, whiteBlock);
            }
            else {
                changeColor(13, j, blueBlock);
            }
        }

        //15
        for (int j = 0; j < NEW_WORLD_WIDTH / 2; j++) {
            if(j < 2) {
                changeColor(14, j, greenBlock);
            }
            else if (j < 3) {
                changeColor(14, j, whiteBlock);
            }
            else {
                changeColor(14, j, blueBlock);
            }
        }

    }

    /*
    private static void generateEmptyWorld() {
        //1
        System.out.print(ANSI_GREEN + "▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " ▒" + ANSI_RESET);
        System.out.println(ANSI_RED + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);
//2
        System.out.print(ANSI_GREEN + "▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " ▒" + ANSI_RESET);
        System.out.println(ANSI_RED + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);
//3
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " ▒" + ANSI_RESET);
        System.out.println(ANSI_RED + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);
//4
        System.out.print(ANSI_BLACK + " ▒" + ANSI_RESET);
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " ▒" + ANSI_RESET);
        System.out.println(ANSI_RED + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);
//5
        System.out.print(ANSI_BLACK + " ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " ▒" + ANSI_RESET);
        System.out.println(ANSI_RED + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);
//6
        System.out.print(ANSI_BLACK + " ▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "▒ ▒ ▒" + ANSI_RESET);
        System.out.println(ANSI_WHITE + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);
//7
        System.out.print(ANSI_BLACK + " ▒ ▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);
//8
        System.out.print(ANSI_BLACK + " ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);
//9
        System.out.print(ANSI_BLACK + " ▒ ▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);

//10
        System.out.print(ANSI_BLACK + " ▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "▒ ▒ ▒" + ANSI_RESET);
        System.out.println(ANSI_WHITE + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);
//11
        System.out.print(ANSI_BLACK + " ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " ▒" + ANSI_RESET);
        System.out.println(ANSI_BLUE + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);

//12
        System.out.print(ANSI_BLACK + " ▒" + ANSI_RESET);
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " ▒" + ANSI_RESET);
        System.out.println(ANSI_BLUE + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);

//13
        System.out.print(ANSI_YELLOW + " ▒" + ANSI_RESET);
        System.out.print(ANSI_GREEN + "▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " ▒" + ANSI_RESET);
        System.out.println(ANSI_BLUE + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);

//14
        System.out.print(ANSI_GREEN + "▒ ▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " ▒" + ANSI_RESET);
        System.out.println(ANSI_BLUE + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);

//15
        System.out.print(ANSI_GREEN + "▒ ▒" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " ▒" + ANSI_RESET);
        System.out.println(ANSI_BLUE + " ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒ ▒" + ANSI_RESET);

    }

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
            if ((blockType == DIAMOND_ORE || blockType == GOLD_ORE) && !craftedItemsContains(CRAFTED_IRON_PICKAXE, 1)) {
                System.out.println("Can not mine this");
            }
            else {
                inventory.add(blockType);
                world[playerX][playerY] = AIR;
                System.out.println("Mined " + getBlockName(blockType) + ".");
            }
        } else {
            System.out.println("No block to mine here.");
        }
        waitForEnter();
    }

    public static void placeBlock(int blockType) {
        if (blockType >= 0 && blockType <= 9) {
            if (blockType <= 6) {
                if (inventory.contains(blockType)) {
                    inventory.remove(Integer.valueOf(blockType));
                    world[playerX][playerY] = blockType;
                    System.out.println("Placed " + getBlockName(blockType) + " at your position.");
                } else {
                    System.out.println("You don't have " + getBlockName(blockType) + " in your inventory.");
                }
            } else {
                int craftedItem = getCraftedItemFromBlockType(blockType);
                if (craftedItems.contains(craftedItem)) {
                    craftedItems.remove(Integer.valueOf(craftedItem));
                    world[playerX][playerY] = blockType;
                    System.out.println("Placed " + getCraftedItemName(craftedItem) + " at your position.");
                } else {
                    System.out.println("You don't have " + getCraftedItemName(craftedItem) + " in your crafted items.");
                }
            }
        } else {
            System.out.println("Invalid block number. Please enter a valid block number.");
            System.out.println(BLOCK_NUMBERS_INFO);
        }
        waitForEnter();
    }

    private static int getBlockTypeFromCraftedItem(int craftedItem) {
        switch (craftedItem) {
            case CRAFTED_WOODEN_PLANKS:
                return 5;
            case CRAFTED_STICK:
                return 6;
            case CRAFTED_IRON_INGOT:
                return 7;
            case CRAFTED_IRON_PICKAXE:
                return 8;
            case CRAFTED_GOLD_INGOT:
                return 9;
            case CRAFTED_DIAMOND_INGOT:
                return 10;
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
                return CRAFTED_IRON_PICKAXE;
            case 9:
                return CRAFTED_GOLD_INGOT;
            case 10:
                return CRAFTED_DIAMOND_INGOT;
            default:
                return -1;
        }
    }

    public static void displayCraftingRecipes() {
        System.out.println("Crafting Recipes:");
        System.out.println("1. Craft Wooden Planks: 2 Wood");
        System.out.println("2. Craft Stick: 1 Wood");
        System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
        System.out.println("4. Craft Iron Pickaxe: 3 Iron Ore + 2 Sticks");
        System.out.println("5. Craft Gold Ingot: 3 Gold Ore");
        System.out.println("6. Craft Diamond Ingot: 3 Diamond Ore");
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
                craftIronPickaxe();
                break;
            case 5:
                craftGoldIngot();
                break;
            case 6:
                craftDiamondIngot();
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

    public static void craftIronPickaxe() {;
        if (inventoryContains(IRON_ORE, 3) && craftedItemsContains(CRAFTED_STICK, 2)) {
            removeItemsFromInventory(IRON_ORE, 3);
            removeItemsFromCraftedItems(CRAFTED_STICK, 2);
            addCraftedItem(CRAFTED_IRON_PICKAXE);
            System.out.println("Crafted Iron Pickaxe.");
        } else {
            System.out.println("Insufficient resources to craft Iron Pickaxe.");
        }
    }

    public static void craftGoldIngot() {
        if (inventoryContains(GOLD_ORE, 1)) {
            removeItemsFromInventory(GOLD_ORE, 1);
            addCraftedItem(CRAFTED_GOLD_INGOT);
            System.out.println("Crafted Gold Ingot.");
        } else {
            System.out.println("Insufficient resources to craft Gold Ingot.");
        }
    }

    public static void craftDiamondIngot() {
        if (inventoryContains(DIAMOND_ORE, 1)) {
            removeItemsFromInventory(DIAMOND_ORE, 1);
            addCraftedItem(CRAFTED_DIAMOND_INGOT);
            System.out.println("Crafted Diamond Ingot.");
        } else {
            System.out.println("Insufficient resources to craft Diamond Ingot.");
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

    public static boolean craftedItemsContains(int item, int count) {
        int itemCount = 0;
        for (int i : craftedItems) {
            if (i == item) {
                itemCount++;
                if (itemCount == count) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void removeItemsFromCraftedItems(int item, int count) {
        int removedCount = 0;
        Iterator<Integer> iterator = craftedItems.iterator();
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
            case DIAMOND_ORE:
                if (inventoryContains(CRAFTED_IRON_PICKAXE, 1)) {
                    System.out.println("You mine diamond ore from the ground.");
                    inventory.add(DIAMOND_ORE);
                }
                else
                    System.out.println("You can not mine this!");
                break;
            case GOLD_ORE:
                if (inventoryContains(CRAFTED_IRON_PICKAXE, 1)) {
                    System.out.println("You mine gold ore from the ground.");
                    inventory.add(GOLD_ORE);
                }
                else
                    System.out.println("You can not mine this!");
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
            case GOLD_ORE:
                return "Gold Ore";
            case DIAMOND_ORE:
                return "Diamond Ore";
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
        System.out.println(ANSI_WHITE + "\u00B0\u00B0 - Iron ore block");
        System.out.println(ANSI_YELLOW + "\u0E51\u0E51 - Gold ore block");
        System.out.println(ANSI_CYAN + "\u09F0\u09F0 - Diamond ore block");
        System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
    }

    public static void displayInventory() {
        System.out.println("Inventory:");
        if (inventory.isEmpty()) {
            System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
        } else {
            int[] blockCounts = new int[10];
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
                return ANSI_RED;
            case LEAVES:
                return ANSI_GREEN;
            case STONE:
                return ANSI_GRAY;
            case IRON_ORE:
                return ANSI_YELLOW;
            case GOLD_ORE:
                return ANSI_PURPLE;
            case DIAMOND_ORE:
                return ANSI_CYAN;
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
            case CRAFTED_IRON_PICKAXE:
                return "Iron Pickaxe";
            case CRAFTED_GOLD_INGOT:
                return "Gold Ingot";
            case CRAFTED_DIAMOND_INGOT:
                return "Diamond Ingot";
            default:
                return "Unknown";
        }
    }

    private static String getCraftedItemColor(int craftedItem) {
        switch (craftedItem) {
            case CRAFTED_WOODEN_PLANKS:
            case CRAFTED_STICK:
            case CRAFTED_IRON_INGOT:
            case CRAFTED_IRON_PICKAXE:
            case CRAFTED_GOLD_INGOT:
            case CRAFTED_DIAMOND_INGOT:
                return ANSI_BROWN;
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
            String payload = "{\"group_number\": \"70\",\n" +
                    "            \"group_name\": \"group70\",\n" +
                    "            \"difficulty_level\": \"hard\"}";
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