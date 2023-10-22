package Images.Decode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class Decoder {

    public static PixelColor[][] decodeImage(String url) throws IOException {
        return get2DPixelArrayFast(ImageIO.read(new File(url)));
    }

    public static PixelColor[][] toRGBArray(BufferedImage bufferedImage) throws IOException {
        return get2DPixelArrayFast(bufferedImage);
    }

    private static int[][] get2DPixelArraySlow(BufferedImage sampleImage) {
        int width = sampleImage.getWidth();
        int height = sampleImage.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = sampleImage.getRGB(col, row);
            }
        }

        return result;
    }

    private static PixelColor[][] get2DPixelArrayFast(BufferedImage image) {
        byte[] pixelData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int width = image.getWidth();
        int height = image.getHeight();
        boolean hasAlphaChannel = image.getAlphaRaster() != null;

        PixelColor[][] result = new PixelColor[height][width];
        if (hasAlphaChannel) {
            int numberOfValues = 4;
            for (int valueIndex = 0, row = 0, col = 0; valueIndex + numberOfValues - 1 < pixelData.length; valueIndex += numberOfValues) {

                int argb = 0;
                argb += (((int) pixelData[valueIndex] & 0xff) << 24); // alpha value
                argb += ((int) pixelData[valueIndex + 1] & 0xff); // blue value
                argb += (((int) pixelData[valueIndex + 2] & 0xff) << 8); // green value
                argb += (((int) pixelData[valueIndex + 3] & 0xff) << 16); // red value

                result[row][col] = new RGBA(argb);

                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            int numberOfValues = 3;
            for (int valueIndex = 0, row = 0, col = 0; valueIndex + numberOfValues - 1 < pixelData.length; valueIndex += numberOfValues) {
                int argb = 0;
                argb += -16777216; // 255 alpha value (fully opaque)
                argb += ((int) pixelData[valueIndex] & 0xff); // blue value
                argb += (((int) pixelData[valueIndex + 1] & 0xff) << 8); // green value
                argb += (((int) pixelData[valueIndex + 2] & 0xff) << 16); // red value

                result[row][col] = new RGBA(argb);

                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }
}
