# void fillInventory()

```java
private static void fillInventory() {
    inventory.clear();
    for (int blockType = 1; blockType <= 6; blockType++) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            inventory.add(blockType);
        }
    }
}
```

clear Inventory
Loop for all uncraftable blockTypes wich are 4 beginning with blockType 1.
loop for the length of the Inventory wich is a 100.
add blockType from the first loop to the inventory.