/**
 * DisplayUtils
 *
 * <p>
 * The {@code DisplayUtils} class manages the visual aspects of the JavaCraft game.
 * It contains methods for displaying the game world, the player's inventory, and other game-related
 * elements on the console.
 * </p>
 *
 * <p>
 * ANSI escape codes are used to display colored text within the console.
 * </p>
 *
 * @author  Group 49
 * @version 1.0
 */
package com.javacraft.utils;

import com.javacraft.main.*;
import com.javacraft.player.Inventory;
import com.javacraft.player.PlayerMovement;

public class DisplayUtils {

  /**
   * ANSI escape code for the color Brown.
   */
  public static final String ANSI_BROWN = "\u001B[33m";

  /**
   * ANSI escape code for resetting the color.
   */
  public static final String ANSI_RESET = "\u001B[0m";

  /**
   * ANSI escape code for the color Green.
   */
  public static final String ANSI_GREEN = "\u001B[32m";

  /**
   * ANSI escape code for the color Yellow. Note: same as Brown.
   */
  public static final String ANSI_YELLOW = "\u001B[33m";

  /**
   * ANSI escape code for the color Cyan.
   */
  public static final String ANSI_CYAN = "\u001B[36m";

  /**
   * ANSI escape code for the color Red.
   */
  public static final String ANSI_RED = "\u001B[31m";

  /**
   * ANSI escape code for the color Purple.
   */
  public static final String ANSI_PURPLE = "\u001B[35m";

  /**
   * ANSI escape code for the color Blue.
   */
  public static final String ANSI_BLUE = "\u001B[34m";

  /**
   * ANSI escape code for the color Gray.
   */
  public static final String ANSI_GRAY = "\u001B[37m";

  /**
   * ANSI escape code for the color White.
   */
  public static final String ANSI_WHITE = "\u001B[97m";

  /**
   * Method to display the game world on the console.
   *
   * Loops through the 2D array representing the game world, and prints it to the console.
   * Uses ANSI escape codes for colored text.
   */
  public static void displayWorld() {
    // ANSI_CYAN sets the text color to cyan. "World Map:" will be printed in this color.
    System.out.println(ANSI_CYAN + "World Map:" + ANSI_RESET);

    // Validate worldWidth to prevent a negative repeat count.
    // If worldWidth is invalid, it prints a message and returns early.
    if (InitGame.worldWidth >= 1) {
      System.out.println("".repeat(InitGame.worldWidth * 2 - 2));
    } else {
      System.out.println("World width is invalid! Please make sure it's >= 1.");
      return; // Exit the method early.
    }

    // Loop through the rows of the game world.
    for (int y = 0; y < InitGame.worldHeight; y++) {
      // Loop through the columns of the game world.
      for (int x = 0; x < InitGame.worldWidth; x++) {
        // If the current position matches the player's position and the player is not in a secret area.
        if (
          x == PlayerMovement.playerX &&
          y == PlayerMovement.playerY &&
          !PlayerMovement.inSecretArea
        ) {
          // ANSI_BLUE sets the text color to blue, "\u2588 " is a full block Unicode character.
          // ANSI_RESET resets the text color back to the default.
          System.out.print(ANSI_BLUE + "\u2588 " + ANSI_RESET);
          // If the current position matches the player's position and the player is in a secret area.
        } else if (
          x == PlayerMovement.playerX &&
          y == PlayerMovement.playerY &&
          PlayerMovement.inSecretArea
        ) {
          // "\u263A " is a Unicode smiling face. Indicates the player is in a secret area.
          System.out.print(ANSI_BLUE + "\u263A " + ANSI_RESET);
        } else {
          // Calls getBlockSymbol() to get the ANSI color code and character symbol for the current block.
          System.out.print(getBlockSymbol(InitGame.world[x][y]));
        }
      }

      // Move to the next line.
      System.out.println();
    }
  }

  /**
   * Displays the player's inventory in the console.
   */
  public static void displayInventory() {
    System.out.println("Inventory:");
    if (Inventory.inventory.isEmpty()) {
      System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
    } else {
      int[] blockCounts = new int[5];
      for (int i = 0; i < Inventory.inventory.size(); i++) {
        int block = Inventory.inventory.get(i);
        blockCounts[block]++;
      }
      for (int blockType = 1; blockType < blockCounts.length; blockType++) {
        int occurrences = blockCounts[blockType];
        if (occurrences > 0) {
          System.out.println(getBlockName(blockType) + " - " + occurrences);
        }
      }
    }
  }

  /**
   * Returns the ANSI color code for a given block type.
   * @param blockType The type of the block
   * @return The ANSI color code as a string
   */
  public static String getBlockColor(int blockType) {
    switch (blockType) {
      case CraftingUtils.AIR:
        return "";
      case CraftingUtils.WOOD:
        return DisplayUtils.ANSI_RED;
      case CraftingUtils.LEAVES:
        return DisplayUtils.ANSI_GREEN;
      case CraftingUtils.STONE:
        return DisplayUtils.ANSI_GRAY;
      case CraftingUtils.IRON_ORE:
        return DisplayUtils.ANSI_YELLOW;
      default:
        return "";
    }
  }

  /**
   * Displays a legend for block types.
   */
  public static void displayLegend() {
    System.out.println(ANSI_BLUE + "Legend:");
    System.out.println(ANSI_WHITE + "-- - Empty block");
    System.out.println(ANSI_RED + "\u2592\u2592 - Wood block");
    System.out.println(ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
    System.out.println(ANSI_BLUE + "\u2593\u2593 - Stone block");
    System.out.println(ANSI_WHITE + "\u00B0\u00B0 - Iron ore block");
    System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
  }

  /**
   * Returns a colored string representation for a block.
   * @param blockType The type of the block
   * @return A colored string
   */
  public static String getBlockSymbol(int blockType) {
    String blockColor = getBlockColor(blockType);
    return blockColor + getBlockChar(blockType) + " " + ANSI_RESET;
  }

  /**
   * Returns the character representation for a block.
   * @param blockType The type of the block
   * @return A char representing the block
   */
  public static char getBlockChar(int blockType) {
    switch (blockType) {
      case CraftingUtils.WOOD:
        return '\u2592';
      case CraftingUtils.LEAVES:
        return '\u00A7';
      case CraftingUtils.STONE:
        return '\u2593';
      case CraftingUtils.IRON_ORE:
        return '\u00B0';
      default:
        return '-';
    }
  }

  /**
   * Returns the name of a block type.
   * @param blockType The type of the block
   * @return The name of the block as a string
   */
  public static String getBlockName(int blockType) {
    switch (blockType) {
      case CraftingUtils.AIR:
        return "Empty Block";
      case CraftingUtils.WOOD:
        return "Wood";
      case CraftingUtils.LEAVES:
        return "Leaves";
      case CraftingUtils.STONE:
        return "Stone";
      case CraftingUtils.IRON_ORE:
        return "Iron Ore";
      default:
        return "Unknown";
    }
  }
}
