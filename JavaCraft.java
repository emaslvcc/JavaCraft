import java.util.*;
import java.net.*;
import java.io.*;

public class JavaCraft {
  private static final int AIR = 0;
  private static final int WOOD = 1;
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int IRON_ORE = 4;
  private static int NEW_WORLD_WIDTH = 25;
  private static int NEW_WORLD_HEIGHT = 15;
  private static int EMPTY_BLOCK = 0;
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;

  // Sets the color of the text
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


  /**
   *
   */
  private static final String BLOCK_NUMBERS_INFO = "Block Numbers:\n" +
          "0 - Empty block\n" +
          "1 - Wood block\n" +
          "2 - Leaves block\n" +
          "3 - Stone block\n" +
          "4 - Iron ore block\n" +
          "5 - Wooden Planks (Crafted Item)\n" +
          "6 - Stick (Crafted Item)\n" +
          "7 - Iron Ingot (Crafted Item)";
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


  //
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

    //Calls the start game method if the "Y" key is pressed
    if (startGameChoice.equals("Y")) {
      startGame();
    } else {
      System.out.println("Game not started. Goodbye!");
    }
  }


  /**
   * Method used to initialize the world based on given world dimensions
   * @param worldWidth Width of the world
   * @param worldHeight Height of the world
   */
  public static void initGame(int worldWidth, int worldHeight) {
    JavaCraft.worldWidth = worldWidth;
    JavaCraft.worldHeight = worldHeight;
    JavaCraft.world = new int[worldWidth][worldHeight];

    //Places the player in the center of the world
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
        } else {
          world[x][y] = AIR;
        }
      }
    }
  }


  /**
   * Method is responsible for the printing of the world, depending on if the door is unlocked or not it will generate a different world
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
   * Method returns the symbol to be used to represent each tile in the game, depending on the number used it will return a different color
   * to represent the block type
   * @param blockType Integer value corresponding to the tile type
   * @return Returns the symbol to be used to represent each tile in the game.
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
      default:
        return '-';
    }
  }


  //Method to start the game
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

      //Prints out the list of possible moves
      System.out.println(ANSI_CYAN
              + "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door"
              + ANSI_RESET);
      String input = scanner.next().toLowerCase();

      //Checking what the input is and depending on the input it completes a different action

      //Checks if the player decides to move
      if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("up") ||
              input.equalsIgnoreCase("s") || input.equalsIgnoreCase("down") ||
              input.equalsIgnoreCase("a") || input.equalsIgnoreCase("left") ||
              input.equalsIgnoreCase("d") || input.equalsIgnoreCase("right")) {
        //If the player has unlocked, then if they move it will make the boolean movementCommandEntered to be true
        if (unlockMode) {
          movementCommandEntered = true;
        }
        //Moves the player based on the input
        movePlayer(input);

        //Mining input detection
      } else if (input.equalsIgnoreCase("m")) { //Checks if the player decided to mine
        if (unlockMode) {//If the player has unlocked, then if they mine it will make the boolean miningCommandEntered to be true
          miningCommandEntered = true;
        }
        //Mines the block
        mineBlock();

        //Block placement detection
      } else if (input.equalsIgnoreCase("p")) {
        displayInventory();
        System.out.print("Enter the block type to place: ");
        int blockType = scanner.nextInt();
        placeBlock(blockType);

        //Crafting detection
      } else if (input.equalsIgnoreCase("c")) {
        displayCraftingRecipes();
        System.out.print("Enter the recipe number to craft: ");
        int recipe = scanner.nextInt();
        craftItem(recipe);

        //Interaction Detection
      } else if (input.equalsIgnoreCase("i")) {
        interactWithWorld();

        //Saves the game
      } else if (input.equalsIgnoreCase("save")) {
        System.out.print("Enter the file name to save the game state: ");
        String fileName = scanner.next();
        saveGame(fileName);

        //Loads the game from file
      } else if (input.equalsIgnoreCase("load")) {
        System.out.print("Enter the file name to load the game state: ");
        String fileName = scanner.next();
        loadGame(fileName);

        //quits the game
      } else if (input.equalsIgnoreCase("exit")) {
        System.out.println("Exiting the game. Goodbye!");
        break;

        //looks around
      } else if (input.equalsIgnoreCase("look")) {
        lookAround();

        //Makes the unlockMode boolean to be true
      } else if (input.equalsIgnoreCase("unlock")) {
        unlockMode = true;

        //Not sure what the point of it is, it returns an error
      } else if (input.equalsIgnoreCase("getflag")) {
        getCountryAndQuoteFromServer();
        waitForEnter();
      }

      // Tries to unlock the secret door but first checks to see all of the necessary requirements are satisfied
      else if (input.equalsIgnoreCase("open")) {
        if (unlockMode && craftingCommandEntered && miningCommandEntered && movementCommandEntered) {
          secretDoorUnlocked = true;
          resetWorld();
          System.out.println("Secret door unlocked!");
          waitForEnter();
        }

        //If the requirements are not satisfied all possibly true variables are reset and the user is prompted to try again
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

      //If the user inputs something that is not an action then they are prompted to try again with a correct command
      else {
        System.out.println(ANSI_YELLOW + "Invalid input. Please try again." + ANSI_RESET);
      }

      //Seemingly redudant code for making the requirements for the secret door true, however door does not unlock if this method is not used
      if (unlockMode) {
        if (input.equalsIgnoreCase("c")) {
          craftingCommandEntered = true;
        } else if (input.equalsIgnoreCase("m")) {
          miningCommandEntered = true;
        } else if (input.equalsIgnoreCase("open")) {
          openCommandEntered = true;
        }
      }

      //Enables access to the secret door area.
      if (secretDoorUnlocked) {
        clearScreen();  // Clears the map

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

  /**
   * Fills the users inventory with every item in the game
   */
  private static void fillInventory() {
    inventory.clear();
    for (int blockType = 1; blockType <= 4; blockType++) {
      for (int i = 0; i < INVENTORY_SIZE; i++) {
        inventory.add(blockType);
      }
    }
  }

  /**
   * Method generates an empty world with no blocks and resets the players position
   */
  private static void resetWorld() {
    generateEmptyWorld();
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;
  }

  /**
   * Method for making the striped Dutch flag when you unlock the secret door
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
   * Method clears the command line space
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
   * Method looks around in a 3x3 grid around the player and reprints the surrounding tiles/blocks
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
   * Depending on the player input it moves the player up, down, left, or right
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
   * Method responsible for mining a block, it checks to see the tile the player is occupying and then if the block isn't air the material
   * is added to the player's inventory. If it is air the player is told that there is nothing to mine and is prompted for a new action.
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
   * Method places a block depending on the ID integer that the player inputs, if the player has the item in their inventory it is placed where
   * the player is standing. Otherwise the player is told they do not have it in their inventory.
   * @param blockType
   */
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


  //The method is passed the ID integer of the crafted item and then depending on the crafted item it will return a corresponding ID
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


  /**
   * Depending on the ID of the block type entered it will return the ID of the crafted item
   * @param blockType Integer ID of the block type
   * @return Returns the Integer ID values of the crafted items
   */
  private static int getCraftedItemFromBlockType(int blockType) {
    switch (blockType) {
      case 5:
        return CRAFTED_WOODEN_PLANKS;
      case 6:
        return CRAFTED_STICK;
      case 7:
        return CRAFTED_IRON_INGOT;
      default:
        return -1;
    }
  }

  /**
   * Method prints out all the available options for crafting recipes
   */
  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
  }

  /**
   * Depending on the integer value input by the player, the method will call on the corresponding method to craft each item
   * @param recipe Integer value for each corresponding item
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

  /**
   * Method to craft wooden planks, checks if the player has the necessary materials if they do the item is crafted and added to their inventory,
   * if they don't the player is told they dont have sufficient resources.
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
   * Method to craft sticks, checks if the player has the necessary materials if they do the item is crafted and added to their inventory,
   * if they don't the player is told they dont have sufficient resources.
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
   * Method to craft iron ingots, checks if the player has the necessary materials if they do the item is crafted and added to their inventory,
   * if they don't the player is told they dont have sufficient resources.
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
   * Depending on a passed integer the method checks to see if the player has the corresponding item in their inventory and returns a boolean
   * @param item Integer ID value
   * @return Returns a true boolean value if the player has the item, if they dont the boolean returned is false
   */
  public static boolean inventoryContains(int item) {
    return inventory.contains(item);
  }


  /**
   * Checks to see if the player has a certain amount of a certain item in their inventory
   * @param item Intger ID for the item
   * @param count Amount of items that are to be checked
   * @return Returns a true or false boolean value depending on if the requirements are met
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

  /**
   * Method removes a quantity of a certain item from the inventory
   * @param item Integer ID for the item to be removed
   * @param count Integer quantity of the items to be removed
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

  /**
   * Method adds a crafted item to the players inventory
   * @param craftedItem Integer ID of the crafted item.
   */
  public static void addCraftedItem(int craftedItem) {
    if (craftedItems == null) {
      craftedItems = new ArrayList<>();
    }
    craftedItems.add(craftedItem);
  }

  /**
   * Depending on what block the player is standing on the player will interact in a different
   * way with each block type.
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
      case AIR:
        System.out.println("Nothing to interact with here.");
        break;
      default:
        System.out.println("Unrecognized block. Cannot interact.");
    }
    waitForEnter();
  }


  /**
   * Method takes care of saving the game to an external file
   * @param fileName Name of the save file
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
   * Method used to load the game from an external save file
   * @param fileName Name of the file to load from
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
   * Depending on the ID thats been passed to the method it will return the name of the block
   * @param blockType Integer ID
   * @return Returns a string value for the corresponing block
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
      default:
        return "Unknown";
    }
  }

  /**
   * Method prints out the game legend
   */
  public static void displayLegend() {
    System.out.println(ANSI_BLUE + "Legend:");
    System.out.println(ANSI_WHITE + "-- - Empty block");
    System.out.println(ANSI_RED + "\u2592\u2592 - Wood block");
    System.out.println(ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
    System.out.println(ANSI_BLUE + "\u2593\u2593 - Stone block");
    System.out.println(ANSI_WHITE + "\u00B0\u00B0- Iron ore block");
    System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
  }

  /**
   * Method prints out all the items as a list that are in the inventory of the player
   */
  public static void displayInventory() {

    //This inventory is for raw materials like wood and stone
    System.out.println("Inventory:");
    if (inventory.isEmpty()) {
      System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
    } else {
      int[] blockCounts = new int[5];
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

    //This is just for the crafted items
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
   * Depending on the ID passed the method returns the color of the block
   * @param blockType Integer ID for the block type to be checked
   * @return Returns the corresponding ANSI color code
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
      default:
        return "";
    }
  }

  /**
   * Method puts the game in a waiting state where it pauses until the player presses enter.
   */
  private static void waitForEnter() {
    System.out.println("Press Enter to continue...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
  }

  /**
   * Returns the name of the crafted item depending on the ID given to the method
   * @param craftedItem Integer ID of the item
   * @return Returns the string name of the crafted item
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

  /**
   * Method returns the color that corresponds to the crafted item
   * @param craftedItem Integer ID for the crafted item
   * @return Returns the ANSI color code of the corresponding crafted item
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


  /**
   * Method purpose will be clear later
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