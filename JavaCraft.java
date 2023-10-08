import java.util.*;

import javax.net.ssl.HttpsURLConnection;

import java.net.*;
import java.io.*;

public class JavaCraft {
  private static final int AIR = 0;
  private static final int WOOD = 1;
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int IRON_ORE = 4;
  private static final int GOLD_ORE = 5;
  private static final int DIAMOND_ORE = 6;
  private static int NEW_WORLD_WIDTH = 29;
  private static int NEW_WORLD_HEIGHT = 19;
  private static int EMPTY_BLOCK = 0;
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;
  private static final int CRAFT_GOLD_PICKAXE = 103;
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;
  private static final int CRAFTED_GOLD_PICKAXE = 203;
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
      "5 - Gold ore block\n" +
      "6 - Diamonnd ore block\n" +
      "7 - Wooden Planks (Crafted Item)\n" +
      "8 - Stick (Crafted Item)\n" +
      "9 - Iron Ingot (Crafted Item)" +
      "10 - Gold pickaxe (Crafted Item)";
  private static int[][] world;
  private static int worldWidth;
  private static int worldHeight;
  private static int playerX;
  private static int playerY;
  private static List<Integer> inventory = new ArrayList<>();
  private static List<Integer> craftedItems = new ArrayList<>();
 
  private static boolean secretDoorUnlocked = false;
  private static boolean inSecretArea = false;
  private static final int INVENTORY_SIZE = 100;
    private static Set<String> movements = new HashSet<>();


