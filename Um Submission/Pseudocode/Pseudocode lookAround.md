# void lookAround()

## Java

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

## Pseudocode

```java
BEGIN



END
```

Print "You look around and see: "
for y = max of player Y - 1 or 0 ,until y <= min of playerY - 1 or
worldHeight - 1, y++
	for x + max of player X - 1 or 0, until y <= min of playerX + 1 or worldWidth - 1, x++
		if x == playerX && y == playerY	
			Print the player icon in Green then reset the color for future inputs
		else Print the blockType for the coordinates x and y
	Print nothing
Print nothing
waitForEnter