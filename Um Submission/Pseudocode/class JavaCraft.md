# class JavaCraft

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
			
		