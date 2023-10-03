# void craftIronIngot()

```java
public static void craftIronIngot() {
    if (inventoryContains(IRON_ORE, 3)) {
        removeItemsFromInventory(IRON_ORE, 3);
        addCraftedItem(CRAFTED_IRON_INGOT);
        System.out.println("Crafted Iron Ingot.");
    } else {
        System.out.println("Insufficient resources to craft Iron Ingot.");
    }
}
```
{
if (inventory contains `3 x IRON_ORE`) {

remove `3 x IRON_ORE` from inventory
add `1 x IRON_INGOT` to inventory
Print "Crafted Iron Ingot." to the console

} else {

Print "Insufficient resources to craft Iron Ingot." to the console

}
}