  private static boolean unlockMode = false;
  private static boolean craftingCommandEntered = false;
  private static boolean miningCommandEntered = false;
  private static boolean movementCommandEntered = false;
  private static boolean openCommandEntered = false;
  private static Scanner scanner;
  /** 
   * @param args
   */
  public static void main(String[] args) {
    initGame(29, 19);
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

  
  /** 
   * @param worldWidth
   * @param worldHeight
   * 
   * Initializes the game with a given world width and height
   */
  public static void initGame(int worldWidth, int worldHeight) {
    JavaCraft.worldWidth = worldWidth;
    JavaCraft.worldHeight = worldHeight;
    JavaCraft.world = new int[worldWidth][worldHeight];
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;
    inventory = new ArrayList<>();
  }

  /**
   * Generates the game world with random blocks
   */
  public static void generateWorld() {
    Random rand = new Random();
    for (int y = 0; y < worldHeight; y++) {
      for (int x = 0; x < worldWidth; x++) {
        int randValue = rand.nextInt(130);
        if (randValue < 30) {
          world[x][y] = WOOD;
        } else if (randValue < 40) {
          world[x][y] = LEAVES;
        } else if (randValue < 60) {
          world[x][y] = STONE;
        } else if (randValue < 80) {
          world[x][y] = IRON_ORE;
        } else if (randValue < 95) {
          world[x][y] = GOLD_ORE;
        } else if (randValue < 105) {
          world[x][y] = DIAMOND_ORE;
        } else {
          world[x][y] = AIR;
        }
      }
    }
  }

  /**
   * Displays the game world to the console
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

  
  /** 
   * @param blockType
   * @return String
   * Returns the symbol representation of a block type
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
        case GOLD_ORE:
        blockColor = ANSI_YELLOW;
        break;
        case DIAMOND_ORE:
        blockColor = ANSI_CYAN;
        break;
      default:
        blockColor = ANSI_RESET;
        break;
    }
    return blockColor + getBlockChar(blockType) + " ";
  }

  /**
   * @param blockType
   * @return char
   * Returns the character representation of a block type
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
        case GOLD_ORE:
        return '\u00A2';
        case DIAMOND_ORE:
        return '\u0023';
      default:
        return '-';
    }
  }

  
  /**
   * The game loop where player input is handled
   */
  public static void startGame() {
    addMovement();
    scanner = new Scanner(System.in);
    
    while (true) {
      handleGUI();
      String input = scanner.next().toLowerCase();

      if (input.equalsIgnoreCase("exit")) {
        System.out.println("Exiting the game. Goodbye!");
        break;
      }
      handleInput(input);
      if (secretDoorUnlocked) {
        openSecretDoor();
      }
    }
  }


  /**
   * Opens a secret door in the game
   */
  private static void openSecretDoor() {
    clearScreen();
    System.out.println("You have entered the secret area!");
    System.out.println("You are now presented with a game board with a flag!");
    inSecretArea = true;
    resetWorld();
    secretDoorUnlocked = false;
    fillInventory();
    waitForEnter();
  }

  /**
   *  Handles different types of player input commands
   */
  private static void handleInput(String input){
    if (movements.contains(input)) {
        handleMovement(input);
      } else if (input.equalsIgnoreCase("m")) {
        handleMining();
      } else if (input.equalsIgnoreCase("p")) {
        handlePlacing();
      } else if (input.equalsIgnoreCase("c")) {
        handleCrafting();
      } else if (input.equalsIgnoreCase("i")) {
        interactWithWorld();
      } else if (input.equalsIgnoreCase("save")) {
        handleSaving();
      } else if (input.equalsIgnoreCase("load")) {
        handleLoading();
      } else if (input.equalsIgnoreCase("look")) {
        lookAround();
      } else if (input.equalsIgnoreCase("unlock")) {
        unlockMode = true;
      } else if (input.equalsIgnoreCase("getflag")) {
        getCountryAndQuoteFromServer();
        waitForEnter();
      } else if (input.equalsIgnoreCase("open")) {

        handleSecretDoor();
      } else {
        System.out.println(ANSI_YELLOW + "Invalid input. Please try again." + ANSI_RESET);
      }
  }

  /**
   *  Handles the process of unlocking and opening a secret door
   */
  private static void handleSecretDoor() {
    if (unlockMode&&craftingCommandEntered && miningCommandEntered && movementCommandEntered) {
        openCommandEntered = true;
        
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
  }

  /**
   * Handles loading a saved game state from a file
   */
  private static void handleLoading() {
    System.out.print("Enter the file name to load the game state: ");
    String fileName = scanner.next();
    loadGame(fileName);
  }


  /**
   * Handles saving the current game state to a file
   */
  private static void handleSaving() {
    System.out.print("Enter the file name to save the game state: ");
    String fileName = scanner.next();
    saveGame(fileName);
  }


  /**
   * Handles crafting items in the game
   */
  private static void handleCrafting() {
    if(unlockMode){
      craftingCommandEntered = true;
    }
    displayCraftingRecipes();
    System.out.print("Enter the recipe number to craft: ");
    int recipe = scanner.nextInt();
    craftItem(recipe);
  }


  /**
   * Handles placing blocks from the player's inventory
   */
  private static void handlePlacing() {
    displayInventory();
    System.out.print("Enter the block type to place: ");
    int blockType = scanner.nextInt();
    placeBlock(blockType);
  }


  /**
   * Handles mining blocks in the game
   */
  private static void handleMining() {
    if (unlockMode) {
      miningCommandEntered = true;
    }
    mineBlock();
  }


  /**
   * @param input String
   * Handles player movement
   */
  private static void handleMovement(String input) {
    if (unlockMode) {
      movementCommandEntered = true;
    } 
    movePlayer(input);
  }
  
  /**
   * 
   */
  private static void handleGUI() {
    clearScreen();
    displayLegend();
    displayWorld();
    displayInventory();
    System.out.println(ANSI_CYAN
        + "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door"
        + ANSI_RESET);
  }

  /**
   * Fills the player's inventory with items
   */
  private static void fillInventory() {
    inventory.clear();
    for (int blockType = 1; blockType <= 6; blockType++) {
      for (int i = 0; i < INVENTORY_SIZE; i++) {
        inventory.add(blockType);
      }
    }
  }

  /**
   * Resets the game world
   */
  private static void resetWorld() {
    generateEmptyWorld();
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;
  }

  /**
   * Generates an empty world
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

  /**
   *  Clears the console screen
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

  /**
   * Displays what the player can see in their immediate vicinity
   */
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

  /**
   * @param direction
   * Moves the player in a specified direction
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

  /**
   * Allows the player to mine a block and add it to their inventory
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

  /**
   * @param blockType
   * Allows the player to place a block from their inventory
   */
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

  /**
   * @param craftedItem
   * @return int
   * Converts a block type to a crafted item
   */
  private static int getBlockTypeFromCraftedItem(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
        return 7;
      case CRAFTED_STICK:
        return 8;
      case CRAFTED_IRON_INGOT:
        return 9;
      case CRAFTED_GOLD_PICKAXE:
        return 10;
      default:
        return -1;
    }
  }

  /**
   * @param blockType
   * @return int
   * Converts a crafted item to a block type
   */
  private static int getCraftedItemFromBlockType(int blockType) {
    switch (blockType) {
      case 7:
        return CRAFTED_WOODEN_PLANKS;
      case 8:
        return CRAFTED_STICK;
      case 9:
        return CRAFTED_IRON_INGOT;
        case 10:
        return CRAFTED_GOLD_PICKAXE;
      default:
        return -1;
    }
  }

  /**
   * Displays crafting recipes to the player
   */
  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    System.out.println("4. Craft Gold Pickaxe: 3 Gold Ore & 2 Sticks");
  }

  /**
   * @param recipe
   * Crafts an item based on a recipe
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
      case 4:
        craftGoldPickaxe();
        break;
      default:
        System.out.println("Invalid recipe number.");
    }
    waitForEnter();
  }

  /**
   * Crafts wooden planks
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

  /**
   * Craft sticks
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

  /**
   * Crafts iron ingots
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

  /**
   * Crafts a gold pickaxe
   */
  public static void craftGoldPickaxe() {
    if (inventoryContains(GOLD_ORE, 3) && inventoryContains(CRAFTED_STICK, 2)) {
      removeItemsFromInventory(GOLD_ORE, 3);
      removeItemsFromInventory(CRAFTED_STICK, 2);
      addCraftedItem(CRAFTED_GOLD_PICKAXE);
      System.out.println("Crafted Gold Pickaxe.");
    } else {
      System.out.println("Insufficient resources to craft Gold Pickaxe.");
    }
  }

