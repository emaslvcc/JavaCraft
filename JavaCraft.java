import java.util.*;
import java.net.*;
import java.io.*;
//nath comment
//nils comment
//zofia comment
public class JavaCraft
{
	// Constants where their name is the name of the block and their value is the number which
	// represents it (often referred in the code as blockType)
	private static final int AIR = 0;
	private static final int WOOD = 1;
	private static final int LEAVES = 2;
	private static final int STONE = 3;
	private static final int IRON_ORE = 4;
	// New blocks
	private static final int HONEY = 5;
	private static final int HOPS = 6;

	// Variables which represent the width and height of the world for flag drawing
	private static int NEW_WORLD_WIDTH = 50;
	private static int NEW_WORLD_HEIGHT = 30;

	// Unused variables and constants
	private static int EMPTY_BLOCK = 0;
	private static final int CRAFT_WOODEN_PLANKS = 100;
	private static final int CRAFT_STICK = 101;
	private static final int CRAFT_IRON_INGOT = 102;

	// Constants where their name is the name of a crafted item and their value is the number which
	// represents it (reffered in the code as either blockType or craftedItem)
	private static final int CRAFTED_WOODEN_PLANKS = 200;
	private static final int CRAFTED_STICK = 201;
	private static final int CRAFTED_IRON_INGOT = 202;
	// New craftable
	private static final int CRAFTED_MEAD = 203;

	// Constants where their name is the name of a color and their value is a string which
	// represents that color as an ANSI escape character (outputting those will cause all the
	// following text to have that color)
	private static final String ANSI_BROWN = "\u001B[33m";
	private static final String ANSI_RESET = "\u001B[0m"; // This one changes the color back to default
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_GRAY = "\u001B[37m";
	private static final String ANSI_WHITE = "\u001B[97m";
	private static final String ANSI_BLACK = "\u001b[30m";

	// Constant which contains a string that represents the block legend
	private static final String BLOCK_NUMBERS_INFO = "Block Numbers:\n" +
			"0 - Empty block\n" +
			"1 - Wood block\n" +
			"2 - Leaves block\n" +
			"3 - Stone block\n" +
			"4 - Iron ore block\n" +
			"5 - Honey block\n" +
			"6 - Hops block\n" +
			"7 - Wooden Planks (Crafted Item)\n" +
			"8 - Stick (Crafted Item)\n" +
			"9 - Iron Ingot (Crafted Item)" +
			"10 - Mead (Crafted Item)";
	
	// A two dimensioonal integer array (matrix) which represents the world. The value at
	// world[i][j] represents the block that is there eg. world[0][0] == 0 means that the block in
	// the upper left corner is AIR
	private static int[][] world;

	// Variables which represent the width and height of the world for gameplay
	private static int worldWidth;
	private static int worldHeight;

	// Variables which represent the X and Y cooridinates of the player
	private static int playerX;
	private static int playerY;

	// An array of integers which represents the blocks in the players inventory
	private static List<Integer> inventory = new ArrayList<>();

	// An array of integers which represents the crafted intems in the players inventory
	private static List<Integer> craftedItems = new ArrayList<>();

	// Variables for secret door logic
	private static boolean unlockMode = false;
	private static boolean secretDoorUnlocked = false;
	private static boolean inSecretArea = false;

	// Variable that controls how many of each type of block the fillInventory() function puts in
	// the players inventory
	private static final int INVENTORY_SIZE = 100;

	// Entry point of the program. Initializes necessary variables, prints intro and instructions
	// and starts the game if prompted to do so
	public static void main(String[] args)
	{
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
		if (startGameChoice.equals("Y"))
		{
			startGame();
		}
		else
		{
			System.out.println("Game not started. Goodbye!");
		}
	}

