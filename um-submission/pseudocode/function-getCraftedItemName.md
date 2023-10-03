# String getCraftedItemName(int craftedItem)

```java
private static String getCraftedItemName(int craftedItem) {
    switch (craftedItem) {
        case CRAFTED_WOODEN_PLANKS:
            return "Wooden Planks";
        case CRAFTED_STICK:
            return "Stick";
        case CRAFTED_IRON_INGOT:
            return "Iron Ingot";
        default:
            return "Unknown";
    }
}
```
create method getCraftedItemName
compare craftedItem to hardcoded values
	in the case that craftedItem corresponds to CRAFTED_WOODEN_PLANKS then
		return "Wooden"

	in the case that craftedItem corresponds to CRAFTED_STICK then
		return "Stick"

	in the case that craftedItem corresponds to CRAFTED_IRON_INGOT then
		return "Iron Ingot"

	in the case that craftedItem corresponds to no hardcoded values then
		return "Unknown"