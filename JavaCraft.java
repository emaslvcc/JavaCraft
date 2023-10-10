
//Imports
import java.util.*;
import java.net.*;
import java.io.*;

public class JavaCraft {

  // Block types IDs
  private static final int ITEM_COUNT = 11;
  private static final int BLOCK_COUNT = 7;

  private static final int AIR = 0;
  private static final int WOOD = 1;
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int IRON_ORE = 4;
  private static final int GLASS = 5;
  private static final int MAGIC_POWDER = 6;
  private static final int BANGLADESH_GREEN = 7;
  private static final int BANGLADESH_RED = 8;
  // Crafted items IDs
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;
  private static final int CRAFTED_MAGIC_LIQUID = 203;
  // Saved colors
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
  private static final String ANSI_BANGLADESH_GREEN = "\u001B[38;2;0;106;78m";
  private static final String ANSI_BANGLADESH_RED = "\u001B[38;2;244;42;65m";

  private static final char WOOD_BLOCK = '\u2592';
  private static final char LEAVES_BLOCK = '\u00A7';
  private static final char STONE_BLOCK = '\u2593';
  private static final char IRONE_ORE_BLOCK = '\u00B0';
  private static final char GLASS_BLOCK = (char) 206;
  private static final char MAGIC_POWDER_BLOCK = (char) 176;
  private static final char BANGLADESH_BLOCK_GREEN = (char) 244;
  private static final char BANGLADESH_BLOCK_RED = (char) 244;

  private static final char WOOD_BLOCK_ALT = (char) 177 - 10;
  private static final char LEAVES_BLOCK_ALT = (char) 244 - 10;
  private static final char STONE_BLOCK_ALT = (char) 178 - 10;
  private static final char IRONE_ORE_BLOCK_ALT = (char) 220 - 10;
  private static final char GLASS_BLOCK_ALT = (char) 206 - 10;
  private static final char MAGIC_POWDER_BLOCK_ALT = (char) 176;

  // Saved messages
  private static final String BLOCK_NUMBERS_INFO = "Block Numbers:\n" +
      "0 - Empty block\n" +
      "1 - Wood block\n" +
      "2 - Leaves block\n" +
      "3 - Stone block\n" +
      "4 - Iron ore block\n" +
      "5 - Glass block\n" +
      "6 - Magic Powder block\n" +
      "101 - Wooden Planks (Crafted Item)\n" +
      "102 - Stick (Crafted Item)\n" +
      "103 - Iron Ingot (Crafted Item)" +
      "104 - Magic Liquid (Crafted Item)\n";
  // Preset variables
  private static final int INVENTORY_SIZE = 100;
  private static int NEW_WORLD_WIDTH = 25;
  private static int NEW_WORLD_HEIGHT = 15;
  private static int MIN_lIQUID_EFFECT = 5;
  private static int MAX_lIQUID_EFFECT = 10;
  private static int OVERDOSE_THRESHOLD = 15;

  // Game world
  private static int[][] world;
  private static int worldWidth;
  private static int worldHeight;
  // Player
  private static int playerX;
  private static int playerY;
  private static List<Integer> inventory;
  private static List<Integer> craftedItems;
  private static int drunkState = 0;
  // Flags
  private static boolean unlockMode = false;
  private static boolean secretDoorUnlocked = false;
  private static boolean inSecretArea = false;
  private static boolean isQuit = false;

  // Helpers
  private static Random random = new Random();
  // ?
  private int x;
  private static int EMPTY_BLOCK = 0;
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;

  public static void main(String[] args) {
    // Initiate game variables and world grid
    initGame(NEW_WORLD_WIDTH, NEW_WORLD_HEIGHT);
    generateWorld();
    Scanner scanner = new Scanner(System.in);
    // print game start instructions
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
    System.out.print("Start the game? (Y/N): ");
    String startGameChoice = scanner.next().toUpperCase();
    if (startGameChoice.equals("Y")) {
      startGame();
    } else {
      System.out.println("Game not started. Goodbye!");
    }
  }

  // Setup world size, player position in the center, and Initialize inventory
  // lists
  public static void initGame(int worldWidth, int worldHeight) {
    JavaCraft.worldWidth = worldWidth;
    JavaCraft.worldHeight = worldHeight;
    JavaCraft.world = new int[worldWidth][worldHeight];
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;
    inventory = new ArrayList<>();
    craftedItems = new ArrayList<>();
  }

