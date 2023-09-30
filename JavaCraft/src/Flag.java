import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Flag {

    public static String returnASCIIFromImage(String countryName) {
        String filePath = "flagImages/" + countryName + ".png";

        BufferedImage flagImage = loadFlagImage(filePath);

        BufferedImage resizedImage = resizeImage(flagImage, 50, 15);

        return imageToASCII(resizedImage);
    }

    private static BufferedImage loadFlagImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        resizedImage.createGraphics().drawImage(image, 0, 0, width, height, null);
        return resizedImage;
    }

    private static String imageToASCII(BufferedImage image) {
        if (image == null) {
            return "Failed to load the flag image.";
        }

        int width = image.getWidth();
        int height = image.getHeight();
        StringBuilder asciiArt = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                // Converting RGB pixels into Grayscale
                int grayscaleValue = (int) (0.2126 * pixelColor.getRed() + 0.7152 * pixelColor.getGreen()
                        + 0.0722 * pixelColor.getBlue());
                char asciiChar = grayScaleToCharacter(grayscaleValue);
                asciiArt.append(setColorOfCharacter(asciiChar, pixelColor));
            }
            asciiArt.append("\n");
        }

        return asciiArt.toString();
    }

    private static char grayScaleToCharacter(int grayscaleValue) {
        char[] asciiChars = {' ', '.', ':', '-', '=', '+', '*', '#', '%', '8', 'O', 'M', 'W', '$', '@', ' '};
        int index = (grayscaleValue * (asciiChars.length - 1)) / 255;
        return asciiChars[index];
    }

    private static String setColorOfCharacter(char character, Color color) {
        return "\u001B[48;2;" + color.getRed() + ";" + color.getGreen() + ";" + color.getBlue() + "m" + character
                + "\u001B[0m";
    }
}