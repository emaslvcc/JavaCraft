public class Block {
    public String displayString;
    private final String name;

    public Block(String name, String displayString) {
        this.name = name;
        this.displayString = displayString;
    }

    public String getName() {
        return name;
    }

    public String getDisplayCharacter() {
        return displayString;
    }

}

