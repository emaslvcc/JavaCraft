import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.ImageIO;

public class JavaCraft {

  private static final int AIR = 0;
  private static final int WOOD = 1;
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int IRON_ORE = 4;
  //2 new blocks below
  private static final int SHEEP = 5;
  private static final int COW = 6;
  // private static int NEW_WORLD_WIDTH = 25;
  // private static int NEW_WORLD_HEIGHT = 15;
  private static int NEW_WORLD_WIDTH = 100; //given that we removed spacing (looks better) we need to double the width to match the required 50x30
  private static int NEW_WORLD_HEIGHT = 30;
  private static int EMPTY_BLOCK = 0;
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;
  //1 new crafted item
  private static final int CRAFT_BED = 103;
  private static final int CRAFTED_BED = 203;

  //////////////// colors work like this \u001B[38;2;R;G;Bm   //////////////// 38 sets foreground and 48 sets background
  private static final String ANSI_BROWN = "\u001B[38;2;102;51;0m"; // RGB for brown
  private static final String ANSI_RESET = "\u001B[0m"; // No change
  private static final String ANSI_GREEN = "\u001B[38;2;0;204;0m"; // RGB for green
  private static final String ANSI_YELLOW = "\u001B[38;2;210;205;50m"; // RGB for yellow
  private static final String ANSI_CYAN = "\u001B[38;2;50;210;136m"; // RGB for cyan
  private static final String ANSI_RED = "\u001B[38;2;210;50;50m"; // RGB for red
  private static final String ANSI_PURPLE = "\u001B[38;2;162;50;210m"; // RGB for purple
  private static final String ANSI_BLUE = "\u001B[38;2;0;0;255m"; // RGB for blue
  private static final String ANSI_GRAY = "\u001B[38;2;128;128;128m"; // RGB for gray
  private static final String ANSI_WHITE = "\u001B[38;2;255;255;255m"; // RGB for white
  private static final String ANSI_PLAYER = "\u001B[38;2;250;0;255m"; // RGB for white

  private static final String BLOCK_NUMBERS_INFO =
    "Block Numbers:\n" +
    "0 - Empty block\n" +
    "1 - Wood block\n" +
    "2 - Leaves block\n" +
    "3 - Stone block\n" +
    "4 - Iron ore block\n" +
    "5 - Sheep\n" +
    "6 - Cow\n" +
    "7 - Wooden Planks (Crafted Item)\n" +
    "8 - Stick (Crafted Item)\n" +
    "9 - Iron Ingot (Crafted Item)" +
    "10 - Bed (Crafted Item)";

  private static int[][] world;
  // Define the ArrayList for the image (r,g,b) at each (x,y) coordinate
  static HashMap<String, int[]> imageMap = new HashMap<>();
  private static int worldWidth;
  private static int worldHeight;
  private static int playerX;
  private static int playerY;
  private static List<Integer> inventory = new ArrayList<>();
  private static List<Integer> craftedItems = new ArrayList<>();
  private static boolean unlockMode = false;
  private static boolean secretDoorUnlocked = false;
  private static boolean inSecretArea = false;
  private static boolean hasRequestedFlag = false;
  private static final int INVENTORY_SIZE = 100;

