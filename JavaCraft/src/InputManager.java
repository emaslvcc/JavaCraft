import java.util.Scanner;

public class InputManager {
    private static boolean unlockMode = false;

    public static void waitForEnter() {
        System.out.println("Press Enter to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static void getInput(String input, Scanner scanner) {
        if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("up") ||
                input.equalsIgnoreCase("s") || input.equalsIgnoreCase("down") ||
                input.equalsIgnoreCase("a") || input.equalsIgnoreCase("left") ||
                input.equalsIgnoreCase("d") || input.equalsIgnoreCase("right")) {
            if (unlockMode) {
                SecretDoor.setMovementCommandEntered(true);
            }
            Player.movePlayer(input);
        } else if (input.equalsIgnoreCase("m")) {
            if (unlockMode) {
                SecretDoor.setMiningCommandEntered(true);
            }
            Player.mineBlock();
        } else if (input.equalsIgnoreCase("p")) {
            GameLoop.inventoryManager.displayInventory();
            System.out.print("Enter the block type to place: ");
            int blockType = scanner.nextInt();
            Player.placeBlock(blockType);
        } else if (input.equalsIgnoreCase("c")) {
            Crafting.displayCraftingRecipes();
            System.out.print("Enter the recipe number to craft: ");
            int recipe = scanner.nextInt();
            Crafting.craftItem(recipe);
        } else if (input.equalsIgnoreCase("i")) {
            Player.interactWithWorld();
        } else if (input.equalsIgnoreCase("save")) {
            System.out.print("Enter the file name to save the game state: ");
            String fileName = scanner.next();
            GameLoop.saveGame(fileName);
        } else if (input.equalsIgnoreCase("load")) {
            System.out.print("Enter the file name to load the game state: ");
            String fileName = scanner.next();
            GameLoop.loadGame(fileName);
        } else if (input.equalsIgnoreCase("exit")) {
            System.out.println("Exiting the game. Goodbye!");
            return;
        } else if (input.equalsIgnoreCase("look")) {
            Player.lookAround();
        } else if (input.equalsIgnoreCase("unlock")) {
            unlockMode = true;
        } else if (input.equalsIgnoreCase("getflag")) {
            ServerInteraction.getCountryAndQuoteFromServer();
            waitForEnter();
        } else if (input.equalsIgnoreCase("open")) {
            if (unlockMode && SecretDoor.isCraftingCommandEntered() && SecretDoor.isMiningCommandEntered() && SecretDoor.isMovementCommandEntered()) {
                GameLoop.setSecretDoorUnlocked(true);
                GameLoop.resetWorld();
                System.out.println("Secret door unlocked!");
                waitForEnter();
            } else {
                System.out.println("Invalid passkey. Try again!");
                waitForEnter();
                SecretDoor.Reset();
            }
        } else {
            System.out.println(GameValues.ANSI_YELLOW + "Invalid input. Please try again." + GameValues.ANSI_RESET);
        }
        if (unlockMode) {
            if (input.equalsIgnoreCase("c")) {
                SecretDoor.setCraftingCommandEntered(true);
            } else if (input.equalsIgnoreCase("m")) {
                SecretDoor.setMiningCommandEntered(true);
            } else if (input.equalsIgnoreCase("open")) {
                SecretDoor.setOpenCommandEntered(true);
            }
        }
    }
}