  /**
   * @param item
   * @return boolean
   * Checks if the player's inventory contains a specific item
   */
  public static boolean inventoryContains(int item) {
    return inventory.contains(item);
  }

  /**
   * @param item
   * @param count
   * @return boolean
   * Checks if the player's inventory contains a specific number of a specific item
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
    if (itemCount < count) {
      for (int craftedItem : craftedItems) {
          if (craftedItem == item) {
              itemCount++;
              if (itemCount == count) {
                  return true;
              }
          }
      }
    }
    return false;
  }

  /**
   * @param item
   * @param count
   * Removes items from the player's inventory
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
    if (removedCount < count) {
      Iterator<Integer> craftedIterator = craftedItems.iterator();
      while (craftedIterator.hasNext()) {
          int craftedItem = craftedIterator.next();
          if (craftedItem == item) {
              craftedIterator.remove();
              removedCount++;
              if (removedCount == count) {
                  break;
              }
          }
      }
  
}
  }

  /**
   * @param craftedItem
   * Adds a crafted item to the player's inventory
   */
  public static void addCraftedItem(int craftedItem) {
    if (craftedItems == null) {
      craftedItems = new ArrayList<>();
    }
    craftedItems.add(craftedItem);
  }

  /**
   * Allows the player to interact with elements in the game world
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
        case GOLD_ORE:
        System.out.println("You mine gold ore from the ground.");
        inventory.add(GOLD_ORE);
        case DIAMOND_ORE:
        System.out.println("You mine diamond from the ground.");
        inventory.add(DIAMOND_ORE);
        break;
      case AIR:
        System.out.println("Nothing to interact with here.");
        break;
      default:
        System.out.println("Unrecognized block. Cannot interact.");
    }
    waitForEnter();
  }

  /**
   * @param fileName
   * Save the current game state to a file
   */
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


  /**
   * @param fileName
   * Load game state from a file
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

  /**
   * @param blockType
   * @return String
   * Returns the name of a block based on its type
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
      case GOLD_ORE:
        return "Gold Ore";
      case DIAMOND_ORE:
        return "Diamond Ore";
      default:
        return "Unknown";
    }
  }

  /**
   * Displays a legend explaining block symbols and colors
   */
  public static void displayLegend() {
    System.out.println(ANSI_BLUE + "Legend:");
    System.out.println(ANSI_WHITE + "-- - Empty block");
    System.out.println(ANSI_RED + "\u2592\u2592 - Wood block");
    System.out.println(ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
    System.out.println(ANSI_BLUE + "\u2593\u2593 - Stone block");
    System.out.println(ANSI_WHITE + "\u00B0\u00B0- Iron ore block");
    System.out.println(ANSI_YELLOW + "\u00A2\u00A2- Gold ore block");
    System.out.println(ANSI_CYAN + "\u0023\u0023- Diamond ore block");
    System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
  }

  /**
   * Displays the player's inventory and crafted items
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
        System.out.print(getCraftedItemColor(item) + getCraftedItemName(item) + ", " + ANSI_RESET);
      }
      System.out.println();
    }
    System.out.println();
  }

  /**
   * @param blockType
   * @return String
   * Returns the color associated with a block type
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
        return ANSI_WHITE;
        case GOLD_ORE:
        return ANSI_YELLOW;
        case DIAMOND_ORE:
        return ANSI_CYAN;
      default:
        return "";
    }
  }

  /**
   * Pauses the game and waits for the player to press Enter
   */
  private static void waitForEnter() {
    System.out.println("Press Enter to continue...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
  }

  /**
   * @param craftedItem
   * @return String
   * 
   */
  private static String getCraftedItemName(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
        return "Wooden Planks";
      case CRAFTED_STICK:
        return "Stick";
      case CRAFTED_IRON_INGOT:
        return "Iron Ingot";
        case CRAFTED_GOLD_PICKAXE:
        return "Gold pickaxe";
      default:
        return "Unknown";
    }
  }

  /**
   * @param craftedItem 
   * @return 
   */
  private static String getCraftedItemColor(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
      case CRAFTED_STICK:
      case CRAFTED_IRON_INGOT:
      case CRAFTED_GOLD_PICKAXE:
        return ANSI_BROWN;
      default:
        return "";
    }
  }

  /**
   * Add all possible input movements to the set
   */
  private static void addMovement(){
    String[] s = {"w","s","a","d","up","down","left","right"};
    movements.addAll(Arrays.asList(s));
  }
  /**
   * Makes an HTTP request to a server to get a country and a quote
   */
  public static void getCountryAndQuoteFromServer() {
    try {
      URL url = new URL("http://flag.ashish.nl/");
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
        System.out.println(line);
        sb.append(line);
      }
      String json = sb.toString();
      int countryStart = json.indexOf(" ") + 11;
      int countryEnd = json.indexOf(" ", countryStart);
      System.out.println(json);
      System.out.println(countryEnd);
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