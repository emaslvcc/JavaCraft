import java.util.*;
import java.net.*;
import java.io.*;

public class JavaCraftMod {
  private static final int AIR = 0;
  private static final int WOOD = 1;
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int IRON_ORE = 4;
  private static final int DIAMOND_ORE = 5;
  private static final int GOLD_ORE = 6;
  private static int NEW_WORLD_WIDTH = 50;
  private static int NEW_WORLD_HEIGHT = 30;
  private static int EMPTY_BLOCK = 0;
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;
  private static final int CRAFTED_GOLD_RING = 203;
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
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_BG_BLUE = "\u001B[44m";
  public static final String ANSI_BG_WHITE = "\u001B[47m";

  private static final String BLOCK_NUMBERS_INFO = "Block Numbers:\n" +
      "0 - Empty block\n" +
      "1 - Wood block\n" +
      "2 - Leaves block\n" +
      "3 - Stone block\n" +
      "4 - Iron ore block\n" +
	    "5 - Diamond ore block\n" +
	    "6 - Gold ore block\n" +
      "7 - Wooden Planks (Crafted Item)\n" +
      "8 - Stick (Crafted Item)\n" +
      "9 - Iron Ingot (Crafted Item)\n" +
      "10 - Gold ring (Crafted Item)\n" +
      specialBlockNumberInfo();

