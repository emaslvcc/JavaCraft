package Images.Decode;

public class RGBA implements PixelColor {
    int red;
    int green;
    int blue;
    int alpha;

    RGBA(int i){
        this.blue = i & 0xff;
        this.green = (i & 0xff00) >> 8;
        this.red = (i & 0xff0000) >> 16;
        this.alpha = (i & 0xff000000) >>> 24;
    }

    public RGBA(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}
