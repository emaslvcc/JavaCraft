# String getBlockName(int blockType)

## Java

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

## Pseudocode

```java
BEGIN

IF `<Integer> blockType` == `<Integer> air`
    RETURN `<String> "Empty Block"`;
ELSE IF `<Integer> blockType` == `<Integer> wood`
    RETURN `<String> "Wood"`;
ELSE IF `<Integer> blockType` == `<Integer> leaves`
    RETURN `<String> "Leaves"`;
ELSE IF `<Integer> blockType` == `<Integer> stone`
    RETURN `<String> "Stone"`;
ELSE IF `<Integer> blockType` == `<Integer> iron ore`
    RETURN `<String> "Iron Ore"`;
ELSE IF `<Integer> blockType` == `<Integer> emerald ore`
    RETURN `<String> "Emerald Ore"`;
ELSE IF `<Integer> blockType` == `<Integer> coal ore`
    RETURN `<String> "Coal Ore"`;
ELSE
    RETURN `<String> "Unknown"`

END
```
