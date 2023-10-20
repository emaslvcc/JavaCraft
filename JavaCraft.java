import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;


public class JavaCraft {

  private static final int AIR = 0;
  private static final int WOOD = 1;
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int IRON_ORE = 4;
  private static final int DIRT = 5;
  private static final int FLOWER = 6;
  private static int NEW_WORLD_WIDTH = 25;
  private static int NEW_WORLD_HEIGHT = 15;

  private static int NEW_WORLD_FLAG_WIDTH = 50;
  private static int NEW_WORLD_FLAG_HEIGHT = 30;
  private static int EMPTY_BLOCK = 0;
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;
  private static final int CRAFTED_IRON_PICKAXE = 203;
  private static final String ANSI_BROWN = "\u001B[33m"; //RGB(255, 255, 0) (Yellow)
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\u001B[32m"; // RGB(0, 255, 0) (Green)
  private static final String ANSI_YELLOW = "\u001B[33m"; // RGB(255, 255, 0) (Yellow)
  private static final String ANSI_CYAN = "\u001B[36m"; // RGB(0, 255, 255) (Cyan)
  private static final String ANSI_RED = "\u001B[31m"; // RGB(255, 0, 0) (Red)
  private static final String ANSI_PURPLE = "\u001B[35m"; // RGB(128, 0, 128) (Purple)
  private static final String ANSI_BLUE = "\u001B[34m"; // RGB(0, 0, 255) (Blue)
  private static final String ANSI_GRAY = "\u001B[37m"; // RGB(128, 128, 128) (Gray)
  private static final String ANSI_WHITE = "\u001B[97m"; // RGB(255, 255, 255) (White)

  private static final String BLOCK_NUMBERS_INFO = "Block Numbers:\n" +
          "0 - Empty block\n" +
          "1 - Wood block\n" +
          "2 - Leaves block\n" +
          "3 - Stone block\n" +
          "4 - Iron ore block\n" +
          "5 - Dirt block\n"+
          "6 - Flower\n"+
          "7 - Wooden Planks (Crafted Item)\n" +
          "8 - Stick (Crafted Item)\n" +
          "9 - Iron Ingot (Crafted Item)"+
          "10 - Iron Pickaxe (Crafted Item)";

  private static int[][] world;
  private static String[][] flagWorld;
  private static int worldWidth;
  private static int worldHeight;
  private static int playerX;
  private static int playerY;
  private static String country;
  private static String countryURL;
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
    System.out.println(ANSI_RED+" - Write getflag to get a new flag of level hard displayed in the secret room"+ANSI_RESET);

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
    JavaCraft.flagWorld = new String[NEW_WORLD_FLAG_WIDTH][NEW_WORLD_FLAG_HEIGHT];
    JavaCraft.country = "Malaysia";
    JavaCraft.countryURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2b/Flag_of_Malaysia.png/1200px-Flag_of_Malaysia.png";

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
        } else if (randValue < 45) {
          world[x][y] = STONE;
        } else if (randValue < 50) {
          world[x][y] = IRON_ORE;
        } else if (randValue < 65) {
          world[x][y] = DIRT;
        } else if (randValue < 80) {
          world[x][y] = FLOWER;
        }   else {
          world[x][y] = AIR;
        }
      }
    }
  }

  public static void displayWorld() {
    String[] legend = {
            ANSI_BLUE + "  Legend:",
            ANSI_WHITE + "  -- - Empty block",
            ANSI_RED + "  \u2591\u2591 - Wood block",
            ANSI_GREEN + "  \u2591\u2591 - Leaves block",
            ANSI_BLUE + "  \u2592\u2592 - Stone block",
            ANSI_WHITE + "  \u25A0\u25A0 - Iron ore block",
            ANSI_PURPLE + "  \u2592\u2592 - Flower",
            ANSI_BROWN + "  \u2588\u2588 - Dirt",
            ANSI_BLUE + "  P - Player" + ANSI_RESET
    };

    int legendWidth = legend[0].length();

    System.out.println(ANSI_CYAN + "World Map:" + ANSI_RESET + " ".repeat(legendWidth));
    System.out.println("╔══" + "═".repeat(worldWidth * 2 - 2) + "╗" + " ".repeat(legendWidth));

    for (int y = 0; y < worldHeight; y++) {
      System.out.print("║");
      for (int x = 0; x < worldWidth; x++) {
        if (x == playerX && y == playerY && !inSecretArea) {
          System.out.print(ANSI_BLUE + "P " + ANSI_RESET);
        } else if (x == playerX && y == playerY && inSecretArea) {
          System.out.print(ANSI_BLUE + "P " + ANSI_RESET);
        } else {
          System.out.print(getBlockSymbol(world[x][y]));
        }
      }
      System.out.print("║");

      // Print legend line if it exists, otherwise print spaces
      if (y < legend.length) {
        System.out.print(legend[y]);
      }

      System.out.println();
    }

    System.out.println("╚══" + "═".repeat(worldWidth * 2 - 2) + "╝" + " ".repeat(legendWidth));
  }
  public static void displayEmptyWorld() {

    System.out.println("╔══" + "═".repeat(NEW_WORLD_FLAG_WIDTH * 2 - 2) + "╗");

    for (int y = 0; y < NEW_WORLD_FLAG_HEIGHT; y++) {
      System.out.print("║");
      for (int x = 0; x < NEW_WORLD_FLAG_WIDTH; x++) {
        if (x == playerX && y == playerY && !inSecretArea) {
          System.out.print(ANSI_BLUE + "P " + ANSI_RESET);
        } else if (x == playerX && y == playerY && inSecretArea) {
          System.out.print(ANSI_BLUE + "P " + ANSI_RESET);
        } else {
          System.out.print(flagWorld[x][y] + " ");
        }
      }
      System.out.print("║");


      System.out.println();
    }

    System.out.println("╚══" + "═".repeat(NEW_WORLD_FLAG_WIDTH * 2 - 2) + "╝");
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
      case DIRT:
        blockColor = ANSI_BROWN;
        break;
      case FLOWER:
        blockColor = ANSI_PURPLE;
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
        return '\u2591'; // \u2592
      case LEAVES:
        return '\u2591'; // \u00A7
      case STONE:
        return '\u2592'; // \u2593
      case IRON_ORE:
        return '\u25A0'; // \u00B0
      case FLOWER:
        return '\u2592'; // \u00A4
      case DIRT:
        return '\u2588'; // \u25A3
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
      if (secretDoorUnlocked){
        resetWorld();
        displayEmptyWorld();
      } else {
        displayWorld();
      }
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
        if (unlockMode  && craftingCommandEntered && miningCommandEntered && movementCommandEntered) {
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
    // This allows us to take block from a given range of colors
    ColorUtils color = new ColorUtils();
    world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];

    try {
      String imageUrl = countryURL;
      URL url = new URL(imageUrl);
      BufferedImage image = ImageIO.read(url);

      int width = image.getWidth();
      int height = image.getHeight();

      int desiredWidth = 50;
      int desiredHeight = 30;

      BufferedImage resizedImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2d = resizedImage.createGraphics();
      g2d.drawImage(image.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH), 0, 0, null);
      g2d.dispose();
      ImageIO.write(resizedImage, "jpg", new File("resized_image.jpg"));
      width = resizedImage.getWidth();
      height = resizedImage.getHeight();

      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int pixel = resizedImage.getRGB(x, y);
          int red = (pixel >> 16) & 0xFF;
          int green = (pixel >> 8) & 0xFF;
          int blue = pixel & 0xFF;
          char symbol = '▓';

          flagWorld[x][y] = "\u001B[38;2;"+ color.getColorNameFromRgb(red,green,blue)+symbol;

          //System.out.print("\u001B[38;2;"+ color.getColorNameFromRgb(red,green,blue)+symbol + ' ');

          //System.out.print("\u001B[38;2;"+red + ";" +green + ";"+blue + "m"+symbol + " " +color.getColorNameFromRgb(red,green,blue));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
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

  // This
  private static void lookAround() {
    System.out.println("You look around and see:");
    for (int y = Math.max(0, playerY - 1); y <= Math.min(playerY + 1, worldHeight - 1); y++) {
      for (int x = Math.max(0, playerX - 1); x <= Math.min(playerX + 1, worldWidth - 1); x++) {
        if (x == playerX && y == playerY) {
          System.out.print(ANSI_BLUE + "P " + ANSI_RESET);
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
    Random random = new Random();
    int rand = random.nextInt(50);
    int blockType = world[playerX][playerY];
    if (blockType != AIR) {
      if(rand > 30 && rand < 45 && craftedItems.contains(CRAFTED_IRON_PICKAXE))
      {
        System.out.println("You were lucky enough to mine 2 " + getBlockName(blockType) + ".");
        inventory.add(blockType);
        inventory.add(blockType);
      }
      else if(rand > 45 && craftedItems.contains(CRAFTED_IRON_PICKAXE))
      {
        System.out.println("You were lucky enough to mine 3 " + getBlockName(blockType) + ".");
        inventory.add(blockType);
        inventory.add(blockType);
        inventory.add(blockType);
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
      case CRAFTED_IRON_PICKAXE:
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
        return CRAFTED_IRON_PICKAXE;
      default:
        return -1;
    }
  }

  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 1 Iron Ore");
    System.out.println("4. Craft Iron Pickaxe: 2 Iron ingots and 1 Wood");
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
  public static void craftIronPickaxe()
  {
    if(craftedItemsContain(CRAFTED_STICK) && craftedItemsContain(CRAFTED_IRON_INGOT, 2))
    {
      removeCraftedItems(CRAFTED_STICK, 1);
      removeCraftedItems(CRAFTED_IRON_INGOT, 2);
      addCraftedItem(CRAFTED_IRON_PICKAXE);
      System.out.println("Crafter Iron Pickaxe");
    }
    else {
      System.out.println("Insufficient resources to craft a Iron Pickaxe.");
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
    if (inventoryContains(IRON_ORE, 1)) {
      removeItemsFromInventory(IRON_ORE, 1);
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
  public static boolean craftedItemsContain(int item) {
    return craftedItems.contains(item);
  }

  public static boolean craftedItemsContain(int item, int count) {
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
  public static void removeCraftedItems(int item, int count) {
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
      case DIRT:
        System.out.println("You gathered some dirt from the ground");
        break;
      case FLOWER:
        System.out.println("You collected a flower");
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
      case FLOWER:
        return "Flower";
      case DIRT:
        return "Dirt Block";
      default:
        return "Unknown";
    }
  }

  public static void displayLegend() {
    System.out.println(ANSI_BLUE + "Legend:");
    System.out.println(ANSI_WHITE + "-- - Empty block");
    System.out.println(ANSI_RED + "\u2591\u2591 - Wood block");
    System.out.println(ANSI_GREEN + "\u2591\u2591 - Leaves block");
    System.out.println(ANSI_BLUE + "\u2592\u2592 - Stone block");
    System.out.println(ANSI_WHITE + "\u25A0\u25A0- Iron ore block");
    System.out.println(ANSI_PURPLE + "\u2592\u2592- Flower");
    System.out.println(ANSI_BROWN + "\u2588\u2588- Dirt");
    System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
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
      case FLOWER:
        return ANSI_PURPLE;
      case DIRT:
        return ANSI_BROWN;
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
        return ANSI_BROWN;
      default:
        return "";
    }
  }
  public static void getCountryUrl(){
    try {

      String apiUrl = "https://restcountries.com/v2/name/" + country.replace(" ", "%20");
      URL url = new URL(apiUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      int responseCode = connection.getResponseCode();
      if (responseCode == 200) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          response.append(line);
        }
        reader.close();
        // Parse the JSON response to extract the flag URL
        String jsonResponse = response.toString();

          int pngIndex = jsonResponse.indexOf("\"png\":\"");

          if (pngIndex != -1) {
            // Extract the URL of the flag image (PNG)
            int startIndex = pngIndex + "\"png\":\"".length();
            int endIndex = jsonResponse.indexOf("\"}", startIndex);
            if (endIndex != -1) {
              countryURL = jsonResponse.substring(startIndex, endIndex);
            } else {
              System.out.println("Flag URL (PNG) not found in the JSON.");
            }
          } else {
            System.out.println("Flag URL (PNG) key not found in the JSON.");
          }
      }  else {
        connection.disconnect();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  };
  public static void getCountryAndQuoteFromServer() {
    try {
      URL url = new URL("https://flag.ashish.nl/get_flag");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);
      String payload = "        {\n" +
              "            \"group_number\": \"53\",\n" +
              "            \"group_name\": \"Group53\",\n" +
              "            \"difficulty_level\": \"hard\"\n" +
              "        }";
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

      int startIndex = json.indexOf("\"country\":\"") + "\"country\":\"".length();
      int endIndex = json.indexOf("\",\"", startIndex);

      if (startIndex != -1 && endIndex != -1) {
        country = json.substring(startIndex, endIndex);
        System.out.println("Country: " + country);
      } else {
        System.out.println("Country not found in the JSON string.");
      }

      getCountryUrl();

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error connecting to the server");
    }
  }
}