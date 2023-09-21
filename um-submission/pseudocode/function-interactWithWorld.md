# function interactWithWorld()

```java
int blockType = world[playerX][playerY];
switch (blockType) {
    case WOOD:
        System.out.println("You gather wood from the tree.");
        inventory.add(WOOD);
        break;
    case LEAVES:
        System.out.println("You gather leaves from the tree.");
        inventory.add(LEAVES);
        break;
    case STONE:
        System.out.println("You gather stones from the ground.");
        inventory.add(STONE);
        break;
    case IRON_ORE:
        System.out.println("You mine iron ore from the ground.");
        inventory.add(IRON_ORE);
        break;
    case AIR:
        System.out.println("Nothing to interact with here.");
        break;
    default:
        System.out.println("Unrecognized block. Cannot interact.");
}
waitForEnter();
```

1. **TRY TO:** Create an inputstream from parameter fileName for the corresponding file to deserialize its data.

   **CATCH:** for ClassNotFoundException or IOException: Print "Error while loading the game state: `<errormessage from exception>`"
   1. Get the new world width as `JavaCraft.NEW_WORLD_WIDTH` from the created inputstream
   2. Get the new world height as `JavaCraft.NEW_WORLD_HEIGHT` from the created inputstream
   3. Get the game world as `JavaCraft.world` from the created inputstream
   4. Get the players X position as `JavaCraft.playerX` from the created inputstream
   5. Get the players Y position as `JavaCraft.playerY` from the created inputstream
   6. Get the players inventory as `JavaCraft.inventory` from the created inputstream
   7. Get the players crafted items as `JavaCraft.craftedItems` from the created inputstream
   8. Get the value of the unlock mode as `JavaCraft.unlockMode` from the created inputstream
2.  Print "Game state loaded from file: `<fileName>`"
3.  Wait for player to press ENTER
