import java.util.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.net.*;
import java.io.*;

public class JavaCraft {
	private static final int AIR = 0;
	private static final int WOOD = 1;
	private static final int LEAVES = 2;
	private static final int STONE = 3;
	private static final int GOLD_ORE = 4;
	private static final int IRON_ORE = 5;
	private static final int DIRT = 6;
	//private static final int OBSIDIAN = 6;
	private static final int GOLD_BLOCK = 7;
	private static int NEW_WORLD_WIDTH = 25;
	private static int NEW_WORLD_HEIGHT = 15;
	private static int EMPTY_BLOCK = 0;

	private static final int CRAFT_WOODEN_PLANKS = 100;
	private static final int CRAFT_STICK = 101;
	private static final int CRAFT_IRON_INGOT = 102;
	private static final int CRAFT_GOLD_INGOT = 103;
	private static final int CRAFTED_WOODEN_PLANKS = 200;
	private static final int CRAFTED_STICK = 201;
	private static final int CRAFTED_IRON_INGOT = 202;
	private static final int CRAFTED_GOLD_INGOT = 203;	
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

	public static void main(final String[] args) {
		System.out.println("raided by angelo");
		System.out.println("destroyed the raider angelo");
    	System.out.println("Greghi is here :)");
		System.out.println();

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
		JavaCraft.world = new int[worldWidth][worldHeight];
		playerX = worldWidth / 2;
		playerY = worldHeight / 2;
		inventory = new ArrayList<>();
	}

