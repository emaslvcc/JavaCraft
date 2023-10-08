# void initGame(int worldWidth, int worldHeight)

## Java

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

## Pseudocode

```java
BEGIN

Set `<Integer> blockCounts = 1;
Set `<Integer> blockCounts = 1;
Set `<Integer> blockCounts = 1;
Set `<Integer> blockCounts = 1;
Set `<Integer> blockCounts = 1;
Set `<Integer> blockCounts = 1;

END
```

## Flowchart

<img src="./src/flowchart-initGame.svg" alt="flowchart-initGame.svg" width="600"/>