  // Generates random tiles in the world
  public static void generateWorld() {
    Random rand = new Random();
    for (int y = 0; y < worldHeight; y++) {
      for (int x = 0; x < worldWidth; x++) {
        int randValue = rand.nextInt(100);
        if (randValue < 15) {
          world[x][y] = WOOD;
        } else if (randValue < 30) {
          world[x][y] = LEAVES;
        } else if (randValue < 45) {
          world[x][y] = STONE;
        } else if (randValue < 60) {
          world[x][y] = IRON_ORE;
        } else if (randValue < 70) {
          world[x][y] = GLASS;
        } else if (randValue < 75) {
          world[x][y] = MAGIC_POWDER;
        } else {
          world[x][y] = AIR;
        }
      }
    }
  }

  // Display the world grid in the console
  public static void displayWorld() {
    System.out.println(inSecretArea ? ANSI_BANGLADESH_GREEN : ANSI_CYAN + "World Map:" + ANSI_RESET);
    System.out
        .println(inSecretArea ? ANSI_BANGLADESH_GREEN : ANSI_RESET + "╔══" + "═".repeat(worldWidth * 2 - 2) + "╗");
    for (int y = 0; y < worldHeight; y++) {
      System.out.print(inSecretArea ? ANSI_BANGLADESH_GREEN : ANSI_RESET + "║");
      for (int x = 0; x < worldWidth; x++) {
        if (x == playerX && y == playerY && !inSecretArea) {
          System.out.print(ANSI_YELLOW + "P " + ANSI_RESET);
        } else if (x == playerX && y == playerY && inSecretArea) {
          if (world[playerX][playerY] == BANGLADESH_GREEN)
            System.out.print(ANSI_BANGLADESH_GREEN + "P " + ANSI_RESET);
          else
            System.out.print(ANSI_BANGLADESH_RED + "P " + ANSI_RESET);
        } else {
          System.out.print(getBlockSymbol(world[x][y]));
        }
      }
      System.out.println(inSecretArea ? ANSI_BANGLADESH_GREEN : ANSI_RESET + "║");
    }
    System.out.println(inSecretArea ? ANSI_BANGLADESH_GREEN : ANSI_RESET + "╚══" + "═".repeat(worldWidth * 2 - 2) + "╝"+ANSI_RESET);
  }