	public static void generateWorld() {
		final int[] blocks = {WOOD, LEAVES, STONE, IRON_ORE, GOLD_ORE, AIR};
		final double[] probabilities = {0.15, 0.15, 0.15, 0.15, 0.1, 0.3};

		final Random rand = new Random();
		for (int y = 0; y < worldHeight; y++) {
			for (int x = 0; x < worldWidth; x++) {
				double threshold = rand.nextDouble();

				int block = AIR;
				blockTypeLoop:
				for (int i = 0; i < probabilities.length; i++) {
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

	private static String getBlockSymbol(final int blockType) {
		final String blockColor;
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
			default:
				blockColor = ANSI_RESET;
				break;
		}

		return blockColor + getBlockChar(blockType) + " ";
	}

	private static char getBlockChar(final int blockType) {
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
				return '\u25A2';
			default:
				return '-';
		}
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
			System.out.println(ANSI_CYAN
					+ "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door"
					+ ANSI_RESET);
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
				placeBlock(blockType);
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
				// getCountryAndQuoteFromServer(); TODO commented this to prevent us from accidently changing the flag
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

	/**
	 * This creates the flag of our country
	 */
	public static void generateEmptyWorld() {
        try {
            final String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/Flag_of_Canada.svg/1600px-Flag_of_Canada.svg.png";
            final URL url = new URL(imageUrl);
            final BufferedImage image = ImageIO.read(url);
            
            int sampleSize = 12; // Increase this for a smaller output
            for (int y = 0; y < image.getHeight(); y += sampleSize) {
                for (int x = 0; x < image.getWidth(); x += sampleSize) {
                    Color color = new Color(image.getRGB(x, y), true);
                    // You can adjust the condition for better appearance
                    if (color.getRed() > 200 && color.getGreen() < 50 && color.getBlue() < 50) {
                        System.out.print(ANSI_RED + "R");
                    } else {
                        System.out.print(ANSI_WHITE+ "W");
                    }
                }
                System.out.println();
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
		final int blockType = world[playerX][playerY];
		if (blockType != AIR) {
			inventory.add(blockType);
			world[playerX][playerY] = AIR;
			System.out.println("Mined " + getBlockName(blockType) + ".");
		} else {
			System.out.println("No block to mine here.");
		}
		waitForEnter();
	}

	public static void placeBlock(final int blockType) {
		if (blockType >= 0 && blockType <= 8) {
			if (blockType <= 4) {
				if (inventory.contains(blockType)) {
					inventory.remove(Integer.valueOf(blockType));
					world[playerX][playerY] = blockType;
					System.out.println("Placed " + getBlockName(blockType) + " at your position.");
				} else {
					System.out.println("You don't have " + getBlockName(blockType) + " in your inventory.");
				}
			} else {
				final int craftedItem = getCraftedItemFromBlockType(blockType);
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

	private static int getBlockTypeFromCraftedItem(final int craftedItem) {
		switch (craftedItem) {
			case CRAFTED_WOODEN_PLANKS:
				return 5;
			case CRAFTED_STICK:
				return 6;
			case CRAFTED_IRON_INGOT:
				return 7;
			case CRAFTED_GOLD_INGOT:
				return 8;
			default:
				return -1;
		}
	}

	private static int getCraftedItemFromBlockType(final int blockType) {
		switch (blockType) {
			case 5:
				return CRAFTED_WOODEN_PLANKS;
			case 6:
				return CRAFTED_STICK;
			case 7:
				return CRAFTED_IRON_INGOT;
			case 8:
				return CRAFTED_GOLD_INGOT;
			default:
				return -1;
		}
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
		if (inventoryContains(WOOD, 2)) {
			removeItemsFromInventory(WOOD, 2);
			addCraftedItem(CRAFTED_WOODEN_PLANKS);
			System.out.println("Crafted Wooden Planks.");
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

	public static void craftGoldIngot() {
		if (inventoryContains(GOLD_ORE, 2)) {
			removeItemsFromInventory(GOLD_ORE, 2);
			addCraftedItem(CRAFTED_GOLD_INGOT);
			System.out.println("Crafted Gold Ingot.");
		} else {
			System.out.println("Insufficient resources to craft Gold Ingot.");
		}
	}

	public static boolean inventoryContains(final int item) {
		return inventory.contains(item);
	}

	public static boolean inventoryContains(final int item, final int count) {
		int itemCount = 0;
		for (final int i : inventory) {
			if (i == item) {
				itemCount++;
				if (itemCount == count) {
					return true;
				}
			}
		}
		return false;
	}

	public static void removeItemsFromInventory(final int item, final int count) {
		int removedCount = 0;
		final Iterator<Integer> iterator = inventory.iterator();
		while (iterator.hasNext()) {
			final int i = iterator.next();
			if (i == item) {
				iterator.remove();
				removedCount++;
				if (removedCount == count) {
					break;
				}
			}
		}
	}

	public static void addCraftedItem(final int craftedItem) {
		if (craftedItems == null) {
			craftedItems = new ArrayList<>();
		}
		craftedItems.add(craftedItem);
	}

	public static void interactWithWorld() {
		final int blockType = world[playerX][playerY];
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

	private static String getBlockName(final int blockType) {
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
		System.out.println(ANSI_YELLOW + "\u001B\u001B- Gold ore block");
		System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
	}

	public static void displayInventory() {
		System.out.println("Inventory:");
		if (inventory.isEmpty()) {
			System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
		} else {
			final int[] blockCounts = new int[5];
			for (int i = 0; i < inventory.size(); i++) {
				final int block = inventory.get(i);
				blockCounts[block]++;
			}
			for (int blockType = 1; blockType < blockCounts.length; blockType++) {
				final int occurrences = blockCounts[blockType];
				if (occurrences > 0) {
					System.out.println(getBlockName(blockType) + " - " + occurrences);
				}
			}
		}
		System.out.println("Crafted Items:");
		if (craftedItems == null || craftedItems.isEmpty()) {
			System.out.println(ANSI_YELLOW + "None" + ANSI_RESET);
		} else {
			for (final int item : craftedItems) {
				System.out.print(getCraftedItemColor(item) + getCraftedItemName(item) + ", " + ANSI_RESET);
			}
			System.out.println();
		}
		System.out.println();
	}

	private static String getBlockColor(final int blockType) {
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
			default:
				return "";
		}
	}

	private static void waitForEnter() {
		System.out.println("Press Enter to continue...");
		final Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}

	private static String getCraftedItemName(final int craftedItem) {
		switch (craftedItem) {
			case CRAFTED_WOODEN_PLANKS:
				return "Wooden Planks";
			case CRAFTED_STICK:
				return "Stick";
			case CRAFTED_IRON_INGOT:
				return "Iron Ingot";
			case CRAFTED_GOLD_INGOT:
				return "Gold Ingot";
			default:
				return "Unknown";
		}
	}

	private static String getCraftedItemColor(final int craftedItem) {
		switch (craftedItem) {
			case CRAFTED_WOODEN_PLANKS:
			case CRAFTED_STICK:
			case CRAFTED_IRON_INGOT:
				return ANSI_BROWN;
			case CRAFT_GOLD_INGOT:
				return ANSI_YELLOW;
			default:
				return "";
		}
	}

	public static void getCountryAndQuoteFromServer() {
		try {
			final URL url = new URL("https://flag.ashish.nl/get_flag");
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			final String payload = "{\n" +
					"            \"group_number\": \"62\",\n" +
					"            \"group_name\": \"group62\",\n" +
					"            \"difficulty_level\": \"hard\"\n" +
					"        }";
			final OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(payload);
			writer.flush();
			writer.close();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			final String json = sb.toString();
			//final String json = testSb.toString();
			final int countryStart = json.indexOf("\"country\":\"") + 11;
			final int countryEnd = json.indexOf(" ", countryStart);
			final String country = json.substring(countryStart, countryEnd);
			final int quoteStart = json.indexOf("\"quote\":\"") + 9;
			final int quoteEnd = json.indexOf(" ", quoteStart);
			String quote = json.substring(quoteStart, quoteEnd);
			quote = quote.replace("\\\"", "\"");
			System.out.println("Country: " + country);
			System.out.println("Quote: " + quote);
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Error connecting to the server");
		}
	}
}