	// Takes two integers, 'worldWidth' and 'worldHeight' and initializes the world matrix using
	// them. Initializes the inventory. Sets the player's coordinates to the middle of the world
	public static void initGame(int worldWidth, int worldHeight)
	{
		JavaCraft.worldWidth = worldWidth;
		JavaCraft.worldHeight = worldHeight;
		JavaCraft.world = new int[worldWidth][worldHeight];
		playerX = worldWidth / 2;
		playerY = worldHeight / 2;
		inventory = new ArrayList<>();
	}

	// Randomly fills the world matrix with blocks according to hardcoded probabilities.
	public static void generateWorld()
	{
		Random rand = new Random();
		for (int y = 0; y < worldHeight; y++)
		{
			for (int x = 0; x < worldWidth; x++)
			{
				int randValue = rand.nextInt(100);
				if (randValue < 20)
				{
					world[x][y] = WOOD;
				}
				else if (randValue < 35)
				{
					world[x][y] = LEAVES;
				}
				else if (randValue < 50)
				{
					world[x][y] = STONE;
				}
				else if (randValue < 60)
				{
					world[x][y] = IRON_ORE;
				}
				else if (randValue < 80)
				{
					world[x][y] = HONEY;
				}
				else if (randValue < 95)
				{
					world[x][y] = HOPS;
				}
				else
				{
					world[x][y] = AIR;
				}
			}
		}
	}

	// Outputs a visual representation of the world matrix to the terminal
	public static void displayWorld()
	{
		System.out.println(ANSI_CYAN + "World Map:" + ANSI_RESET);
		System.out.println("╔══" + "═".repeat(worldWidth * 2 - 2) + "╗");
		for (int y = 0; y < worldHeight; y++)
		{
			System.out.print("║");
			for (int x = 0; x < worldWidth; x++)
			{
				if (x == playerX && y == playerY && !inSecretArea)
				{
					System.out.print(ANSI_GREEN + "P " + ANSI_RESET);
				}
				else if (x == playerX && y == playerY && inSecretArea)
				{
					System.out.print(ANSI_GREEN + "P " + ANSI_RESET);
				}
				else
				{
					System.out.print(getBlockSymbol(world[x][y]));
				}
			}
			System.out.println("║");
		}
		System.out.println("╚══" + "═".repeat(worldWidth * 2 - 2) + "╝");
	}

	// Takes an integer representation of a block and returns the string needed to visually
	// represent it in the terminal
	private static String getBlockSymbol(int blockType)
	{
		String blockColor;
		switch (blockType)
		{
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
			case HONEY:
				blockColor = ANSI_YELLOW;
				break;
			case HOPS:
				if(!inSecretArea)
				{
					blockColor = ANSI_PURPLE;
				}
				else
				{
					blockColor = ANSI_BLACK;
				}
				break;
			default:
				blockColor = ANSI_RESET;
				break;
		}
		return blockColor + getBlockChar(blockType) + " ";
	}

	// Takes an integer representation of a block and returns the character needed to visually
	// represent it in the terminal 
	private static char getBlockChar(int blockType)
	{
		switch (blockType)
		{
			case WOOD:
				return '\u2592';
			case LEAVES:
				return '\u00A7';
			case STONE:
				return '\u2593';
			case IRON_ORE:
				return '\u00B0';
			case HONEY:
				return '\u00A4';
			case HOPS:
				return '\u00B6';
			default:
				return '-';
		}
	}

