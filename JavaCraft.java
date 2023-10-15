import java.util.*;
import java.net.*;
import java.io.*;

public class JavaCraft {
  private static boolean draw;
  private static final int AIR = 0;
  private static final int WOOD = 1;
  private static final int LEAVES = 2;
  private static final int STONE = 3;
  private static final int white = 69;
  private static final int special = 70;
  private static final int special2 = 71;

  private static final int IRON_ORE = 4;
  private static final int DIAMOND_ORE = 8;
  private static final int GOLD_ORE = 9;
  private static int NEW_WORLD_WIDTH = 25;
  private static int NEW_WORLD_HEIGHT = 15;
  private static int FLAG_WIDTH = 64;//54
  private static int FLAG_HEIGHT = 31;//26
  private static int EMPTY_BLOCK = 0;
  private static final int CRAFT_WOODEN_PLANKS = 100;
  private static final int CRAFT_STICK = 101;
  private static final int CRAFT_IRON_INGOT = 102;
  private static final int CRAFT_GOLD_INGOT = 103;
  private static final int CRAFT_DIAMOND_GEM = 104;
  private static final int CRAFTED_WOODEN_PLANKS = 200;
  private static final int CRAFTED_STICK = 201;
  private static final int CRAFTED_IRON_INGOT = 202;
  private static final int CRAFTED_GOLD_INGOT = 203;
  private static final int CRAFTED_DIAMOND_GEM = 204;
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
      "7 - Iron Ingot (Crafted Item)\n"+
      "8 - Diamond ore block\n "+
      "9 - Gold ore block\n"+
      "10 - Gold Ingot (Crafted Item)\n"+
      "11 - Diamond Gem (Crafted Item)";
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
        } else if (randValue <80){
          world[x][y]= GOLD_ORE;
        }else if (randValue <90)
          world[x][y]= DIAMOND_ORE;
        else {
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
  
  public static void displayFlag() {
    System.out.println(ANSI_CYAN + "FLAG:" + ANSI_RESET);
    System.out.println("╔══" + "═".repeat(FLAG_WIDTH * 2 - 2) + "╗");
    for (int y = 0; y < FLAG_HEIGHT; y++) {
      System.out.print("║");
      for (int x = 0; x < FLAG_WIDTH; x++) {
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
    System.out.println("╚══" + "═".repeat(FLAG_WIDTH * 2 - 2) + "╝");
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
        blockColor = ANSI_PURPLE;
        break;
      case GOLD_ORE:
        blockColor = ANSI_YELLOW; 
        break;    
        case white:
        blockColor = ANSI_WHITE; 
        case special:
        blockColor = ANSI_WHITE; 
        break;    
        case special2:
        blockColor = ANSI_WHITE; 
        break;    
      default:
        blockColor = ANSI_RESET;
        break;
    }

    if(!draw)
    return blockColor + getBlockChar(blockType) + " ";
    else if(blockType==special)
      {
        return ANSI_BLUE+ getBlockChar(blockType) + ANSI_WHITE+getBlockChar(blockType);
      }
      else if(blockType==special2)
      {
          return ANSI_WHITE+ getBlockChar(blockType) + ANSI_BLUE+getBlockChar(blockType);      
      }
      else
      return blockColor + getBlockChar(blockType)+getBlockChar(blockType);/////////////////////////////////CAREFULLLLL MAYBE
    
  }

  private static char getBlockChar(int blockType) {
    switch (blockType) {
      case WOOD:
        return '\u2592';
      case LEAVES:
        return '\u00A7';
      case STONE:
        return '\u2593';//\u2B1B
      case IRON_ORE:
        return '\u00B0';
      case DIAMOND_ORE:
        return '\u0021';
      case GOLD_ORE:
        return '\u002B';  
      case white:
        return '\u2593';  
      case special:
        return '\u2593'; 
      case special2:
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
      if(!draw)
      displayWorld();
      else displayFlag();
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
        lookAround();}
        else if (input.equalsIgnoreCase("drawflag")) {
        drawflag();
        draw=true;
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
  private static void drawflag() {
    world = new int[FLAG_WIDTH][FLAG_HEIGHT];
    int redBlock = 1;
    int wBlock = 69;
    int special=70;
    int special2=71;
    int blueBlock = 3;
    //int stripeHeight = FLAG_HEIGHT / 2; // Divide the height into three equal parts

    for (int y = 0; y < FLAG_HEIGHT; y++) {
      for (int x = 0; x < FLAG_WIDTH; x++) {
        world[x][y] = blueBlock;
      }
    }
    for (int y = 6; y <= 6; y++) {
      for (int x = 0; x < FLAG_WIDTH/2; x++) {//hor white
        world[x][y] = wBlock;
      }
    }
     for (int y = 9; y <= 9; y++) {
      for (int x = 0; x < FLAG_WIDTH/2; x++) {//hor white down
        world[x][y] = wBlock;
      }
    }
    for (int y = 0; y <= FLAG_HEIGHT/2; y++) {
      for (int x = 14; x <=14; x++) {//vert white
        world[x][y] = wBlock;
      }
    }
    for (int y = 0; y <= FLAG_HEIGHT/2; y++) {
      for (int x = 17; x <=17; x++) {//vert white 2nd
        world[x][y] = wBlock;
      }
    }
    for (int y = 7; y <= 8; y++) {
      for (int x = 0; x < FLAG_WIDTH/2; x++) {//hor red
        world[x][y] = redBlock;
      }
    }
    for (int y = 0; y <= FLAG_HEIGHT/2; y++) {
      for (int x = 15; x <=16; x++) {//vert red
        world[x][y] = redBlock;
      }
    }
    world[0][0]=redBlock;
      world[0][1]=wBlock;
    world[1][1]=redBlock;
    world[2][1]=redBlock;
    world[3][1]=redBlock;
          //world[3][2]=wBlock;
    world[1][0]=wBlock;
    world[2][0]=wBlock;
    world[3][0]=wBlock;
      world[1][2]=wBlock;
      world[2][2]=wBlock;
      world[3][2]=wBlock;
    world[4][2]=redBlock;
    world[5][2]=redBlock;
    world[6][2]=redBlock;
          //world[6][3]=wBlock;
    world[4][1]=wBlock;
    world[5][1]=wBlock;
    world[6][1]=wBlock;
      world[4][3]=wBlock;
      world[5][3]=wBlock;
      world[6][3]=wBlock;
    world[7][3]=redBlock;
    world[8][3]=redBlock;
    world[9][3]=redBlock;
            //world[9][4]=wBlock;///////////left top
    world[7][2]=wBlock;
    world[8][2]=wBlock;
    world[9][2]=wBlock;
      world[7][4]=wBlock;
      world[8][4]=wBlock;
      world[9][4]=wBlock;
    world[10][4]=redBlock;
    world[11][4]=redBlock;
    world[12][4]=redBlock;
    world[10][3]=wBlock;
    world[11][3]=wBlock;
    world[12][3]=wBlock;
      world[10][5]=wBlock;
      world[11][5]=wBlock;
      world[12][5]=wBlock;
    world[13][5]=redBlock;
    world[13][4]=wBlock;
    /////////////////////////////////////////
    world[18][10]=redBlock;
    world[18][11]=wBlock;
    
    world[19][11]=redBlock;
    world[20][11]=redBlock;
    world[21][11]=redBlock;
      world[19][10]=wBlock;
      world[20][10]=wBlock;
      world[21][10]=wBlock;
      world[19][12]=wBlock;
      world[20][12]=wBlock;
      world[21][12]=wBlock;
    world[22][12]=redBlock;
    world[23][12]=redBlock;
    world[24][12]=redBlock;
      world[22][11]=wBlock;
      world[23][11]=wBlock;
      world[24][11]=wBlock;
      world[22][13]=wBlock;
      world[23][13]=wBlock;///////////////right down
      world[24][13]=wBlock;
    world[25][13]=redBlock;
    world[26][13]=redBlock;
    world[27][13]=redBlock;
      world[25][12]=wBlock;
      world[26][12]=wBlock;
      world[27][12]=wBlock;
      world[25][14]=wBlock;
      world[26][14]=wBlock;
      world[27][14]=wBlock;
    world[28][14]=redBlock;
    world[29][14]=redBlock;
    world[30][14]=redBlock;
      world[28][13]=wBlock;
      world[29][13]=wBlock;
      world[30][13]=wBlock;
      world[28][15]=wBlock;
      world[29][15]=wBlock;
      world[30][15]=wBlock;
    world[31][15]=redBlock;
    world[31][14]=wBlock;
////////////////////////////////////////////////
    world[13][10]=redBlock;
    world[13][11]=wBlock;
    
    world[12][11]=redBlock;
    world[11][11]=redBlock;
    world[10][11]=redBlock;
      world[12][10]=wBlock;
      world[11][10]=wBlock;
      world[10][10]=wBlock;
      world[12][12]=wBlock;
      world[11][12]=wBlock;
      world[10][12]=wBlock;
    world[9][12]=redBlock;
    world[8][12]=redBlock;
    world[7][12]=redBlock;
      world[9][11]=wBlock;
      world[8][11]=wBlock;
      world[7][11]=wBlock;
      world[9][13]=wBlock;
      world[8][13]=wBlock;///////////////left down
      world[7][13]=wBlock;
    world[6][13]=redBlock;
    world[5][13]=redBlock;
    world[4][13]=redBlock;
      world[6][12]=wBlock;
      world[5][12]=wBlock;
      world[4][12]=wBlock;
      world[6][14]=wBlock;
      world[5][14]=wBlock;
      world[4][14]=wBlock;
    world[3][14]=redBlock;
    world[2][14]=redBlock;
    world[1][14]=redBlock;
      world[3][13]=wBlock;
      world[2][13]=wBlock;
      world[1][13]=wBlock;
      world[3][15]=wBlock;
      world[2][15]=wBlock;
      world[1][15]=wBlock;
    world[0][15]=redBlock;
    world[0][14]=wBlock;
    //////////////////////////////////////////
     world[31][0]=redBlock;
      world[31][1]=wBlock;
    world[28][1]=redBlock;
    world[29][1]=redBlock;
    world[30][1]=redBlock;
          //world[3][2]=wBlock;
    world[28][0]=wBlock;
    world[29][0]=wBlock;
    world[30][0]=wBlock;
      world[28][2]=wBlock;
      world[29][2]=wBlock;
      world[30][2]=wBlock;
    world[25][2]=redBlock;
    world[26][2]=redBlock;
    world[27][2]=redBlock;
          //world[6][3]=wBlock;
    world[25][1]=wBlock;
    world[26][1]=wBlock;
    world[27][1]=wBlock;
      world[25][3]=wBlock;
      world[26][3]=wBlock;
      world[27][3]=wBlock;
    world[22][3]=redBlock;
    world[23][3]=redBlock;
    world[24][3]=redBlock;
            //world[9][4]=wBlock;///////////right top
    world[22][2]=wBlock;
    world[23][2]=wBlock;
    world[24][2]=wBlock;
      world[22][4]=wBlock;
      world[23][4]=wBlock;
      world[24][4]=wBlock;
    world[19][4]=redBlock;
    world[20][4]=redBlock;
    world[21][4]=redBlock;
    world[19][3]=wBlock;
    world[20][3]=wBlock;
    world[21][3]=wBlock;
      world[19][5]=wBlock;
      world[20][5]=wBlock;
      world[21][5]=wBlock;
    world[18][5]=redBlock;
    world[18][4]=wBlock;
////////////////////////////////////////////////////////
      world[15][23]=wBlock;
      world[16][23]=wBlock;
      world[17][23]=wBlock;
      world[14][23]=wBlock;
      world[15][22]=wBlock;
      world[16][22]=wBlock;
      world[17][22]=wBlock;
      world[14][22]=wBlock;
      world[15][24]=wBlock;
      world[16][24]=wBlock;
      world[17][24]=wBlock;
      world[14][24]=wBlock;
      world[15][21]=wBlock;
      world[16][21]=wBlock;
      world[17][21]=wBlock;
      world[14][21]=wBlock;
      world[15][25]=wBlock;
      world[16][25]=wBlock;
      world[17][25]=wBlock;
      world[14][25]=wBlock;
      world[13][21]=wBlock;
      world[13][22]=wBlock;
      world[13][23]=wBlock;///cube
      world[13][24]=wBlock;
      world[13][25]=wBlock;
      world[18][21]=wBlock;
      world[18][22]=wBlock;
      world[18][23]=wBlock;
      world[18][24]=wBlock;
      world[18][25]=wBlock;


      world[15][20]=wBlock;
      world[16][20]=wBlock;

      world[14][26]=wBlock;
      world[17][26]=wBlock;

      world[14][27]=wBlock;
      world[17][27]=wBlock;
      world[14][28]=wBlock;
      world[17][28]=wBlock;

      world[15][26]=wBlock;
      world[16][26]=wBlock;
      
    
      world[19][25]=wBlock;
      world[12][25]=wBlock;
      world[20][25]=wBlock;
      world[11][25]=wBlock;
      world[21][25]=wBlock;
      world[10][25]=wBlock;
      world[19][24]=wBlock;
      world[12][24]=wBlock;

      world[19][20]=wBlock;
      world[12][20]=wBlock;

     
      world[19][21]=wBlock;
      world[12][21]=wBlock;

      world[20][20]=wBlock;
      world[11][20]=wBlock;
      
      world[15][19]=special;
      world[16][19]=special2;
/////////////2nd star
      world[47][27]=wBlock;
      world[48][27]=wBlock;
      world[46][27]=wBlock;
      world[49][27]=wBlock;
      world[47][26]=wBlock;
      world[48][26]=wBlock;
      world[46][26]=wBlock;
      world[49][26]=wBlock;
      world[47][25]=wBlock;
      world[48][25]=wBlock;
      world[46][25]=wBlock;
      world[49][25]=wBlock;

      world[45][24]=wBlock;
      world[50][24]=wBlock;
      world[47][24]=wBlock;
      world[48][24]=wBlock;
      world[47][23]=special;
      world[48][23]=special2;

      world[45][24]=wBlock;
      world[50][24]=wBlock;
      world[47][24]=wBlock;
      world[48][24]=wBlock;

      world[50][27]=wBlock;
      world[49][28]=wBlock;

      world[45][27]=wBlock;
      world[46][28]=wBlock;

      
      world[51][17]=wBlock;
      
      world[52][16]=wBlock;
      world[50][16]=wBlock;//small star

      world[52][18]=wBlock;
      world[50][18]=wBlock;

      world[38][13]=wBlock;
      world[39][13]=wBlock;
      world[40][13]=wBlock;
      world[38][14]=wBlock;
      world[39][14]=wBlock;
      world[40][14]=wBlock;

      world[41][14]=wBlock;
      world[40][15]=wBlock;

      world[37][14]=wBlock;
      world[38][15]=wBlock;

      world[39][12]=wBlock;
      
      world[41][12]=wBlock;
      world[37][12]=wBlock;

      ///////////////////
      world[55][11]=wBlock;
      world[56][11]=wBlock;
      world[57][11]=wBlock;
      world[55][12]=wBlock;
      world[56][12]=wBlock;
      world[57][12]=wBlock;

      world[58][12]=wBlock;
      world[57][13]=wBlock;

      world[54][12]=wBlock;
      world[55][13]=wBlock;

      world[56][10]=wBlock;
      
      world[58][10]=wBlock;
      world[54][10]=wBlock;
      ///////////////////////
      world[46][4]=wBlock;
      world[47][4]=wBlock;
      world[48][4]=wBlock;
      world[46][5]=wBlock;
      world[47][5]=wBlock;
      world[48][5]=wBlock;

      world[49][5]=wBlock;
      world[45][5]=wBlock;

      world[48][6]=wBlock;
      world[46][6]=wBlock;

      world[47][3]=wBlock;
      
      world[45][3]=wBlock;
      world[49][3]=wBlock;

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
    if (blockType >= 0 && blockType <= 12) {
      if (blockType <= 4 || blockType==8 || blockType==9) {
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
      case 10 :
        return CRAFTED_GOLD_INGOT;
      case 11:  
        return CRAFTED_DIAMOND_GEM;
      default:
        return -1;
    }
  }

  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    System.out.println("4. Craft Gold Ingot: 3 Gold Ore");
    System.out.println("5. Craft Diamond Gem: 3 Diamond Ore");

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
        craftDiamondGem();
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
  public static void craftGoldIngot() {
    if (inventoryContains(GOLD_ORE, 3)) {
      removeItemsFromInventory(GOLD_ORE, 3);
      addCraftedItem(CRAFTED_GOLD_INGOT);
      System.out.println("Crafted Gold Ingot.");
    } else {
      System.out.println("Insufficient resources to craft Wooden Planks.");
    }
  }
  public static void craftDiamondGem() {
    if (inventoryContains(DIAMOND_ORE, 3)) {
      removeItemsFromInventory(DIAMOND_ORE, 3);
      addCraftedItem(CRAFTED_DIAMOND_GEM);
      System.out.println("Crafted Diamond Gem.");
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
        return "Diamond ore";
      case GOLD_ORE:
        return "Gold ore";
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
    System.out.println(ANSI_YELLOW + "\u002B\u002B- Gold ore block");
    System.out.println(ANSI_PURPLE + "\u0021\u0021- Diamond ore block");

    System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
  }

  public static void displayInventory() {
    System.out.println("Inventory:");
    if (inventory.isEmpty()) {
      System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
    } else {
      int[] blockCounts = new int[20];
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
        return ANSI_PURPLE;
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
      case CRAFTED_DIAMOND_GEM:
        return "Diamond Gem";
      case CRAFTED_GOLD_INGOT:
        return "Gold Ingot";    
      default:
        return "Unknown";
    }
  }

  private static String getCraftedItemColor(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
      case CRAFTED_STICK:
      case CRAFTED_IRON_INGOT:
      case CRAFTED_DIAMOND_GEM:
      case CRAFTED_GOLD_INGOT:
        return ANSI_BROWN;
      default:
        return "";
    }
  }

  public static void getCountryAndQuoteFromServer() {
    try {
      URL url = new URL("");//"https://flag.ashish.nl/get_flag
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);
      String payload = "{\"group_number\": \"9\",\"group_name\": \"Group9\",\"difficulty_level\": \"hard\"}";
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