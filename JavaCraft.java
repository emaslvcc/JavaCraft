import java.util.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.net.*;
import java.io.*;

public class JavaCraft {
	private static int NEW_WORLD_WIDTH = 56;
	private static int NEW_WORLD_HEIGHT = 35;

	private static final String BLOCK_NUMBERS_INFO = "Block Numbers:\n" + Block.blockNumbersInfo();
	private static Block[][] world;
	private static int worldWidth;
	private static int worldHeight;
	private static int playerX;
	private static int playerY;
	private static List<Block> inventory = new ArrayList<>();
	private static List<Block> craftedItems = new ArrayList<>();

	private static boolean unlockMode = false;
	private static boolean secretDoorUnlocked = false;
	private static boolean inSecretArea = false;
	private static final int INVENTORY_SIZE = 100;

	private static final String groupNumber = "62";
  	private static final String groupName = "group--";
  	private static final String difficultyLevel = FlagDifficultyLevel.HARD.token();

	public static void main(final String[] args) {
		System.out.println("raided by angelo");
		System.out.println("destroyed the raider angelo");
    	System.out.println("Greghi is here :)");
		System.out.println("ℰ𝓈𝓀𝒾𝓁 was here ");
		System.out.println();

		// This sets the program into “Headless mode” which means it does not
		// need to open a separate application for graphics operations.
		System.setProperty("java.awt.headless", "true");

		initGame(NEW_WORLD_WIDTH, NEW_WORLD_HEIGHT);
		generateWorld();

		System.out.println(Colors.GREEN.ansi() + "Welcome to Simple Minecraft!" + Colors.RESET.ansi());
		System.out.println("Instructions:");
		System.out.println(" - Use 'W', 'A', 'S', 'D', or arrow keys to move the player.");
		System.out.println(" - Press 'M' to mine the block at your position and add it to your inventory.");
		System.out.println(" - Press 'P' to place a block from your inventory at your position.");
		System.out.println(" - Press 'C' to view crafting recipes and 'I' to interact with elements in the world.");
		System.out.println(" - Press 'Save' to save the game state and 'Load' to load a saved game state.");
		System.out.println(" - Press 'Exit' to quit the game.");
		System.out.println(" - Type 'Help' to display these instructions again.");
		System.out.println();

		final Scanner scanner = new Scanner(System.in);
		System.out.print("Start the game? (Y/N): ");
		final String startGameChoice = scanner.next().toUpperCase();

		if (startGameChoice.equals("Y")) {
			startGame();
		} else {
			System.out.println("Game not started. Goodbye!");
		}
	}

	public static void initGame(final int worldWidth, final int worldHeight) {
		JavaCraft.worldWidth = worldWidth;
		JavaCraft.worldHeight = worldHeight;
		JavaCraft.world = new Block[worldWidth][worldHeight];
		playerX = worldWidth / 2;
		playerY = worldHeight / 2;
		inventory = new ArrayList<>();
	}

	public static void generateWorld() {
		Block[] blocks = Block.values();
		double[] probabilities = Arrays.stream(blocks)
		.mapToDouble(block -> block.probability())
		.toArray();

		Random rand = new Random();
		for (int y = 0; y < worldHeight; y++) {
		for (int x = 0; x < worldWidth; x++) {
			double threshold = rand.nextDouble();

			Block block = Block.AIR;
			blockTypeLoop: for (int i = 0; i < probabilities.length; i++) {
			threshold = threshold - probabilities[i];

			if (threshold <= 0) {
				block = blocks[i];
				break blockTypeLoop;
			}
			}

			world[x][y] = block;
		}
		}
	}

	public static void displayWorld() {
		System.out.println(Colors.CYAN.ansi() + "World Map:" + Colors.RESET.ansi());
		System.out.println("╔══" + "═".repeat(worldWidth * 2 - 2) + "╗");

		for (int y = 0; y < worldHeight; y++) {
			System.out.print("║");
			for (int x = 0; x < worldWidth; x++) {
				if (x == playerX && y == playerY && !inSecretArea) {
					System.out.print(Colors.GREEN.ansi() + "P " + Colors.RESET.ansi());
				} else if (x == playerX && y == playerY && inSecretArea) {
					System.out.print(Colors.BLUE.ansi() + "P " + Colors.RESET.ansi());
				} else {
					System.out.print(world[x][y].getSymbol());
				}
			}

			System.out.println("║");
		}

		System.out.println("╚══" + "═".repeat(worldWidth * 2 - 2) + "╝");
	}

