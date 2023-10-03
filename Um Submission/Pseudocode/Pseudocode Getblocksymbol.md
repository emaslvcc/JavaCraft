# String getBlockSymbol(int blockType)

```java
private static String getBlockSymbol(int blockType) {
    String blockColor;
    switch (blockType) {
        case AIR:
            return ANSI_RESET + "- ";
        case WOOD:
            blockColor = ANSI_RED;
            break;
        case LEAVES:
            blockColor = ANSI_GREEN;
            break;
        case STONE:
            blockColor = ANSI_BLUE;
            break;
        case IRON_ORE:
            blockColor = ANSI_WHITE;
            break;
        case EMERALD_ORE:
            blockColor = ANSI_EMERALD_GREEN;
            break;
        case COAL_ORE:
            blockColor = ANSI_COAL_GRAY;
            break;    
        default:
            blockColor = ANSI_RESET;
            break;
    }
    return blockColor + getBlockChar(blockType) + " ";
}
```

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