### void loadGame(String fileName)

#### Java

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

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

TRY TO
    Set `<stream> inputStream` = `<stream> of contents from file matching <String> fileName relative to current working directory`;
    Set `<Integer> NEW_WORLD_WIDTH` = `<Integer> get next line containing serialized <Integer> in <stream> inputStream`;
    Set `<Integer> NEW_WORLD_HEIGHT` = `<Integer> get next line containing serialized <Integer> in <stream> inputStream`;
    Set `<two dimensional Integer array> world` = `<two dimensional Integer array> get next line containing any serialized object in <stream> inputStream`;
    Set `<Integer> playerX` = `<Integer> get next line containing serialized <Integer> in <stream> inputStream`;
    Set `<Integer> playerY` = `<Integer> get next line containing serialized <Integer> in <stream> inputStream`;
    Set `<Integer list> inventory` = `<Integer list> get next line containing any serialized object in <stream> inputStream` and cast to <Integer list>;
    Set `<Integer list> craftedItems` = `<Integer list> get next line containing any serialized object in <stream> inputStream`  and cast to <Integer list>;
    Set `<boolean> unlockMode` = `<boolean> get next line containing serialized <boolean> in <stream> inputStream`;
    PRINT INFO "Game state loaded from file: " + `<String> fileName` + "\n";
    Close `<stream> inputStream`;
ON EXCEPTION
    PRINT ERROR "Error while loading the game state: " + `errormessage` + "\n";
    Close `<stream> inputStream`;
Wait on player to press ENTER;

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-loadGame.svg" alt="flowchart-loadGame.svg"/>
