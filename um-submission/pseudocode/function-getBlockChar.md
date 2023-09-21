# function getBlockChar(int blockType)

```java
switch (blockType) {
    case WOOD:
        return '\u2592';
    case LEAVES:
        return '\u00A7';
    case STONE:
        return '\u2593';
    case IRON_ORE:
        return '\u00B0';
    default:
        return '-';
}
```

1. Compare `blockType` parameter to valid recipes
   1. If it matches WOOD, return ANSI CODE `'\u2592'`
   2. If it matches LEAVES, return ANSI CODE `'\u00A7'`
   3. If it matches STONE, return ANSI CODE `'\u2593'`
   4. If it matches IRON_ORE, return ANSI CODE `'\u00B0'`
   5. By default, return `'-'`
