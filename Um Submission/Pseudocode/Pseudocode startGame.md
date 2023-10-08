# void startGame()

## Java

```java
public static void startGame() {
    JavaCraft.scanner = new Scanner(System.in);
    boolean unlockMode = false;
    boolean craftingCommandEntered = false;
    boolean miningCommandEntered = false;
    boolean movementCommandEntered = false;
    while (true) {
        clearScreen();
        displayLegend();
        displayWorld();
        displayInventory();
        System.out.println(ANSI_CYAN
                + "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door"
                + ANSI_RESET);
        String input = scanner.next().toLowerCase();
        if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("up")
                || input.equalsIgnoreCase("s") || input.equalsIgnoreCase("down")
                || input.equalsIgnoreCase("a") || input.equalsIgnoreCase("left")
                || input.equalsIgnoreCase("d") || input.equalsIgnoreCase("right")) {
            if (unlockMode) {
                movementCommandEntered = true;
            }
            movePlayer(input);
        } else if (input.equalsIgnoreCase("m")) {
            if (unlockMode) {
                miningCommandEntered = true;
            }
            mineBlock();
        } else if (input.equalsIgnoreCase("p")) {
            displayInventory();
            System.out.print("Enter the block type to place: ");
            int blockType = scanner.nextInt();
            placeBlock(blockType);
        } else if (input.equalsIgnoreCase("c")) {
            displayCraftingRecipes();
            System.out.print("Enter the recipe number to craft: ");
            int recipe = scanner.nextInt();
            craftItem(recipe);
        } else if (input.equalsIgnoreCase("i")) {
            interactWithWorld();
        } else if (input.equalsIgnoreCase("save")) {
            System.out.print("Enter the file name to save the game state: ");
            String fileName = scanner.next();
            saveGame(fileName);
        } else if (input.equalsIgnoreCase("load")) {
            System.out.print("Enter the file name to load the game state: ");
            String fileName = scanner.next();
            loadGame(fileName);
        } else if (input.equalsIgnoreCase("exit")) {
            System.out.println("Exiting the game. Goodbye!");
            break;
        } else if (input.equalsIgnoreCase("look")) {
            lookAround();
        } else if (input.equalsIgnoreCase("unlock")) {
            unlockMode = true;
        } else if (input.equalsIgnoreCase("getflag")) {
            getCountryAndQuoteFromServer();
            waitForEnter();
        } else if (input.equalsIgnoreCase("open")) {
            if (unlockMode && craftingCommandEntered && miningCommandEntered
                    && movementCommandEntered) {
                secretDoorUnlocked = true;
                resetWorld();
                System.out.println("Secret door unlocked!");
                waitForEnter();
            } else {
                System.out.println("Invalid passkey. Try again!");
                waitForEnter();
                unlockMode = false;
                craftingCommandEntered = false;
                miningCommandEntered = false;
                movementCommandEntered = false;
            }
        } else {
            System.out.println(ANSI_YELLOW + "Invalid input. Please try again." + ANSI_RESET);
        }
        if (unlockMode) {
            if (input.equalsIgnoreCase("c")) {
                craftingCommandEntered = true;
            } else if (input.equalsIgnoreCase("m")) {
                miningCommandEntered = true;
            } else if (input.equalsIgnoreCase("open")) {
            }
        }
        if (secretDoorUnlocked) {
            clearScreen();
            System.out.println("You have entered the secret area!");
            System.out.println("You are now presented with a game board with a flag!");
            inSecretArea = true;
            resetWorld();
            secretDoorUnlocked = false;
            fillInventory();
            waitForEnter();
        }
    }
}
```

## Pseudocode

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