  public static String specialBlockNumberInfo() {
    String result = "";
    for (int i = 0; i < 100000; i++) {
      String symbol = getBlockNameSpecial(i);
      if (symbol != null) {
        result += i + " - " + symbol + ANSI_RESET + "\n";
      }
    }
    return result;
  }
    
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
    JavaCraftMod.worldWidth = worldWidth;
    JavaCraftMod.worldHeight = worldHeight;
    JavaCraftMod.world = new int[worldWidth][worldHeight];
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
          world[x][y] = GOLD_ORE;
        }
        else if (randValue < 15) {
          world[x][y] = DIAMOND_ORE;
        } else if (randValue < 30) {
          world[x][y] = WOOD;
        }
        else if (randValue < 45) {
          world[x][y] = LEAVES;
        } else if (randValue < 60) {
          world[x][y] = STONE;
        } else if (randValue < 80) {
          world[x][y] = IRON_ORE;
        }  else {
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
          System.out.print(ANSI_RESET + ANSI_GREEN + "P " + ANSI_RESET);
        } else if (x == playerX && y == playerY && inSecretArea) {
          System.out.print(ANSI_RESET + ANSI_BLUE + "P " + ANSI_RESET);
        } else {
          System.out.print(getBlockSymbol(world[x][y]));
        }
      }
      System.out.println(ANSI_RESET + "║");
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
      case DIAMOND_ORE:
        blockColor = ANSI_BLUE;
        break;
      case GOLD_ORE:
        blockColor = ANSI_YELLOW;
        break;
      default:
        String special = getSpecialBlockSymmbol(blockType);
        if (special != null) {
          return special;
        }
        blockColor = ANSI_RESET;
        break;
    }
    return ANSI_RESET + blockColor + getBlockChar(blockType) + " ";
  }

  private static String getSpecialBlockSymmbol(int blockType) {
    switch(blockType) {
      case 'o' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + "  ";
      case 'm' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + "▄█";
      case 'e' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + "█ ";
      case 'f' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + " █";
      case 'n' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + "█▄";
      case 'k' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + "█▀";
      case 'd' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + " ▄";
      case 'p' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + "██";
      case 'a' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + "▄ ";
      case 'l' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + "▀█";
      case 'b' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + "▀ ";
      case 'c' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLACK + " ▀";

      case 'h' * 100 + 2: return ANSI_BG_WHITE + ANSI_RED + "▄▄";
      case 'm' * 100 + 2: return ANSI_BG_WHITE + ANSI_RED + "▄█";
      case 'p' * 100 + 2: return ANSI_BG_WHITE + ANSI_RED + "██";
      case 'n' * 100 + 2: return ANSI_BG_WHITE + ANSI_RED + "█▄";
      case 'd' * 100 + 2: return ANSI_BG_WHITE + ANSI_RED + " ▄";
      case 'a' * 100 + 2: return ANSI_BG_WHITE + ANSI_RED + "▄ ";
      case 'e' * 100 + 2: return ANSI_BG_WHITE + ANSI_RED + "█ ";
      case 'b' * 100 + 2: return ANSI_BG_WHITE + ANSI_RED + "▀ ";

      case 'd' * 100 + 3: return ANSI_BG_WHITE + ANSI_BLUE + " ▄";
      case 'f' * 100 + 3: return ANSI_BG_WHITE + ANSI_BLUE + " █";
      case 'p' * 100 + 3: return ANSI_BG_WHITE + ANSI_BLUE + "██";
      case 'c' * 100 + 3: return ANSI_BG_WHITE + ANSI_BLUE + " ▀";
      case 'l' * 100 + 3: return ANSI_BG_WHITE + ANSI_BLUE + "▀█";
      case 'b' * 100 + 3: return ANSI_BG_WHITE + ANSI_BLUE + "▀ ";
      case 'k' * 100 + 3: return ANSI_BG_WHITE + ANSI_BLUE + "█▀";
      case 'g' * 100 + 3: return ANSI_BG_WHITE + ANSI_BLUE + "▀▀";

      case 'c' * 100 + 4: return ANSI_BG_BLUE + ANSI_RED + " ▀";
      case 'k' * 100 + 4: return ANSI_BG_BLUE + ANSI_RED + "█▀";
      case 'g' * 100 + 4: return ANSI_BG_BLUE + ANSI_RED + "▀▀";
      case 'l' * 100 + 4: return ANSI_BG_BLUE + ANSI_RED + "▀█";
      case 'b' * 100 + 4: return ANSI_BG_BLUE + ANSI_RED + "▀ ";
      case 'f' * 100 + 4: return ANSI_BG_BLUE + ANSI_RED + " █";

      case 's' * 100 + 1: return ANSI_BG_WHITE + ANSI_BLUE + "▄" + ANSI_RED + "█";
      case 's' * 100 + 2: return ANSI_BLUE + "█" + ANSI_BG_WHITE + ANSI_RED + "▀";
    }

    return null;
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
      case DIAMOND_ORE:
        return '\03';
      case GOLD_ORE:
        return '\04';
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

  private static void generateEmptyWorld() {
    world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
    worldWidth = 50;
    worldHeight = 30;
    
    for (int i = 0; i < NEW_WORLD_HEIGHT; i++) {
      String line = "";
      switch(i) {
          case 4:
              line += "O1".repeat(12);
              line += "M1E1";
              line += "O1".repeat(22);
              line += "F1N1";
              line += "O1".repeat(12);
              break;
          case 5:
              line += "O1".repeat(11);
              line += "M1K1D1P1";
              line += "o1".repeat(20);
              line += "p1a1l1n1";
              line += "O1".repeat(11);
              break;
          case 6:
              line += "o1".repeat(10);
              line += "d1p1d1p1b1m1e1";
              line += "o1".repeat(16);
              line += "f1n1c1p1a1k1o1";
              line += "o1".repeat(10);
              break;
          case 7:
              line += "o1".repeat(10);
              line += "p1b1m1e1m1k1";
              line += "o1".repeat(18);
              line += "l1n1f1n1o1m1";
              line += "o1".repeat(10);
              break;
          case 8:
              line += "o1".repeat(9);
              line += "m1k1f1k1d1p1";
              line += "o1".repeat(7);
              line += "h2m2p2p2n2h2";
              line += "o1".repeat(7);
              line += "k1o1l1e1l1n1";
              line += "o1".repeat(9);
              break;
          case 9:
              line += "o1".repeat(8);
              line += "f1k1d1p1b1p1b1";
              line += "o1".repeat(4);
              line += "d2m2p2p2p2p2p2p2p2p2n2a2";
              line += "o1".repeat(4);
              line += "o1m1c1p1a1l1e1";
              line += "o1".repeat(8);
              break;
          case 10:
              line += "o1".repeat(10);
              line += "p1b1m1k1";
              line += "o1".repeat(4);
              line += "d2p2p2p2p2p2p2p2p2p2p2p2p2a2";
              line += "o1".repeat(4);
              line += "l1n1c1p1";
              line += "o1".repeat(10);
              break;
          case 11:
              line += "o1".repeat(11);
              line += "f1k1";
              line += "o1".repeat(4);
              line += "d2p2p2p2p2p2p2p2p2p2p2p2p2p2p2a2";
              line += "o1".repeat(4);
              line += "l1e1";
              line += "o1".repeat(11);
              break;

          case 12:
              line += "o1".repeat(17);
              line += "s1p2p2p2p2p2p2p2p2p2p2p2p2p2p2n2";
              line += "o1".repeat(17);
              break;
          case 13:
              line += "o1".repeat(16);
              line += "d3c4p2p2p2p2p2p2p2p2p2k4g4g4l4p2p2a2";
              line += "o1".repeat(16);
              break;
          case 14:
              line += "o1".repeat(16);
              line += "f3p3l4p2p2p2p2p2p2g4p3p3p3p3p3l4p2e2";
              line += "o1".repeat(16);
              break;
          case 15:
              line += "o1".repeat(16);
              line += "f3p3p3l4p2p2p2p2g4p3p3p3p3p3p3p3p2e2";
              line += "o1".repeat(16);
              break;
          case 16:
              line += "o1".repeat(16);
              line += "c3p3p3p3g4g4b4p3p3p3p3p3p3p3p3p3f4b2";
              line += "o1".repeat(16);
              break;
          case 17:
              line += "o1".repeat(17);
              line += "l3p3p3p3p3p3p3p3p3p3p3p3p3p3p3s2";
              line += "o1".repeat(17);
              break;
          case 30-1-11:
              line += "o1".repeat(11);
              line += "f1n1";
              line += "o1".repeat(4);
              line += "c3p3p3p3p3p3p3p3p3p3p3p3p3p3p3b3";
              line += "o1".repeat(4);
              line += "m1e1";
              line += "o1".repeat(11);
              break;
          case 30-1-10:
              line += "o1".repeat(10);
              line += "p1a1l1n1";
              line += "o1".repeat(4);
              line += "c3p3p3p3p3p3p3p3p3p3p3p3p3b3";
              line += "o1".repeat(4);
              line += "m1k1d1p1";
              line += "o1".repeat(10);
              break;
          case 30-1-9:
              line += "o1".repeat(8);
              line += "f1n1c1p1a1p1a1";
              line += "o1".repeat(4);
              line += "c3l3p3p3p3p3p3p3p3p3k3b3";
              line += "o1".repeat(4);
              line += "o1l1d1p1b1m1e1";
              line += "o1".repeat(8);
              break;
          case 30-1-8:
              line += "o1".repeat(9);
              line += "l1n1f1b1c1p1";
              line += "o1".repeat(7);
              line += "g3l3p3p3k3g3";
              line += "o1".repeat(7);
              line += "n1o1c1e1m1k1";
              line += "o1".repeat(9);
              break;
          case 30-1-7:
              line += "o1".repeat(10);
              line += "p1a1d1e1l1n1";
              line += "o1".repeat(18);
              line += "m1k1f1a1o1l1";
              line += "o1".repeat(10);
              break;
          case 30-1-6:
              line += "o1".repeat(10);
              line += "c1p1c1p1a1l1e1";
              line += "o1".repeat(16);
              line += "f1k1d1p1b1n1o1";
              line += "o1".repeat(10);
              break;
          case 30-1-5:
              line += "O1".repeat(11);
              line += "l1n1c1P1";
              line += "o1".repeat(20);
              line += "p1b1m1k1";
              line += "O1".repeat(11);
              break;
          case 30-1-4:
              line += "O1".repeat(12);
              line += "l1E1";
              line += "O1".repeat(22);
              line += "F1k1";
              line += "O1".repeat(12);
              break;
          default:
              line += "O1".repeat(NEW_WORLD_WIDTH);
              break;
      }

      for (int x = 0; x < 50; x++) {
        placeSpecialBlock(line, x, i);
      }
    }
  }

  private static void placeSpecialBlock(String line, int x, int y) {
    char c = ("" + line.charAt(x * 2)).toLowerCase().charAt(0);
    int num = line.charAt(x * 2 + 1) - '0';

    world[x][y] = c * 100 + num;
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
    if (blockType >= 0 && blockType <= 10) {
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
        return 7;
      case CRAFTED_STICK:
        return 8;
      case CRAFTED_IRON_INGOT:
        return 9;
      case CRAFTED_GOLD_RING:
        return 10;
      default:
        return -1;
    }
  }

  private static int getCraftedItemFromBlockType(int blockType) {
    switch (blockType) {
      case 7:
        return CRAFTED_WOODEN_PLANKS;
      case 8:
        return CRAFTED_STICK;
      case 9:
        return CRAFTED_IRON_INGOT;
      case 10: 
        return CRAFTED_GOLD_RING;
      default:
        return -1;
    }
  }

  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    System.out.println("4. Craft Gold Ring: 1 Gold 1 Diamond");
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
        craftgoldring();
        break;
      default:
        System.out.println("Invalid recipe number.");
    }
    waitForEnter();
  }

  public static void craftgoldring() {
    if (inventoryContains(GOLD_ORE, 1) && inventoryContains(DIAMOND_ORE, 1)) {
      removeItemsFromInventory(GOLD_ORE, 1);
      removeItemsFromInventory(DIAMOND_ORE, 1);
      addCraftedItem(CRAFTED_GOLD_RING);
      System.out.println("Crafted Gold Ring.");
    } else {
      System.out.println("Insufficient resources to craft Gold Ring.");
    }
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
      case DIAMOND_ORE:
        System.out.println("You mine diamond ore from the ground.");
        inventory.add(DIAMOND_ORE);
        break;
      case GOLD_ORE:
        System.out.println("You mine gold ore from the ground.");
        inventory.add(GOLD_ORE);
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
      case DIAMOND_ORE:
        return "Diamond Ore";
      case GOLD_ORE:
        return "Gold Ore";
      default:
        String special = getBlockNameSpecial(blockType);
        if (special != null) { 
          return special;
        }
        return "Unknown";
    }
  }

  public static String getBlockNameSpecial(int blockType) {
    switch(blockType) {
      case 'o' * 100 + 1: return "Paper";
      case 'm' * 100 + 1: return "Knife";
      case 'e' * 100 + 1: return "Bible";
      case 'f' * 100 + 1: return "Koran";
      case 'n' * 100 + 1: return "Crane";
      case 'k' * 100 + 1: return "Little football player";
      case 'd' * 100 + 1: return "Baseball";
      case 'p' * 100 + 1: return "TV Screen";
      case 'a' * 100 + 1: return "Football";
      case 'l' * 100 + 1: return "Spike";
      case 'b' * 100 + 1: return "Lamp";
      case 'c' * 100 + 1: return "Ceiling fan";

      case 'h' * 100 + 2: return "Blanket";
      case 'm' * 100 + 2: return "Waterbed";
      case 'p' * 100 + 2: return "Dollhouse";
      case 'n' * 100 + 2: return "Ikea chair";
      case 'd' * 100 + 2: return "Apple";
      case 'a' * 100 + 2: return "Umbrella";
      case 'e' * 100 + 2: return "Action figure";
      case 'b' * 100 + 2: return "Toy sun";

      case 'd' * 100 + 3: return "Vacuum cleaner";
      case 'f' * 100 + 3: return "Suit";
      case 'p' * 100 + 3: return "Paper mache";
      case 'c' * 100 + 3: return "Helicopter figurine";
      case 'l' * 100 + 3: return "Desk";
      case 'b' * 100 + 3: return "Floodlight";
      case 'k' * 100 + 3: return "Powerdrill";
      case 'g' * 100 + 3: return "2 floor bed";

      case 'c' * 100 + 4: return "Painting";
      case 'k' * 100 + 4: return "Yoga ball";
      case 'g' * 100 + 4: return "Blender";
      case 'l' * 100 + 4: return "Lamborghini minigifure";
      case 'b' * 100 + 4: return "Lego set";
      case 'f' * 100 + 4: return "Curtains";

      case 's' * 100 + 1: return "Doorset";
      case 's' * 100 + 2: return "Dog";
    }

    return null;
  }

  public static void displayLegend() {
    System.out.println(ANSI_BLUE + "Legend:");
    System.out.println(ANSI_WHITE + "-- - Empty block");
    System.out.println(ANSI_RED + "\u2592\u2592 - Wood block");
    System.out.println(ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
    System.out.println(ANSI_BLUE + "\u2593\u2593 - Stone block");
    System.out.println(ANSI_WHITE + "\u00B0\u00B0- Iron ore block");
    System.out.println(ANSI_BLUE + "\03\03 - Diamond ore block");
    System.out.println(ANSI_YELLOW + "\04\04 - Gold ore block");
    System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
    System.out.println(specialLegend());
  }

  public static String specialLegend() {
    String result = "";
    for (int i = 0; i < 100000; i++) {
      String symbol = getSpecialBlockSymmbol(i);
      String name = getBlockNameSpecial(i);
      if (symbol != null) {
        result += symbol + ANSI_RESET + " - " + name + ANSI_RESET + "\n";
      }
    }
    return result;
  }

  public static void displayInventory() {
    System.out.println("Inventory:");
    if (inventory.isEmpty()) {
      System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
    } else {
      int[] blockCounts = new int[100000];
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
      case DIAMOND_ORE:
        return ANSI_BLUE;
      case GOLD_ORE:
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
      case CRAFTED_GOLD_RING:
        return "Gold Ring";
      default:
        return "Unknown";
    }
  }

  private static String getCraftedItemColor(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
      case CRAFTED_STICK:
      case CRAFTED_GOLD_RING:
      case CRAFTED_IRON_INGOT:
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
      String payload = " { \"group_number\": \"48\", \"group_name\": \"group48\", \"difficulty_level\": \"hard\" }";
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
      int countryStart = json.indexOf(" ") + 11;
      int countryEnd = json.indexOf(" ", countryStart);
      String country = json.substring(countryStart, countryEnd);
      int quoteStart = json.indexOf(" ") + 9;
      int quoteEnd = json.indexOf(" ", quoteStart);
      String quote = json.substring(quoteStart, quoteEnd);
      quote = quote.replace(" ", " ");
      System.out.println(" " + country);
      System.out.println(" " + quote);
      int start = 123;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error connecting to the server");
    }
  }
}