import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.ImageIO;

public class JavaCraft {

  // Define the ArrayList for the image (r,g,b) at each (x,y) coordinate
  static HashMap<String, int[]> imageMap = new HashMap<>();
  private static int playerX;
  private static int playerY;
  private static List<Block> inventory = new ArrayList<>();
  private static List<Item> craftedItems = new ArrayList<>();
  private static boolean unlockMode = false;
  private static boolean secretDoorUnlocked = false;
  private static boolean inSecretArea = false;
  private static boolean hasRequestedFlag = false;
  private static final int INVENTORY_SIZE = 100;

  public static void main(String[] args) throws IOException {
    initGame();
    World.generateWorld();
    World.populateWorldWithAnimals();
    System.out.println(
      Color.ANSI_GREEN + "Welcome to Simple Minecraft!" + Color.ANSI_RESET
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

  public static void initGame() {
    playerX = World.width / 2;
    playerY = World.height / 2;
    inventory = new ArrayList<>();
  }

  /**
   * Displays the game world.
   *
   * @param isMap If true, display the world as a map with colors. Otherwise, display the normal game world.
   */
  public static void displayWorld(boolean isMap) {
    // Print the world map header
    System.out.println(Color.ANSI_CYAN + "World Map:" + Color.ANSI_RESET);

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
    int width = isMap ? uniqueXValues.size() : World.width;
    int height = isMap ? uniqueYValues.size() : World.height;

    // Resize the world dimensions to match the map dimensions if displaying as a map (does not do anything if we use the world dimension for the map)
    World.height = height;
    World.width = width;

    // Print the top boundary of the world/map display
    System.out.println(Color.ANSI_WHITE + "╔══" + "═".repeat(width - 2) + "╗");

    // Loop through each row (y coordinate)
    for (int yIndex = 0; yIndex < height; yIndex++) {
      int y = isMap ? (int) uniqueYValues.toArray()[yIndex] : yIndex;
      System.out.print(Color.ANSI_WHITE + "║");

      for (int xIndex = 0; xIndex < width; xIndex++) {
        int x = isMap ? (int) uniqueXValues.toArray()[xIndex] : xIndex;

        if (x == playerX && y == playerY) {
          if (!inSecretArea) {
            System.out.print(Color.ANSI_PLAYER + "\u2588" + Color.ANSI_RESET);
          } else {
            System.out.print(Color.ANSI_BLUE + "\u2588" + Color.ANSI_RESET);
          }
        } else if (World.animals[x][y] != null) {
          System.out.print(World.animals[x][y].getSymbol());
        } else if (isMap) {
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
          System.out.print((World.world[x][y]).getSymbol());
        }
      }

      // Print the right boundary for this row of the world/map
      System.out.println(Color.ANSI_WHITE + "║");
    }

    // Print the bottom boundary of the world/map display
    System.out.println(Color.ANSI_WHITE + "╚══" + "═".repeat(width - 2) + "╝");
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
      World.moveAnimals();
      System.out.println(
        Color.ANSI_CYAN +
        "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door" +
        Color.ANSI_RESET
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
        System.out.println("Enter the block name: ");
        String blockName = scanner.next();
        try {
          Block blockType = Block.valueOf(blockName.toUpperCase());
          placeBlock(blockType);
        } catch (IllegalArgumentException e) {
          System.out.println(
            "Invalid block name. Please enter a valid block name."
          );
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
        try {
          printFlagToScreen("mozambique");
        } catch (IOException e) {
          e.printStackTrace();
        }
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
          Color.ANSI_YELLOW +
          "Invalid input. Please try again." +
          Color.ANSI_RESET
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
    for (Block block : Block.values()) {
      for (int i = 0; i < INVENTORY_SIZE; i++) {
        inventory.add(block);
      }
    }
  }

  private static void resetWorld() throws IOException {
    printFlagToScreen("mozambique");
    playerX = World.width / 2;
    playerY = World.height / 2;
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
      y <= Math.min(playerY + 1, World.height - 1);
      y++
    ) {
      for (
        int x = Math.max(0, playerX - 1);
        x <= Math.min(playerX + 1, World.width - 1);
        x++
      ) {
        if (x == playerX && y == playerY) {
          System.out.print(Color.ANSI_PLAYER + "P " + Color.ANSI_RESET);
        } else {
          System.out.print(World.world[x][y].getSymbol());
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
        if (playerY < World.height - 1) {
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
        if (playerX < World.width - 1) {
          playerX++;
        }
        break;
      default:
        break;
    }
  }

  public static void mineBlock() {
    Block blockType = World.world[playerX][playerY];
    if (blockType != Block.AIR) {
      inventory.add(blockType);
      World.world[playerX][playerY] = Block.AIR;
      System.out.println(
        "Mined " + blockType.getName() + "." + Color.ANSI_RESET
      );
    } else {
      System.out.println("No block to mine here.");
    }
    waitForEnter();
  }

  public static void placeBlock(Block blockType) {
    if (blockType != null) { // Ensure blockType is not null // Replacing integer checks with ordinal checks
      if (inventory.contains(blockType)) {
        inventory.remove(blockType);
        World.world[playerX][playerY] = blockType; // Storing the enum's ordinal value if you are still using int array
        System.out.println(
          "Placed " + blockType.name() + " at your position." // Using name() to get the block's name
        );
      } else {
        System.out.println(
          "You don't have " + blockType.name() + " in your inventory."
        );
      }
    }
  }

  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    System.out.println("4. Craft Bed: 2 Wool, 1 Skin");
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
    if (inventoryContains(Block.WOOD, 2)) {
      removeItemsFromInventory(Block.WOOD, 2);
      addCraftedItem(Item.WOODEN_PLANKS);
      System.out.println("Crafted Wooden Planks.");
    } else {
      System.out.println("Insufficient resources to craft Wooden Planks.");
    }
  }

  public static void craftBed() {
    if (
      (inventoryContains(Block.WOOL, 2)) && (inventoryContains(Block.SKIN, 1))
    ) {
      removeItemsFromInventory(Block.WOOL, 2);
      removeItemsFromInventory(Block.SKIN, 1);
      addCraftedItem(Item.BED);
      System.out.println("Crafted Bed");
    } else {
      System.out.println("Insufficient resources to craft Bed");
    }
  }

  public static void craftStick() {
    if (inventoryContains(Block.WOOD)) {
      removeItemsFromInventory(Block.WOOD, 1);
      addCraftedItem(Item.STICK);
      System.out.println("Crafted Stick.");
    } else {
      System.out.println("Insufficient resources to craft Stick.");
    }
  }

  public static void craftIronIngot() {
    if (inventoryContains(Block.IRON_ORE, 3)) {
      removeItemsFromInventory(Block.IRON_ORE, 3);
      addCraftedItem(Item.IRON_INGOT);
      System.out.println("Crafted Iron Ingot.");
    } else {
      System.out.println("Insufficient resources to craft Iron Ingot.");
    }
  }

  public static boolean inventoryContains(Block item) {
    return inventory.contains(item);
  }

  public static boolean inventoryContains(Block item, int count) {
    int itemCount = 0;
    for (Block block : inventory) {
      if (block == item) {
        itemCount++;
        if (itemCount == count) {
          return true;
        }
      }
    }
    return itemCount >= count;
  }

  public static void removeItemsFromInventory(Block item, int count) {
    int removedCount = 0;
    Iterator<Block> iterator = inventory.iterator();
    while (iterator.hasNext()) {
      Block currentBlock = iterator.next();
      if (currentBlock == item) {
        iterator.remove();
        removedCount++;
        if (removedCount == count) {
          break;
        }
      }
    }
  }

  public static void addCraftedItem(Item craftedItem) {
    if (craftedItems == null) {
      craftedItems = new ArrayList<>();
    }
    craftedItems.add(craftedItem);
  }

  public static void interactWithWorld() {
    Animal entity = World.animals[playerX][playerY];

    if (entity != null) {
      switch (entity) {
        case SHEEP:
          if (entity.getAttribute() > 0) {
            inventory.add(Block.WOOL);
            entity.setAttribute(entity.getAttribute() - 1);
            System.out.println(
              "You ruthlessly shear the sheep, leaving it cold and vulnerable..."
            );
          } else {
            System.out.println(
              "The sheep stands before you, stripped of its dignity, shivering in the cold."
            );
          }
          break;
        case COW:
          if (entity.getAttribute() > 0) {
            inventory.add(Block.SKIN);
            entity.setAttribute(entity.getAttribute() - 1);
            System.out.println(
              "You take a piece from the cow without mercy. It looks at you with pleading eyes, but you remain indifferent."
            );
          } else {
            World.removeEntity(playerX, playerY);
            System.out.println(
              "The lifeless eyes of the cow haunt you as it collapses. Another life taken by your hand..."
            );
          }
          break;
        default:
          System.out.println(
            "You gaze upon the entity, unsure of its origins. It exudes a foreboding aura."
          );
      }
    } else {
      // Logic for when there's no entity at the player's position
      System.out.println(
        "You stand alone in this desolate void, the weight of solitude pressing down on you."
      );
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
      outputStream.writeInt(World.width);
      outputStream.writeInt(World.height);
      outputStream.writeObject(World.world);
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
      World.width = inputStream.readInt();
      World.height = inputStream.readInt();
      World.world = (Block[][]) inputStream.readObject();
      playerX = inputStream.readInt();
      playerY = inputStream.readInt();
      inventory = (List<Block>) inputStream.readObject();
      craftedItems = (List<Item>) inputStream.readObject();
      unlockMode = inputStream.readBoolean();

      System.out.println("Game state loaded from file: " + fileName);
    } catch (IOException | ClassNotFoundException e) {
      System.out.println(
        "Error while loading the game state: " + e.getMessage()
      );
    }
    waitForEnter();
  }

  public static void displayLegend() {
    //new
    System.out.println(Color.ANSI_BLUE + "Legend:");
    System.out.println(Color.ANSI_CYAN + "Blocks: ");
    System.out.println(Block.AIR.getSymbol() + "- Air");
    System.out.println(Block.WOOD.getSymbol() + "- Wood");
    System.out.println(Block.LEAVES.getSymbol() + "- Leaves");
    System.out.println(Block.STONE.getSymbol() + "- Stone");
    System.out.println(Block.IRON_ORE.getSymbol() + "- Iron Ore");
    System.out.println(Block.WOOL.getSymbol() + "- Sheep Wool");
    System.out.println(Block.SKIN.getSymbol() + "- Cow Skin");
    System.out.println(
      Block.PLAYER.getSymbol() + "- Player" + Color.ANSI_RESET
    );
    System.out.println(Color.ANSI_CYAN + "Animals: ");
    System.out.println(Animal.SHEEP.getSymbol() + "- Sheep");
    System.out.println(Animal.COW.getSymbol() + "- Cow" + Color.ANSI_RESET);
  }

  public static void displayInventory() {
    System.out.println("Inventory: " + Color.ANSI_RESET);
    if (inventory.isEmpty()) {
      System.out.println(Color.ANSI_YELLOW + "Empty" + Color.ANSI_RESET);
    } else {
      // Use an EnumMap for efficient counting of blocks.
      EnumMap<Block, Integer> blockCounts = new EnumMap<>(Block.class);

      // Initialize with 0 counts for each block type.
      for (Block block : Block.values()) {
        blockCounts.put(block, 0);
      }

      // Iterate over the inventory and update the block counts.
      for (Block block : inventory) {
        blockCounts.put(block, blockCounts.get(block) + 1);
      }

      // Display non-zero counts.
      for (Block block : Block.values()) {
        int occurrences = blockCounts.get(block);
        if (occurrences > 0) {
          System.out.println(
            block.getName() + " - " + occurrences + Color.ANSI_RESET
          );
        }
      }
    }

    System.out.println("Crafted Items: " + Color.ANSI_RESET);
    if (craftedItems == null || craftedItems.isEmpty()) {
      System.out.println(Color.ANSI_YELLOW + "None" + Color.ANSI_RESET);
    } else {
      for (Item item : craftedItems) {
        System.out.print(
          item.getColor() + item.getName() + ", " + Color.ANSI_RESET
        );
      }
      System.out.println();
    }
    System.out.println();
  }

  static void waitForEnter() {
    System.out.println("Press Enter to continue...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
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
    int newWidth = World.width;
    int newHeight = World.height;
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
    World.massMurder(); //elliminate all animals muahahahahahahahah
    downloadImage(Country); // Download and resize image
    populateMap(); // Populate the map with the image
  }
}