	public static void startGame() {
		final Scanner scanner = new Scanner(System.in);
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
			System.out.println(Colors.CYAN.ansi()
					+ "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door"
					+ Colors.RESET.ansi());
			if (inSecretArea) {
				System.out.println(Colors.MAGENTA.ansi()
					+ "Special actions in Secret Area: 'Getflag': Retrieve a new flag"
					+ Colors.RESET.ansi());
			}
			final String input = scanner.next().toLowerCase();
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
				final int blockType = scanner.nextInt();
				Block.parseFromInput(blockType)
				.ifPresentOrElse(
				  block -> placeBlock(block), 
				  () -> handleInvalidBlock()
				);
			} else if (input.equalsIgnoreCase("c")) {
				displayCraftingRecipes();
				System.out.print("Enter the recipe number to craft: ");
				final int recipe = scanner.nextInt();
				craftItem(recipe);
			} else if (input.equalsIgnoreCase("i")) {
				interactWithWorld();
			} else if (input.equalsIgnoreCase("save")) {
				System.out.print("Enter the file name to save the game state: ");
				final String fileName = scanner.next();
				saveGame(fileName);
			} else if (input.equalsIgnoreCase("load")) {
				System.out.print("Enter the file name to load the game state: ");
				final String fileName = scanner.next();
				loadGame(fileName);
			} else if (input.equalsIgnoreCase("exit")) {
				System.out.println("Exiting the game. Goodbye!");
				break;
			} else if (input.equalsIgnoreCase("look")) {
				lookAround();
			} else if (input.equalsIgnoreCase("unlock")) {
				unlockMode = true;
			} else if (input.equalsIgnoreCase("getflag")) {
				fetchFlagAndGenerateEmptyWorld();
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
				System.out.println(Colors.YELLOW.ansi() + "Invalid input. Please try again." + Colors.RESET.ansi());
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
		for (int blockType = 1; blockType <= Block.values().length; blockType++) {
		  for (int i = 0; i < INVENTORY_SIZE; i++) {
			Block.parseFromInput(i)
			  .ifPresent(block -> inventory.add(block));
		  }
		}
	}

	private static void resetWorld() {
		generateEmptyWorld();
		playerX = worldWidth / 2;
		playerY = worldHeight / 2;
	}

