import java.util.Arrays;
import java.util.Optional;

public enum Block {
  AIR, WOOD, LEAVES, STONE, IRON_ORE, MAGIC_ORE, GOLD_ORE, OBSIDIAN, WATER,
  CRAFTED_WOODEN_PLANKS, CRAFTED_STICK, CRAFTED_IRON_INGOT, CRAFTED_GOLD_INGOT;

  public static Block[] uncraftableBlocks() {
    return Arrays.stream(Block.values())
      .filter(block -> !block.isCrafted())
      .toArray(Block[]::new);
  }

  public static Optional<Block> parseFromInput(int blockType) {
    Block[] blocks = Block.uncraftableBlocks();
    if (blockType < 0 || blockType >= blocks.length) {
      return Optional.empty();
    } else {
      return Optional.of(blocks[blockType]);
    }
  }

  public static String blockNumbersInfo() {
    Block[] blocks = Block.values();
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < blocks.length; i++) {
      Block block = blocks[i];
      String base = String.format("%d - %s", block.ordinal(), block.blockName());

      result.append(base);
      
      if (block.isCrafted()) {
        result.append(" (Crafted Item)");
      }

      if (i < blocks.length - 1) {
        result.append('\n');
      }
    }

    return result.toString();
  }

  public static String blocksLegend() {
    Block[] blocks = Block.uncraftableBlocks();
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < blocks.length; i++) {
      Block block = blocks[i];
      String base = String.format("%s %c%c - %s", block.getColor().ansi(), block.getChar(), block.getChar(), block.blockName());

      result.append(base);

      if (i < blocks.length - 1) {
        result.append('\n');
      }
    }

    return result.toString();
  }

  public Colors getColor() {
    switch (this) {
      case AIR:
        return Colors.WHITE;
      case WOOD:
        return Colors.RED;
      case LEAVES:
        return Colors.GREEN;
      case STONE:
        return Colors.BLUE;
      case IRON_ORE:
        return Colors.GRAY;
      case MAGIC_ORE:
        return Colors.MAGENTA;
      case GOLD_ORE:
        return Colors.YELLOW;
      case WATER:
        return Colors.CYAN;
      case OBSIDIAN:
        return Colors.BLACK;
      // Crafted Items:
      case CRAFTED_WOODEN_PLANKS:
      case CRAFTED_STICK:
      case CRAFTED_IRON_INGOT:
      case CRAFTED_GOLD_INGOT:
        return Colors.YELLOW;
      default:
        return Colors.RESET;
    }
  }

  public String getSymbol() {
    String blockColor = getColor().ansi();
    return blockColor + getChar() + " ";
  }

  public char getChar() {
    switch (this) {
      case AIR:
      return '-';
      case WOOD:
        return '\u2592';
      case LEAVES:
        return '\u00A7';
      case STONE:
        return '\u2593';
      case IRON_ORE:
        return '\u00B0';
      case MAGIC_ORE:
        return '☘';
      case GOLD_ORE:
        return '\u25A2';
      case WATER:
        return '∭';
      case OBSIDIAN:
        return '✦';
      // Crafted Items:
      case CRAFTED_WOODEN_PLANKS:
        return 'W';
      case CRAFTED_STICK:
        return 'S';
      case CRAFTED_IRON_INGOT:
        return 'I';
      case CRAFTED_GOLD_INGOT:
        return 'G';
      default:
        return '-';
    }
  }

  public double probability() {
    switch (this) {
      case AIR:
      return 0.25;
      case WOOD:
        return 0.1;
      case LEAVES:
        return 0.1;
      case STONE:
        return 0.15;
      case IRON_ORE:
        return 0.1;
      case MAGIC_ORE:
        return 0.04;
      case GOLD_ORE:
        return 0.06;
      case WATER:
        return 0.1;
      case OBSIDIAN:
        return 0.1;
      // Crafted Items:
      case CRAFTED_WOODEN_PLANKS:
      case CRAFTED_STICK:
      case CRAFTED_IRON_INGOT:
      case CRAFTED_GOLD_INGOT:
        return 0;
      default:
        return 0;
    }
  }

  public String blockName() {
    switch (this) {
      case AIR:
      return "Empty block";
      case WOOD:
        return "Wood";
      case LEAVES:
        return "Leaves";
      case STONE:
        return "Stone";
      case IRON_ORE:
        return "Iron Ore";
      case MAGIC_ORE:
        return "Magic Ore";
      case GOLD_ORE:
        return "Gold Ore";
      case WATER:
        return "Water";
      case OBSIDIAN:
        return "Obsidian";
      // Crafted Items:
      case CRAFTED_WOODEN_PLANKS:
        return "Wooden Planks";
      case CRAFTED_STICK:
        return "Stick";
      case CRAFTED_IRON_INGOT:
        return "Iron Ingot";
      case CRAFTED_GOLD_INGOT:
        return "Gold Ingot";
      default:
        return "--";
    }
  }

  public boolean isCrafted() {
    switch (this) {
      case AIR:
      case WOOD:
      case LEAVES:
      case STONE:
      case IRON_ORE:
      case MAGIC_ORE:
      case GOLD_ORE:
      case WATER:
      case OBSIDIAN:
        return false;
      case CRAFTED_WOODEN_PLANKS:
      case CRAFTED_IRON_INGOT:
      case CRAFTED_STICK:
      case CRAFTED_GOLD_INGOT:
        return true;
      default:
        return false;
    }
  }
}
