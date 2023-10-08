# void craftStick()

## Java

```java
public static void craftStick() {
    if (inventoryContains(WOOD)) {
        removeItemsFromInventory(WOOD, 1);
        addCraftedItem(CRAFTED_STICK);
        System.out.println("Crafted Stick.");
    } else {
        System.out.println("Insufficient resources to craft Stick.");
    }
}
```

## Pseudocode

```java
BEGIN

IF <list> inventory contains wood
    Remove 1 wood from <list> inventory;
    Add the crafted item 1 stick to <list> inventory;
    PRINT INFO "Crafted Stick.\n";
ELSE
    PRINT WARNING "Insufficient resources to craft Stick.\n";

END
```
