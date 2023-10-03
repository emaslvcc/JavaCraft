# void displayInventory()

```java
public static void displayInventory() {
    System.out.println("Inventory:");
    if (inventory.isEmpty()) {
        System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
    } else {
        int[] blockCounts = new int[7];
        for (int i = 0; i < inventory.size(); i++) {
            int block = inventory.get(i);
            blockCounts[block]++;
        }
        for (int blockType = 1; blockType < blockCounts.length; blockType++) {
            int occurrences = blockCounts[blockType];
            if (occurrences > 0) {
                System.out.println(getBlockName(blockType) + " - " + occurrences);
            }
        }
    }
    System.out.println("Crafted Items:");
    if (craftedItems == null || craftedItems.isEmpty()) {
        System.out.println(ANSI_YELLOW + "None" + ANSI_RESET);
    } else {
        for (int item : craftedItems) {
            System.out.print(
                    getCraftedItemColor(item) + getCraftedItemName(item) + ", " + ANSI_RESET);
        }
        System.out.println();
    }
    System.out.println();
}
```

method displayInventory
print "Inventory"
if inventory is empty, print a yellow block + "Empty" + an empty line

else 
create an empty array named blockCounts with five available spots, because there are five blocks in the game

for each item in the inventory array, 
	get the item from the inventory,
	then add one to the corresponding spot in the array.

print "Crafted Items:"

if crafted items list is null or the crafted item list is empty then

print a yellow block + "none" + two empty lines

else
for each item in craftedItems list print corresponding colour of that item + name of the item + "," + two empty lines



