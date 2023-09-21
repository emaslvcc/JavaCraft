function-lookAround

Print "You look around and see: "
for y = max of player Y - 1 or 0 ,until y <= min of playerY - 1 or
worldHeight - 1, y++
	for x + max of player X - 1 or 0, until y <= min of playerX + 1 or worldWidth - 1, x++
		if x == playerX && y == playerY	
			Print the player icon in Green then reset the color for future inputs
		else Print the blockType for the coordinates x and y
	Print nothing
Print nothing
waitForEnter