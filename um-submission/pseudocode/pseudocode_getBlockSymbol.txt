create method getBlockSymbol with the variable blockType as input
create the string blockColor
compare blockType to hardcoded values
	in the case that blockType corresponds to AIR then
		return ANSI_RESET and an empty string

	in the case that blockType corresponds to WOOD then
		set blockColor as ANSI_RED
		stop checking values

	in the case that blockType corresponds to LEAVES then
		set blockColor as ANSI_GREEN
		stop checking values

	in the case that blockType corresponds to STONE then
		set blockColor as ANSI_BLUE
		stop checking values

	in the case that blockType corresponds to IRON_ORE then
		set blockColor as ANSI_WHITE
		stop checking values

	in the case that blockType corresponds to no hardcoded values then
		set blockColor as ANSI_RESET

get the character for the corresponding block by using the method getBlcokChar and inserting blockType
return blockColor, the previously received character and an empty string