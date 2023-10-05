// ****************************************************************************************************

// ****************************************************************************************************
import java.util.Scanner;

/**
 * Manages the player's movement and interactions within the game world.
 *
 * The PlayerMovement class is responsible for handling the movement and interaction logic for the player character.
 * It provides methods for moving the player, looking around the environment, and interacting with the game world.
 *
 * @author Group49
 * @version 1.0
 * @see JavaCraft
 * @see Inventory
 */

public class PlayerMovement {

  /**
   * X-coordinate of the player in the world grid.
   */
  public static int playerX;

  /**
   * Y-coordinate of the player in the world grid.
   */
  public static int playerY;

  /**
   * Whether the secret door in the game is unlocked.
   */
  public static boolean secretDoorUnlocked = false;

  /**
   * Whether the player is currently in the secret area.
   */
  public static boolean inSecretArea = false;

  /**
   * Mode to unlock the secret door.
   */
  public static boolean unlockMode = false;

  /**
   * Scanner object for reading player input.
   */
  static Scanner scanner = new Scanner(System.in);

  /**
   * Moves the player in a specified direction.
   *
   * @param direction The direction in which the player moves, represented as a string.
   * @see #playerX
   * @see #playerY
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
        if (playerY < InitGame.worldHeight - 1) {
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
        if (playerX < InitGame.worldWidth - 1) {
          playerX++;
        }
        break;
      default:
        break;
    }
  }

  /**
   * Describes the blocks in the player's immediate environment.
   *
   * @see JavaCraft
   * @see #playerX
   * @see #playerY
   */
  public static void lookAround() {
    System.out.println("You look around and see:");
    for (
      int y = Math.max(0, playerY - 1);
      y <= Math.min(playerY + 1, InitGame.worldHeight - 1);
      y++
    ) {
      for (
        int x = Math.max(0, playerX - 1);
        x <= Math.min(playerX + 1, InitGame.worldWidth - 1);
        x++
      ) {
        if (x == playerX && y == playerY) {
          System.out.print(
            DisplayUtils.ANSI_GREEN + "P " + DisplayUtils.ANSI_RESET
          );
        } else {
          System.out.print(DisplayUtils.getBlockSymbol(JavaCraft.world[x][y]));
        }
      }
      System.out.println();
    }
    JavaCraft.waitForEnter();
  }

  /**
   * Handles player interactions with the game world, like picking up resources or crafting.
   *
   * @see Inventory
   * @see CraftingUtils
   */
  public static void interactWithWorld() {
    int blockType = InitGame.world[playerX][playerY];
    switch (blockType) {
      case CraftingUtils.WOOD:
        System.out.println("You gather wood from the tree.");
        Inventory.inventory.add(CraftingUtils.WOOD);
        break;
      case CraftingUtils.LEAVES:
        System.out.println("You gather leaves from the tree.");
        Inventory.inventory.add(CraftingUtils.LEAVES);
        break;
      case CraftingUtils.STONE:
        System.out.println("You gather stones from the ground.");
        Inventory.inventory.add(CraftingUtils.STONE);
        break;
      case CraftingUtils.IRON_ORE:
        System.out.println("You mine iron ore from the ground.");
        Inventory.inventory.add(CraftingUtils.IRON_ORE);
        break;
      case CraftingUtils.AIR:
        System.out.println("Nothing to interact with here.");
        break;
      default:
        System.out.println("Unrecognized block. Cannot nteract.");
    }
    JavaCraft.waitForEnter();
  }
}
