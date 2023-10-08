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
ELSE
    PRINT WARNING "Invalid recipe number.\n";
Wait on player to press ENTER;

END
```
