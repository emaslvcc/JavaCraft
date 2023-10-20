//v4 : updated the time it takes to break the items.

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class JavaCraft {

  private static final int AIR = 0;
  private static final int WOOD = 1;
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int IRON_ORE = 4;
  private static final int DIRT = 5;
  private static final int DIAMOND = 6;
  private static int NEW_WORLD_HEIGHT = 25;
  private static int NEW_WORLD_WIDTH = 50;
  private static int EMPTY_BLOCK = 0;
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;
  private static final int CRAFTED_DIAMOND_PICKAXE = 203;
  public static final String ANSI_BLINK = "\u001B[5m";
  public static final String ANSI_LIGHT_YELLOW = "\u001B[93m";
  public static final String ANSI_BOLD = "\u001B[1m";
  private static final String ANSI_BROWN = "\u001B[33m";
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_YELLOW = "\u001B[33m";
  private static final String ANSI_YELLOW_NICE = "\u001b[43m";
  private static final String ANSI_CYAN = "\u001B[36m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_PURPLE = "\u001B[35m";
  private static final String ANSI_BLUE = "\u001B[34m";
  private static final String ANSI_GRAY = "\u001B[37m";
  private static final String ANSI_WHITE = "\u001B[97m";

  private static final String BLOCK_NUMBERS_INFO =
    "Block Numbers:\n" +
    "0 - Empty block\n" +
    "1 - Wood block\n" +
    "2 - Leaves block\n" +
    "3 - Stone block\n" +
    "4 - Iron ore block\n" +
    "5 - Dirt block\n" +
    "6 - Diamond block\n" +
    "7 - Wooden Planks (Crafted Item)\n" +
    "8 - Diamond Pickaxe (Crafted Item)\n" +
    "9 - Stick (Crafted Item)\n" +
    "10 - Iron Ingot (Crafted Item)";
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

  static int centerX = 90;
  static int centerY = 21;
  static int starX;
  static int starY;

  public static void main(String[] args) {
    initGame(51, 180);
    generateWorld();
    System.out.println(
      ANSI_GREEN + "Welcome to Simple Minecraft!" + ANSI_RESET
    );
    System.out.println("Instructions:");
    System.out.println(
      " - Use 'W', 'A', 'S', 'D', or arrow keys to move the player."
    );
    System.out.println(
      " - Press 'M' to mine the block at your position and add it to your inventory."
    );
    System.out.println(
      " - Press 'P' to place a block from your inventory at your position."
    );
    System.out.println(
      " - Press 'C' to view crafting recipes and 'I' to interact with elements in the world."
    );
    System.out.println(
      " - Press 'Save' to save the game state and 'Load' to load a saved game state."
    );
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

    scanner.close();
  }

  public static void initGame(int worldHeight, int worldWidth) {
    JavaCraft.worldWidth = worldWidth;
    JavaCraft.worldHeight = worldHeight;
    JavaCraft.world = new int[worldHeight][worldWidth];
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;
    inventory = new ArrayList<>();
  }

  public static void generateWorld() {
    Random rand = new Random();
    for (int y = 0; y < worldHeight; y++) {
      for (int x = 0; x < worldWidth; x++) {
        int randValue = rand.nextInt(100);
        if (randValue < 1) {
          world[y][x] = DIAMOND;
        } else if (randValue < 20) {
          world[y][x] = WOOD;
        } else if (randValue < 35) {
          world[y][x] = LEAVES;
        } else if (randValue < 50) {
          world[y][x] = STONE;
        } else if (randValue < 70) {
          world[y][x] = IRON_ORE;
        } else if (randValue < 80) {
          world[y][x] = DIRT;
        } else {
          world[y][x] = AIR;
        }
      }
    }
  }

  public static void displayWorld() {
    System.out.println(ANSI_CYAN + "World Map:" + ANSI_RESET);
    System.out.println("╔══" + "═".repeat(worldWidth - 2) + "╗");
    for (int y = 0; y < worldHeight; y++) {
      System.out.print("║");
      for (int x = 0; x < worldWidth; x++) {
        if (x == playerX && y == playerY && !inSecretArea) {
          System.out.print(ANSI_WHITE + ANSI_BOLD + "P" + ANSI_RESET);
        } else if (x == playerX && y == playerY && inSecretArea) {
          System.out.print(ANSI_BLUE + ANSI_BOLD + "P" + ANSI_RESET);
        } else {
          System.out.print(getBlockSymbol(world[y][x]));
        }
      }
      System.out.println("║");
    }
    System.out.println("╚══" + "═".repeat(worldWidth - 2) + "╝");
  }

  private static String getBlockSymbol(int blockType) {
    String blockColor;
    // System.out.print(blockType);
    switch (blockType) {
      case AIR:
        return ANSI_RESET + "-";
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
      case DIRT:
        blockColor = ANSI_LIGHT_YELLOW;
        break;
      case DIAMOND:
        blockColor = ANSI_PURPLE;
        break;
      default:
        blockColor = ANSI_RESET;
        break;
    }
    return blockColor + getBlockChar(blockType);
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
      case DIRT:
        return '\u2592';
      case DIAMOND:
        return '\u2593';
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
      System.out.println(
        ANSI_CYAN +
        "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door" +
        ANSI_RESET
      );
      String input = scanner.next().toLowerCase();
      if (
        input.equalsIgnoreCase("w") ||
        input.equalsIgnoreCase("up") ||
        input.equalsIgnoreCase("s") ||
        input.equalsIgnoreCase("down") ||
        input.equalsIgnoreCase("a") ||
        input.equalsIgnoreCase("left") ||
        input.equalsIgnoreCase("d") ||
        input.equalsIgnoreCase("right")
      ) {
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
      } else if (input.equalsIgnoreCase("getworld")) {
        getCountryAndQuoteFromServer();
        waitForEnter();
      } else if (input.equalsIgnoreCase("open")) {
        if (
          unlockMode &&
          craftingCommandEntered &&
          miningCommandEntered &&
          movementCommandEntered
        ) {
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
        System.out.println(
          ANSI_YELLOW + "Invalid input. Please try again." + ANSI_RESET
        );
        waitForEnter();
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
        System.out.println(
          "You are now presented with a game board with a world!"
        );
        inSecretArea = true;
        resetWorld();
        secretDoorUnlocked = false;
        fillInventory();
        waitForEnter();
      }
    }
    scanner.close();
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
    printSecretworld(null);
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;
  }

  public static void printSecretworld(String[] args) {
    world = new int[worldHeight][worldWidth];
    getStarCordsLeftBottom(world, centerX, centerY);
    getStarCordRightBottom(world, centerX, centerY);
    getStarCordTop(world, centerX, centerY);
    getStarCordsWingRoof(world, centerX, centerY);
    getStarCordsWingBottom(world, centerX, centerY);
    printworld();
  }

  static HashSet<String> starBoundarySet = new HashSet<>();

  public static void getStarCordTop(int[][] world, int centerX, int centerY) {
    int[][] starCordsTop = {
      { -13, -1 },
      { -12, -2 },
      { -11, -3 },
      { -10, -4 },
      { -9, -5 },
      { -8, -6 },
      { -7, -7 },
      { -6, -8 },
      { -5, -9 },
      { -4, -10 },
      { -3, -11 },
      { -2, -12 },
      { -1, -13 },
      { 0, -14 },
      { 1, -14 },
      { 2, -13 },
      { 3, -12 },
      { 4, -11 },
      { 5, -10 },
      { 6, -9 },
      { 7, -8 },
      { 8, -7 },
      { 9, -6 },
      { 10, -5 },
      { 11, -4 },
      { 12, -3 },
      { 13, -2 },
      { 14, -1 },
    };
    for (int i = 0; i < starCordsTop.length; i++) {
      int starX = centerX + starCordsTop[i][0];
      int starY = centerY + starCordsTop[i][1];
      if (
        starX >= 0 && starX < worldWidth && starY >= 0 && starY < worldHeight
      ) {
        world[starY][starX] = 1;
        starBoundarySet.add(starX + "," + starY);
      }
    }
  }

  public static void getStarCordsWingRoof(
    int[][] world,
    int centerX,
    int centerY
  ) {
    int[][] starCordsWingRoof = { { -39, 0 }, { 39, 0 } };
    for (int i = 0; i < starCordsWingRoof.length; i++) {
      int starX = centerX + starCordsWingRoof[i][0];
      int starY = centerY + starCordsWingRoof[i][1];
      if (
        starX >= 0 && starX < worldWidth && starY >= 0 && starY < worldHeight
      ) {
        world[starY][starX] = 1;
        starBoundarySet.add(starX + "," + starY);
      }
    }
  }

  public static void getStarCordsWingBottom(
    int[][] world,
    int centerX,
    int centerY
  ) {
    int[][] starCordsWingBottom = {
      { -37, 1 },
      { -35, 2 },
      { -33, 3 },
      { -31, 4 },
      { -29, 5 },
      { -27, 6 },
      { -25, 7 },
      { -23, 8 },
      { -21, 9 },
      { -19, 10 },
      { 38, 1 },
      { 36, 2 },
      { 34, 3 },
      { 32, 4 },
      { 30, 5 },
      { 28, 6 },
      { 26, 7 },
      { 24, 8 },
      { 22, 9 },
      { 20, 10 },
    };
    for (int i = 0; i < starCordsWingBottom.length; i++) {
      int starX = centerX + starCordsWingBottom[i][0];
      int starY = centerY + starCordsWingBottom[i][1];
      if (
        starX >= 0 && starX < worldWidth && starY >= 0 && starY < worldHeight
      ) {
        world[starY][starX] = 1;
        starBoundarySet.add(starX + "," + starY);
      }
    }
  }

  public static void getStarCordsLeftBottom(
    int[][] world,
    int centerX,
    int centerY
  ) {
    int[][] starCordsLeftBottom = {
      { -29, 23 },
      { -28, 22 },
      { -27, 21 },
      { -26, 20 },
      { -25, 19 },
      { -24, 18 },
      { -23, 17 },
      { -22, 16 },
      { -21, 15 },
      { -20, 14 },
      { -19, 13 },
      { -18, 12 },
      { -17, 11 },
      { -27, 23 },
      { -25, 22 },
      { -22, 21 },
      { -19, 20 },
      { -16, 19 },
      { -13, 18 },
      { -10, 17 },
      { -7, 16 },
      { -4, 15 },
      { -1, 14 },
    };
    for (int i = 0; i < starCordsLeftBottom.length; i++) {
      int starX = centerX + starCordsLeftBottom[i][0];
      int starY = centerY + starCordsLeftBottom[i][1];
      if (
        starX >= 0 && starX < worldWidth && starY >= 0 && starY < worldHeight
      ) {
        world[starY][starX] = 1;
        starBoundarySet.add(starX + "," + starY);
      }
    }
  }

  public static void getStarCordRightBottom(
    int[][] world,
    int centerX,
    int centerY
  ) {
    int[][] starCordRightBottom = {
      { 18, 11 },
      { 19, 12 },
      { 20, 13 },
      { 21, 14 },
      { 22, 15 },
      { 23, 16 },
      { 24, 17 },
      { 25, 18 },
      { 26, 19 },
      { 27, 20 },
      { 28, 21 },
      { 29, 22 },
      { 30, 23 },
      { 28, 23 },
      { 26, 22 },
      { 23, 21 },
      { 20, 20 },
      { 17, 19 },
      { 14, 18 },
      { 11, 17 },
      { 8, 16 },
      { 5, 15 },
      { 2, 14 },
    };
    for (int i = 0; i < starCordRightBottom.length; i++) {
      int starX = centerX + starCordRightBottom[i][0];
      int starY = centerY + starCordRightBottom[i][1];
      if (
        starX >= 0 && starX < worldWidth && starY >= 0 && starY < worldHeight
      ) {
        world[starY][starX] = 1;
        starBoundarySet.add(starX + "," + starY);
      }
    }
  }

  public static void printworld() {
    int redBlock = 1;
    int yellowBlock = 5;

    for (int y = 0; y < worldHeight; y++) {
      boolean fillYellow = false;
      for (int x = 0; x < worldWidth; x++) {
        if (world[y][x] == yellowBlock && fillYellow) {
          fillYellow = false;
          continue;
        }
        if (isStarBoundary(x, y)) {
          if (fillYellow) {
            fillYellow = false;
            continue;
          } else {
            fillYellow = true;
          }
        }
        if (fillYellow) {
          world[y][x] = yellowBlock;
        } else {
          if (world[y][x] == 0) {
            world[y][x] = redBlock;
          }
        }
      }
    }
  }

  public static boolean isStarBoundary(int x, int y) {
    return starBoundarySet.contains(x + "," + y);
  }

  private static void generateEmptyWorld() {
    world = new int[worldWidth][worldHeight];
    int redBlock = 1;
    int whiteBlock = 4;
    int blueBlock = 3;

    int stripeHeight = worldHeight / 3; // Divide the height into three equal parts

    // Fill the top stripe with red blocks
    for (int y = 0; y < stripeHeight; y++) {
      for (int x = 0; x < worldWidth; x++) {
        world[x][y] = redBlock;
      }
    }

    // Fill the middle stripe with white blocks
    for (int y = stripeHeight; y < stripeHeight * 2; y++) {
      for (int x = 0; x < worldWidth; x++) {
        world[x][y] = whiteBlock;
      }
    }

    // Fill the bottom stripe with blue blocks
    for (int y = stripeHeight * 2; y < worldHeight; y++) {
      for (int x = 0; x < worldWidth; x++) {
        world[x][y] = blueBlock;
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
    for (
      int y = Math.max(0, playerY - 1);
      y <= Math.min(playerY + 1, worldHeight - 1);
      y++
    ) {
      for (
        int x = Math.max(0, playerX - 1);
        x <= Math.min(playerX + 1, worldWidth - 1);
        x++
      ) {
        if (x == playerX && y == playerY) {
          System.out.print(ANSI_GREEN + "P " + ANSI_RESET);
        } else {
          System.out.print(getBlockSymbol(world[y][x]));
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
        if (playerY < worldHeight - 1 || playerY < worldHeight - 1) {
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
        if (playerX < worldWidth - 1 || playerX < worldWidth - 1) {
          playerX++;
        }
        break;
      default:
        break;
    }
  }

  public static void mineBlock() {
    int blockType = world[playerY][playerX];
    if (blockType != AIR && !craftedItemContains(CRAFTED_DIAMOND_PICKAXE, 1)) {
      inventory.add(blockType);
      world[playerY][playerX] = AIR;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
        Thread.currentThread().interrupt();
      }
      System.out.println("Mined " + getBlockName(blockType) + ".");
    } else if (
      blockType != AIR && craftedItemContains(CRAFTED_DIAMOND_PICKAXE, 1)
    ) {
      inventory.add(blockType);
      world[playerY][playerX] = AIR;
      System.out.println(
        "Mined " + getBlockName(blockType) + " with Diamond Pickaxe" + "."
      );
    } else {
      System.out.println("No block to mine here.");
    }
    waitForEnter();
  }

  public static void placeBlock(int blockType) {
    if (blockType >= 0 && blockType <= 7) {
      if (blockType <= 6) {
        if (inventory.contains(blockType)) {
          inventory.remove(Integer.valueOf(blockType));
          world[playerY][playerX] = blockType;
          System.out.println(
            "Placed " + getBlockName(blockType) + " at your position."
          );
        } else {
          System.out.println(
            "You don't have " + getBlockName(blockType) + " in your inventory."
          );
        }
      } else {
        int craftedItem = getCraftedItemFromBlockType(blockType);
        if (craftedItems.contains(craftedItem)) {
          craftedItems.remove(Integer.valueOf(craftedItem));
          world[playerY][playerX] = blockType;
          System.out.println(
            "Placed " + getCraftedItemName(craftedItem) + " at your position."
          );
        } else {
          System.out.println(
            "You don't have " +
            getCraftedItemName(craftedItem) +
            " in your crafted items."
          );
        }
      }
    } else {
      System.out.println(
        "Invalid block number. Please enter a valid block number."
      );
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
      case CRAFTED_DIAMOND_PICKAXE:
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
        return CRAFTED_DIAMOND_PICKAXE;
      default:
        return -1;
    }
  }

  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    System.out.println("4. Craft diamond pickaxe: 2 sticks and 3 diamonds");
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
        craftDiamondPickaxe();
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

  public static void craftDiamondPickaxe() {
    if (
      inventoryContains(DIAMOND, 3) && craftedItemContains(CRAFTED_STICK, 2)
    ) {
      removeItemsFromInventory(DIAMOND, 3);
      removeCraftedItems(CRAFTED_STICK, 2);
      addCraftedItem(CRAFTED_DIAMOND_PICKAXE);
      System.out.println("Crafted Diamond Pickaxe.");
    } else {
      System.out.println("Insufficient resources to craft a Diamond Pickaxe.");
    }
  }

  public static boolean inventoryContains(int item) {
    return inventory.contains(item);
  }

  public static boolean craftedItemContains(int crafteditem, int f) {
    int counter = 0;
    Iterator<Integer> iterator = craftedItems.iterator();
    while (iterator.hasNext()) {
      int i = iterator.next();
      if (i == crafteditem) {
        counter++;
        if (counter == f) return true;
      }
    }
    return false;
  }

  public static void removeCraftedItems(int crafteditem, int count) {
    int removedCount = 0;
    Iterator<Integer> iterator = craftedItems.iterator();
    while (iterator.hasNext()) {
      int i = iterator.next();
      if (i == crafteditem) {
        iterator.remove();
        removedCount++;
        if (removedCount == count) {
          break;
        }
      }
    }
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
    int blockType = world[playerY][playerX];
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
      case DIRT:
        System.out.println("You dig up some dirt.");
        break;
      case DIAMOND:
        System.out.println("You found a diamond.");
        break;
      default:
        System.out.println("Unrecognized block. Cannot interact.");
    }
    waitForEnter();
  }

  public static void saveGame(String fileName) {
    try (
      ObjectOutputStream outputStream = new ObjectOutputStream(
        new FileOutputStream(fileName)
      )
    ) {
      // Serialize game state data and write to the file
      outputStream.writeInt(worldWidth);
      outputStream.writeInt(worldHeight);
      outputStream.writeObject(world);
      outputStream.writeInt(playerX);
      outputStream.writeInt(playerY);
      outputStream.writeObject(inventory);
      outputStream.writeObject(craftedItems);
      outputStream.writeBoolean(unlockMode);

      System.out.println("Game state saved to file: " + fileName);
    } catch (IOException e) {
      System.out.println(
        "Error while saving the game state: " + e.getMessage()
      );
    }
    waitForEnter();
  }

  public static void loadGame(String fileName) {
    // Implementation for loading the game state from a file goes here
    try (
      ObjectInputStream inputStream = new ObjectInputStream(
        new FileInputStream(fileName)
      )
    ) {
      // Deserialize game state data from the file and load it into the program
      worldWidth = inputStream.readInt();
      worldHeight = inputStream.readInt();
      world = (int[][]) inputStream.readObject();
      playerX = inputStream.readInt();
      playerY = inputStream.readInt();
      inventory = (List<Integer>) inputStream.readObject();
      craftedItems = (List<Integer>) inputStream.readObject();
      unlockMode = inputStream.readBoolean();

      System.out.println("Game state loaded from file: " + fileName);
    } catch (IOException | ClassNotFoundException e) {
      System.out.println(
        "Error while loading the game state: " + e.getMessage()
      );
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
      case DIRT:
        return "Dirt";
      case DIAMOND:
        return "Diamond";
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
    System.out.println(ANSI_BROWN + "\u2592\u2592 - Dirt block");
    System.out.println(ANSI_PURPLE + "\u2593\u2593 - Diamond");
    System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
  }

  public static void displayInventory() {
    System.out.println("Inventory:");
    if (inventory.isEmpty()) {
      System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
    } else {
      int[] blockCounts = new int[8];
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
          getCraftedItemColor(item) +
          getCraftedItemName(item) +
          ", " +
          ANSI_RESET
        );
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
      case DIRT:
        return ANSI_YELLOW_NICE;
      case DIAMOND:
        return ANSI_PURPLE;
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
      case CRAFTED_DIAMOND_PICKAXE:
        return "Diamond Pickaxe";
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
      case CRAFTED_DIAMOND_PICKAXE:
        return ANSI_YELLOW;
      default:
        return "";
    }
  }

  public static void getCountryAndQuoteFromServer() {
    try {
      URL url = new URL("");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);
      String payload =
        "        {" + //
        "            \"group_number\": \"78\",\r\n" + //
        "            \"group_name\": \"Group78\",\r\n" + //
        "            \"difficulty_level\": \"hard\"\r\n" + //
        "        }";

      OutputStreamWriter writer = new OutputStreamWriter(
        conn.getOutputStream()
      );
      writer.write(payload);
      writer.flush();
      writer.close();
      BufferedReader reader = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      String json = sb.toString();
      /*int countryStart = json.indexOf("\"country\":\"") + 11;
      int countryEnd = json.indexOf("\"", countryStart);
      String country = json.substring(countryStart, countryEnd);
      int quoteStart = json.indexOf("\"quote\":\"") + 9;
      int quoteEnd = json.indexOf("\"", quoteStart);
      String quote = json.substring(quoteStart, quoteEnd);
      quote = quote.replace("\\\"", "\"");
      
      System.out.println("Country: " + country);
      */
      System.out.println(json);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error connecting to the server");
    }
  }
}
