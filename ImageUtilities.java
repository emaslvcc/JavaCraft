import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageUtilities {

    /**
     * Generates world map of blocks from a bitmap image.
     * @param imageStream Image data as a stream. Can be read from an URL or a file.
     * @param width Width for image to be resized into
     * @param height Height for image to be resized into
     * @param horizontalInsets Horizontal insets to be cropped away
     * @param verticalInsets Vertical insets to be cropped away
     * @param availableBlocks An array of possible blocks
     * @param availableBlockColors An array of possible colors for each block.
     * @return A world map based on the image.
     * @throws Exception
     */
    public static Block[][] imageToWorld(InputStream imageStream, int width, int height, int horizontalInsets, int verticalInsets, Block[] availableBlocks, Colors[] availableBlockColors) throws Exception {
        Block[][] world = new Block[width][height];

        BufferedImage image = readImageToBuffer(imageStream, width, height, horizontalInsets, verticalInsets);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = image.getRGB(x, y);
                int[] rgb = colorToRGB(color);
                
                int bestMatchColorIndex = nearestMatchingColorIndex(rgb, availableBlockColors);
                Block block = availableBlocks[bestMatchColorIndex];
                
                world[x][y] = block;
            }
        }

        return world;
    }

    private static BufferedImage readImageToBuffer(InputStream imageStream, int width, int height, int horizontalInsets, int verticalInsets) throws Exception {
        BufferedImage image = ImageIO.read(imageStream);
        BufferedImage croppedImage = image.getSubimage(horizontalInsets, verticalInsets, image.getWidth() - 2 * horizontalInsets, image.getHeight() - 2 * verticalInsets);
        Image resizedImage = croppedImage.getScaledInstance(width, height, Image.SCALE_FAST);

        BufferedImage bufferedResizedImage = new BufferedImage(resizedImage.getWidth(null), resizedImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bufferedImageGraphics = bufferedResizedImage.createGraphics();
        bufferedImageGraphics.drawImage(resizedImage, 0, 0, null);
        bufferedImageGraphics.dispose();

        return bufferedResizedImage;
    }

    /**
	 * Resizes the image using Nearest Neighbour
	 * @param image Image to scale
	 * @param width New width
	 * @param height New height
	 * @return
	 */
	public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
		BufferedImage destinationBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = destinationBufferedImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2.drawImage(image, 0, 0, width, height, null);
		g2.dispose();
		return destinationBufferedImage;
	}

    private static int[] colorToRGB(int color) {
        int r = (color >> 16)   & 0xFF;
        int g = (color >> 8)    & 0xFF;
        int b = (color >> 0)    & 0xFF;

        return new int[] { r, g, b };
    }

    private static int nearestMatchingColorIndex(int[] color, Colors[] colors) {
        int bestMatchIndex = 0;
        int bestMatchDistance = -1;

        for (int i = 0; i < colors.length; i++) {
            int[] paletteColor = colors[i].color();
            int distance = euclidianDistance3D(color, paletteColor);
            if (bestMatchDistance == -1 || distance < bestMatchDistance) {
                bestMatchDistance = distance;
                bestMatchIndex = i;
            }
        }

        return bestMatchIndex;
    }

    private static int euclidianDistance3D(int[] vecA, int[] vecB) {
        return power2(vecB[0] - vecA[0]) + power2(vecB[1] - vecA[1]) + power2(vecB[2] - vecA[2]);
    }

    private static int power2(int a) {
        return a * a;
    }
    
}
