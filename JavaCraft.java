import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class JavaCraft {//
  private static final int AIR = 0;
  private static final int WOOD = 1;
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int IRON_ORE = 4;
  private static final int GOLD_ORE = 8;
  private static final int DIAMOND_ORE = 9;
  private static final int STAR = 10;
  private static int NEW_WORLD_WIDTH = 80;
  private static int NEW_WORLD_HEIGHT = 40;
  private static int EMPTY_BLOCK = 0;
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;
  private static final int CRAFT_GOLD_INGOT = 103;
  private static final int CRAFT_DIAMOND_INGOT = 104;
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;
  private static final int CRAFTED_GOLD_INGOT = 203;
  private static final int CRAFTED_DIAMOND_INGOT = 204;
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
      "8 - Gold ore block\n" +
      "9 - Diamond ore block\n" +
      "10 - Gold Ingot (Crafted Item)" +
      "11 - Diamond Ingot (Crafted Item)";
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
    initGame(80, 39);
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
        if (randValue < 10) {
          world[x][y] = WOOD;
        } else if (randValue < 20) {
          world[x][y] = LEAVES;
        } else if (randValue < 25) {
          world[x][y] = STONE;
        } else if (randValue < 35) {
          world[x][y] = IRON_ORE;
        } else if (randValue < 40) {
          world[x][y] = GOLD_ORE;
        } else if (randValue < 45) {
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
        blockColor = ANSI_CYAN;
        break;
      case STAR:
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
        return '\u2593';
      case LEAVES:
        return '\u0026';
      case STONE:
        return '\u2593';
      case IRON_ORE:
        /* return '\u00B0'; */
        return '\u2593';
      case GOLD_ORE:
        return '\u0040';
      case DIAMOND_ORE:
        return '\u0024';
      case STAR:
        return '\u25A0';
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
    for (int blockType = 1; blockType <= 4; blockType++) {
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

  private static void generateEmptyWorld() {
    world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
    int redBlock = 1;
    int whiteBlock = 4;
    int blueBlock = 3;
    int stripeHeight = NEW_WORLD_HEIGHT / 13; // Divide the height into three equal parts

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
    for (int y = stripeHeight * 2; y < stripeHeight * 3; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = redBlock;
      }
    }
    for (int y = stripeHeight * 3; y < stripeHeight * 4; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = whiteBlock;
      }
    }
    for (int y = stripeHeight * 4; y < stripeHeight * 5; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = redBlock;
      }
    }
    for (int y = stripeHeight * 5; y < stripeHeight * 6; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = whiteBlock;
      }
    }
    for (int y = stripeHeight * 6; y < stripeHeight * 7; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = redBlock;
      }
    }
    for (int y = stripeHeight * 7; y < stripeHeight * 8; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = whiteBlock;
      }
    }
    for (int y = stripeHeight * 8; y < stripeHeight * 9; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = redBlock;
      }
    }
    for (int y = stripeHeight * 9; y < stripeHeight * 10; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = whiteBlock;
      }
    }
    for (int y = stripeHeight * 10; y < stripeHeight * 11; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = redBlock;
      }
    }
    for (int y = stripeHeight * 11; y < stripeHeight * 12; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = whiteBlock;
      }
    }
    for (int y = stripeHeight * 12; y < stripeHeight * 13; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = redBlock;
      }
    }
    int bluePart = NEW_WORLD_HEIGHT / 2;
    int bluePart1 = NEW_WORLD_WIDTH / 3;

    for (int y = 0; y <= bluePart; y++) {
      for (int x = 0; x <= bluePart1 + 8; x++) {
        world[x][y] = blueBlock;
      }
    }

    for (int y = 0; y <= bluePart; y++) {
      for (int x = 0; x <= bluePart1 + 8; x++) {
        world[2][1] = STAR;
        world[8][1] = STAR;
        world[14][1] = STAR;
        world[20][1] = STAR;
        world[26][1] = STAR;
        world[32][1] = STAR;
        world[1][2] = STAR;
        world[2][2] = STAR;
        world[3][2] = STAR;
        world[7][2] = STAR;
        world[8][2] = STAR;
        world[9][2] = STAR;
        world[13][2] = STAR;
        world[14][2] = STAR;
        world[15][2] = STAR;
        world[19][2] = STAR;
        world[20][2] = STAR;
        world[21][2] = STAR;
        world[25][2] = STAR;
        world[26][2] = STAR;
        world[27][2] = STAR;
        world[31][2] = STAR;
        world[32][2] = STAR;
        world[33][2] = STAR;
        world[2][3] = STAR;
        world[8][3] = STAR;
        world[14][3] = STAR;
        world[20][3] = STAR;
        world[26][3] = STAR;
        world[32][3] = STAR;
        world[5][3] = STAR;
        world[11][3] = STAR;
        world[17][3] = STAR;
        world[23][3] = STAR;
        world[29][3] = STAR;
        world[4][4] = STAR;
        world[5][4] = STAR;
        world[6][4] = STAR;
        world[10][4] = STAR;
        world[11][4] = STAR;
        world[12][4] = STAR;
        world[16][4] = STAR;
        world[17][4] = STAR;
        world[18][4] = STAR;
        world[22][4] = STAR;
        world[23][4] = STAR;
        world[24][4] = STAR;
        world[28][4] = STAR;
        world[29][4] = STAR;
        world[30][4] = STAR;
        world[5][5] = STAR;
        world[11][5] = STAR;
        world[17][5] = STAR;
        world[23][5] = STAR;
        world[29][5] = STAR;
        world[2][5] = STAR;
        world[8][5] = STAR;
        world[14][5] = STAR;
        world[20][5] = STAR;
        world[26][5] = STAR;
        world[32][5] = STAR;
        world[1][6] = STAR;
        world[2][6] = STAR;
        world[3][6] = STAR;
        world[7][6] = STAR;
        world[8][6] = STAR;
        world[9][6] = STAR;
        world[13][6] = STAR;
        world[14][6] = STAR;
        world[15][6] = STAR;
        world[19][6] = STAR;
        world[20][6] = STAR;
        world[21][6] = STAR;
        world[25][6] = STAR;
        world[26][6] = STAR;
        world[27][6] = STAR;
        world[31][6] = STAR;
        world[32][6] = STAR;
        world[33][6] = STAR;
        world[2][7] = STAR;
        world[8][7] = STAR;
        world[14][7] = STAR;
        world[20][7] = STAR;
        world[26][7] = STAR;
        world[32][7] = STAR;
        world[5][7] = STAR;
        world[11][7] = STAR;
        world[17][7] = STAR;
        world[23][7] = STAR;
        world[29][7] = STAR;
        world[4][8] = STAR;
        world[5][8] = STAR;
        world[6][8] = STAR;
        world[10][8] = STAR;
        world[11][8] = STAR;
        world[12][8] = STAR;
        world[16][8] = STAR;
        world[17][8] = STAR;
        world[18][8] = STAR;
        world[22][8] = STAR;
        world[23][8] = STAR;
        world[24][8] = STAR;
        world[28][8] = STAR;
        world[29][8] = STAR;
        world[30][8] = STAR;
        world[5][9] = STAR;
        world[11][9] = STAR;
        world[17][9] = STAR;
        world[23][9] = STAR;
        world[29][9] = STAR;
        world[2][9] = STAR;
        world[8][9] = STAR;
        world[14][9] = STAR;
        world[20][9] = STAR;
        world[26][9] = STAR;
        world[32][9] = STAR;
        world[1][10] = STAR;
        world[2][10] = STAR;
        world[3][10] = STAR;
        world[7][10] = STAR;
        world[8][10] = STAR;
        world[9][10] = STAR;
        world[13][10] = STAR;
        world[14][10] = STAR;
        world[15][10] = STAR;
        world[19][10] = STAR;
        world[20][10] = STAR;
        world[21][10] = STAR;
        world[25][10] = STAR;
        world[26][10] = STAR;
        world[27][10] = STAR;
        world[31][10] = STAR;
        world[32][10] = STAR;
        world[33][10] = STAR;
        world[2][11] = STAR;
        world[8][11] = STAR;
        world[14][11] = STAR;
        world[20][11] = STAR;
        world[26][11] = STAR;
        world[32][11] = STAR;
        world[5][11] = STAR;
        world[11][11] = STAR;
        world[17][11] = STAR;
        world[23][11] = STAR;
        world[29][11] = STAR;
        world[4][12] = STAR;
        world[5][12] = STAR;
        world[6][12] = STAR;
        world[10][12] = STAR;
        world[11][12] = STAR;
        world[12][12] = STAR;
        world[16][12] = STAR;
        world[17][12] = STAR;
        world[18][12] = STAR;
        world[22][12] = STAR;
        world[23][12] = STAR;
        world[24][12] = STAR;
        world[28][12] = STAR;
        world[29][12] = STAR;
        world[30][12] = STAR;
        world[5][13] = STAR;
        world[11][13] = STAR;
        world[17][13] = STAR;
        world[23][13] = STAR;
        world[29][13] = STAR;
        world[2][13] = STAR;
        world[8][13] = STAR;
        world[14][13] = STAR;
        world[20][13] = STAR;
        world[26][13] = STAR;
        world[32][13] = STAR;
        world[1][14] = STAR;
        world[2][14] = STAR;
        world[3][14] = STAR;
        world[7][14] = STAR;
        world[8][14] = STAR;
        world[9][14] = STAR;
        world[13][14] = STAR;
        world[14][14] = STAR;
        world[15][14] = STAR;
        world[19][14] = STAR;
        world[20][14] = STAR;
        world[21][14] = STAR;
        world[25][14] = STAR;
        world[26][14] = STAR;
        world[27][14] = STAR;
        world[31][14] = STAR;
        world[32][14] = STAR;
        world[33][14] = STAR;
        world[2][15] = STAR;
        world[8][15] = STAR;
        world[14][15] = STAR;
        world[20][15] = STAR;
        world[26][15] = STAR;
        world[32][15] = STAR;
        world[5][15] = STAR;
        world[11][15] = STAR;
        world[17][15] = STAR;
        world[23][15] = STAR;
        world[29][15] = STAR;
        world[4][16] = STAR;
        world[5][16] = STAR;
        world[6][16] = STAR;
        world[10][16] = STAR;
        world[11][16] = STAR;
        world[12][16] = STAR;
        world[16][16] = STAR;
        world[17][16] = STAR;
        world[18][16] = STAR;
        world[22][16] = STAR;
        world[23][16] = STAR;
        world[24][16] = STAR;
        world[28][16] = STAR;
        world[29][16] = STAR;
        world[30][16] = STAR;
        world[5][17] = STAR;
        world[11][17] = STAR;
        world[17][17] = STAR;
        world[23][17] = STAR;
        world[29][17] = STAR;
        world[2][17] = STAR;
        world[8][17] = STAR;
        world[14][17] = STAR;
        world[20][17] = STAR;
        world[26][17] = STAR;
        world[32][17] = STAR;
        world[1][18] = STAR;
        world[2][18] = STAR;
        world[3][18] = STAR;
        world[7][18] = STAR;
        world[8][18] = STAR;
        world[9][18] = STAR;
        world[13][18] = STAR;
        world[14][18] = STAR;
        world[15][18] = STAR;
        world[19][18] = STAR;
        world[20][18] = STAR;
        world[21][18] = STAR;
        world[25][18] = STAR;
        world[26][18] = STAR;
        world[27][18] = STAR;
        world[31][18] = STAR;
        world[32][18] = STAR;
        world[33][18] = STAR;
        world[2][19] = STAR;
        world[8][19] = STAR;
        world[14][19] = STAR;
        world[20][19] = STAR;
        world[26][19] = STAR;
        world[32][19] = STAR;
      }
    }
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

  public static void placeBlock(int blockType) {
    if (blockType >= 0 && blockType <= 7) {
      if (blockType <= 4) {
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
      case CRAFTED_GOLD_INGOT:
        return 8;
      case CRAFTED_DIAMOND_INGOT:
        return 9;
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
        return CRAFTED_GOLD_INGOT;
      case 9:
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
    System.out.println("4. Craft Gold Ingot: 1 Iron Ore and 1 Wood");
    System.out.println("5. Craft Diamond Ingot: 2 Gold Ore and 1 Iron Ore");
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
        craftGoldIngot();
        break;
      case 5:
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

  public static void craftDiamondIngot() {
    if (inventoryContains(GOLD_ORE, 2)) {
      removeItemsFromInventory(GOLD_ORE, 2);
    } else {
      System.out.println("Insufficient resources to craft Diamond Ingot");
    }
    if (inventoryContains(IRON_ORE, 1)) {
      removeItemsFromInventory(IRON_ORE, 1);
      addCraftedItem(CRAFTED_DIAMOND_INGOT);
      System.out.println("Crafted Diamond Ingot");
    } else {
      System.out.println("Insufficient resources to craft Diamond Ingot");
    }
  }

  public static void craftGoldIngot() {
    if (inventoryContains(GOLD_ORE, 1)) {
      removeItemsFromInventory(GOLD_ORE, 1);
    } else {
      System.out.println("Insufficient resources to craft Gold Ingot");
    }
    if (inventoryContains(WOOD, 1)) {
      removeItemsFromInventory(WOOD, 1);
      addCraftedItem(CRAFTED_GOLD_INGOT);
      System.out.println("Crafted Gold Ingot");
    } else {
      System.out.println("Insufficient resources to craft Gold Ingot");
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
      case GOLD_ORE:
        System.out.println("You mine gold ore from the ground");
        inventory.add(GOLD_ORE);
        break;
      case DIAMOND_ORE:
        System.out.println("You mine diamond ore from the ground");
        inventory.add(DIAMOND_ORE);
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
    System.out.println(ANSI_GREEN + "\u0026\u0026 - Leaves block");
    System.out.println(ANSI_BLUE + "\u2593\u2593 - Stone block");
    /* System.out.println(ANSI_WHITE + "\u00B0\u00B0 - Iron ore block"); */
    System.out.println(ANSI_WHITE + "\u2592\u2592 - Iron ore block");
    System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
    System.out.println(ANSI_YELLOW + "\u0040\u0040 - Gold ore block");
    System.out.println(ANSI_CYAN + "\u0024\u0024 - Diamond ore block");
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
        return ANSI_WHITE;
      case GOLD_ORE:
        return ANSI_YELLOW;
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
      String payload = "{\"group_number\": \"34\",\r\n" + //
          "            \"group_name\": \"area34\",\r\n" + //
          "            \"difficulty_level\": \"hard\" }";
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