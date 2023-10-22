package Images.Scale;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ImageScaler {

    public static BufferedImage scale(String url, Integer targetWidth, Integer targetHeight) throws IOException {

        BufferedImage bf = ImageIO.read(new File(url));

        int[] oldRes = {bf.getWidth(), bf.getHeight()};

        if (targetWidth == null){
            targetWidth = Integer.valueOf(getNewResolution(oldRes, 0, targetHeight.doubleValue())[0]);
        } else if (targetHeight == null) {
            targetHeight = Integer.valueOf(getNewResolution(oldRes, targetWidth.doubleValue(), 0)[1]);
        }

        return resizeImage(bf, targetWidth, targetHeight);
    }


    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private static int[] getNewResolution(int[] oldResolution, double newWidth, double newHeight){
        //1920 * 1080
        //2560 * 1440
        //     * 720
        int oldWidth = oldResolution[0];
        int oldHeight = oldResolution[1];
        if (newWidth == 0){
            // scale by height
            return new int[]{(int) (newHeight/oldHeight * oldWidth), (int) newHeight};
        } else {
            //scale by width
            return new int[]{(int) newWidth, (int) (newWidth/oldWidth * oldHeight)};
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getNewResolution(new int[]{1920, 1080}, 2560, 0)));
        System.out.println(Arrays.toString(getNewResolution(new int[]{1920, 1080}, 0, 720)));
    }

}
