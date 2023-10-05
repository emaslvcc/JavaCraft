public enum Item {
  WOODEN_PLANKS(Color.ANSI_RED),
  STICK(Color.ANSI_RED),
  BED(Color.ANSI_RED),
  IRON_INGOT(Color.ANSI_WHITE);

  private final String color;

  Item(String color) {
    this.color = color;
  }

  public String getColor() {
    return color;
  }

  public String getName() {
    return this.name().replace("_", " ").toLowerCase();
  }

  public static String getCraftedItemName(Item craftedItem) {
    return craftedItem.getName();
  }

  public static String getCraftedItemColor(Item craftedItem) {
    return craftedItem.getColor();
  }
}
