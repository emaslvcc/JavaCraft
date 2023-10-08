# void craftItem(int recipe)

## Java

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
        case 4:
            craftStonePickaxe();
            break;
        case 5:
            craftIronPickaxe();
            break;
        default:
            System.out.println("Invalid recipe number.");
    }
    waitForEnter();
}
```

## Pseudocode

```java
BEGIN

IF <Integer> recipe == 1
    Craft wooden planks;
ELSE IF <Integer> recipe == 2
    Craft stick;
ELSE IF <Integer> recipe == 3
    Craft iron ingot;
ELSE IF <Integer> recipe == 4
    Craft stone pickaxe;
ELSE IF <Integer> recipe == 5
    Craft iron pickaxe;
ELSE
    PRINT WARNING "Invalid recipe number.\n";
Wait on player to press ENTER;

END
```
