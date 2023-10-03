# void craftItem(int recipe)

```java
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
    waitForEnter();
}
```

1. Compare `recipe` parameter to valid recipes
   1. If it matches Wooden Planks, craft Wooden Planks
   2. If it matches Stick, craft Stick
   3. If it matches Iron Ingot, craft Iron Ingot
   4. By default print INFO "Invalid recipe number."
2.  Wait for player to press ENTER
