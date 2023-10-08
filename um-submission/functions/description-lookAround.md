### void lookAround()

#### Java

```java
private static void lookAround() {
    System.out.println("You look around and see:");
    for (int y = Math.max(0, playerY - 1); y <= Math.min(playerY + 1, worldHeight - 1); y++) {
        for (int x = Math.max(0, playerX - 1); x <= Math.min(playerX + 1,
                worldWidth - 1); x++) {
            if (x == playerX && y == playerY) {
                System.out.print(ANSI_GREEN + "P " + ANSI_RESET);
            } else {
                System.out.print(getBlockSymbol(world[x][y]) + ANSI_RESET);
            }
        }
        System.out.println();
    }
    System.out.println();
    waitForEnter();
}
```

#### Pseudocode

```java
BEGIN

PRINT INFO "You look around and see:";
FOR `<Integer> y` = `Maximum {of} 0 and {<Integer> playerY - 1}`; `<Integer> y` <= `Minimum of {<Integer> playerY + 1} and {<Integer> worldHeight - 1}`
    FOR `<Integer> x` = `Maximum of {0} and {<Integer> playerX - 1}`; `<Integer> x` <= `Minimum of {<Integer> playerX + 1} and {<Integer> worldWidth - 1}`
        IF `<Integer> x` == `<Integer> playerX` AND `<Integer> y` == `<Integer> playerY`
            PRINT INFO "P " (colored green);
        ELSE
            PRINT INFO `get block symbol from <two dimensional Integer array> world @ indexes <Integer> x, <Integer> y`;
        Set `<Integer> x` += 1;
    PRINT INFO "\n";
    Set `<Integer> y` += 1;
PRINT INFO "\n";
Wait on player to press ENTER;

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-lookAround.svg" alt="flowchart-lookAround.svg"/>
