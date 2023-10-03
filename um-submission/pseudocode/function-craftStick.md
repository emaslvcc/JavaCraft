# void craftStick()

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

{
if (inventory contains `1 x WOOD`) {

remove `1 x WOOD` from inventory
add `1 x STICKS` to inventory
Print "Crafted Sticks." to the console

} else {

Print "Insufficient resources to craft Sticks." to the console

}
}