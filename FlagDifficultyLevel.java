public enum FlagDifficultyLevel {
    EASY, MEDIUM, HARD;

    public String token() {
        switch (this) {
        case EASY:
            return "easy";
        case MEDIUM:
            return "medium";
        case HARD:
            return "hard";
        default:
            return "easy";
        }
    }
}
