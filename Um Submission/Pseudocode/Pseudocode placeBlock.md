# void placeBlock(int blockType)

## Java

```java
public static void placeBlock(int blockType) {
    if (blockType >= 0 && blockType <= 11) {
        if (blockType <= 6) {
            if (inventory.contains(blockType)) {
                inventory.remove(Integer.valueOf(blockType));
                world[playerX][playerY] = blockType;
                System.out.println("Placed " + getBlockName(blockType) + " at your position.");
            } else {
                System.out.println(
                        "You don't have " + getBlockName(blockType) + " in your inventory.");
            }
        } else {
            int craftedItem = getCraftedItemFromBlockType(blockType);
            if (craftedItems.contains(craftedItem)) {
                craftedItems.remove(Integer.valueOf(craftedItem));
                world[playerX][playerY] = blockType;
                System.out.println(
                        "Placed " + getCraftedItemName(craftedItem) + " at your position.");
            } else {
                System.out.println("You don't have " + getCraftedItemName(craftedItem)
                        + " in your crafted items.");
            }
        }
    } else {
        System.out.println("Invalid block number. Please enter a valid block number.");
        System.out.println(BLOCK_NUMBERS_INFO);
    }
    waitForEnter();
}
```

## Pseudocode

Check if the blocktype is bigger or equal to 0 and smaller or equal to 7.
    If that is true check if the blocktype is not a Craftable Block, it is smaller or equal to 4.
        If that is true check if the Inventory contains the blockType.
            If yes, then remove the blockType from the Inventory.
            Then place the Block at the coordinates of the Player in the World.
            Print that the specific Block is placed at your position.
        Else If the Blocktype is not in your Inventory.
            Print that you dont have the Blocktype in your Inventory.
    Else If the Blocktype is a Craftable Block, it is bigger then 4.
        Returns the int craftedItem from the blocktype.
        If the List craftedItems contains the before returned craftedItem.
            Then remove the craftedItem from the List craftedItems.
            Place the blockType at the coordinates of the Player in the Worls.
            Print that you Placed the craftedItem with the Name of that Itemat your Location.
        Else if the List does not contain the craftedItem.
            Print that you don't have that Item in your craftedItem List.
Else If the Number is not between or equal to 0 or smaller or equal to 7.
    Print that the block Number is Invalid and enter a valid block number.
    Print Info about the blocks with theyr Name and associated number.
Start the Function waitForEnter.