  public static void main(String[] args) throws IOException {
    initGame(NEW_WORLD_WIDTH, NEW_WORLD_HEIGHT);
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
        } else if (randValue < 35) {
          world[x][y] = LEAVES;
        } else if (randValue < 50) {
          world[x][y] = STONE;
        } else if (randValue < 70) {
          world[x][y] = IRON_ORE;
        } else if (randValue < 75) {
          world[x][y] = SHEEP;
        } else if (randValue < 80) {
          world[x][y] = COW;
        } else {
          world[x][y] = AIR;
        }
      }
    }
  }

  /**
   * Displays the game world.
   *
   * @param isMap If true, display the world as a map with colors. Otherwise, display the normal game world.
   */
  public static void displayWorld(boolean isMap) {
    // Print the world map header
    System.out.println(ANSI_CYAN + "World Map:" + ANSI_RESET);

    // Treesets to hold unique x and y coordinates when displaying the map
    Set<Integer> uniqueXValues = new TreeSet<>();
    Set<Integer> uniqueYValues = new TreeSet<>();

    // If displaying as a map, populate the Treesets with unique x and y coordinates
    if (isMap) {
      for (String key : imageMap.keySet()) {
        String[] parts = key.split(",");
        uniqueXValues.add(Integer.parseInt(parts[0]));
        uniqueYValues.add(Integer.parseInt(parts[1]));
      }
    }

    // Determine the width and height to be displayed (either from the map or the game world)
    int width = isMap ? uniqueXValues.size() : worldWidth;
    int height = isMap ? uniqueYValues.size() : worldHeight;

    // Resize the world dimensions to match the map dimensions if displaying as a map (does not do anything if we use the world dimension for the map)
    worldHeight = height;
    worldWidth = width;

    // Print the top boundary of the world/map display
    System.out.println(ANSI_WHITE + "╔══" + "═".repeat(width - 2) + "╗");

    // Loop through each row (y coordinate)
    for (int yIndex = 0; yIndex < height; yIndex++) {
      int y = isMap ? (int) uniqueYValues.toArray()[yIndex] : yIndex;
      System.out.print(ANSI_WHITE + "║");

      // Loop through each column (x coordinate)
      for (int xIndex = 0; xIndex < width; xIndex++) {
        int x = isMap ? (int) uniqueXValues.toArray()[xIndex] : xIndex;

        // Display player character with different colors based on their location and state
        if (x == playerX && y == playerY && !inSecretArea) {
          System.out.print(ANSI_PLAYER + "\u2588" + ANSI_RESET);
        } else if (x == playerX && y == playerY && inSecretArea) {
          System.out.print(ANSI_BLUE + "\u2588" + ANSI_RESET);
        } else {
          // Display as a colored map or a standard game world based on the isMap flag
          if (isMap) {
            int[] pixel = imageMap.get(x + "," + y);
            if (pixel != null) {
              // Display the colored pixel from the map
              System.out.print(
                "\u001B[38;2;" +
                pixel[0] + // red value
                ";" +
                pixel[1] + // green value
                ";" +
                pixel[2] + // blue value
                "m" +
                "\u2588"
              );
            }
          } else {
            // Display the standard game world symbol for this block
            System.out.print(getBlockSymbol(world[x][y]));
          }
        }
      }

      // Print the right boundary for this row of the world/map
      System.out.println(ANSI_WHITE + "║");
    }

    // Print the bottom boundary of the world/map display
    System.out.println(ANSI_WHITE + "╚══" + "═".repeat(width - 2) + "╝");
  }

  private static String getBlockSymbol(int blockType) {
    String blockColor;
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
      case SHEEP:
        blockColor = ANSI_PURPLE;
        break;
      case COW:
        blockColor = ANSI_BROWN;
        break;
      default:
        blockColor = ANSI_RESET;
        break;
    }
    return blockColor + getBlockChar(blockType); //this uses the icons
    // return blockColor + "\u2588" + " "; //this forces full block as icon
  }

  private static char getBlockChar(int blockType) {
    switch (blockType) {
      case WOOD:
        return '\u2593';
      case LEAVES:
        return '\u2591';
      case STONE:
        return '\u2593';
      case IRON_ORE:
        return '\u00B0';
      case SHEEP:
        return '\u00E6';
      case COW:
        return '\u00FE';
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
      displayWorld(hasRequestedFlag);
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
        try {
          int blockType = scanner.nextInt();
          placeBlock(blockType);
        } catch (InputMismatchException e) {
          scanner.next(); // consume the invalid input
        }
      } else if (input.equalsIgnoreCase("c")) {
        displayCraftingRecipes();
        System.out.print("Enter the recipe number to craft: ");
        try {
          int recipe = scanner.nextInt();
          craftItem(recipe);
        } catch (InputMismatchException e) {
          scanner.next(); // consume the invalid input
        }
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
        System.out.println("flag flag");
        //non test environment just use getCountryAndQuoteFromServer();
        //for testing purposes a flag is hardcoded
        /*try {
          printFlagToScreen("United States");
        } catch (IOException e) {
          e.printStackTrace();
        } */
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
          "You are now presented with a game board with a flag!"
        );
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

  private static void resetWorld() throws IOException {
    //generateEmptyWorld();
    printFlagToScreen("mozambique");
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
          System.out.print(ANSI_PLAYER + "P " + ANSI_RESET);
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
      if (blockType <= 6) {
        if (inventory.contains(blockType)) {
          inventory.remove(Integer.valueOf(blockType));
          world[playerX][playerY] = blockType;
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
          world[playerX][playerY] = blockType;
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

  // private static int getBlockTypeFromCraftedItem(int craftedItem) {
  //   switch (craftedItem) {
  //     case CRAFTED_WOODEN_PLANKS:
  //       return 5;
  //     case CRAFTED_STICK:
  //       return 6;
  //     case CRAFTED_IRON_INGOT:
  //       return 7;
  //     case CRAFTED_BED:
  //       return 8;
  //     default:
  //       return -1;
  //   }
  // }

  private static int getCraftedItemFromBlockType(int blockType) {
    switch (blockType) {
      case 5:
        return CRAFTED_WOODEN_PLANKS;
      case 6:
        return CRAFTED_STICK;
      case 7:
        return CRAFTED_IRON_INGOT;
      case 8:
        return CRAFTED_BED;
      default:
        return -1;
    }
  }

  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    System.out.println("4. Craft Bed: 2 Wool, 1 Cow");
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
        craftBed();
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

  public static void craftBed() {
    if ((inventoryContains(SHEEP, 2)) && (inventoryContains(COW, 1))) {
      removeItemsFromInventory(SHEEP, 2);
      removeItemsFromInventory(COW, 1);
      addCraftedItem(CRAFTED_BED);
      System.out.println("Crafted Bed");
    } else {
      System.out.println("Insufficient resources to craft Bed");
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
      case SHEEP:
        System.out.println("You put the sheep in your pocket.");
        inventory.add(SHEEP);
        break;
      case COW:
        System.out.println("You put the cow in your pocket.");
        inventory.add(COW);
      case AIR:
        System.out.println("Nothing to interact with here.");
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
      case SHEEP:
        return "Wool";
      case COW:
        return "Cow";
      default:
        return "Unknown";
    }
  }

  public static void displayLegend() {
    System.out.println(ANSI_BLUE + "Legend:");
    System.out.println(ANSI_WHITE + "-- - Empty block");
    System.out.println(ANSI_RED + "\u2593 - Wood block");
    System.out.println(ANSI_GREEN + "\u2591 - Leaves block");
    System.out.println(ANSI_BLUE + "\u2593 - Stone block");
    System.out.println(ANSI_WHITE + "\u00B0 - Iron ore block");
    System.out.println(ANSI_PURPLE + "\u00E6 - Sheep");
    System.out.println(ANSI_BROWN + "\u00FE - Cow");
    System.out.println(ANSI_PLAYER + "\u2593 - Player" + ANSI_RESET);
  }

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

  // private static String getBlockColor(int blockType) {
  //   switch (blockType) {
  //     case AIR:
  //       return "";
  //     case WOOD:
  //       return ANSI_RED;
  //     case LEAVES:
  //       return ANSI_GREEN;
  //     case STONE:
  //       return ANSI_GRAY;
  //     case IRON_ORE:
  //       return ANSI_WHITE;
  //     case SHEEP:
  //       return ANSI_PURPLE;
  //     case COW:
  //       return ANSI_BROWN;
  //     default:
  //       return "";
  //   }
  // }

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
      case CRAFTED_BED:
        return "Bed";
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
      default:
        return "";
    }
  }

  public static void getCountryAndQuoteFromServer() {
    try {
      URL url = URI.create("https://flag.ashish.nl/get_flag").toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

      String payload =
        "{\"group_number\":\"49\",\"group_name\":\"Group49\",\"difficulty_level\":\"hard\"}";
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
      int countryStart = json.indexOf("{\"country\":") + 11;
      int countryEnd = json.indexOf(",", countryStart);
      String country = json
        .substring(countryStart, countryEnd)
        .replace("\"", ""); // Remove quotation marks
      int quoteStart = json.indexOf("\"quote\": ") + 9;
      int quoteEnd = json.indexOf("}", quoteStart);
      String quote = json.substring(quoteStart, quoteEnd).replace("\"", ""); // Remove quotation marks
      System.out.println(" " + country);
      System.out.println(" " + quote);
      //printFlagToScreen(country);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error connecting to the server");
    }
  }

  //logic to print out the image
  private static BufferedImage image;

  public static Map<String, String> createCountryToCodeMap() {
    Map<String, String> countryToCodeMap = new HashMap<>();
    countryToCodeMap.put("ANDORRA", "AD");
    countryToCodeMap.put("UNITED ARAB EMIRATES", "AE");
    countryToCodeMap.put("AFGHANISTAN", "AF");
    countryToCodeMap.put("ANTIGUA AND BARBUDA", "AG");
    countryToCodeMap.put("ANGUILLA", "AI");
    countryToCodeMap.put("ALBANIA", "AL");
    countryToCodeMap.put("ARMENIA", "AM");
    countryToCodeMap.put("NETHERLANDS ANTILLES", "AN");
    countryToCodeMap.put("ANGOLA", "AO");
    countryToCodeMap.put("ANTARCTICA", "AQ");
    countryToCodeMap.put("ARGENTINA", "AR");
    countryToCodeMap.put("AMERICAN SAMOA", "AS");
    countryToCodeMap.put("AUSTRIA", "AT");
    countryToCodeMap.put("AUSTRALIA", "AU");
    countryToCodeMap.put("ARUBA", "AW");
    countryToCodeMap.put("ÅLAND ISLANDS", "AX");
    countryToCodeMap.put("AZERBAIJAN", "AZ");
    countryToCodeMap.put("BOSNIA AND HERZEGOVINA", "BA");
    countryToCodeMap.put("BARBADOS", "BB");
    countryToCodeMap.put("BANGLADESH", "BD");
    countryToCodeMap.put("BELGIUM", "BE");
    countryToCodeMap.put("BURKINA FASO", "BF");
    countryToCodeMap.put("BULGARIA", "BG");
    countryToCodeMap.put("BAHRAIN", "BH");
    countryToCodeMap.put("BURUNDI", "BI");
    countryToCodeMap.put("BENIN", "BJ");
    countryToCodeMap.put("SAINT BARTHÉLEMY", "BL");
    countryToCodeMap.put("BERMUDA", "BM");
    countryToCodeMap.put("BRUNEI DARUSSALAM", "BN");
    countryToCodeMap.put("BOLIVIA", "BO");
    countryToCodeMap.put("BONAIRE, SINT EUSTATIUS AND SABA", "BQ");
    countryToCodeMap.put("BRAZIL", "BR");
    countryToCodeMap.put("BAHAMAS", "BS");
    countryToCodeMap.put("BHUTAN", "BT");
    countryToCodeMap.put("BOUVET ISLAND", "BV");
    countryToCodeMap.put("BOTSWANA", "BW");
    countryToCodeMap.put("BELARUS", "BY");
    countryToCodeMap.put("BELIZE", "BZ");
    countryToCodeMap.put("CANADA", "CA");
    countryToCodeMap.put("COCOS (KEELING) ISLANDS", "CC");
    countryToCodeMap.put("CONGO, THE DEMOCRATIC REPUBLIC OF THE", "CD");
    countryToCodeMap.put("CENTRAL AFRICAN REPUBLIC", "CF");
    countryToCodeMap.put("CONGO", "CG");
    countryToCodeMap.put("SWITZERLAND", "CH");
    countryToCodeMap.put("CÔTE D'IVOIRE", "CI");
    countryToCodeMap.put("COOK ISLANDS", "CK");
    countryToCodeMap.put("CHILE", "CL");
    countryToCodeMap.put("CAMEROON", "CM");
    countryToCodeMap.put("CHINA", "CN");
    countryToCodeMap.put("COLOMBIA", "CO");
    countryToCodeMap.put("COSTA RICA", "CR");
    countryToCodeMap.put("CUBA", "CU");
    countryToCodeMap.put("CAPE VERDE", "CV");
    countryToCodeMap.put("CURAÇAO", "CW");
    countryToCodeMap.put("CHRISTMAS ISLAND", "CX");
    countryToCodeMap.put("CYPRUS", "CY");
    countryToCodeMap.put("CZECH REPUBLIC", "CZ");
    countryToCodeMap.put("GERMANY", "DE");
    countryToCodeMap.put("DJIBOUTI", "DJ");
    countryToCodeMap.put("DENMARK", "DK");
    countryToCodeMap.put("DOMINICA", "DM");
    countryToCodeMap.put("DOMINICAN REPUBLIC", "DO");
    countryToCodeMap.put("ALGERIA", "DZ");
    countryToCodeMap.put("ECUADOR", "EC");
    countryToCodeMap.put("ESTONIA", "EE");
    countryToCodeMap.put("EGYPT", "EG");
    countryToCodeMap.put("WESTERN SAHARA", "EH");
    countryToCodeMap.put("ERITREA", "ER");
    countryToCodeMap.put("SPAIN", "ES");
    countryToCodeMap.put("ETHIOPIA", "ET");
    countryToCodeMap.put("EU", "EU");
    countryToCodeMap.put("FINLAND", "FI");
    countryToCodeMap.put("FIJI", "FJ");
    countryToCodeMap.put("FALKLAND ISLANDS (MALVINAS)", "FK");
    countryToCodeMap.put("MICRONESIA, FEDERATED STATES OF", "FM");
    countryToCodeMap.put("FAROE ISLANDS", "FO");
    countryToCodeMap.put("FRANCE", "FR");
    countryToCodeMap.put("GABON", "GA");
    countryToCodeMap.put("UNITED KINGDOM", "GB");
    countryToCodeMap.put("GRENADA", "GD");
    countryToCodeMap.put("GEORGIA", "GE");
    countryToCodeMap.put("FRENCH GUIANA", "GF");
    countryToCodeMap.put("GUERNSEY", "GG");
    countryToCodeMap.put("GHANA", "GH");
    countryToCodeMap.put("GIBRALTAR", "GI");
    countryToCodeMap.put("GREENLAND", "GL");
    countryToCodeMap.put("GAMBIA", "GM");
    countryToCodeMap.put("GUINEA", "GN");
    countryToCodeMap.put("GUADELOUPE", "GP");
    countryToCodeMap.put("EQUATORIAL GUINEA", "GQ");
    countryToCodeMap.put("GREECE", "GR");
    countryToCodeMap.put("SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS", "GS");
    countryToCodeMap.put("GUATEMALA", "GT");
    countryToCodeMap.put("GUAM", "GU");
    countryToCodeMap.put("GUINEA-BISSAU", "GW");
    countryToCodeMap.put("GUYANA", "GY");
    countryToCodeMap.put("HONG KONG", "HK");
    countryToCodeMap.put("HEARD AND MCDONALD ISLANDS", "HM");
    countryToCodeMap.put("HONDURAS", "HN");
    countryToCodeMap.put("CROATIA", "HR");
    countryToCodeMap.put("HAITI", "HT");
    countryToCodeMap.put("HUNGARY", "HU");
    countryToCodeMap.put("IC", "IC");
    countryToCodeMap.put("INDONESIA", "ID");
    countryToCodeMap.put("IRELAND", "IE");
    countryToCodeMap.put("ISRAEL", "IL");
    countryToCodeMap.put("ISLE OF MAN", "IM");
    countryToCodeMap.put("INDIA", "IN");
    countryToCodeMap.put("BRITISH INDIAN OCEAN TERRITORY", "IO");
    countryToCodeMap.put("IRAQ", "IQ");
    countryToCodeMap.put("IRAN, ISLAMIC REPUBLIC OF", "IR");
    countryToCodeMap.put("ICELAND", "IS");
    countryToCodeMap.put("ITALY", "IT");
    countryToCodeMap.put("JERSEY", "JE");
    countryToCodeMap.put("JAMAICA", "JM");
    countryToCodeMap.put("JORDAN", "JO");
    countryToCodeMap.put("JAPAN", "JP");
    countryToCodeMap.put("KENYA", "KE");
    countryToCodeMap.put("KYRGYZSTAN", "KG");
    countryToCodeMap.put("CAMBODIA", "KH");
    countryToCodeMap.put("KIRIBATI", "KI");
    countryToCodeMap.put("COMOROS", "KM");
    countryToCodeMap.put("SAINT KITTS AND NEVIS", "KN");
    countryToCodeMap.put("KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF", "KP");
    countryToCodeMap.put("KOREA, REPUBLIC OF", "KR");
    countryToCodeMap.put("KUWAIT", "KW");
    countryToCodeMap.put("CAYMAN ISLANDS", "KY");
    countryToCodeMap.put("KAZAKHSTAN", "KZ");
    countryToCodeMap.put("LAO PEOPLE'S DEMOCRATIC REPUBLIC", "LA");
    countryToCodeMap.put("LEBANON", "LB");
    countryToCodeMap.put("SAINT LUCIA", "LC");
    countryToCodeMap.put("LIECHTENSTEIN", "LI");
    countryToCodeMap.put("SRI LANKA", "LK");
    countryToCodeMap.put("LIBERIA", "LR");
    countryToCodeMap.put("LESOTHO", "LS");
    countryToCodeMap.put("LITHUANIA", "LT");
    countryToCodeMap.put("LUXEMBOURG", "LU");
    countryToCodeMap.put("LATVIA", "LV");
    countryToCodeMap.put("LIBYA", "LY");
    countryToCodeMap.put("MOROCCO", "MA");
    countryToCodeMap.put("MONACO", "MC");
    countryToCodeMap.put("MOLDOVA, REPUBLIC OF", "MD");
    countryToCodeMap.put("MONTENEGRO", "ME");
    countryToCodeMap.put("SAINT MARTIN", "MF");
    countryToCodeMap.put("MADAGASCAR", "MG");
    countryToCodeMap.put("MARSHALL ISLANDS", "MH");
    countryToCodeMap.put("MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF", "MK");
    countryToCodeMap.put("MALI", "ML");
    countryToCodeMap.put("MYANMAR", "MM");
    countryToCodeMap.put("MONGOLIA", "MN");
    countryToCodeMap.put("MACAO", "MO");
    countryToCodeMap.put("NORTHERN MARIANA ISLANDS", "MP");
    countryToCodeMap.put("MARTINIQUE", "MQ");
    countryToCodeMap.put("MAURITANIA", "MR");
    countryToCodeMap.put("MONTSERRAT", "MS");
    countryToCodeMap.put("MALTA", "MT");
    countryToCodeMap.put("MAURITIUS", "MU");
    countryToCodeMap.put("MALDIVES", "MV");
    countryToCodeMap.put("MALAWI", "MW");
    countryToCodeMap.put("MEXICO", "MX");
    countryToCodeMap.put("MALAYSIA", "MY");
    countryToCodeMap.put("MOZAMBIQUE", "MZ");
    countryToCodeMap.put("NAMIBIA", "NA");
    countryToCodeMap.put("NEW CALEDONIA", "NC");
    countryToCodeMap.put("NIGER", "NE");
    countryToCodeMap.put("NORFOLK ISLAND", "NF");
    countryToCodeMap.put("NIGERIA", "NG");
    countryToCodeMap.put("NICARAGUA", "NI");
    countryToCodeMap.put("NETHERLANDS", "NL");
    countryToCodeMap.put("NORWAY", "NO");
    countryToCodeMap.put("NEPAL", "NP");
    countryToCodeMap.put("NAURU", "NR");
    countryToCodeMap.put("NIUE", "NU");
    countryToCodeMap.put("NY", "NY");
    countryToCodeMap.put("NEW ZEALAND", "NZ");
    countryToCodeMap.put("OMAN", "OM");
    countryToCodeMap.put("PANAMA", "PA");
    countryToCodeMap.put("PERU", "PE");
    countryToCodeMap.put("FRENCH POLYNESIA", "PF");
    countryToCodeMap.put("PAPUA NEW GUINEA", "PG");
    countryToCodeMap.put("PHILIPPINES", "PH");
    countryToCodeMap.put("PAKISTAN", "PK");
    countryToCodeMap.put("POLAND", "PL");
    countryToCodeMap.put("SAINT PIERRE AND MIQUELON", "PM");
    countryToCodeMap.put("PITCAIRN", "PN");
    countryToCodeMap.put("PUERTO RICO", "PR");
    countryToCodeMap.put("PALESTINE, STATE OF", "PS");
    countryToCodeMap.put("PORTUGAL", "PT");
    countryToCodeMap.put("PALAU", "PW");
    countryToCodeMap.put("PARAGUAY", "PY");
    countryToCodeMap.put("QATAR", "QA");
    countryToCodeMap.put("RÉUNION", "RE");
    countryToCodeMap.put("ROMANIA", "RO");
    countryToCodeMap.put("SERBIA", "RS");
    countryToCodeMap.put("RUSSIAN FEDERATION", "RU");
    countryToCodeMap.put("RWANDA", "RW");
    countryToCodeMap.put("SAUDI ARABIA", "SA");
    countryToCodeMap.put("SOLOMON ISLANDS", "SB");
    countryToCodeMap.put("SEYCHELLES", "SC");
    countryToCodeMap.put("SUDAN", "SD");
    countryToCodeMap.put("SWEDEN", "SE");
    countryToCodeMap.put("SINGAPORE", "SG");
    countryToCodeMap.put("SAINT HELENA", "SH");
    countryToCodeMap.put("SLOVENIA", "SI");
    countryToCodeMap.put("SVALBARD AND JAN MAYEN", "SJ");
    countryToCodeMap.put("SLOVAKIA", "SK");
    countryToCodeMap.put("SIERRA LEONE", "SL");
    countryToCodeMap.put("SAN MARINO", "SM");
    countryToCodeMap.put("SENEGAL", "SN");
    countryToCodeMap.put("SOMALIA", "SO");
    countryToCodeMap.put("SURINAME", "SR");
    countryToCodeMap.put("SOUTH SUDAN", "SS");
    countryToCodeMap.put("SAO TOME AND PRINCIPE", "ST");
    countryToCodeMap.put("EL SALVADOR", "SV");
    countryToCodeMap.put("SINT MAARTEN", "SX");
    countryToCodeMap.put("SYRIAN ARAB REPUBLIC", "SY");
    countryToCodeMap.put("SWAZILAND", "SZ");
    countryToCodeMap.put("TURKS AND CAICOS ISLANDS", "TC");
    countryToCodeMap.put("CHAD", "TD");
    countryToCodeMap.put("FRENCH SOUTHERN TERRITORIES", "TF");
    countryToCodeMap.put("TOGO", "TG");
    countryToCodeMap.put("THAILAND", "TH");
    countryToCodeMap.put("TAJIKISTAN", "TJ");
    countryToCodeMap.put("TOKELAU", "TK");
    countryToCodeMap.put("TIMOR-LESTE", "TL");
    countryToCodeMap.put("TURKMENISTAN", "TM");
    countryToCodeMap.put("TUNISIA", "TN");
    countryToCodeMap.put("TONGA", "TO");
    countryToCodeMap.put("TURKEY", "TR");
    countryToCodeMap.put("TRINIDAD AND TOBAGO", "TT");
    countryToCodeMap.put("TUVALU", "TV");
    countryToCodeMap.put("TAIWAN, REPUBLIC OF CHINA", "TW");
    countryToCodeMap.put("TANZANIA, UNITED REPUBLIC OF", "TZ");
    countryToCodeMap.put("UKRAINE", "UA");
    countryToCodeMap.put("UGANDA", "UG");
    countryToCodeMap.put("UNITED STATES MINOR OUTLYING ISLANDS", "UM");
    countryToCodeMap.put("UNITED STATES", "US");
    countryToCodeMap.put("URUGUAY", "UY");
    countryToCodeMap.put("UZBEKISTAN", "UZ");
    countryToCodeMap.put("HOLY SEE (VATICAN CITY STATE)", "VA");
    countryToCodeMap.put("SAINT VINCENT AND THE GRENADINES", "VC");
    countryToCodeMap.put("VENEZUELA, BOLIVARIAN REPUBLIC OF", "VE");
    countryToCodeMap.put("VIRGIN ISLANDS, BRITISH", "VG");
    countryToCodeMap.put("VIRGIN ISLANDS, U.S.", "VI");
    countryToCodeMap.put("VIETNAM", "VN");
    countryToCodeMap.put("VANUATU", "VU");
    countryToCodeMap.put("WALLIS AND FUTUNA", "WF");
    countryToCodeMap.put("SAMOA", "WS");
    countryToCodeMap.put("XK", "XK");
    countryToCodeMap.put("YEMEN", "YE");
    countryToCodeMap.put("MAYOTTE", "YT");
    countryToCodeMap.put("SOUTH AFRICA", "ZA");
    countryToCodeMap.put("ZAMBIA", "ZM");
    countryToCodeMap.put("ZIMBABWE", "ZW");

    return countryToCodeMap;
  }

  public static void downloadImage(String country) throws IOException {
    country = country.toUpperCase(); // Convert to uppercase to avoid case sensitivity
    Map<String, String> countryToCodeMap = createCountryToCodeMap();
    String countryCode = countryToCodeMap.get(country);
    if (countryCode == null) {
      throw new IllegalArgumentException("Unknown country: " + country);
    }
    URL url = new URL(
      String.format(
        "https://flagcdn.com/w160/%s.png",
        countryCode.toLowerCase()
      )
    );
    BufferedImage originalImage = ImageIO.read(url);

    //flexible approach
    // Resize the image to squish it vertically by a factor of 2, to account for the character aspect ratio not being 1:1
    //int newHeight = originalImage.getHeight() / 2;
    //int newWidth = originalImage.getWidth();

    //resize to the size of the map
    int newWidth = NEW_WORLD_WIDTH;
    int newHeight = NEW_WORLD_HEIGHT;
    Image tmp = originalImage.getScaledInstance(
      newWidth,
      newHeight,
      Image.SCALE_SMOOTH
    );
    BufferedImage resizedImage = new BufferedImage(
      newWidth,
      newHeight,
      BufferedImage.TYPE_INT_ARGB
    );

    // Draw the resized image
    Graphics2D g2d = resizedImage.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    // Set the image to the resized version
    image = resizedImage;
  }

  public static void populateMap() {
    int width = image.getWidth();
    int height = image.getHeight();

    // Iterate over every pixel
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        // Get the RGB values for the pixel by shifting and masking the pixel valuer (shoutout to xs0, https://stackoverflow.com/questions/6126439/what-does-0xff-do)
        int rgb = image.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        imageMap.put(x + "," + y, new int[] { red, green, blue });
      }
    }
    //once the image is in the map, we can print it out
    hasRequestedFlag = true;
  }

  public static void printFlagToScreen(String Country) throws IOException {
    downloadImage(Country); // Download and resize image
    populateMap(); // Populate the map with the image
  }
}
