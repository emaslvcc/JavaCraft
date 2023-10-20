import java.util.*;

import javax.print.attribute.standard.RequestingUserName;

import java.net.*;
import java.rmi.server.RemoteStub;
import java.io.*;

public class JavaCraft {
  private static final int AIR = 0; // Can't be changed troughout the code, because of the "final" (they are the index numbers)
  private static final int WOOD = 1; 
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int IRON_ORE = 4;
  private static final int COAL=5;
  private static final int GOLD_ORE = 6;
  private static final int APPLE=7;
  private static int NEW_WORLD_WIDTH = 25; // variable, an integer that can and will be changed troughout the code
  private static int NEW_WORLD_HEIGHT = 15;
  private static int EMPTY_BLOCK = 0;
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;
  private static final int CRAFT_APPEL_CANDY=103;
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;
  private static final int CRAFTED_TORCH=203;
  private static final int CRAFTED_APPLE_CANDY=204;
  private static final int CRAFTED_GOLDEN_INGOT = 300;
  private static final int CRAFTED_GOLDEN_PICKAXE = 205;

  private static final String ANSI_BROWN = "\u001B[33m"; //Colors pallete in java code
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_YELLOW = "\u001B[33m";
  private static final String ANSI_CYAN = "\u001B[36m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_PURPLE = "\u001B[35m";
  private static final String ANSI_BLUE = "\u001B[34m";
  private static final String ANSI_GRAY = "\u001B[37m";
  private static final String ANSI_WHITE = "\u001B[97m";
  private static final String PARLAK_KIRMIZI="\u001B[30m";
  private static final String ANSI_MAGENTA = "\u001b[35m";

  private static final String BLOCK_NUMBERS_INFO = "Block Numbers:\n" +
      "0 - Empty block\n" + //new line
      "1 - Wood block\n" +
      "2 - Leaves block\n" +
      "3 - Stone block\n" +
      "4 - Iron ore block\n" +
      "5 - Gold ore block\n" +
      "6 - Wooden Planks (Crafted Item)\n" +
      "7 - Stick (Crafted Item)\n" +
      "8 - Iron Ingot (Crafted Item)\n"+
      "9 - Gold Ingot (Crafted Item\n" +
      "10 - Gold Pickaxe (Crafted Item\n" +
      "11 - Coal block \n"+
      "12 - Torch(Crafted Item)\n"+
      "13-  Apple Candy(Crafted Item)\n"+
      "14-  Apple\n";
  private static int[][] world;
  private static int worldWidth;
  private static int worldHeight;
  private static int playerX;
  private static int playerY;
  private static List<Integer> inventory = new ArrayList<>(); // creates an array for the inventory items
  private static List<Integer> craftedItems = new ArrayList<>(); // creates an array for the crafteditems
  private static boolean unlockMode = false;
  private static boolean secretDoorUnlocked = false; // true or false to the unlock mode (secret doo)
  private static boolean inSecretArea = false;
  private static final int INVENTORY_SIZE = 100;

  public static void main(String[] args) { // This functions exists to explain to the player the command line and "teach him how to play the game"
    initGame(25, 15);
    generateWorld();
    System.out.println(ANSI_GREEN + "Welcome to Simple Minecraft!" + ANSI_RESET); //
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
    System.out.print("Start the game? (Y/N): "); // Scans the input of the player, if it is yes, then the game start, if it is no, then the game does not start
    String startGameChoice = scanner.next().toUpperCase();

    if (startGameChoice.equals("Y")) {
      startGame();
    } else {
      System.out.println("Game not started. Goodbye!");
    }
  }

  public static void initGame(int worldWidth, int worldHeight) { // Defines the size of the world
    JavaCraft.worldWidth = worldWidth;
    JavaCraft.worldHeight = worldHeight;
    JavaCraft.world = new int[worldWidth][worldHeight];
    playerX = worldWidth / 2;   // horizontal line
    playerY = worldHeight / 2; // vertical line
    inventory = new ArrayList<>();
  }

  public static void generateWorld() {  // creates the world
    Random rand = new Random();
    for (int y = 0; y < worldHeight; y++) {
      for (int x = 0; x < worldWidth; x++) {
        int randValue = rand.nextInt(100); // will pick an Integer between 0 and 100 hundred
        if (randValue < 10){
          world[x][y]=APPLE;
        }else if (randValue < 15){
          world[x][y]=COAL;
        }else if (randValue < 20) {
          world[x][y] = WOOD;
        } else if (randValue < 35) {
          world[x][y] = LEAVES;
        } else if (randValue < 50) {
          world[x][y] = STONE;
        } else if (randValue < 70) {
          world[x][y] = IRON_ORE;
        } else if (randValue < 75){
          world[x][y] = GOLD_ORE;
        } else {
          world[x][y] = AIR;
        }
      }
    }
  }

  public static void displayWorld() { // world borders , how the world will look like
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

  private static String getBlockSymbol(int blockType) { //block symbols
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
        case COAL:
        blockColor=PARLAK_KIRMIZI;
        break;
        case GOLD_ORE:
        blockColor = ANSI_YELLOW;
        break;
        case APPLE:
       blockColor=ANSI_YELLOW;
       break;
      default:
        blockColor = ANSI_RESET;
        break;
    }
    return blockColor + getBlockChar(blockType) + " ";
  }

  private static char getBlockChar(int blockType) { // When you place blocks in the world
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
        return '\u0024';
        case COAL:
      return '\u00A4';
      case APPLE:
      return '\u002A';
      default:
        return '-';
    }
  }