	// Initializes necessary variables and starts the game loop
	public static void startGame()
	{
		Scanner scanner = new Scanner(System.in);
		boolean unlockMode = false;
		boolean craftingCommandEntered = false;
		boolean miningCommandEntered = false;
		boolean movementCommandEntered = false;
		boolean openCommandEntered = false;
		while (true)
		{
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
					input.equalsIgnoreCase("d") || input.equalsIgnoreCase("right"))
			{
				if (unlockMode)
				{
					movementCommandEntered = true;
				}
				movePlayer(input);
			}
			else if (input.equalsIgnoreCase("m"))
			{
				if (unlockMode)
				{
					miningCommandEntered = true;
				}
				mineBlock();
			}
			else if (input.equalsIgnoreCase("p"))
			{
				displayInventory();
				System.out.print("Enter the block type to place: ");
				int blockType = scanner.nextInt();
				placeBlock(blockType);
			}
			else if (input.equalsIgnoreCase("c"))
			{
				displayCraftingRecipes();
				System.out.print("Enter the recipe number to craft: ");
				int recipe = scanner.nextInt();
				craftItem(recipe);
			}
			else if (input.equalsIgnoreCase("i"))
			{
				interactWithWorld();
			}
			else if (input.equalsIgnoreCase("save"))
			{
				System.out.print("Enter the file name to save the game state: ");
				String fileName = scanner.next();
				saveGame(fileName);
			}
			else if (input.equalsIgnoreCase("load"))
			{
				System.out.print("Enter the file name to load the game state: ");
				String fileName = scanner.next();
				loadGame(fileName);
			}
			else if (input.equalsIgnoreCase("exit"))
			{
				System.out.println("Exiting the game. Goodbye!");
				break;
			}
			else if (input.equalsIgnoreCase("look"))
			{
				lookAround();
			}
			else if (input.equalsIgnoreCase("unlock"))
			{
				unlockMode = true;
			}
			else if (input.equalsIgnoreCase("getflag"))
			{
				//getCountryAndQuoteFromServer();
				//waitForEnter();
			}
			else if (input.equalsIgnoreCase("open"))
			{
				if ((unlockMode && craftingCommandEntered && miningCommandEntered && movementCommandEntered))
				{
					secretDoorUnlocked = true;
					resetWorld();
					System.out.println("Secret door unlocked!");
					waitForEnter();
				}
				else
				{
					System.out.println("Invalid passkey. Try again!");
					waitForEnter();
					unlockMode = false;
					craftingCommandEntered = false;
					miningCommandEntered = false;
					movementCommandEntered = false;
					openCommandEntered = false;
				}
			}
			else
			{
				System.out.println(ANSI_YELLOW + "Invalid input. Please try again." + ANSI_RESET);
			}
			if (unlockMode)
			{
				if (input.equalsIgnoreCase("c"))
				{
					craftingCommandEntered = true;
				}
				else if (input.equalsIgnoreCase("m"))
				{
					miningCommandEntered = true;
				}
				else if (input.equalsIgnoreCase("open"))
				{
					openCommandEntered = true;
				}
			}
			if (secretDoorUnlocked)
			{
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

	// Clears the player's inventory and adds to it INVENTORY_SIZE (100 by default)
	// of each block except for air
	private static void fillInventory()
	{
		inventory.clear();
		for (int blockType = 1; blockType <= 6; blockType++)
		{
			for (int i = 0; i < INVENTORY_SIZE; i++)
			{
				inventory.add(blockType);
			}
		}
	}
	
	// Fills the world matrix with blocks in such a way that its visual representation becomes the
	// flag of The Netherlands and sets the player's coordinates to the middle
	private static void resetWorld()
	{
		generateEmptyWorld();
		playerX = worldWidth / 2;
		playerY = worldHeight / 2;
	}

	// Fills the world matrix with blocks in such a way that its visual representation becomes the
	// flag of South Africa. In order for the flag to be displayed properly the program needs to be
	// combiled with -encoding UTF-8 and the terminal font needs to include all the necessary
	// symbols. e.g. Consolas
	private static void generateEmptyWorld()
	{
		worldWidth = NEW_WORLD_WIDTH;
		worldHeight = NEW_WORLD_HEIGHT;

		world = new int[worldWidth][worldHeight];
		int redBlock = WOOD;
		int whiteBlock = IRON_ORE;
		int blueBlock = STONE;
		int yellowBlock = HONEY;
		int greenBlock = LEAVES;
		int blackBlock = HOPS;

		for (int x = 0; x < worldWidth; x++)
		{
			for (int y = 0; y < worldHeight; y++)
			{
				world[x][y] = greenBlock;
			}
		}
		drawTriangle(0, 3, 19, 15, yellowBlock, true);

		drawTriangle(0, 5, 16, 15, blackBlock, true);

		drawTriangle(6, 0, 26, 12, whiteBlock, false);
		drawRectangle(27, 0, worldWidth-1, 12, whiteBlock);

		drawTriangle(9, 0, 27, 10, redBlock, false);
		drawRectangle(27, 0, worldWidth-1, 10, redBlock);

		for (int x = 0; x < worldWidth; x++)
		{
			for (int y = worldHeight / 2; y < worldHeight; y++)
			{
				world[x][y] = world[x][worldHeight - y - 1];
			}
		}
		for (int x = 0; x < worldWidth; x++)
		{
			for (int y = worldHeight / 2; y < worldHeight; y++)
			{
				if (world[x][y] == redBlock)
				{
					world[x][y] = blueBlock;
				}
			}
		}

	}

	// Set the values of the world matrix so that a part of it represents a rectangle of color
	// 'color' with its upper left corner at (x1, y1) and lower right corner at (x2, y2).
	public static void drawRectangle(int x1, int y1, int x2, int y2, int color)
	{
		for (int x = 0; x < worldWidth; x++)
		{
			for (int y = 0; y < worldHeight; y++)
			{
				if (y >= y1 && y <= y2)
				{
					if (x >= x1 && x <= x2)
					{
						world[x][y] = color;
					}
				}
			}
		}		
	}

	// Set the values of the world matrix so that a part of it represents a triangle of color
	// 'color' with its upper left corner at (x1, y1) and lower right corner at (x2, y2). 
	// If 'down' is true then the triangle will be under the line from (x1, y1) to (x2, y2). If
	// 'down' is false then the opposite will be true.
	public static void drawTriangle(int x1, int y1, int x2, int y2, int color, boolean down)
	{

		double slope = ((double)y2 - (double)y1) / ((double)x2 - (double)x1);
		double c = (double)y1 - (slope * (double)x1);

		for (int x = 0; x < worldWidth; x++)
		{
			for (int y = 0; y < worldHeight; y++)
			{
				if (y >= y1 && y <= y2)
				{
					if (x >= x1 && x <= x2)
					{
						if (down)
						{
							if ((double)y >= (slope * (double)x) + c)
							{
								world[x][y] = color;
							}
						}
						else
						{
							if ((double)y <= (slope * (double)x) + c)
							{
								world[x][y] = color;
							}
						}
					}
				}
			}
		}
	}

	// Clears the terminal
	private static void clearScreen()
	{
		try
		{
			if (System.getProperty("os.name").contains("Windows"))
			{
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			}
			else
			{
				System.out.print("\033[H\033[2J");
				System.out.flush();
			}
		}
		catch (IOException | InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}

	// Outputs a visual representation of the player and the adjecent blocks to the terminal
	private static void lookAround()
	{
		System.out.println("You look around and see:");
		for (int y = Math.max(0, playerY - 1); y <= Math.min(playerY + 1, worldHeight - 1); y++)
		{
			for (int x = Math.max(0, playerX - 1); x <= Math.min(playerX + 1, worldWidth - 1); x++)
			{
				if (x == playerX && y == playerY)
				{
					System.out.print(ANSI_GREEN + "P " + ANSI_RESET);
				}
				else
				{
					System.out.print(getBlockSymbol(world[x][y]));
				}
			}
			System.out.println();
		}
		System.out.println();
		waitForEnter();
	}

	// Takes a string representing a direction and updates the player's coordinates by one block in
	// that direction
	public static void movePlayer(String direction)
	{
		switch (direction.toUpperCase())
		{
			case "W":
			case "UP":
				if (playerY > 0)
				{
					playerY--;
				}
				break;
			case "S":
			case "DOWN":
				if (playerY < worldHeight - 1)
				{
					playerY++;
				}
				break;
			case "A":
			case "LEFT":
				if (playerX > 0)
				{
					playerX--;
				}
				break;
			case "D":
			case "RIGHT":
				if (playerX < worldWidth - 1)
				{
					playerX++;
				}
				break;
			default:
				break;
		}
	}

	// If possible, replaces the block in the world matrix at the player's coordinates and with air
	// and adds the replaced block to the player's inventory
	public static void mineBlock()
	{
		int blockType = world[playerX][playerY];
		if (blockType != AIR)
		{
			inventory.add(blockType);
			world[playerX][playerY] = AIR;
			System.out.println("Mined " + getBlockName(blockType) + ".");
		}
		else
		{
			System.out.println("No block to mine here.");
		}
		waitForEnter();
	}

	// Takes an integer representing a block or item from the player's inventory. If possible,
	// replaces the block in the world matrix at the player's coordinates with the given block and
	// removes it from the player's inventory
	public static void placeBlock(int blockType)
	{
		if (blockType >= 0 && blockType <= 7)
		{
			if (blockType <= 6)
			{
				if (inventory.contains(blockType))
				{
					inventory.remove(Integer.valueOf(blockType));
					world[playerX][playerY] = blockType;
					System.out.println("Placed " + getBlockName(blockType) + " at your position.");
				}
				else
				{
					System.out.println("You don't have " + getBlockName(blockType) + " in your inventory.");
				}
			}
			else
			{
				int craftedItem = getCraftedItemFromBlockType(blockType);
				if (craftedItems.contains(craftedItem))
				{
					craftedItems.remove(Integer.valueOf(craftedItem));
					world[playerX][playerY] = blockType;
					System.out.println("Placed " + getCraftedItemName(craftedItem) + " at your position.");
				}
				else
				{
					System.out.println("You don't have " + getCraftedItemName(craftedItem) + " in your crafted items.");
				}
			}
		}
		else
		{
			System.out.println("Invalid block number. Please enter a valid block number.");
			System.out.println(BLOCK_NUMBERS_INFO);
		}
		waitForEnter();
	}

	// Unused function for getting the block type from an integer representation of a crafted item 
	private static int getBlockTypeFromCraftedItem(int craftedItem)
	{
		switch (craftedItem)
		{
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

	// Takes a blockType integer representation of a crafted item (as outlined in
	// BLOCK_NUMBERS_INFO) and returns it's integer representation according to the crafted item
	// constants
	private static int getCraftedItemFromBlockType(int blockType)
	{
		switch (blockType)
		{
			case 7:
				return CRAFTED_WOODEN_PLANKS;
			case 8:
				return CRAFTED_STICK;
			case 9:
				return CRAFTED_IRON_INGOT;
			case 10:
				return CRAFTED_MEAD;
			default:
				return -1;
		}
	}

	// Outputs crafting recipes to the terminal
	public static void displayCraftingRecipes()
	{
		System.out.println("Crafting Recipes:");
		System.out.println("1. Craft Wooden Planks: 2 Wood");
		System.out.println("2. Craft Stick: 1 Wood");
		System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
		System.out.println("4. Craft Mead: 1 Honey, 1 Hops");
	}

	// Takes an integer representing a crafting recipe (as outlined in displayCraftingRecipes()) and
	// calls the corresponding function if it exists
	public static void craftItem(int recipe)
	{
		switch (recipe)
		{
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
				craftMead();
				break;
			default:
				System.out.println("Invalid recipe number.");
		}
		waitForEnter();
	}

	public static void craftMead()
	{
		if (inventoryContains(HONEY) && inventoryContains(HOPS))
		{
			removeItemsFromInventory(HONEY, 1);
			removeItemsFromInventory(HOPS, 1);
			addCraftedItem(CRAFTED_MEAD);
			System.out.println("Crafted Mead.");
		}
		else
		{
			System.out.println("Insufficient resources to craft Mead.");
		}
	}

	// If possible, removes ingredients for wooden planks from the player's inventory and adds
	// wooden planks to it
	public static void craftWoodenPlanks()
	{
		if (inventoryContains(WOOD, 2))
		{
			removeItemsFromInventory(WOOD, 2);
			addCraftedItem(CRAFTED_WOODEN_PLANKS);
			System.out.println("Crafted Wooden Planks.");
		}
		else
		{
			System.out.println("Insufficient resources to craft Wooden Planks.");
		}
	}

	// If possible, removes ingredients for stick from the player's inventory and adds
	// stick to it
	public static void craftStick()
	{
		if (inventoryContains(WOOD))
		{
			removeItemsFromInventory(WOOD, 1);
			addCraftedItem(CRAFTED_STICK);
			System.out.println("Crafted Stick.");
		}
		else
		{
			System.out.println("Insufficient resources to craft Stick.");
		}
	}

	// If possible, removes ingredients for iron ingot from the player's inventory and adds
	// iron ingot to it
	public static void craftIronIngot()
	{
		if (inventoryContains(IRON_ORE, 3))
		{
			removeItemsFromInventory(IRON_ORE, 3);
			addCraftedItem(CRAFTED_IRON_INGOT);
			System.out.println("Crafted Iron Ingot.");
		}
		else
		{
			System.out.println("Insufficient resources to craft Iron Ingot.");
		}
	}

	// Takes an integer representing a block. Returns true if the player's inventory contains it and
	// false otherwise 
	public static boolean inventoryContains(int item)
	{
		return inventory.contains(item);
	}

	// Takes an integer representing a block and an integer representing an amount. Returns true if
	// the player's inventory contains at least the specified amount of the specified block and
	// false otherwise
	public static boolean inventoryContains(int item, int count)
	{
		int itemCount = 0;
		for (int i : inventory)
		{
			if (i == item)
			{
				itemCount++;
				if (itemCount == count)
				{
					return true;
				}
			}
		}
		return false;
	}

	// Takes an integer representing a block and an integer representing an amount. Removes as much
	// as possible (but not more than the specified amount) of the specified block from the player's
	// inventory
	public static void removeItemsFromInventory(int item, int count)
	{
		int removedCount = 0;
		Iterator<Integer> iterator = inventory.iterator();
		while (iterator.hasNext())
		{
			int i = iterator.next();
			if (i == item)
			{
				iterator.remove();
				removedCount++;
				if (removedCount == count)
				{
					break;
				}
			}
		}
	}

	// Takes an integer representing a crafted item and adds it to the craftedItems array
	public static void addCraftedItem(int craftedItem)
	{
		if (craftedItems == null)
		{
			craftedItems = new ArrayList<>();
		}
		craftedItems.add(craftedItem);
	}

	// If possible, adds the block at the player's cooridnates in the world matrix to the player's
	// inventory
	public static void interactWithWorld()
	{
		int blockType = world[playerX][playerY];
		switch (blockType)
		{
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
			case HONEY:
				System.out.println("You gather honey from the nest");
				inventory.add(HONEY);
				break;
			case HOPS:
				System.out.println("You gather hops from the ground");
				inventory.add(HOPS);
				break;
			case AIR:
				System.out.println("Nothing to interact with here.");
				break;
			default:
				System.out.println("Unrecognized block. Cannot interact.");
		}
		waitForEnter();
	}

	// Prompts the user for a name and tries to save the game to a file with the specified name
	public static void saveGame(String fileName)
	{
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName)))
		{
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
		}
		catch (IOException e)
		{
			System.out.println("Error while saving the game state: " + e.getMessage());
		}
		waitForEnter();
	}

	// Prompts the user for a name and tries to load the game from the file with the specified name
	public static void loadGame(String fileName)
	{
		// Implementation for loading the game state from a file goes here
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName)))
		{
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
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.out.println("Error while loading the game state: " + e.getMessage());
		}
		waitForEnter();
	}

	// Takes an integer representing a block and returns a string representing its name
	private static String getBlockName(int blockType)
	{
		switch (blockType)
		{
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
			case HONEY:
				return "Honey";
			case HOPS:
				return "Hops";
			default:
				return "Unknown";
		}
	}

	// Ouputs the legend to the terminal
	public static void displayLegend()
	{
		System.out.println(ANSI_BLUE + "Legend:");
		System.out.println(ANSI_WHITE + "-- - Empty block");
		System.out.println(ANSI_RED + "\u2592\u2592 - Wood block");
		System.out.println(ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
		System.out.println(ANSI_BLUE + "\u2593\u2593 - Stone block");
		System.out.println(ANSI_WHITE + "\u00B0\u00B0 - Iron ore block");
		System.out.println(ANSI_YELLOW + "\u00A4\u00A4 - Honey block");
		System.out.println(ANSI_PURPLE + "\u00B6\u00B6 - Hops block");
		System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
	}

	// Outputs a visual representation of the inventory to the terminal
	public static void displayInventory()
	{
		System.out.println("Inventory:");
		if (inventory.isEmpty())
		{
			System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
		}
		else
		{
			int[] blockCounts = new int[7];
			for (int i = 0; i < inventory.size(); i++)
			{
				int block = inventory.get(i);
				blockCounts[block]++;
			}
			for (int blockType = 1; blockType < blockCounts.length; blockType++)
			{
				int occurrences = blockCounts[blockType];
				if (occurrences > 0)
				{
					System.out.println(getBlockName(blockType) + " - " + occurrences);
				}
			}
		}
		System.out.println("Crafted Items:");
		if (craftedItems == null || craftedItems.isEmpty())
		{
			System.out.println(ANSI_YELLOW + "None" + ANSI_RESET);
		}
		else
		{
			for (int item : craftedItems)
			{
				System.out.print(getCraftedItemColor(item) + getCraftedItemName(item) + ", " + ANSI_RESET);
			}
			System.out.println();
		}
		System.out.println();
	}

	// Unused function for getting the color of a specified block
	private static String getBlockColor(int blockType)
	{
		switch (blockType)
		{
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

	// Prompts the user to press enter and waits for it
	private static void waitForEnter()
	{
		System.out.println("Press Enter to continue...");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}

	// Takes an integer representation of a crafted item and returns a string representing its name
	private static String getCraftedItemName(int craftedItem)
	{
		switch (craftedItem)
		{
			case CRAFTED_WOODEN_PLANKS:
				return "Wooden Planks";
			case CRAFTED_STICK:
				return "Stick";
			case CRAFTED_IRON_INGOT:
				return "Iron Ingot";
			case CRAFTED_MEAD:
				return "Mead";
			default:
				return "Unknown";
		}
	}

	// Takes an integer representation of a crafted item and returns the ANSI_BROWN string if it is
	// valid. Returns an empty string otherwise
	private static String getCraftedItemColor(int craftedItem)
	{
		switch (craftedItem)
		{
			case CRAFTED_WOODEN_PLANKS:
			case CRAFTED_STICK:
			case CRAFTED_IRON_INGOT:
			case CRAFTED_MEAD:
				return ANSI_BROWN;
			default:
				return "";
		}
	}

	// Fetches a country name and flag from the server, then outputs them to the terminal
	public static void getCountryAndQuoteFromServer()
	{
		try
		{
			URL url = new URL("https://flag.ashish.nl/get_flag");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			String payload = "{\"group_number\": 80, \"group_name\": \"notGroup80\", \"difficulty_level\": \"hard\"}";
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(payload);
			writer.flush();
			writer.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line);
			}
			String json = sb.toString();
			System.out.println("json: " + json);
			int countryStart = json.indexOf(" ") + 11;
			int countryEnd = json.indexOf(" ", countryStart);
			String country = json.substring(countryStart, countryEnd);
			int quoteStart = json.indexOf(" ") + 9;
			int quoteEnd = json.indexOf(" ", quoteStart);
			String quote = json.substring(quoteStart, quoteEnd);
			quote = quote.replace(" ", " ");
			System.out.println(" " + country);
			System.out.println(" " + quote);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error connecting to the server");
		}
	}
}