# class JavaCraft

## Pseudocode

```java
BEGIN

Define global constants/variables and assign values to some;
Initialize game by assigning some global variables;
Generate world with different blocks by using randomness;
PRINT INFO `instrucions`;
PRINT INFO "Start the game? (Y/N): ";
IF `<String> read user input` == y (ignore case)
    Set `<boolean> unlockMode` = false;
    Set `<boolean> craftingCommandEntered` = false;
    Set `<boolean> miningCommandEntered` = false;
    Set `<boolean> movementCommandEntered` = false;
    WHILE true
        PRINT INFO `initial UI containing legend, world, inventory`;
        PRINT INFO "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door\n" (colored in green);
        IF `<String> read user input` == "<String> w" OR "<String> up" OR "<String> s" OR "<String> down" OR "<String> a" OR "<String> left" OR "<String> d" OR "<String> right" (ignore case)
            IF `<boolean> unlockMode` == true
                Set `<boolean> movementCommandEntered` = true;
            Move player;
        ELSE IF `<String> read user input` == "<String> m" (ignore case)
            IF `<boolean> unlockMode` == true
                Set `<boolean> miningCommandEntered` = true;
            Mine block;
        ELSE IF `<String> read user input` == "<String> p" (ignore case)
            PRINT INFO `players inventory`;
            PRINT INFO "Enter the block type to place: ";
            Place block `<String> read user input`;
        ELSE IF `<String> read user input` == "<String> c" (ignore case)
            PRINT INFO `crafting recipes`;
            PRINT INFO "Enter the recipe number to craft: ";
            Craft item `<String> read user input`;
        ELSE IF `<String> read user input` == "<String> i" (ignore case)
            Interact with world;
        ELSE IF `<String> read user input` == "<String> save" (ignore case)
            PRINT INFO "Enter the file name to save the game state: ";
            Save game as `<String> read user input`;
        ELSE IF `<String> read user input` == "<String> load" (ignore case)
            PRINT INFO "Enter the file name to load the game state: ";
            Load game from `<String> read user input`;
        ELSE IF `<String> read user input` == "<String> exit" (ignore case)
            PRINT INFO "Exiting the game. Goodbye!\n";
            Exit game;
        ELSE IF `<String> read user input` == "<String> look" (ignore case)
            Print all blocks sorrounding player;
        ELSE IF `<String> read user input` == "<String> unlock" (ignore case)
            Set `<boolean> unlockMode` = true;
        ELSE IF `<String> read user input` == "<String> getflag" (ignore case)
            TRY TO
                Set up connection to a server;
                PRINT " " + `<String> get country from server via a POST request`;
                PRINT " " + `<String> get quote from server via a POST request`;
            ON EXCEPTION
                PRINT ERROR containing `stacktrace`;
                PRINT ERROR "Error connecting to the server";
            Wait on player to press ENTER;
        ELSE IF `<String> read user input` == "<String> open" (ignore case)
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
            IF `<String> read user input` == "<String> c" (ignore case)
                Set `<boolean> craftingCommandEntered` = true;
            IF `<String> read user input` == "<String> m" (ignore case)
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
start game
define static and final variables
call Main method
call initGame method
call generateWorld method
print instructions for the player how to play the game
ask user for input
make a decision based on the input of the player
if input is no:
	print that the game is not started and wish the player goodbye
if input is yes:
	call method startGame
	loop while true:
		call method clearScreen
		call method displayLegend
		call method displayWorld
		call method displayInventory
		ask the user for action
		make a decision based on the input of the player
		if input is p:
			call displayInventory method
			ask the user which block to place
			call method placeBlock
			check if the selected block is valid:
			if it is valid:
				add block to the world
				tell the user that the block has been placed in the world
			
			else:
				tell the player that the block is invalid or that the player does not have the block in their inventory
				
			call method waitForEnter
			return to main loop
			
		if input is m:
			call method mineBlock
			remove the block from the world
			print name of the mined block
			add mined block to inventory?
			call method waitForEnter
			return to mainloop
			
		if input is save:
			call method saveGame
			store all important variables to disk
			if storing was succesful:
				state that the game was saved
			else:
				tell the user that the game failed to save
			
			call method waitForEnter
			go to main loop
			
		if input is look:
			call method lookAround
			print the blocks that are arround the player in a 3x3 grid
			call method waitForEnter
			go to main loop
		
		if input is c:
			call method displayCraftingRecipes
			ask the user which recipy they want to use
			call method craftItem
			if the user has enough resources to craf to item:
				craftItem according to recipy
			
			else:
				tell the user that the recipe is invalid or that there are insufficient resources
				
			call method waitForEnter
			go to main loop
			
		if all requirements to open the door are fullfilled:
			tell the use that the secret door is unlocked
			call method resetWorld
			??
			
		 call method waitForEnter
		 go back to main loop
		 
		 if input is exit:
			break
			
		if input is load:
			call method loadGame
			if loadGame was succesful:
				tell the user that the laod was succesful
			
			else:
				tell the user that it failed to load the game
				
			call method waitForEnter
			
		if input is getflag:
			call method getCountryAndQuoteFromServer
			pull flag and quote from server
			display flag and quote
			call method waitForEnter
			go to main loop
			
			
		if input is awsd or any arrow key:
			call method movePlayer
			change position of player according to input
			go to main loop
			
		if input is unlock:
			set unlockMode to true
			go to main loop
		
		else:
			tell the user that the input is invalid
			go to main loop
			
		