  public static void startGame() { // player inputs, how the player interacts with the world
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

  private static void fillInventory() { // Inventory numbers, when something is mined
    inventory.clear();
    for (int blockType = 1; blockType <= 10; blockType++) {
      for (int i = 0; i < INVENTORY_SIZE; i++) {
        inventory.add(blockType);
      }
    }
  }

  private static void resetWorld() { // when and how do you reset a world?
    generateEmptyWorld();
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;
  }

  private static void generateEmptyWorld() { // creation of the world
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

  private static void clearScreen() { // clear the console 
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

  private static void lookAround() {  //Look around command
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

  public static void movePlayer(String direction) { //Player movement, if player press W moves up, if player press S moves down (case: Up is not working though)
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

  public static void mineBlock() {  // Mining blocks
    int blockType = world[playerX][playerY];
    if (blockType != AIR && blockType!=APPLE) {
      inventory.add(blockType);
      world[playerX][playerY] = AIR;
      System.out.println("Mined " + getBlockName(blockType) + ".");
    }else if (blockType==APPLE){
      inventory.add(blockType);
        world[playerX][playerY] = AIR;
        System.out.println("Gathered " + getBlockName(blockType) + ".");
      } else {
        System.out.println("No block to mine here.");
      }
      waitForEnter();
    }

  public static void placeBlock(int blockType) {  // it is used for placing blocks, so if the player wishes to place a block it can. 
    if (blockType >= 0 && blockType <= 11) {
      if (blockType <= 9) {
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
      case CRAFTED_TORCH:
        return 4;
      case CRAFTED_WOODEN_PLANKS:
        return 5;
      case CRAFTED_STICK:
        return 6;
      case CRAFTED_IRON_INGOT:
        return 7;
        case CRAFTED_GOLDEN_INGOT:
        return 8;
        case CRAFTED_GOLDEN_PICKAXE:
        return 9;
        case CRAFTED_APPLE_CANDY:
      return 10;
      default:
        return -1;
    }
  }

  private static int getCraftedItemFromBlockType(int blockType) {
    switch (blockType) {
      case 4:
        return CRAFTED_TORCH;
      case 5:
        return CRAFTED_WOODEN_PLANKS;
      case 6:
        return CRAFTED_STICK;
      case 7:
        return CRAFTED_IRON_INGOT;
         case 8:
        return CRAFTED_GOLDEN_INGOT;
        case 9:
        return CRAFTED_GOLDEN_PICKAXE;
        case 10:
      return CRAFTED_APPLE_CANDY;
      default:
        return -1;
    }
  }

  public static void displayCraftingRecipes() { // Display for the crafting recipes the player has (it is very limited maybe expand a little bit?)
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    System.out.println("4. Craft Gold Ingot: 3 Gold Ore");
    System.out.println("5. Craft Gold Pickaxe: 3 Gold Ingot + 2 Stick");
    System.out.println("6. Craft Torch: 2 Coal");
    System.out.println("7. Craft Apple Candy: 1 Apple and 1 Stick");
  }

  public static void craftItem(int recipe) { // Creafting function, when the player craft something( uses the cases 1, 2, 3)
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
      craftGoldenIngot();
      break;
      case 5:
      craftGoldPickaxe();
      break;
      case 6:
       craftTorch();
       break;
       case 7:
       craftAppleCandy();
       break;
      default:
        System.out.println("Invalid recipe number.");
    }
    waitForEnter();
  }

  public static void craftWoodenPlanks() { // Wooden planks crafting function
    if (inventoryContains(WOOD, 2)) {
      removeItemsFromInventory(WOOD, 2);
      addCraftedItem(CRAFTED_WOODEN_PLANKS);
      System.out.println("Crafted Wooden Planks.");
    } else {
      System.out.println("Insufficient resources to craft Wooden Planks.");
    }
  }

  public static void craftStick() { // Stick crafting function
    if (inventoryContains(WOOD)) {
      removeItemsFromInventory(WOOD, 1);
      addCraftedItem(CRAFTED_STICK);
      System.out.println("Crafted Stick.");
    } else {
      System.out.println("Insufficient resources to craft Stick.");
    }
  }

  public static void craftIronIngot() { // Iron  crafting function, it takes 3 iron ores and create 1 iron ingot
    if (inventoryContains(IRON_ORE, 3)) {
      removeItemsFromInventory(IRON_ORE, 3);
      addCraftedItem(CRAFTED_IRON_INGOT);
      System.out.println("Crafted Iron Ingot.");
    } else {
      System.out.println("Insufficient resources to craft Iron Ingot.");
    }
  }
  public static void craftAppleCandy(){
    if( inventoryContains(APPLE,  1) && (craftedItems(CRAFTED_STICK, 1))){
      removeItemsFromInventory(APPLE, 1);
      removeItemsFromCraftedItems(CRAFTED_STICK, 1);
      addCraftedItem(CRAFTED_APPLE_CANDY);
      System.out.println("Produced an Apple Candy.");
    }else{
      System.out.println("Insufficient resources to produce Apple Candy");}
    }
    public static void craftTorch(){
      if( inventoryContains(COAL, 2)){
        removeItemsFromInventory(COAL, 2);
        addCraftedItem(CRAFTED_TORCH);
        System.out.println("Crafted torch.");
      }else{
        System.out.println("Insufficient resources to craft torch");
      }
      }


  public static void craftGoldenIngot(){
    if(inventoryContains(GOLD_ORE, 3)){
      removeItemsFromInventory(GOLD_ORE, 3);
      addCraftedItem(CRAFTED_GOLDEN_INGOT);
      System.out.println("Crafted Gold Ingot.");
    }
    else{
      System.out.println("Insufficient resources to craft Gold Ingot.");
    }
  }

  public static void craftGoldPickaxe(){
    if(inventoryContains(CRAFTED_GOLDEN_INGOT, 3) && inventoryContains(CRAFTED_STICK, 2)){
      removeItemsFromInventory(CRAFTED_STICK, 2);
      removeItemsFromInventory(CRAFTED_GOLDEN_INGOT, 3);
      
      addCraftedItem(CRAFTED_GOLDEN_PICKAXE); 
      System.out.println("Crafted Gold Pickaxe.");
    }
    else{
      System.out.println("Insufficient resources to craft Gold Pickaxe.");
    }
  }


  public static boolean inventoryContains(int item) { //  returns the items that the player currently possesses
    return inventory.contains(item);
  }

  public static boolean inventoryContains(int item, int count) { // Measures the ammount of items in the players inventory ( it will also add the items)
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
  public static boolean craftedItems(int item) { //  returns the items that the player currently possesses
    return craftedItems.contains(item);
  }
  public static void removeItemsFromCraftedItems(int item, int count) {  //when an Item is used it needs to be removed from tghe player inventory
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

  public static void removeItemsFromInventory(int item, int count) {  //when an Item is used it needs to be removed from tghe player inventory
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

  public static void addCraftedItem(int craftedItem) { //after a item is created it will add the new item to the player inventory (?)
    if (craftedItems == null) {
      craftedItems = new ArrayList<>();
    }
    craftedItems.add(craftedItem);
  }

  public static void interactWithWorld() { // when a player breaks a block it will "alert" the player the action that just occurred
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
        case COAL:
        System.out.println("You mine coal block from the ground.");
        inventory.add(COAL);//benim ekledigim
        break;
        case APPLE:
        System.out.println("You find an apple from the ground");
        inventory.add(APPLE);
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

  public static void saveGame(String fileName) { //allows the player to save the current game he is, allowing him to load the game later
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


    public static void loadGame(String fileName) { // will load the saved game a player has, its needed to type the save name and press enter
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

  private static String getBlockName(int blockType) { // when a player interacts with a block it will let the player know if there is something there or not
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
        case COAL:
        return "Coal";
        case APPLE:
       return "Apple";
      default:
        return "Unknown";
    }
  }

  public static void displayLegend() { // Legend from the items images, world border, and player symbol
    System.out.println(ANSI_BLUE + "Legend:");
    System.out.println(ANSI_WHITE + "-- - Empty block");
    System.out.println(ANSI_RED + "\u2592\u2592 - Wood block");
    System.out.println(ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
    System.out.println(PARLAK_KIRMIZI+ "\u00A4- Coal");
    System.out.println(ANSI_YELLOW + "\u002A"+ "- Apple");
    System.out.println(ANSI_BLUE + "\u2593\u2593 - Stone block");
    System.out.println(ANSI_WHITE + "\u00B0\u00B0 - Iron ore block");
    System.out.println(ANSI_YELLOW + "\u0024\u0024 - Gold ore block");
    System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
  }
  public static boolean craftedItems(int item, int count) {
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
  public static void displayInventory() { // displays a player inventory, the amount of item the player currently possess
    System.out.println("Inventory:");
    if (inventory.isEmpty()) {
      System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
    } else {
      int[] blockCounts = new int[500];
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
        case COAL:
      return PARLAK_KIRMIZI;
      case APPLE:
       return ANSI_YELLOW;
      case IRON_ORE:
        return ANSI_YELLOW;
        case GOLD_ORE:
        return ANSI_YELLOW; //meow
      default:
        return "";
    }
  }

  private static void waitForEnter() { // Scanner system for the game to know that you are sure about the action you want to make
    System.out.println("Press Enter to continue...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
  }

  private static String getCraftedItemName(int craftedItem) { // When you craft something you need to receive the name back, this function creates the name of the crafted items
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
        return "Wooden Planks";
      case CRAFTED_STICK:
        return "Stick";
      case CRAFTED_IRON_INGOT:
        return "Iron Ingot";
        case CRAFTED_GOLDEN_INGOT:
        return "Gold Ingot";
        case CRAFTED_TORCH:
       return "Torch";
       case CRAFTED_APPLE_CANDY:
       return "Apple Candy";
        case CRAFTED_GOLDEN_PICKAXE:
        return "Gold Pickaxe";
      default:
        return "Unknown";
    }
  }

  private static String getCraftedItemColor(int craftedItem) { // Color of the crafted items
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
      case CRAFTED_STICK:
      case CRAFTED_IRON_INGOT:
      case CRAFTED_GOLDEN_INGOT:
      case CRAFTED_GOLDEN_PICKAXE:
      case CRAFTED_TORCH:
      case CRAFTED_APPLE_CANDY:
        return ANSI_BROWN;
      default:
        return "";
    }
  }

  public static void getCountryAndQuoteFromServer() { // is what actually allows the game to run smoothly with java.
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