# void initGame(int worldWidth, int worldHeight)

```java
public static void initGame(int worldWidth, int worldHeight) {
    JavaCraft.worldWidth = worldWidth;
    JavaCraft.worldHeight = worldHeight;
    JavaCraft.world = new int[worldWidth][worldHeight];
    JavaCraft.playerX = worldWidth / 2;
    JavaCraft.playerY = worldHeight / 2;
    JavaCraft.inventory = new ArrayList<>();
}
```

create method initGame with the variables worldHeight and worldWidth as input
	set worldWidth to the worldWidth variable of the JavaCraft class
	set worldHeight to the worldHeight variable of the JavaCraft class
	create a two dimensional array, the first dimension has the size of the worldWidth
	variable, the second dimensional has the size of the worldHeight variable.
	divide worldWidth by 2 and set it this value to the playerX value of the JavaCraft class
	divide worldHeight by 2 and set it this value to the playerY value of the JavaCraft class
	create an list and assign it to the inventory variable of the JavaCraft class
