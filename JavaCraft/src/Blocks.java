public class Blocks {


    public static void main(String args[])
    {
        Block[] availableBlocks= new Block[2];
        availableBlocks[0] = new Block("Air",GameValues.ANSI_RESET+"- ");
        availableBlocks[1] = new Block("Wood", GameValues.ANSI_RED);
    }

    public static String getBlockSymbol(int blockType) {
        String blockColor;
        switch (blockType) {
            case GameValues.AIR:
                return GameValues.ANSI_RESET + "- ";
            case GameValues.WOOD:
                blockColor = GameValues.ANSI_RED;
                break;
            case GameValues.LEAVES:
                blockColor = GameValues.ANSI_GREEN;
                break;
            case GameValues.STONE:
                blockColor = GameValues.ANSI_BLUE;
                break;
            case GameValues.IRON_ORE:
                blockColor = GameValues.ANSI_WHITE;
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
                return '\u2593';
            case GameValues.IRON_ORE:
                return '\u00B0';
            default:
                return '-';
        }
    }
}
