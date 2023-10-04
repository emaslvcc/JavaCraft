enum Colors {
	BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE, GRAY, RESET;

	private static final String ANSI_ESCAPE = "\u001b[";

	public String ansi() {
		switch (this) {
		case BLACK:
			return ANSI_ESCAPE + "30m";
		case RED:
			return ANSI_ESCAPE + "31m";
		case GREEN:
			return ANSI_ESCAPE + "32m";
		case YELLOW:
			return ANSI_ESCAPE + "33m";
		case BLUE:
			return ANSI_ESCAPE + "34m";
		case MAGENTA:
			return ANSI_ESCAPE + "35m";
		case CYAN:
			return ANSI_ESCAPE + "36m";
		case WHITE:
			return ANSI_ESCAPE + "97m";
        case GRAY:
            return ANSI_ESCAPE + "90m";
		case RESET:
			return ANSI_ESCAPE + "0m";
		default:
			return "";
		}
	}

    public int[] color() {
        switch (this) {
		case BLACK:
			return new int[] { 0, 0, 0 };
		case RED:
            return new int[] { 205, 49, 40 };
		case GREEN:
			return new int[] { 13, 188, 121 };
		case YELLOW:
			return new int[] { 229, 229, 16 };
		case BLUE:
			return new int[] { 36, 114, 200 };
		case MAGENTA:
			return new int[] { 188, 63, 188 };
		case CYAN:
			return new int[] { 17, 168, 205 };
		case WHITE:
			return new int[] { 229, 229, 229 };
        case GRAY:
            return new int[] { 102, 102, 102 };
		case RESET:
			return new int[] { 0, 0, 0 };
		default:
			return new int[] { 0, 0, 0 };
		}
    }
}