# class JavaCraft

## Pseudocode

```java
BEGIN

Define global constants/variables and assign values to some;
Initialize game by assigning some global variables;
Generate world with different blocks by using randomness;
PRINT INFO `instrucions`;
PRINT INFO "Start the game? (Y/N): ";
IF `<String> read user input` == y (caseless)
    Set `<boolean> unlockMode` = false;
    Set `<boolean> craftingCommandEntered` = false;
    Set `<boolean> miningCommandEntered` = false;
    Set `<boolean> movementCommandEntered` = false;
    WHILE true
        PRINT INFO `initial UI containing legend, world, inventory`;
        PRINT INFO "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door\n" (colored in green);
        IF `<String> read user input` == "w" OR "up" OR "s" OR "down" OR "a" OR "left" OR "d" OR "right" (caseless)
            IF `<boolean> unlockMode` == true
                Set `<boolean> movementCommandEntered` = true;
            Move player;
        ELSE IF `<String> read user input` == "m" (caseless)
            IF `<boolean> unlockMode` == true
                Set `<boolean> miningCommandEntered` = true;
            Mine block;
        ELSE IF `<String> read user input` == "p" (caseless)
            PRINT INFO `players inventory`;
            PRINT INFO "Enter the block type to place: ";
            Place block `<String> read user input`;
        ELSE IF `<String> read user input` == "c" (caseless)
            PRINT INFO `crafting recipes`;
            PRINT INFO "Enter the recipe number to craft: ";
            Craft item `<String> read user input`;
        ELSE IF `<String> read user input` == "i" (caseless)
            Interact with world;
        ELSE IF `<String> read user input` == "save" (caseless)
            PRINT INFO "Enter the file name to save the game state: ";
            Save game as `<String> read user input`;
        ELSE IF `<String> read user input` == "load" (caseless)
            PRINT INFO "Enter the file name to load the game state: ";
            Load game from `<String> read user input`;
        ELSE IF `<String> read user input` == "exit" (caseless)
            PRINT INFO "Exiting the game. Goodbye!\n";
            Exit game;
        ELSE IF `<String> read user input` == "look" (caseless)
            Print all blocks sorrounding player;
        ELSE IF `<String> read user input` == "unlock" (caseless)
            Set `<boolean> unlockMode` = true;
        ELSE IF `<String> read user input` == "getflag" (caseless)
            TRY TO
                Set up connection to a server;
                PRINT " " + `<String> get country from server via a POST request`;
                PRINT " " + `<String> get quote from server via a POST request`;
            ON EXCEPTION
                PRINT ERROR containing `stacktrace`;
                PRINT ERROR "Error connecting to the server";
            Wait on player to press ENTER;
        ELSE IF `<String> read user input` == "open" (caseless)
            IF `<boolean> unlockMode` == true AND `<boolean> craftingCommandEntered` == true AND `<boolean> miningCommandEntered` == true AND `<boolean> movementCommandEntered` == true
                Set `<boolean> secretDoorUnlocked` = true;
                Reset world to an empty world;
                PRINT INFO "Secret door unlocked!\n";
                Wait on player to press ENTER;
            ELSE
                PRINT INFO "Invalid passkey. Try again!\n";
                Set `<boolean> unlockMode` = false;
                Set `<boolean> craftingCommandEntered` = false;
                Set `<boolean> miningCommandEntered` = false;
                Set `<boolean> movementCommandEntered` = false;
        ELSE
            PRINT INFO "Invalid input. Please try again." (colored in yellow)
        IF `<boolean> unlockMode` == true
            IF `<String> read user input` == "c" (caseless)
                Set `<boolean> craftingCommandEntered` = true;
            IF `<String> read user input` == "m" (caseless)
                Set `<boolean> miningCommandEntered` = true;
        IF `<boolean> secretDoorUnlocked` == true
            Clear screen;
            PRINT INFO "You have entered the secret area!\n";
            PRINT INFO "You are now presented with a game board with a flag!\n";
            Set `<boolean> inSecretArea` = true;
            Reset world to an empty world;
            Set `<boolean> secretDoorUnlocked` = false;
            Fill `<Integer list> inventory` with all available blockTypes;
            Wait on player to press ENTER;
ELSE
    Exit game;

END
```
		