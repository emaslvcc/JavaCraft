public class Block {
    private String name;
    public String displayString;

    public Block(String name, String displayString) {
        this.name = name;
        this.displayString = displayString;
    }

    public String getName() {
        return name;
    }
    public String getDisplayCharacter(){
        return displayString;
    }

}

