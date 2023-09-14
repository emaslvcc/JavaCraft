// ****************************************************************************************************
package com.javacraft.main;

// ****************************************************************************************************

/**
 * JavaCraft - The Core Game Loop and Input Handling.
 * <p>
 * This class serves as the central hub for managing the game. It initiates the game world, handles user inputs,
 * and manages the game state.
 * </p>
 * <ul>
 *  <li>Initialize and start the game.</li>
 *  <li>Handle user input for movement, crafting, etc.</li>
 *  <li>Execute the main game loop.</li>
 * </ul>
 *
 * @see com.javacraft.player.PlayerMovement
 * @see com.javacraft.server.ServerInteraction
 * @see com.javacraft.utils.DisplayUtils
 * @see com.javacraft.utils.InitGame
 *
 * @author Group 49
 * @version 1.0
 */
// Import dependencies
import com.javacraft.player.*;
import com.javacraft.server.*;
import com.javacraft.utils.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavaCraft {

  /**
   * The 2D array representing the game world.
   */
  public static int[][] world;

  /**
   * The list representing the player's inventory.
   */
  public static List<Integer> inventory = new ArrayList<>();

  /**
   * Main method that serves as the entry point for the application.
   *
   * @param args Arguments passed from the command line.
   * @see com.javacraft.utils.InitGame#initGame(int, int)
   * @see com.javacraft.utils.InitGame#generateWorld()
   * @see com.javacraft.utils.DisplayUtils
   */
  public static void main(String[] args) {
    InitGame.initGame(25, 15);
    System.out.println("World Dimenstion" + InitGame.worldWidth);
    InitGame.generateWorld();
    System.out.println(
      DisplayUtils.ANSI_GREEN +
      "Welcome to Simple Minecraft!" +
      DisplayUtils.ANSI_RESET
    );
    System.out.println("Instructions:");
    System.out.println(
      " - Use 'W', 'A', 'S', 'D', or arrow keys to move the player."
    );
    System.out.println(
      " - Press 'M' to mine the block at your position and add it to your inventory."
    );
    System.out.println(
      " - Press 'P' to place a block from your inventory at your position."
    );
    System.out.println(
      " - Press 'C' to view crafting recipes and 'I' to interact with elements in the world."
    );
    System.out.println(
      " - Press 'Save' to save the game state and 'Load' to load a saved game state."
    );
    System.out.println(" - Press 'Exit' to quit the game.");
    System.out.println(" - Type 'Help' to display these instructions again.");
    System.out.println();
    Scanner scanner = new Scanner(System.in);
    System.out.print("Start the game? (Y/N): ");
    String startGameChoice = scanner.next().toUpperCase();
    if (startGameChoice.equals("Y")) {
      startGame();
    } else {
      System.out.println("Game not started. Goodbye!");
    }
  }

  /**
   * The Main game loop, which accepts player commands.
   *
   * @see com.javacraft.player.PlayerMovement#movePlayer(String)
   * @see com.javacraft.utils.BlockInteractionUtils#mineBlock()
   * @see com.javacraft.utils.BlockInteractionUtils#placeBlock(int)
   * @see com.javacraft.utils.CraftingUtils#craftItem(int)
   * @see com.javacraft.utils.SaveAndLoadGame#saveGame(String)
   * @see com.javacraft.utils.SaveAndLoadGame#loadGame(String)
   * @see com.javacraft.utils.DisplayUtils
   */
  public static void startGame() {
    Scanner scanner = new Scanner(System.in);
    boolean unlockMode = false;
    boolean craftingCommandEntered = false;
    boolean miningCommandEntered = false;
    boolean movementCommandEntered = false;
    boolean openCommandEntered = false;
    while (true) {
      clearScreen();
      DisplayUtils.displayLegend();
      DisplayUtils.displayWorld();
      DisplayUtils.displayInventory();
      System.out.println(
        DisplayUtils.ANSI_CYAN +
        "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door" +
        DisplayUtils.ANSI_RESET
      );

      String input = scanner.nextLine().toLowerCase();
      if (
        input.equals("w") ||
        input.equals("up") ||
        input.equals("s") ||
        input.equals("down") ||
        input.equals("a") ||
        input.equals("left") ||
        input.equals("d") ||
        input.equals("right")
      ) {
        if (unlockMode) {
          movementCommandEntered = true;
        }
        PlayerMovement.movePlayer(input);
      } else if (input.equals("m")) {
        if (unlockMode) {
          miningCommandEntered = true;
        }
        BlockInteracttionUtils.mineBlock();
      } else if (input.equals("p")) {
        DisplayUtils.displayInventory();
        System.out.print("Enter the block type to place: ");
        int blockType = scanner.nextInt();
        BlockInteracttionUtils.placeBlock(blockType);
      } else if (input.equals("c")) {
        CraftingUtils.displayCraftingRecipes();
        System.out.print("Enter the recipe number to craft: ");
        int recipe = scanner.nextInt();
        CraftingUtils.craftItem(recipe);
      } else if (input.equals("i")) {
        PlayerMovement.interactWithWorld();
      } else if (input.equals("save")) {
        System.out.print("Enter the file name to save the game state: ");
        String fileName = scanner.next();
        SaveAndLoadGame.saveGame(fileName);
      } else if (input.equals("load")) {
        System.out.print("Enter the file name to load the game state: ");
        String fileName = scanner.next();
        SaveAndLoadGame.loadGame(fileName);
      } else if (input.equals("exit")) {
        System.out.println("Exiting the game. Goodbye!");
        break;
      } else if (input.equals("look")) {
        PlayerMovement.lookAround();
      } else if (input.equals("unlock")) {
        unlockMode = true;
      } else if (input.equals("getflag")) {
        ServerInteraction.getCountryAndQuoteFromServer();
        waitForEnter();
      } else if (input.equals("open")) {
        if (
          unlockMode &&
          craftingCommandEntered &&
          miningCommandEntered &&
          movementCommandEntered
        ) {
          PlayerMovement.secretDoorUnlocked = true;
          InitGame.resetWorld();
          System.out.println("Secret door unlocked!");
          waitForEnter();
        } else {
          System.out.println("Invalid passkey. Try again!");
          waitForEnter();
          unlockMode = false;
          craftingCommandEntered = false;
          miningCommandEntered = false;
          movementCommandEntered = false;
          openCommandEntered = false;
        }
      } else {
        System.out.println(
          DisplayUtils.ANSI_YELLOW +
          "Invalid input. Please try again." +
          DisplayUtils.ANSI_RESET
        );
      }
      if (unlockMode) {
        if (input.equals("c")) {
          craftingCommandEntered = true;
        } else if (input.equals("m")) {
          miningCommandEntered = true;
        } else if (input.equals("open")) {
          openCommandEntered = true;
        }
      }
      if (PlayerMovement.secretDoorUnlocked) {
        clearScreen();
        System.out.println("You have entered the secret area!");
        System.out.println(
          "You are now presented with a game board with a flag!"
        );
        PlayerMovement.inSecretArea = true;
        InitGame.resetWorld();
        PlayerMovement.secretDoorUnlocked = false;
        Inventory.fillInventory();
        waitForEnter();
      }
    }
  }

  /**
   * Clears the console screen based on the underlying operating system.
   *
   */
  public static void clearScreen() {
    try {
      final String os = System.getProperty("os.name");
      if (os.contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        Runtime.getRuntime().exec("clear");
      }
    } catch (IOException | InterruptedException ex) {
      System.out.println("An error occurred while trying to clear the console");
    }
  }

  /**
   * Pauses the game and waits for the user to press Enter.
   */
  public static void waitForEnter() {
    System.out.println("Press Enter to continue...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
    // Note: not closing scanner to allow other parts of code to use System.in
  }
}
