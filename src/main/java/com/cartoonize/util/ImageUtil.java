package com.cartoonize.util;

import java.awt.image.BufferedImage;
import java.io.*;

/**
 * ImageUtil can be replaced by micro service in the future
 */
public class ImageUtil {

    private static final int threshold = 3000000;

    /**
     * curve process
     * @param src
     * @return
     */
    public static BufferedImage curveProcess(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        int[] outPixels = new int[width * height];
        src.getRGB(0, 0, width, height, pixels, 0, width);
        int offset = 0;
        for (int row = 1; row < height - 1; row++) {
            offset = row * width;
            for (int col = 1; col < width - 1; col++) {
                int r = (pixels[offset + col] >> 16) & 0xff;
                int g = (pixels[offset + col] >> 8) & 0xff;
                int b = (pixels[offset + col]) & 0xff;
                // red
                r = getSCurve(r);
                g = getSCurve(g);
                b = getSCurve(b);
                outPixels[offset + col] = (0xff << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);
            }
        }
        BufferedImage dest = new BufferedImage(width, height, src.getType());
        dest.setRGB(0, 0, width, height, outPixels, 0, width);
        return dest;
    }

    /**
     * graying image
     * @param bufferedImage
     * @return
     */
    public static BufferedImage grayingImage(BufferedImage bufferedImage) {
        BufferedImage grayImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                int color = bufferedImage.getRGB(i, j);
                grayImage.setRGB(i, j, color);
            }
        }
        return grayImage;
    }

    /**
     * binary image
     * @param bufferedImage
     * @return
     */
    public static BufferedImage binaryImage(BufferedImage bufferedImage) {
        BufferedImage grayImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        int threshold = getMeanThreshold(bufferedImage);
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                int color = bufferedImage.getRGB(i, j);
                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                if (gray > threshold) {
                    // white
                    grayImage.setRGB(i, j, 0xFFFFFF);
                } else {
                    // black
                    grayImage.setRGB(i, j, 0);
                }
            }
        }
        return grayImage;
    }

    /**
     * get mean threshold
     * @param bufferedImage
     * @return
     */
    private static int getMeanThreshold(BufferedImage bufferedImage) {
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        int num = 0;
        int sum = 0;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = bufferedImage.getRGB(i, j);
                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                sum += gray;
                num += 1;
            }
        }
        int threshold = sum / num;
        if (threshold * 1.2 < 255) {
            threshold = (int) (1.2 * sum / num);
        }
        return threshold;
    }

    /**
     * get image border
     * @param bufferedImage
     * @return
     */
    public static BufferedImage getImageBorder(BufferedImage bufferedImage) {
        BufferedImage borderImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        int imgWidth = bufferedImage.getWidth();
        int imgHeight = bufferedImage.getHeight();
        for (int i = 1; i < imgWidth - 1; i++) {
            for (int j = 1; j < imgHeight - 1; j++) {
                // color
                int color = bufferedImage.getRGB(i, j);
                // upColor
                int upColor = bufferedImage.getRGB(i, j - 1);
                // downColor
                int downColor = bufferedImage.getRGB(i, j + 1);
                // leftColor
                int leftColor = bufferedImage.getRGB(i - 1, j);
                // rightColor
                int rightColor = bufferedImage.getRGB(i + 1, j);
                if (isQualified(color, upColor, downColor, leftColor, rightColor)) {
                    borderImage.setRGB(i, j, 0xFFFFFF);
                } else {
                    borderImage.setRGB(i, j, color);
                }
            }
        }
        return borderImage;
    }

    /**
     * is qualified
     * @param color
     * @param upColor
     * @param downColor
     * @param leftColor
     * @param rightColor
     * @return
     */
    public static boolean isQualified(int color, int upColor, int downColor, int leftColor, int rightColor) {
        return color != -1 && (Math.abs(color - upColor) < threshold
                && Math.abs(color - downColor) < threshold
                && Math.abs(color - leftColor) < threshold
                && Math.abs(color - rightColor) < threshold);
    }

    /**
     * clamp
     * @param value
     * @return
     */
    private static int clamp(int value) {
        return value > 255 ? 255 : (Math.max(value, 0));
    }

    /**
     * get Curve
     * @param x
     * @return
     */
    private static int getSCurve(int x) {
        double j = 255 / (1 + Math.exp(5 - x * 2/ 51.0));
        return (int) Math.round(j);
    }

    public static void main(String[] args) throws IOException {

    }
}
