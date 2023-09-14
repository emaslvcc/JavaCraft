/**
 * CraftingUtils
 *
 * <p>
 * The {@code CraftingUtils} class manages the crafting mechanics within the JavaCraft game.
 * It holds methods for crafting items, displaying recipes, and other related functionalities.
 * </p>
 *
 * <p>
 * The class defines multiple constants that represent the types of blocks and crafting recipes available in the game.
 * </p>
 *
 * @author  Group 49
 * @version 1.0
 */
package com.javacraft.utils;

import com.javacraft.main.*;
import com.javacraft.player.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CraftingUtils {

  // Constants and variables declaration
  public static final int AIR = 0;
  public static final int WOOD = 1;
  public static final int LEAVES = 2;
  public static final int STONE = 3;
  public static final int IRON_ORE = 4;

  public static List<Integer> craftedItems = new ArrayList<>();
  public static final int CRAFT_WOODEN_PLANKS = 100;
  public static final int CRAFT_STICK = 101;
  public static final int CRAFT_IRON_INGOT = 102;
  public static final int CRAFTED_WOODEN_PLANKS = 200;
  public static final int CRAFTED_STICK = 201;
  public static final int CRAFTED_IRON_INGOT = 202;

  public static final String BLOCK_NUMBERS_INFO =
    "Block Numbers:\n" +
    "0 - Empty block\n" +
    "1 - Wood block\n" +
    "2 - Leaves block\n" +
    "3 - Stone block\n" +
    "4 - Iron ore block\n" +
    "5 - Wooden Planks (Crafted Item)\n" +
    "6 - Stick (Crafted Item)\n" +
    "7 - Iron Ingot (Crafted Item)";

  /**
   * Display the available crafting recipes to the console.
   */
  public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
  }

  /**
   * Craft an item based on the chosen recipe.
   *
   * @param recipe The chosen recipe to craft an item.
   */
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
      default:
        System.out.println("Invalid recipe number.");
    }
    JavaCraft.waitForEnter();
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
    return Inventory.inventory.contains(item);
  }

  public static boolean inventoryContains(int item, int count) {
    int itemCount = 0;
    for (int i : Inventory.inventory) {
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
    Iterator<Integer> iterator = Inventory.inventory.iterator();
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

  public static void craftWoodenPlanks() {
    if (inventoryContains(WOOD, 2)) {
      removeItemsFromInventory(WOOD, 2);
      addCraftedItem(CRAFTED_WOODEN_PLANKS);
      System.out.println("Crafted Wooden Planks.");
    } else {
      System.out.println("Insufficient resources to craft Wooden Planks.");
    }
  }

  public static void addCraftedItem(int craftedItem) {
    if (craftedItems == null) {
      craftedItems = new ArrayList<>();
    }
    craftedItems.add(craftedItem);
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

  public static int getCraftedItemFromBlockType(int blockType) {
    switch (blockType) {
      case 5:
        return CraftingUtils.CRAFTED_WOODEN_PLANKS;
      case 6:
        return CraftingUtils.CRAFTED_STICK;
      case 7:
        return CraftingUtils.CRAFTED_IRON_INGOT;
      default:
        return -1;
    }
  }

  public static String getCraftedItemName(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
        return "Wooden Planks";
      case CRAFTED_STICK:
        return "Stick";
      case CRAFTED_IRON_INGOT:
        return "Iron Ingot";
      default:
        return "Unknown";
    }
  }

  public static String getCraftedItemColor(int craftedItem) {
    switch (craftedItem) {
      case CRAFTED_WOODEN_PLANKS:
      case CRAFTED_STICK:
      case CRAFTED_IRON_INGOT:
        return DisplayUtils.ANSI_BROWN;
      default:
        return "";
    }
  }
}
