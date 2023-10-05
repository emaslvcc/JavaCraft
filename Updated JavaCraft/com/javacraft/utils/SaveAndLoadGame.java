// SaveAndLoadGame.java

/**
 * SaveAndLoadGame Class
 *
 * <p>
 * This class manages the game state of JavaCraft by saving and loading data.
 * </p>
 *
 * <p>
 * The class offers functionality for serializing and deserializing game state information
 * into a file. It utilizes Java's Object Input and Output streams for this purpose.
 * </p>
 *
 * @author  Group 49
 * @version 1.0
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class SaveAndLoadGame {

  /**
   * Saves the game state to a file by serializing relevant game data.
   *
   * @param fileName Name of the file where the game state will be saved.
   * @throws IOException If any IO errors occur.
   */
  public static void saveGame(String fileName) {
    // Using try-with-resources for automatic resource management
    try (
      // Serialization and saving game state data
      ObjectOutputStream outputStream = new ObjectOutputStream(
        new FileOutputStream(fileName)
      )
    ) {
      // Serialize game state data and write to the file
      outputStream.writeInt(InitGame.NEW_WORLD_WIDTH);
      outputStream.writeInt(InitGame.NEW_WORLD_HEIGHT);
      outputStream.writeObject(InitGame.world);
      outputStream.writeInt(PlayerMovement.playerX);
      outputStream.writeInt(PlayerMovement.playerY);
      outputStream.writeObject(Inventory.inventory);
      outputStream.writeObject(CraftingUtils.craftedItems);
      outputStream.writeBoolean(PlayerMovement.unlockMode);
      System.out.println("Game state saved to file: " + fileName);
    } catch (IOException e) {
      // Handle exceptions
      System.out.println(
        "Error while saving the game state: " + e.getMessage()
      );
    }
    JavaCraft.waitForEnter();
  }

  /**
   * Loads the game state from a file by deserializing relevant game data.
   *
   * @param fileName Name of the file from which the game state will be loaded.
   * @throws IOException            If any IO errors occur.
   * @throws ClassNotFoundException If the class of the serialized object cannot be found.
   */
  public static void loadGame(String fileName) {
    try (
      // Using try-with-resources for automatic resource management
      ObjectInputStream inputStream = new ObjectInputStream(
        new FileInputStream(fileName)
      )
    ) {
      // Deserialize game state data from the file and load it into the program
      InitGame.NEW_WORLD_WIDTH = inputStream.readInt();
      InitGame.NEW_WORLD_HEIGHT = inputStream.readInt();
      InitGame.world = (int[][]) inputStream.readObject();
      PlayerMovement.playerX = inputStream.readInt();
      PlayerMovement.playerY = inputStream.readInt();
      Inventory.inventory = (List<Integer>) inputStream.readObject();
      CraftingUtils.craftedItems = (List<Integer>) inputStream.readObject();
      PlayerMovement.unlockMode = inputStream.readBoolean();
      System.out.println("Game state loaded from file: " + fileName);
    } catch (IOException | ClassNotFoundException e) {
      System.out.println(
        // Handle exceptions
        "Error while loading the game state: " + e.getMessage()
      );
    }
    JavaCraft.waitForEnter();
  }
}
