/**
 * BlockInteractionUtils
 *
 * <p>
 * The {@code BlockInteractionUtils} class is responsible for encapsulating the logic
 * related to the player's interaction with blocks in the JavaCraft game world.
 * </p>
 *
 * <p>
 * It provides static methods for both mining and placing blocks, in coordination with the
 * {@code CraftingUtils} and {@code DisplayUtils} classes to provide a seamless gameplay experience.
 * </p>
 *
 * @author  Group 49
 * @version 1.0
 * @see     CraftingUtils
 * @see     DisplayUtils
 */
package com.javacraft.utils;

import com.javacraft.main.*;
import com.javacraft.player.*;

public class BlockInteracttionUtils {

  /**
   * Mines a block at the player's current position and adds it to the inventory.
   */
  public static void mineBlock() {
    int blockType =
      InitGame.world[PlayerMovement.playerX][PlayerMovement.playerY];
    if (blockType != CraftingUtils.AIR) {
      Inventory.inventory.add(blockType);
      InitGame.world[PlayerMovement.playerX][PlayerMovement.playerY] =
        CraftingUtils.AIR;
      System.out.println("Mined " + DisplayUtils.getBlockName(blockType) + ".");
    } else {
      System.out.println("No block to mine here.");
    }
    JavaCraft.waitForEnter();
  }

  /**
   * Places a block of a given type at the player's current position.
   *
   * @param blockType The type of block to be placed.
   */
  public static void placeBlock(int blockType) {
    if (blockType >= 0 && blockType <= 7) {
      if (blockType <= 4) {
        if (Inventory.inventory.contains(blockType)) {
          Inventory.inventory.remove(Integer.valueOf(blockType));
          InitGame.world[PlayerMovement.playerX][PlayerMovement.playerY] =
            blockType;
          System.out.println(
            "Placed " +
            DisplayUtils.getBlockName(blockType) +
            " at your position."
          );
        } else {
          System.out.println(
            "You don't have " +
            DisplayUtils.getBlockName(blockType) +
            " in your inventory."
          );
        }
      } else {
        int craftedItem = CraftingUtils.getCraftedItemFromBlockType(blockType);
        if (CraftingUtils.craftedItems.contains(craftedItem)) {
          CraftingUtils.craftedItems.remove(Integer.valueOf(craftedItem));
          InitGame.world[PlayerMovement.playerX][PlayerMovement.playerY] =
            blockType;
          System.out.println(
            "Placed " +
            CraftingUtils.getCraftedItemName(craftedItem) +
            " at your position."
          );
        } else {
          System.out.println(
            "You don't have " +
            CraftingUtils.getCraftedItemName(craftedItem) +
            " in your crafted items."
          );
        }
      }
    } else {
      System.out.println(
        "Invalid block number. Please enter a valid block number."
      );
      System.out.println(CraftingUtils.BLOCK_NUMBERS_INFO);
    }
    JavaCraft.waitForEnter();
  }
}