  // Returns block color by block
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
      case GLASS:
        blockColor = ANSI_WHITE;
        break;
      case MAGIC_POWDER:
        blockColor = ANSI_PURPLE;
        break;
      case BANGLADESH_GREEN:
        blockColor = ANSI_BANGLADESH_GREEN;
        break;
      case BANGLADESH_RED:
        blockColor = ANSI_BANGLADESH_RED;
        break;
      default:
        blockColor = ANSI_RESET;
        break;
    }
    return blockColor + getBlockChar(blockType) + " ";
  }

  // Returns block char by block type
  private static char getBlockChar(int blockType) {
    switch (blockType) {
      case WOOD:
        return drunkState == 0 ? WOOD_BLOCK : WOOD_BLOCK_ALT;
      case LEAVES:
        return drunkState == 0 ? LEAVES_BLOCK : LEAVES_BLOCK_ALT;
      case STONE:
        return drunkState == 0 ? STONE_BLOCK : STONE_BLOCK_ALT;
      case IRON_ORE:
        return drunkState == 0 ? IRONE_ORE_BLOCK : IRONE_ORE_BLOCK_ALT;
      case GLASS:
        return drunkState == 0 ? GLASS_BLOCK : GLASS_BLOCK_ALT;
      case MAGIC_POWDER:
        return drunkState == 0 ? MAGIC_POWDER_BLOCK : MAGIC_POWDER_BLOCK_ALT;
      case BANGLADESH_GREEN:
        return BANGLADESH_BLOCK_GREEN;
      case BANGLADESH_RED:
        return BANGLADESH_BLOCK_RED;
      default:
        return '-';
    }
  }

  // Initializes and starts running the game
  public static void startGame() {
    // Initialize flags
    Scanner scanner = new Scanner(System.in);
    boolean unlockMode = false;
    boolean craftingCommandEntered = false;
    boolean miningCommandEntered = false;
    boolean movementCommandEntered = false;
    boolean openCommandEntered = false;
    // Main game loop
    while (true) {
      clearScreen();
      displayLegend();
      displayWorld();
      displayInventory();
      // Get player input
      System.out.println(ANSI_CYAN
          + "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door"
          + ANSI_RESET);
      String input = scanner.next().toLowerCase();
      // Check if player moved
      if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("up") ||
          input.equalsIgnoreCase("s") || input.equalsIgnoreCase("down") ||
          input.equalsIgnoreCase("a") || input.equalsIgnoreCase("left") ||
          input.equalsIgnoreCase("d") || input.equalsIgnoreCase("right")) {
        // If in unlock mode, raise movement flag
        if (unlockMode) {
          movementCommandEntered = true;
        }
        movePlayer(input);

      }
      // Check input for mining
      else if (input.equalsIgnoreCase("m")) {
        if (unlockMode) {
          miningCommandEntered = true;
        }
        mineBlock();
      }
      // Check input for placing block
      else if (input.equalsIgnoreCase("p")) {
        displayInventory();
        System.out.print("Enter the block type to place: ");
        int blockType = scanner.nextInt();
        placeBlock(blockType);
      }
      // Check input for crafting item
      else if (input.equalsIgnoreCase("c")) {
        displayCraftingRecipes();
        System.out.print("Enter the recipe number to craft: ");
        int recipe = scanner.nextInt();
        craftItem(recipe);
      }
      // Check input for interact action
      else if (input.equalsIgnoreCase("i")) {
        interactWithWorld();
      }
      // Check input for save action
      else if (input.equalsIgnoreCase("save")) {
        System.out.print("Enter the file name to save the game state: ");
        String fileName = scanner.next();
        saveGame(fileName);
      }
      // Check input for load action
      else if (input.equalsIgnoreCase("load")) {
        System.out.print("Enter the file name to load the game state: ");
        String fileName = scanner.next();
        loadGame(fileName);
      }
      // Check input for exit
      else if (input.equalsIgnoreCase("exit")) {
        System.out.println("Exiting the game. Goodbye!");
        isQuit = true;
      }
      // Check input for look around action
      else if (input.equalsIgnoreCase("look")) {
        lookAround();
      }
      // Check input for unlock action
      else if (input.equalsIgnoreCase("unlock")) {
        unlockMode = true;
      }
      // Check input for getting flag from server
      else if (input.equalsIgnoreCase("getflag")) {
        getCountryAndQuoteFromServer();
        waitForEnter();
      }
      // Check input for opening opening the "Secret Door"
      // Requires to first use unlock, then move, craft, and mine
      else if (input.equalsIgnoreCase("open")) {
        if (unlockMode && craftingCommandEntered && miningCommandEntered && movementCommandEntered) {
          secretDoorUnlocked = true;
          resetWorld();
          System.out.println("Secret door unlocked!");
          waitForEnter();
        }

        // If not all requirements are met, they will all be reset
        else {
          System.out.println("Invalid passkey. Try again!");
          waitForEnter();
          unlockMode = false;
          craftingCommandEntered = false;
          miningCommandEntered = false;
          movementCommandEntered = false;
          openCommandEntered = false;
        }
      }
      // Invalid input
      else {
        System.out.println(ANSI_YELLOW + "Invalid input. Please try again." + ANSI_RESET);
      }
      // Check if in unlock mode.
      if (unlockMode) {
        // if input c, raise craft flag
        if (input.equalsIgnoreCase("c")) {
          craftingCommandEntered = true;
        }
        // if input m, raise mine flag
        else if (input.equalsIgnoreCase("m")) {
          miningCommandEntered = true;
        }
        // if input open, raise open flag
        else if (input.equalsIgnoreCase("open")) {
          openCommandEntered = true;
        }
      }
      if (isQuit)
        break;
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
      if (drunkState > 0)
        drunkState--;
    }

  }

  // #region SecretDoor

  // For every block type, add to inventory copies of the block equal to the
  // inventory size
  private static void fillInventory() {
    inventory.clear();
    for (int blockType = 1; blockType <= 4; blockType++) {
      for (int i = 0; i < INVENTORY_SIZE; i++) {
        inventory.add(blockType);
      }
    }
  }

  // Generate empty world and reset player position
  private static void resetWorld() {
    generateBangladeshWorld();
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;
  }

  // Create an empty world with the BANGLADESH flag
  private static void generateBangladeshWorld() {
    world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
    int greenBlock = BANGLADESH_GREEN;
    int redBlock = BANGLADESH_RED;
    // Fill the top stripe with red blocks
    for (int y = 0; y < NEW_WORLD_HEIGHT; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = greenBlock;
      }
    }
    for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
      for (int y = 0; y < NEW_WORLD_HEIGHT; y++) {
        // Calculate the distance from the current point (x, y) to the center (centerX,
        // centerY)
        int distanceSquared = (x - 10) * (x - 10) + (y - 7) * (y - 7);

        // If the distance is less than or equal to the radius squared, set the point to
        // 1 (inside the circle)
        if (distanceSquared <= 5.5 * 5.5) {
          world[x][y] = redBlock;
        }
      }
    }
  }

  // Create an empty world with the Dutch flag
  private static void generateDutchWorld() {
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

  // #endregion
  // Clears the display
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

  // Displays the player’s current block and surrounding blocks
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

  // Move player on the world grid in a direction. Does not handle display of
  // movement
  public static void movePlayer(String direction) {
    if (drunkState > 0) {
      direction = getRandomDirection();
    }
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

  private static String getRandomDirection() {
    int rand = random.nextInt(0, 4);
    switch (rand) {
      case 0:
        return "W";
      case 1:
        return "S";
      case 2:
        return "A";
      case 3:
        return "D";
    }
    return "W";
  }

  // Try to mine current block and add to inventory
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

  // Try placing a block in the player’s position, remove from inventory
  public static void placeBlock(int blockType) {
    // if the id is of a block
    if (blockType >= 0 && blockType <= ITEM_COUNT) {
      if (blockType <= BLOCK_COUNT) {
        // check if inventory contains requested block, and place the item
        if (inventory.contains(blockType)) {
          inventory.remove(Integer.valueOf(blockType));
          world[playerX][playerY] = blockType;
          System.out.println("Placed " + getBlockName(blockType) + " at your position.");
        } else {
          System.out.println("You don't have " + getBlockName(blockType) + " in your inventory.");
        }
      }
      // if the id is of a crafted item
      else {
        int craftedItem = getCraftedItemFromBlockType(blockType);
        // chekc if the crafted item list has the requested item, and place the item
        if (craftedItems.contains(craftedItem)) {
          craftedItems.remove(Integer.valueOf(craftedItem));
          world[playerX][playerY] = blockType;
          System.out.println("Placed " + getCraftedItemName(craftedItem) + " at your position.");
        } else {
          System.out.println("You don't have " + getCraftedItemName(craftedItem) + " in your crafted items.");
        }
      }
      // if id is not valid print message to
    } else {
      System.out.println("Invalid block number. Please enter a valid block number.");
      System.out.println(BLOCK_NUMBERS_INFO);
    }
    waitForEnter();
  }

  // Return block type by crafted item type
  private static int getBlockTypeFromCraftedItem(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
        return 5;
      case CRAFTED_STICK:
        return 6;
      case CRAFTED_IRON_INGOT:
        return 7;
      default:
        return -1;
    }
  }

  // Return crafted item type by block type
  private static int getCraftedItemFromBlockType(int blockType) {
    switch (blockType) {
      case 7:
        return CRAFTED_WOODEN_PLANKS;
      case 8:
        return CRAFTED_STICK;
      case 9:
        return CRAFTED_IRON_INGOT;
      case 10:
        return CRAFTED_MAGIC_LIQUID;
      default:
        return -1;
    }
  }

  // Print out the crafting recipes
  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    System.out.println("4. Craft Magic Liquid: 3 Glass, 1 Magic Powder, 1 Leaves");
  }

  // Craft item by id
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
        craftMagicLiquid();
        break;
      default:
        System.out.println("Invalid recipe number.");
    }
    waitForEnter();
  }

  // Try add wooden plank to inventory, remove ingredients
  public static void craftWoodenPlanks() {
    if (inventoryContains(WOOD, 2)) {
      removeItemsFromInventory(WOOD, 2);
      addCraftedItem(CRAFTED_WOODEN_PLANKS);
      System.out.println("Crafted Wooden Planks.");
    } else {
      System.out.println("Insufficient resources to craft Wooden Planks.");
    }
  }

  // Try add stick to inventory, remove ingredients
  public static void craftStick() {
    if (inventoryContains(WOOD)) {
      removeItemsFromInventory(WOOD, 1);
      addCraftedItem(CRAFTED_STICK);
      System.out.println("Crafted Stick.");
    } else {
      System.out.println("Insufficient resources to craft Stick.");
    }
  }

  // Try add iron ingot to inventory, remove ingredients
  public static void craftIronIngot() {
    if (inventoryContains(IRON_ORE, 3)) {
      removeItemsFromInventory(IRON_ORE, 3);
      addCraftedItem(CRAFTED_IRON_INGOT);
      System.out.println("Crafted Iron Ingot.");
    } else {
      System.out.println("Insufficient resources to craft Iron Ingot.");
    }
  }

  // Try add magic liquid to inventory, remove ingredients
  public static void craftMagicLiquid() {
    if (inventoryContains(GLASS, 3) && inventoryContains(MAGIC_POWDER, 1) && inventoryContains(LEAVES, 1)) {
      removeItemsFromInventory(GLASS, 3);
      removeItemsFromInventory(MAGIC_POWDER, 1);
      removeItemsFromInventory(LEAVES, 1);
      drunkState += random.nextInt(MIN_lIQUID_EFFECT, MAX_lIQUID_EFFECT + 1);
      System.out.println("Crafted Magic Liquid\n\nIt looks very tasty, You drink the liquid.\n You feel funky.");
      if (drunkState >= OVERDOSE_THRESHOLD)
        doOverdose();
    } else {
      System.out.println("Insufficient resources to craft Magic Liquid.");
    }
  }

  private static void doOverdose() {
    System.out.println(
        "\n\nYour head is getting heavy, everything becomes blurry around you."
            + "\nSomeone is calling you, who is it?\nLights, the lights all around you."
            + "\nThe lights are so pretty. You walk towards them...\n\nThank you for playing");
    isQuit = true;
  }

  // Returns whether the inventory contains an item
  public static boolean inventoryContains(int item) {
    return inventory.contains(item);
  }

  // Returns whether the inventory contains a given amount of an item
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

  // Try and remove an item from the inventory. Return success or failure of
  // removal
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

  // Adds item to crafted items list
  public static void addCraftedItem(int craftedItem) {
    if (craftedItems == null) {
      craftedItems = new ArrayList<>();
    }
    craftedItems.add(craftedItem);
  }

  // Adds to the inventory an item according to player’s position and print to
  // console
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
      case GLASS:
        System.out.println("You pick up glass from the ground.");
        inventory.add(GLASS);
        break;
      case MAGIC_POWDER:
        System.out.println("You gather magic powder from the ground.");
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

  // Saves the game data to a file
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

  // Load game data from a file
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

  // Returns block’s name by block type
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
      case GLASS:
        return "Glass";
      case MAGIC_POWDER:
        return "Magic Powder";
      default:
        return "Unknown";
    }
  }

  // Prints to console the legend
  public static void displayLegend() {
    System.out.println(ANSI_BLUE + "Legend:");
    System.out.println(ANSI_WHITE + "-- - Empty block");
    System.out.println(ANSI_RED + WOOD_BLOCK + WOOD_BLOCK + " - Wood block");
    System.out.println(ANSI_GREEN + LEAVES_BLOCK + LEAVES_BLOCK + " - Leaves block");
    System.out.println(ANSI_BLUE + STONE_BLOCK + STONE_BLOCK + " - Stone block");
    System.out.println(ANSI_WHITE + IRONE_ORE_BLOCK + IRONE_ORE_BLOCK + " - Iron ore block");
    System.out.println(ANSI_WHITE + GLASS_BLOCK + GLASS_BLOCK + " - Glass block");
    System.out.println(ANSI_PURPLE + MAGIC_POWDER_BLOCK + MAGIC_POWDER_BLOCK + " - Magic Powder block");
    System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
  }

  // Print to console the current inventory
  public static void displayInventory() {
    System.out.println("Inventory:");
    if (inventory.isEmpty()) {
      System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
    } else {
      int[] blockCounts = new int[BLOCK_COUNT];
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

  // Return block color by type
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
      default:
        return "";
    }
  }

  // Print message to player and wait for input "Enter"
  private static void waitForEnter() {
    System.out.println("Press Enter to continue...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
  }

  // Returns crafted item name by type
  private static String getCraftedItemName(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
        return "Wooden Planks";
      case CRAFTED_STICK:
        return "Stick";
      case CRAFTED_IRON_INGOT:
        return "Iron Ingot";
      case CRAFTED_MAGIC_LIQUID:
        return "Magic Liquid";
      default:
        return "Unknown";
    }
  }

  // Returns crafted item color by type
  private static String getCraftedItemColor(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
      case CRAFTED_STICK:
      case CRAFTED_IRON_INGOT:
        return ANSI_BROWN;
      case CRAFTED_MAGIC_LIQUID:
        return ANSI_PURPLE;
      default:
        return "";
    }
  }

  // Connect to the server, request and print data about a countre?
  public static void getCountryAndQuoteFromServer() {
    try {
      URL url = new URL("https://flag.ashish.nl/get_flag");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);
      String payload = "  {\n" +
          "            \"group_number\": \"38\",\n" +
          "            \"group_name\": \"group38\",\n" +
          "            \"difficulty_level\": \"hard\"\n" +
          "        }\n";
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
      System.out.println(sb);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error connecting to the server");
    }
  }
}