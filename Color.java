public class Color {

  public static String ANSI_RESET = "\u001B[0m"; // Reset color
  public static String ANSI_WHITE = "\u001B[38;2;255;255;255m"; // RGB for white

  //////////////// colors work like this \u001B[38;2;R;G;Bm   //////////////// 38 sets foreground and 48 sets background
  public static String ANSI_BROWN = "\u001B[38;2;102;51;0m"; // RGB for brown
  public static String ANSI_GREEN = "\u001B[38;2;0;204;0m"; // RGB for green
  public static String ANSI_YELLOW = "\u001B[38;2;210;205;50m"; // RGB for yellow
  public static String ANSI_CYAN = "\u001B[38;2;50;210;136m"; // RGB for cyan
  public static String ANSI_RED = "\u001B[38;2;210;50;50m"; // RGB for red
  public static String ANSI_PURPLE = "\u001B[38;2;162;50;210m"; // RGB for purple
  public static String ANSI_BLUE = "\u001B[38;2;0;0;255m"; // RGB for blue
  public static String ANSI_GRAY = "\u001B[38;2;128;128;128m"; // RGB for gray
  public static String ANSI_PLAYER = "\u001B[38;2;250;0;255m"; // RGB for white
}
