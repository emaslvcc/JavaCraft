public enum Animal {
  SHEEP(Color.ANSI_PURPLE, "\u00E6", 10),
  COW(Color.ANSI_BROWN, "\u00FE", 10);

  private final String color;
  private final String character;
  private int attribute; // Represents woolAmount for sheep, and health for cow

  Animal(String color, String character, int attribute) {
    this.color = color;
    this.character = character;
    this.attribute = attribute;
  }

  public String getColor() {
    return color;
  }

  public String getCharacter() {
    return character;
  }

  public int getAttribute() {
    return attribute;
  }

  public void setAttribute(int attribute) {
    this.attribute = attribute;
  }

  public String getSymbol() {
    return color + character;
  }

  public String getAttributeDescription() {
    switch (this) {
      case SHEEP:
        return "Wool Amount: " + attribute;
      case COW:
        return "Health: " + attribute;
      default:
        return "Unknown attribute";
    }
  }
}
