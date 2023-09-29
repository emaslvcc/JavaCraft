import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Inventory {
    private static List<Integer> inventory;
    private static List<Integer> craftedItems;

    public Inventory() {
        inventory = new ArrayList<>();
        craftedItems = new ArrayList<>();
    }

    public List<Integer> getInventory() {
        return inventory;
    }

    public void setInventory(List<Integer> inventoryStream) {
        inventory = inventoryStream;
    }

    public List<Integer> getCraftedItems() {
        return craftedItems;
    }

    public void setCraftedItems(List<Integer> craftedItemsStream) {
        craftedItems = craftedItemsStream;
    }


    public void displayInventory() {
        System.out.println("Inventory:");
        if (inventory.isEmpty()) {
            System.out.println(GameValues.ANSI_YELLOW + "Empty" + GameValues.ANSI_RESET);
        } else {
            int[] blockCounts = new int[8];
            for (int block : inventory) {
                blockCounts[block]++;
            }
            for (int blockType = 1; blockType < blockCounts.length; blockType++) {
                int occurrences = blockCounts[blockType];
                if (occurrences > 0) {
                    System.out.println(Blocks.getBlockName(blockType) + " - " + occurrences);
                }
            }
        }
        System.out.println("Crafted Items:");
        if (craftedItems == null || craftedItems.isEmpty()) {
            System.out.println(GameValues.ANSI_YELLOW + "None" + GameValues.ANSI_RESET);
        } else {
            for (int item : craftedItems) {
                System.out.print(Crafting.getCraftedItemColor(item) + Crafting.getCraftedItemName(item) + ", " + GameValues.ANSI_RESET);
            }
            System.out.println();
        }
        System.out.println();
    }


    public void addCraftedItem(int item) {
        craftedItems.add(item);
    }
    public void removeCraftedItem(int item) {
        craftedItems.remove(item);
    }
    public boolean containsCraftedItem(int item) {
        return craftedItems.contains(item);
    }


    public void clearInventory() {
        inventory.clear();
    }

    public void addItem(int item) {
        inventory.add(item);
    }

    public void removeItem(int item) {
        inventory.remove(item);
    }

    public boolean containsItem(int item) {
        return inventory.contains(item);
    }

    public boolean containsItemOfNumber(int item, int count) {
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

    public void removeItems(int item, int count) {
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

}
