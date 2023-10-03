# void craftWoodenPlanks()

```java
public static void craftWoodenPlanks() {
    if (inventoryContains(WOOD, 2)) {
        removeItemsFromInventory(WOOD, 2);
        addCraftedItem(CRAFTED_WOODEN_PLANKS);
        System.out.println("Crafted Wooden Planks.");
    } else {
        System.out.println("Insufficient resources to craft Wooden Planks.");
    }
}
```

{
if (inventory contains `2 x WOOD`) {

remove `2 x WOOD` from inventory
add `1 x WOODEN_PLANKS` to inventory
Print Crafted Wooden Planks. to the console

} else {

Print Insufficient resources to craft Wooden Planks. to the console

}
}