	private static void generateEmptyWorldFallback() {
		world = new Block[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
		Block redBlock = Block.WOOD;
		Block whiteBlock = Block.AIR;
		Block blueBlock = Block.STONE;
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
	 * This creates the flag of our country
	 */
	public static void generateEmptyWorld() {
        try {
            final String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Flag_of_Canada.svg/320px-Flag_of_Canada.svg.png";
            final URL url = new URL(imageUrl);
			final BufferedImage fullSizeImage = ImageIO.read(url);
			double aspectRatio = (double)fullSizeImage.getWidth() / (double)fullSizeImage.getHeight();
			int calculatedAspectWidth = (int)(aspectRatio * worldHeight);
			final BufferedImage image = ImageUtilities.resizeImage(fullSizeImage, calculatedAspectWidth, worldHeight);

			int xOffset = (calculatedAspectWidth - worldWidth) / 2;

			Block redBlock = Block.WOOD;
			Block whiteBlock = Block.AIR;

			for (int y = 0; y < worldHeight; y++) {
				for (int x = 0; x < worldWidth; x++) {
					Color color = new Color(image.getRGB(x + xOffset, y), true);
                    // You can adjust the condition for better appearance
                    if (color.getRed() > 40 && color.getGreen() < 240 && color.getBlue() < 240) {
                        world[x][y] = redBlock;
                    } else {
                        world[x][y] = whiteBlock;
                    }
				}
			}
        } catch (IOException e) {
			System.out.println("Could not retrieve flag of Canada. Try checking your connection. Falling back to flag of Netherlands.");
			generateEmptyWorldFallback();
        }
    }

	private static void fetchFlagAndGenerateEmptyWorld() {
		Block[] availableBlocks = Block.uncraftableBlocks();
	
		Colors[] availableBlockColors = new Colors[availableBlocks.length];
	
		for (int i = 0; i < availableBlocks.length; i++) {
		  availableBlockColors[i] = availableBlocks[i].getColor();
		}
		 
		try {
		  getCountryAndQuoteFromServer((country, quote, getCountryError) -> {
			if (country == null || getCountryError != null) {
			  System.out.println("Cannot get country from server. " + getCountryError);
			  System.out.println("Falling back to flag of Netherlands.");
			  generateEmptyWorldFallback();
			  return;
			}
	
			getFlagImageDataForCountryName(country, (imageStream, getFlagError) -> {
			  if (imageStream == null || getFlagError != null) {
				System.out.println("Cannot get flag image for country. " + getFlagError);
				System.out.println("Falling back to flag of Netherlands.");
				generateEmptyWorldFallback();
				return;
			  }
	
			  int horizontalInsets = 2;
			  int verticalInsets = 12;
	
			  try {
				world = ImageUtilities.imageToWorld(imageStream, NEW_WORLD_WIDTH, NEW_WORLD_HEIGHT, horizontalInsets, verticalInsets, availableBlocks, availableBlockColors);
			  } catch (Exception e) {
				System.out.println("Cannot create world from image. " + e.getMessage());
				System.out.println("Falling back to flag of Netherlands.");
				generateEmptyWorldFallback();
			  }
			});
		  });
		} catch(Exception e) {
		  System.out.println("Cannot create empty world. " + e.getMessage());
		  System.out.println("Falling back to flag of Netherlands.");
		  generateEmptyWorldFallback();
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
					System.out.print(Colors.GREEN.ansi() + "P " + Colors.RESET.ansi());
				} else {
					System.out.print(world[x][y].getSymbol());
				}
			}
			System.out.println();
		}
		System.out.println();
		waitForEnter();
	}

	public static void movePlayer(final String direction) {
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
		Block blockType = world[playerX][playerY];
		
		switch (blockType) {
		case AIR:
		  System.out.println("No block to mine here.");
		  break;
		case OBSIDIAN:
		  System.out.println("Obsidian is too hard to mine");
		  break;
		default:
		  inventory.add(blockType);
		  world[playerX][playerY] = Block.AIR;
		  System.out.println("Mined " + blockType.blockName() + ".");
		}
	
		waitForEnter();
	}

	public static void placeBlock(Block blockType) {
		if (blockType.isCrafted()) {
		  placeCraftedItem(blockType);
		} else {
		  placeBlockItem(blockType);
		}
		waitForEnter();
	}
	
	public static void placeBlockItem(Block blockType) {
		if (inventory.contains(blockType)) {
		  inventory.remove(blockType);
		  world[playerX][playerY] = blockType;
		  System.out.println("Placed " + blockType.blockName() + " at your position.");
		} else {
		  System.out.println("You don't have " + blockType.blockName() + " in your inventory.");
		}
	}
	
	public static void placeCraftedItem(Block craftedItem) {
		if (craftedItems.contains(craftedItem)) {
		  craftedItems.remove(craftedItem);
		  world[playerX][playerY] = craftedItem;
		  System.out.println("Placed " + craftedItem.blockName() + " at your position.");
		} else {
		  System.out.println("You don't have " + craftedItem.blockName() + " in your crafted items.");
		}
	}

	private static void handleInvalidBlock() {
		System.out.println("Invalid block number. Please enter a valid block number.");
		System.out.println(BLOCK_NUMBERS_INFO);
		waitForEnter();
	}

	public static void displayCraftingRecipes() {
		System.out.println("Crafting Recipes:");
		System.out.println("1. Craft Wooden Planks: 2 Wood");
		System.out.println("2. Craft Stick: 1 Wood");
		System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
		System.out.println("4. Craft Gold Ingot: 2 Gold Ore");
	}

