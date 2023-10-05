public enum Block {
  PLAYER(Color.ANSI_PLAYER, "\u2588"),
  AIR(Color.ANSI_RESET, "-"),
  WOOD(Color.ANSI_RED, "\u2593"),
  LEAVES(Color.ANSI_GREEN, "\u2591"),
  STONE(Color.ANSI_RESET, "\u2593"), // I've put ANSI_RESET as an example. Replace with the desired color.
  IRON_ORE(Color.ANSI_WHITE, "\u00B0"),
  WOOL(Color.ANSI_PURPLE, "\u00E6"),
  SKIN(Color.ANSI_YELLOW, "\u00FE");

  private final String color;
  private final String character;

  Block(String color, String character) {
    this.color = color;
    this.character = character;
  }

  public String getColor() {
    return color;
  }

  public String getCharacter() {
    return character;
  }

  public String getSymbol() {
    return color + character;
  }

  public String getName() {
    return this.name().replace("_", " ").toLowerCase();
  }
}
