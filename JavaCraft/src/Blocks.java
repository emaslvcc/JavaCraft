public class Blocks {


    public static void main(String[] args) {
        Block[] availableBlocks = new Block[2];
        availableBlocks[0] = new Block("Air", GameValues.ANSI_RESET + "- ");
        availableBlocks[1] = new Block("Wood", GameValues.ANSI_RED);
    }

    public static String getBlockSymbol(int blockType) {
        String blockColor;
        switch (blockType) {
            case GameValues.AIR:
                return GameValues.ANSI_RESET + "- ";
            case GameValues.WOOD:
                blockColor = GameValues.ANSI_BROWN;
                break;
            case GameValues.LEAVES:
                blockColor = GameValues.ANSI_GREEN;
                break;
            case GameValues.STONE:
                blockColor = GameValues.ANSI_GRAY;
                break;
            case GameValues.IRON_ORE:
                blockColor = GameValues.ANSI_WHITE;
                break;
            case GameValues.GOLD_ORE:
                blockColor = GameValues.ANSI_YELLOW;
                break;
            case GameValues.Diamond:
                blockColor = GameValues.ANSI_CYAN;
                break;
            default:
                blockColor = GameValues.ANSI_RESET;
                break;
        }
        return blockColor + getBlockChar(blockType) + " ";
    }

    public static char getBlockChar(int blockType) {
        switch (blockType) {
            case GameValues.WOOD:
                return '\u2592';
            case GameValues.LEAVES:
                return '\u00A7';
            case GameValues.STONE:
                return '\u2588';
            case GameValues.IRON_ORE:
                return '\u0D4F';
            case GameValues.GOLD_ORE:
                return '\u0D4F';
            case GameValues.Diamond:
                return '\u00B0';
            default:
                return '-';
        }
    }

    static String getBlockName(int blockType) {
        switch (blockType) {
            case GameValues.AIR:
                return "Empty Block";
            case GameValues.WOOD:
                return "Wood";
            case GameValues.LEAVES:
                return "Leaves";
            case GameValues.STONE:
                return "Stone";
            case GameValues.IRON_ORE:
                return "Iron Ore";
            case GameValues.GOLD_ORE:
                return "Gold Ore";
            case GameValues.Diamond:
                return "Diamond";
            default:
                return "Unknown";
        }
    }
}
