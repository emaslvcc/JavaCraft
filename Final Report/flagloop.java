public class flagloop {
    public static void main(String[] args) {
        String redHexColor = "#d0150c";
        String yellowHexColor = "#ffff00";
        String character = "▒";

        // ANSI escape codes for setting text color
        String red = getColorCode(redHexColor);
        String yellow = getColorCode(yellowHexColor);

        // Printing the red only lines at the top of the flag
        int i = 1;
        while (i <= 6) {
            int j = 1;
            while (j <= 80) {
                System.out.print(red + character + "\u001B[0m");
                j++;
            }
            System.out.println();
            i++;
        }
        // Instructions on the amount of characters that need to be prinited
        int[] intstructions1 = {
                39, 2, // First number in line is the red color amount (symmetric on the left and right
                       // of the yellow color), second num in line is for the amount of characters in
                       // yellow.
                38, 4,
                37, 6,
                34, 12,
                26, 28,
                29, 22,
                32, 16,
                34, 12,
        };
        int[] instructions2 = {
                32, 7, 2,
                32, 4, 8,
                31, 2, 14,
        };
        i = 1;
        int x = 0;
        int y = 1;

        while (i <= 9) {
            int j = 1;
            while (j <= intstructions1[x]) {
                System.out.print(red + character + "\u001B[0m");
                j++;
            }
            j = 1;
            while (j <= intstructions1[y]) {
                System.out.print(yellow + character + "\u001B[0m");
                j++;
            }
            j = 1;
            while (j <= intstructions1[x]) {
                System.out.print(red + character + "\u001B[0m");
                j++;
            }
            x += 2;
            y += 2;
            i++;
            System.out.println();
            if (y > 16) {
                break;
            }
        }
        i = 1;
        x = 0;
        y = 1;
        int z = 2;
        while (i <= 3) {
            int j;
            j = 1;
            while (j <= instructions2[x]) {
                System.out.print(red + character + "\u001B[0m");
                j++;
            }
            j = 1;
            while (j <= instructions2[y]) {
                System.out.print(yellow + character + "\u001B[0m");
                j++;
            }
            j = 1;
            while (j <= instructions2[z]) {
                System.out.print(red + character + "\u001B[0m");
                j++;
            }
            j = 1;
            while (j <= instructions2[y]) {
                System.out.print(yellow + character + "\u001B[0m");
                j++;
            }
            j = 1;
            while (j <= instructions2[x]) {
                System.out.print(red + character + "\u001B[0m");
                j++;
            }
            x += 3;
            y += 3;
            z += 3;
            System.out.println();

            if (z > 8) {
                break;
            }
        }
        i = 1;
        while (i <= 6) {
            int j = 1;
            while (j <= 80) {
                System.out.print(red + character + "\u001B[0m");
                j++;
            }
            System.out.println();
            i++;
        }
    }

    public static String getColorCode(String hexColor) {
        // Remove the '#' symbol and convert to RGB values
        int red = Integer.parseInt(hexColor.substring(1, 3), 16);
        int green = Integer.parseInt(hexColor.substring(3, 5), 16);
        int blue = Integer.parseInt(hexColor.substring(5, 7), 16);

        // ANSI escape code for setting text color
        return "\u001B[38;2;" + red + ";" + green + ";" + blue + "m";
    }
}