	public static void craftItem(final int recipe) {
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

			default:
				System.out.println("Invalid recipe number.");
		}
		waitForEnter();
	}

	public static void craftWoodenPlanks() {
		if (inventoryContains(Block.WOOD, 2)) {
		  removeItemsFromInventory(Block.WOOD, 2);
		  addCraftedItem(Block.CRAFTED_WOODEN_PLANKS);
		  System.out.println("Crafted Wooden Planks.");
		} else {
		  System.out.println("Insufficient resources to craft Wooden Planks.");
		}
	}
	
	  public static void craftStick() {
		if (inventoryContains(Block.WOOD)) {
		  removeItemsFromInventory(Block.WOOD, 1);
		  addCraftedItem(Block.CRAFTED_STICK);
		  System.out.println("Crafted Stick.");
		} else {
		  System.out.println("Insufficient resources to craft Stick.");
		}
	}
	
	  public static void craftIronIngot() {
		if (inventoryContains(Block.IRON_ORE, 3)) {
		  removeItemsFromInventory(Block.IRON_ORE, 3);
		  addCraftedItem(Block.CRAFTED_IRON_INGOT);
		  System.out.println("Crafted Iron Ingot.");
		} else {
		  System.out.println("Insufficient resources to craft Iron Ingot.");
		}
	}

	public static void craftGoldIngot() {
		if (inventoryContains(Block.GOLD_ORE, 2)) {
			removeItemsFromInventory(Block.GOLD_ORE, 2);
			addCraftedItem(Block.CRAFTED_GOLD_INGOT);
			System.out.println("Crafted Gold Ingot.");
		} else {
			System.out.println("Insufficient resources to craft Gold Ingot.");
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
		return false;
	}
	
	// NOTE: Do not run without first checking if inventoryContains(item, count)
	public static void removeItemsFromInventory(Block item, int count) {
		int removedCount = 0;
		Iterator<Block> iterator = inventory.iterator();
		while (iterator.hasNext()) {
		  Block block = iterator.next();
		  if (block == item) {
			iterator.remove();
			removedCount++;
			if (removedCount == count) {
			  break;
			}
		  }
		}
	}

	public static void addCraftedItem(final Block craftedItem) {
		if (craftedItems == null) {
			craftedItems = new ArrayList<>();
		}
		craftedItems.add(craftedItem);
	}

	public static void interactWithWorld() {
		Block blockType = world[playerX][playerY];
		switch (blockType) {
		  case WOOD:
			System.out.println("You gather wood from the tree.");
			inventory.add(Block.WOOD);
			break;
		  case LEAVES:
			System.out.println("You gather leaves from the tree.");
			inventory.add(Block.LEAVES);
			break;
		  case STONE:
			System.out.println("You gather stones from the ground.");
			inventory.add(Block.STONE);
			break;
		  case IRON_ORE:
			System.out.println("You mine iron ore from the ground.");
			inventory.add(Block.IRON_ORE);
			break;
		  case GOLD_ORE:
			System.out.println("You mine gold ore from the ground.");
			inventory.add(Block.GOLD_ORE);
			break;
		  case MAGIC_ORE:
			System.out.println("You mine magic ore from the ground.");
			inventory.add(Block.MAGIC_ORE);
			break;
		  case WATER:
			System.out.println("You harvest water from a pond");
			inventory.add(Block.WATER);
			break;
		  case OBSIDIAN:
			System.out.println("Obsidian is too hard to mine.");
			break;
		  case AIR:
			System.out.println("Nothing to interact with here.");
			break;
		  default:
			System.out.println("Unrecognized block. Cannot interact.");
		}
		waitForEnter();
	}

	public static void saveGame(final String fileName) {
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
		} catch (final IOException e) {
			System.out.println("Error while saving the game state: " + e.getMessage());
		}
		waitForEnter();
	}


	public static void loadGame(final String fileName) {
		// Implementation for loading the game state from a file goes here
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
			// Deserialize game state data from the file and load it into the program
			NEW_WORLD_WIDTH = inputStream.readInt();
			NEW_WORLD_HEIGHT = inputStream.readInt();
			world = (Block[][]) inputStream.readObject();
			playerX = inputStream.readInt();
			playerY = inputStream.readInt();
			inventory = (List<Block>) inputStream.readObject();
			craftedItems = (List<Block>) inputStream.readObject();
			unlockMode = inputStream.readBoolean();

			System.out.println("Game state loaded from file: " + fileName);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error while loading the game state: " + e.getMessage());
		}
		waitForEnter();
	}

	public static void displayLegend() {
		System.out.println(Colors.BLUE.ansi() + "Legend:");
		System.out.println(Block.blocksLegend());
		System.out.println(Colors.BLUE.ansi() + "P - Player" + Colors.RESET.ansi());
	}

	public static void displayInventory() {
		System.out.println("Inventory:");
		if (inventory.isEmpty()) {
		  System.out.println(Colors.YELLOW.ansi() + "Empty" + Colors.RESET.ansi());
		} else {
		  Block[] blocks = Block.values();
		  int[] blockCounts = new int[blocks.length];
		  for (int i = 0; i < inventory.size(); i++) {
			Block block = inventory.get(i);
			blockCounts[block.ordinal()]++;
		  }
		  for (int i = 0; i < blockCounts.length; i++) {
			int occurrences = blockCounts[i];
			Block block = blocks[i];
			if (occurrences > 0) {
			  System.out.println(block.blockName() + " - " + occurrences);
			}
		  }
		}
		System.out.println("Crafted Items:");
		if (craftedItems == null || craftedItems.isEmpty()) {
		  System.out.println(Colors.YELLOW.ansi() + "None" + Colors.RESET.ansi());
		} else {
		  for (Block item : craftedItems) {
			System.out.print(item.getColor().ansi() + item.blockName() + ", " + Colors.RESET.ansi());
		  }
		  System.out.println();
		}
		System.out.println();
	}

	private static void waitForEnter() {
		System.out.println("Press Enter to continue...");
		final Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}

	public static void getCountryAndQuoteFromServer(GetCountryAndQuoteResponse responseHandler) {
		try {
			URL url = new URL("https://flag.ashish.nl/get_flag");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			String payload = String.format("{ \"group_number\": \"%s\", \"group_name\": \"%s\", \"difficulty_level\": \"%s\" }", groupNumber, groupName, difficultyLevel);
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

			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				throw new Exception(String.format("(%d) Invalid response from server: %s", responseCode, json));
			}

			int countryStart = json.indexOf("\"") + 11;
			int countryEnd = json.indexOf("\"", countryStart);
			String country = json.substring(countryStart, countryEnd);

			int quoteStart = json.indexOf(",") + 9;
			int quoteEnd = json.indexOf("\"", quoteStart);
			String quote = json.substring(quoteStart, quoteEnd);
			quote = quote.replace(" ", " ");

			System.out.println(" " + country);
			System.out.println(" " + quote);

			responseHandler.countryResponse(country, quote, null);
		} catch (Exception e) {
			System.out.println("Error connecting to the server");

			responseHandler.countryResponse(null, null, e.getMessage());
		}
	}

	public static void getFlagImageDataForCountryName(String countryName, GetFlagImageDataForCountryNameResponse responseHandler) {
		Locale english = new Locale("en");
		Arrays.stream(Locale.getAvailableLocales())
			.filter(locale -> locale.getDisplayCountry(english).equalsIgnoreCase(countryName))
			.findFirst()
			.map(locale -> locale.getCountry())
			.ifPresentOrElse((countryCode) -> {
			try {
				URL url = new URL(String.format("https://flagsapi.com/%s/flat/64.png", countryCode));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				InputStream reader = conn.getInputStream();

				int responseCode = conn.getResponseCode();
				if (responseCode != 200) {
					throw new Exception(String.format("Invalid server response: %d", responseCode));
				}

				responseHandler.countryResponse(reader, null);
			} catch (Exception e) {
				responseHandler.countryResponse(null, e.getMessage());
			}

		}, () -> {
			responseHandler.countryResponse(null, String.format("Cannot get country code for country name “%s”", countryName));
		});
	}
}

interface GetCountryAndQuoteResponse {
  /// Response handler for getCountryAndQuote. country and quote are optional, and nonnull if request succeedes, otherwise, error is populated with the error message.
  void countryResponse(String country, String quote, String error);
}

interface GetFlagImageDataForCountryNameResponse {
  /// Response handler for getFlagImageDataForCountryName. flagImage is optional, and nonnull if request succeedes, otherwise, error is populated with the error message.
  void countryResponse(InputStream flagImage, String error);
}