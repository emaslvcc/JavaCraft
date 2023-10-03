# String getBlockName(int blockType)

```java
private static String getBlockName(int blockType) {
    switch (blockType) {
        case AIR:
            return "Empty Block";
        case WOOD:
            return "Wood";
        case LEAVES:
            return "Leaves";
        case STONE:
            return "Stone";
        case IRON_ORE:
            return "Iron Ore";
        case EMERALD_ORE:
            return "Emerald Ore";
        case COAL_ORE:
            return "Coal Ore";
        default:
            return "Unknown";
    }
}
```

create method getBlockName, with as input the variable blockType
compare the variable blocktype to hardcoded values
	in the case that blockType corresponds to AIR then
		return "Wooden"

	in the case that blockType corresponds to WOOD then
		return "Stick"

	in the case that blockType corresponds to LEAVES then
		return "Leaves"

	in the case that blockType corresponds to STONE then
		return "Stone

	in the case that blockType corresponds to IRON_ORE then
		return "Iron Ore"

	in the case that blockType corresponds to no hardcoded values then
		return "Unknown"