# void loadGame(String fileName)

## Java

```java
public static void loadGame(String fileName) {
    // Implementation for loading the game state from a file goes here
    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
        // Deserialize game state data from the file and load it into the program
        NEW_WORLD_WIDTH = inputStream.readInt();
        NEW_WORLD_HEIGHT = inputStream.readInt();
        world = (int[][]) inputStream.readObject();
        playerX = inputStream.readInt();
        playerY = inputStream.readInt();
        inventory = (List<Integer>) inputStream.readObject();
        craftedItems = (List<Integer>) inputStream.readObject();
        unlockMode = inputStream.readBoolean();
        System.out.println("Game state loaded from file: " + fileName);
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error while loading the game state: " + e.getMessage());
    }
    waitForEnter();
}
```

## Pseudocode

1. **TRY TO:** Create an `inputstream` from parameter `fileName` for the corresponding file to deserialize its data.

   **CATCH:** for ClassNotFoundException or IOException: Print ERROR "Error while loading the game state: `<errormessage from exception>`"
   1. Get the new world width as global variable `NEW_WORLD_WIDTH` from the created `inputstream`
   2. Get the new world height as global variable `NEW_WORLD_HEIGHT` from the created `inputstream`
   3. Get the game world as global variable `world` from the created `inputstream`
   4. Get the players X position as global variable `playerX` from the created `inputstream`
   5. Get the players Y position as global variable `playerY` from the created `inputstream`
   6. Get the players inventory asglobal variable  `inventory` from the created `inputstream`
   7. Get the players crafted items as global variable `craftedItems` from the created `inputstream`
   8. Get the value of the unlock mode as global variable `unlockMode` from the created `inputstream`
   9.  Print INFO "Game state loaded from file: `<fileName>`"
2.  Wait for player to press ENTER
