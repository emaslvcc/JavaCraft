// ****************************************************************************************************
package com.javacraft.player;

// ****************************************************************************************************
/**
 * Manages the player's inventory, including item storage and crafting.
 *
 * The Inventory class is responsible for managing the items and blocks stored in the player's backpack.
 * It provides methods for filling the inventory with default items, crafting items, and checking inventory contents.
 *
 * @author Group49
 * @version 1.0
 * @see CraftingUtils
 */
import com.javacraft.utils.CraftingUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Inventory {

  /**
   * Player's inventory represented as a list of integers, each integer representing a block type.
   */
  public static List<Integer> inventory = new ArrayList<>(); // Player's inventory
  /**
   * Maximum number of items the inventory can hold.
   */
  public static final int INVENTORY_SIZE = 100; // Maximum inventory size

  /**
   * Fills the player's inventory with default block types.
   * Initializes all slots to contain a basic set of blocks.
   *
   * @see #inventory
   * @see #INVENTORY_SIZE
   */
  public static void fillInventory() {
    inventory.clear();
    for (int blockType = 1; blockType <= 4; blockType++) {
      for (int i = 0; i < INVENTORY_SIZE; i++) {
        inventory.add(blockType);
      }
    }
  }

  /**
   * Translates a crafted item's ID to its corresponding block type ID.
   *
   * @param craftedItem The ID of the crafted item.
   * @return The ID of the corresponding block type.
   * @see CraftingUtils
   */
  public static int getBlockTypeFromCraftedItem(int craftedItem) {
    switch (craftedItem) {
      case CraftingUtils.CRAFTED_WOODEN_PLANKS:
        return 5;
      case CraftingUtils.CRAFTED_STICK:
        return 6;
      case CraftingUtils.CRAFTED_IRON_INGOT:
        return 7;
      default:
        return -1;
    }
  }

  /**
   * Crafts wooden planks if resources are sufficient.
   * Because who doesn't love a good plank?
   */
  public static void craftWoodenPlanks() {
    if (inventoryContains(CraftingUtils.WOOD, 2)) {
      removeItemsFromInventory(CraftingUtils.WOOD, 2);
      addCraftedItem(CraftingUtils.CRAFTED_WOODEN_PLANKS);
      System.out.println("Crafted Wooden Planks.");
    } else {
      System.out.println("Insufficient resources to craft Wooden Planks.");
    }
  }

  /**
   * Crafts a stick if resources are sufficient.
   * Time to make your point... stick.
   */
  public static void craftStick() {
    if (inventoryContains(CraftingUtils.WOOD)) {
      removeItemsFromInventory(CraftingUtils.WOOD, 1);
      addCraftedItem(CraftingUtils.CRAFTED_STICK);
      System.out.println("Crafted Stick.");
    } else {
      System.out.println("Insufficient resources to craft Stick.");
    }
  }

  /**
   * Crafts an iron ingot if resources are sufficient.
   * So you can finally be iron man... sort of.
   */
  public static void craftIronIngot() {
    if (inventoryContains(CraftingUtils.IRON_ORE, 3)) {
      removeItemsFromInventory(CraftingUtils.IRON_ORE, 3);
      addCraftedItem(CraftingUtils.CRAFTED_IRON_INGOT);
      System.out.println("Crafted Iron Ingot.");
    } else {
      System.out.println("Insufficient resources to craft Iron Ingot.");
    }
  }

  /**
   * Checks if an item exists in the inventory.
   *
   * @param item The ID of the item to search for
   * @return true if the item exists, false otherwise
   */
  public static boolean inventoryContains(int item) {
    return inventory.contains(item);
  }

  /**
   * Checks if a specific number of an item exists in the inventory.
   *
   * @param item  The ID of the item to search for
   * @param count The number of instances of the item
   * @return true if enough instances exist, false otherwise
   */
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

  /**
   * Removes a specified number of items from the inventory.
   *
   * @param item  The ID of the item to remove
   * @param count The number of instances to remove
   */
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

  /**
   * Adds a crafted item to the crafted items list.
   * Because you're not a true crafter until you add it to the list.
   *
   * @param craftedItem The ID of the crafted item to add
   */
  public static void addCraftedItem(int craftedItem) {
    if (CraftingUtils.craftedItems == null) {
      CraftingUtils.craftedItems = new ArrayList<>();
    }
    CraftingUtils.craftedItems.add(craftedItem);
  }
}
