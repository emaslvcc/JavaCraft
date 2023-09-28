# function generateWorld()

```java
Random rand = new Random();
for (int y = 0; y < worldHeight; y++) {
    for (int x = 0; x < worldWidth; x++) {
        int randValue = rand.nextInt(100);
        if (randValue < 20) {
            world[x][y] = WOOD;
        } else if (randValue < 35) {^
            world[x][y] = LEAVES;
        } else if (randValue < 50) {
            world[x][y] = STONE;
        } else if (randValue < 70) {
            world[x][y] = IRON_ORE;
        } else {
            world[x][y] = AIR;
        }
    }
}
```

1. Loop through Y-Levels from 0 to `worldHeight`
   1. Loop through X-Levels from 0 to `worldWidth`
      1. Create a random Integer `randValue` from 0 to 99
      2. If `randValue` < 20
         1. Assign WOOD to X and Y coordinates of `world`
      3. If `randValue` < 35
         1. Assign LEAVES to X and Y coordinates of `world`
      4. If `randValue` < 50
         1. Assign STONE to X and Y coordinates of `world`
      5. If `randValue` < 70
         1. Assign IRON_ORE to X and Y coordinates of `world`
      6. Else
         1. Assign AIR to X and Y coordinates of `world`
