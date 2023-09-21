# function main(String[] args)

```java
initGame(25, 15);
generateWorld();
System.out.println(ANSI_GREEN + "Welcome to Simple Minecraft!" + ANSI_RESET);
System.out.println("Instructions:");
System.out.println(" - Use 'W', 'A', 'S', 'D', or arrow keys to move the player.");
System.out.println(
    " - Press 'M' to mine the block at your position and add it to your inventory.");
System.out.println(" - Press 'P' to place a block from your inventory at your position.");
System.out.println(
    " - Press 'C' to view crafting recipes and 'I' to interact with elements in the world.");
System.out.println(
    " - Press 'Save' to save the game state and 'Load' to load a saved game state.");
System.out.println(" - Press 'Exit' to quit the game.");
System.out.println(" - Type 'Help' to display these instructions again.");
System.out.println();
JavaCraft.scanner = new Scanner(System.in);
System.out.print("Start the game? (Y/N): ");
String startGameChoice = scanner.next().toUpperCase();
if (startGameChoice.equals("Y")) {
    startGame();
} else {
    System.out.println("Game not started. Goodbye!");
}
scanner.close();
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
