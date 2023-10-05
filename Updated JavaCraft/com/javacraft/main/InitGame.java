


/**
 * InitGame - Class for Initialization and Game World Management
 * <p>
 * This class serves as the foundation for the game world and player environment.
 * It is responsible for the initialization of world dimensions, randomized and empty world generation,
 * and resetting the game world.
 * </p>
 * <ul>
 * <li>Initialize world dimensions</li>
 * <li>Generate worlds (randomized or empty)</li>
 * <li>Reset the game world</li>
 * </ul>
 *
 * @author Group49
 * @version 1.0
 * @see CraftingUtils
 *
 * Package: com.javacraft.main
 */

// Import dependencies
import java.util.ArrayList;
import java.util.Random;

public class InitGame {

  // Explicit type declarations

  /**
   * The 2D array representing the game world.
   */
  public static int[][] world;

  /**
   * Width of the game world.
   */
  public static int worldWidth;

  /**
   * Height of the game world.
   */
  public static int worldHeight;

  /**
   * Player's X-coordinate position.
   */
  public static int playerX;

  /**
   * Player's Y-coordinate position.
   */
  public static int playerY;

  /**
   * Player's inventory as an ArrayList of integers.
   */
  public static ArrayList<Integer> inventory;

  // Constants for a new world
  /**
   * Default width for new worlds.
   */
  public static int NEW_WORLD_WIDTH = 25;

  /**
   * Default height for new worlds.
   */
  public static int NEW_WORLD_HEIGHT = 15;

  /**
   * Initializes the game world dimensions and player position.
   *
   * @param worldWidth  The width of the game world in blocks.
   * @param worldHeight The height of the game world in blocks.
   */
  public static void initGame(int worldWidth, int worldHeight) {
    // Set dimensions
    InitGame.worldWidth = worldWidth;
    InitGame.worldHeight = worldHeight;

    // Initialize game world array
    world = new int[worldWidth][worldHeight];

    // Set player's initial coordinates
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;

    // Initialize player's inventory
    inventory = new ArrayList<>();
  }

  /**
   * Generates the game world with randomized terrain blocks.
   * <p>
   * This method uses a random number generator to set terrain types in the game world 2D array.
   * </p>
   */
  public static void generateWorld() {
    Random rand = new Random();
    for (int y = 0; y < worldHeight; y++) {
      for (int x = 0; x < worldWidth; x++) {
        int randValue = rand.nextInt(100);
        // Conditional statements for setting block types
        if (randValue < 20) {
          world[x][y] = CraftingUtils.WOOD;
        } else if (randValue < 35) {
          world[x][y] = CraftingUtils.LEAVES;
        } else if (randValue < 50) {
          world[x][y] = CraftingUtils.STONE;
        } else if (randValue < 70) {
          world[x][y] = CraftingUtils.IRON_ORE;
        } else {
          world[x][y] = CraftingUtils.AIR;
        }
      }
    }
  }

  /**
   * Generates an empty world divided into three color sections.
   * <p>
   * This method initializes an empty world 2D array and fills it with three stripes of colored blocks.
   * </p>
   */
  public static void generateEmptyWorld() {
    // Initialize new game world array
    world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];

    // Block types for the stripes
    int redBlock = 1;
    int whiteBlock = 4;
    int blueBlock = 3;

    // Height of each stripe
    int stripeHeight = NEW_WORLD_HEIGHT / 3;

    // Fill the stripes
    // (Look ma, it's the making of a patriotic world!)
    for (int y = 0; y < stripeHeight; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = redBlock;
      }
    }
    for (int y = stripeHeight; y < stripeHeight * 2; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = whiteBlock;
      }
    }
    for (int y = stripeHeight * 2; y < NEW_WORLD_HEIGHT; y++) {
      for (int x = 0; x < NEW_WORLD_WIDTH; x++) {
        world[x][y] = blueBlock;
      }
    }
  }

  /**
   * Resets the game world and player position.
   * <p>
   * This method invokes {@code generateEmptyWorld()} to reset the world and repositions the player to the center.
   * </p>
   */
  public static void resetWorld() {
    // Reset world to an empty state
    generateEmptyWorld();

    // Reset player's coordinates
    playerX = worldWidth / 2;
    playerY = worldHeight / 2;
  }
}
