import java.util.*;

import javax.imageio.ImageIO;

import java.net.*;
import java.io.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class JavaCraft {

  //blocks
  private static final int AIR = 0;
  private static final int WOOD = 1;
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int IRON_ORE = 4;
  private static final int ADAMANTITE_ORE = 5;
  private static final int GRAVEL = 6;
  private static final int COAL_ORE = 7;

  //vars
  private static int NEW_WORLD_WIDTH = 29;
  private static int NEW_WORLD_HEIGHT = 15;
  private static int EMPTY_BLOCK = 0;

  //crafted items
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;
  private static final int CRAFTED_ADAMANTITE_PICKAXE = 203;
  private static final int CRAFTED_FLINT = 204;
  private static final int CRAFTED_TORCH = 205;

  // colors
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
  private static final String ANSI_BRIGHT_BLACK = "\u001B[90m"; // Sorin's commit - added new colors
  private static final String ANSI_BRIGHT_RED = "\u001B[91m";
  private static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
  private static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
  private static final String ANSI_BRIGHT_BLUE = "\u001B[94m";
  private static final String ANSI_BRIGHT_PURPLE = "\u001B[95m";
  private static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
  private static final String ANSI_BRIGHT_WHITE = "\u001B[97m";

  private static final String BLOCK_NUMBERS_INFO = "Block Numbers:\n" +
      "0 - Empty block\n" +
      "1 - Wood block\n" +
      "2 - Leaves block\n" +
      "3 - Stone block\n" +
      "4 - Iron ore block\n" +
      "5 - Adamantite Ore\n" +
      "6 - Gravel\n" +
      "7 - Coal Ore\n" +
      "8 - Wooden Planks (Crafted Item)\n" +
      "9 - Stick (Crafted Item)\n" +
      "10 - Iron Ingot (Crafted Item)\n" +
      "11 - Adamantite Pickaxe (Crafted Item)\n" +
      "12 - Flint (Crafted Item)\n" +
      "13 - Torch (Crafted Item)";
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

  public static void main(String[] args) throws IOException {
    initGame(25, 15);
    generateWorld();

    displayUKFlag();
    
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
        } else if (randValue < 50) {
          world[x][y] = IRON_ORE;
        } else if (randValue < 53) {
          world[x][y] = ADAMANTITE_ORE;
        } else if (randValue < 60) {
          world[x][y] = GRAVEL;
        } else if (randValue < 70) {
          world[x][y] = COAL_ORE;
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
      case ADAMANTITE_ORE:
        blockColor = ANSI_BRIGHT_PURPLE;
        break;
      case GRAVEL:
        blockColor = ANSI_GRAY;
        break;
      case COAL_ORE:
        blockColor = ANSI_GRAY;
        break;
      case CRAFTED_WOODEN_PLANKS:
        blockColor = ANSI_BROWN;
        break;
      case CRAFTED_STICK:
        blockColor = ANSI_BROWN;
        break;
      case CRAFTED_IRON_INGOT:
        blockColor = ANSI_GRAY;
        break;
      case CRAFTED_ADAMANTITE_PICKAXE:
        blockColor = ANSI_PURPLE;
        break;
      case CRAFTED_FLINT:
        blockColor = ANSI_GRAY;
        break;
      case CRAFTED_TORCH:
        blockColor = ANSI_YELLOW;
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
      case ADAMANTITE_ORE:
        return '\u26C2';
      case GRAVEL:
        return '\u2591';
      case COAL_ORE:
        return '\u00A9';
      case CRAFTED_STICK:
        return '\u2502';
      case CRAFTED_WOODEN_PLANKS:
        return '\u2588';
      case CRAFTED_IRON_INGOT:
        return '\u25E2';
      case CRAFTED_ADAMANTITE_PICKAXE:
        return '\u2692';
      case CRAFTED_FLINT:
        return '\u25A0';
      case CRAFTED_TORCH:
        return '\u00A9';
      default:
        return '-';
    }
  }

  public static void startGame() throws IOException {
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
    for (int blockType = 1; blockType <= 7; blockType++) {
      for (int i = 0; i < INVENTORY_SIZE; i++) {
        inventory.add(blockType);
      }
    }
  }

  private static void resetWorld() throws IOException {
    // generateRomanianFlag();
    Color[][] colors = loadPixelsFromFile(new File("united-kingdom (1).png"));
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;
  }

  private static void generateEmptyWorld() {
    world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
    int redBlock = 1;
    int whiteBlock = 4;
    int blueBlock = 3;
    int stripeHeight = NEW_WORLD_HEIGHT / 3; // Divide the height into three equal parts

    // Fill the top stripe with red blocks
    for (int y = 0; y < stripeHeight-1; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = redBlock;
      }
    }

    // Fill the middle stripe with white blocks
    for (int y = stripeHeight; y < stripeHeight * 2 - 1; y++) {
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

  private static void generateRomanianFlag() {
    world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
    int redBlock = 1;
    int yellowBlock = 205;
    int blueBlock = 3;
    int stripe = NEW_WORLD_WIDTH / 3; // Divide the height into three equal parts
    int extraRows = NEW_WORLD_WIDTH % 3;

    // Fill the top stripe with red blocks
    for (int y = 0; y < NEW_WORLD_HEIGHT; y++) {
      for (int x = 0; x < stripe - 1; x++) {
        world[x][y] = blueBlock;
      }
    }

    // Fill the middle stripe with white blocks
    for (int y = 0; y < NEW_WORLD_HEIGHT; y++) {
      for (int x = stripe - 1; x < (stripe*2)-1; x++) {
        world[x][y] = yellowBlock;
      }
    }

    // Fill the bottom stripe with blue blocks
    for (int y = 0; y < NEW_WORLD_HEIGHT; y++) {
      for (int x = stripe*2 -1; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = redBlock;
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
      if (blockType >= 0 && blockType <= 7) {
        inventory.add(blockType);
        world[playerX][playerY] = AIR;
        System.out.println("Mined " + getBlockName(blockType) + ".");
      } else {
        craftedItems.add(blockType);
        world[playerX][playerY] = AIR;
        System.out.println("Mined " + getCraftedItemName(blockType) + ".");
      }
    } else {
      System.out.println("No block to mine here.");
    }
    waitForEnter();
  }

  public static void placeBlock(int blockType) {
    if (blockType >= 0 && blockType <= 13) {
      if (blockType <= 7) {
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
          world[playerX][playerY] = craftedItem;
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
        return 8;
      case CRAFTED_STICK:
        return 9;
      case CRAFTED_IRON_INGOT:
        return 10;
      default:
        return -1;
    }
  }

  private static int getCraftedItemFromBlockType(int blockType) {
    switch (blockType) {
      case 8:
        return CRAFTED_WOODEN_PLANKS;
      case 9:
        return CRAFTED_STICK;
      case 10:
        return CRAFTED_IRON_INGOT;
      case 11:
        return CRAFTED_ADAMANTITE_PICKAXE;
      case 12:
        return CRAFTED_FLINT;
      case 13:
        return CRAFTED_TORCH;
      default:
        return -1;
  }
  }

  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    System.out.println("4. Craft Adamantite Pickaxe: 3 Adamantite Ore and 1 Wood");
    System.out.println("5. Craft Flint: 1 Gravel");
    System.out.println("6. Torch: 3 Coal Ore and 2 Wood");
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
        craftAdamantitePickaxe();
        break;
      case 5:
        craftFlint();
        break;
      case 6:
        craftTorch();
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

  public static void craftTorch() {
    if(inventoryContains(COAL_ORE,3) && inventoryContains(WOOD,2)){
      removeItemsFromInventory(COAL_ORE,3);
      removeItemsFromInventory(WOOD, 2);
      addCraftedItem(CRAFTED_TORCH);
      System.out.println("Crafted Torch");
    } else {
      System.out.println("Insuficient resources to craft Torch");
    }
  }

  public static void craftAdamantitePickaxe() {
    if (inventoryContains(ADAMANTITE_ORE, 3) && inventoryContains(WOOD, 1)) {
      removeItemsFromInventory(ADAMANTITE_ORE, 3);
      removeItemsFromInventory(WOOD, 1);
      addCraftedItem(CRAFTED_ADAMANTITE_PICKAXE);
      System.out.println("Crafted Adamantite Pickaxe.");
    } else {
      System.out.println("Insufficient resources to craft Adamantite Pickaxe.");
    }
  }

  public static void craftFlint() {
    if (inventoryContains(GRAVEL)) {
      removeItemsFromInventory(GRAVEL, 1);
      addCraftedItem(CRAFTED_FLINT);
      System.out.println("Crafted Flint.");
    } else {
      System.out.println("Insufficient resources to craft Flint.");
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
      case ADAMANTITE_ORE:
        System.out.println("You mine some adamantite shards from the ground.");
        inventory.add(ADAMANTITE_ORE);
        break;
      case GRAVEL:
        System.out.println("You gather some gravel.");
        inventory.add(GRAVEL);
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
      case ADAMANTITE_ORE:
        return "Adamantite Ore";
      case GRAVEL:
        return "Gravel";
      case COAL_ORE:
        return "Coal Ore";
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
    System.out.println(ANSI_BRIGHT_PURPLE + "\u26C2\u26C2 - Adamantite Ore block");
    System.out.println(ANSI_GRAY + "\u2591\u2591 - Gravel block");
    System.out.println(ANSI_GRAY + "\u00A9\u00A9 - Coal Ore block");
    System.out.println(ANSI_BROWN + "\u2588\u2588 - Crafted Wooden Planks");
    System.out.println(ANSI_BROWN + "\u2502\u2502 - Crafted Sticks");
    System.out.println(ANSI_GRAY + "\u25E2\u25E2 - Crafted Iron Ingot");
    System.out.println(ANSI_GRAY + "\u25A0\u25A0 - Crafted Flint");
    System.out.println(ANSI_PURPLE + "\u2692\u2692 - Crafted Adamantite Pickaxe");
    System.out.println(ANSI_YELLOW + "\u00A9\u00A9 - Crafted Torch");
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
      case GRAVEL:
        return ANSI_GRAY;
      case ADAMANTITE_ORE:
        return ANSI_BRIGHT_PURPLE;
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
      case CRAFTED_ADAMANTITE_PICKAXE:
        return "Adamantite Pickaxe";
      case CRAFTED_FLINT:
        return "Flint";
      case CRAFTED_TORCH:
        return "Torch";
      default:
        return "Unknown";
    }
  }

  private static String getCraftedItemColor(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
        return ANSI_BROWN;
      case CRAFTED_STICK:
        return ANSI_BROWN;
      case CRAFTED_IRON_INGOT:
        return ANSI_BROWN;
      case CRAFTED_ADAMANTITE_PICKAXE:
        return ANSI_PURPLE;
      case CRAFTED_FLINT:
        return ANSI_GRAY;
      case CRAFTED_TORCH:
        return ANSI_YELLOW;
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
      String payload = "{ \"group_number\": \"55\", \"group_name\": \"group55\", \"difficulty_level\": \"hard\" }";
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
      System.out.println(sb);
      System.out.println(" " + country);
      System.out.println(" " + quote);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error connecting to the server");
    }
  }


   public static Color[][] loadPixelsFromFile(File file) throws IOException {

        BufferedImage image = ImageIO.read(file);
        Color[][] colors = new Color[image.getWidth()][image.getHeight()];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                colors[x][y] = new Color(image.getRGB(x, y));
            }
        }

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                System.out.print(getCharForColor(colors[x][y]));
            }
            System.out.println();
        }

        
        return colors;
    }

    public static String getCharForColor(Color color) {
      // You can define your own mapping of grayscale values to characters.
      // For example, use ' ' for white and '#' for black.
      if (color.getRed() > color.getBlue() && color.getRed()> color.getGreen()) {
          return ANSI_RED+"3"; // Dark pixel
      } else if (color.getBlue() > color.getRed() && color.getBlue()> color.getGreen()) {
          return ANSI_BLUE+"3"; // Light pixel
      } else 
      {
        return ANSI_WHITE + "0";
      }
    }

    public static void displayUKFlag() {
      try {
          File imageFile = new File("imageSmall.jpg");
          BufferedImage image = ImageIO.read(imageFile);
  
          int width = image.getWidth();
          int height = image.getHeight();
  
          for (int x = 0; x < width; x++) {
            if (x % 2 == 0 || x % 3 == 0) {
              continue;
            }
              for (int y = 0; y < height; y++) {
                  int pixel = image.getRGB(x, y);
  
                  int red = (pixel >> 16) & 0xFF;
                  int green = (pixel >> 8) & 0xFF;
                  int blue = pixel & 0xFF;
  
                  String colorCode = String.format("\u001B[38;2;%d;%d;%dm", red, green, blue);
  
                  String blockChar = "██";
  
                  System.out.print(colorCode + blockChar);
              }
              System.out.println();
          }
  
          System.out.print("\u001B[0m\n");
      } catch (IOException e) {
          e.printStackTrace();
      }
    }
}

