package com.demo.util;

import io.netty.util.internal.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Image processing tools
 * <p>
 * New features are being developed ...
 *
 * @author Dominate
 */
public class ImageUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * get image size for int array [width,height]
     *
     * @param file must be image file
     * @return [width, height]
     * @throws IOException
     */
    public static int[] getSize(File file) throws IOException {
        assert hasFileType(file.getName());
        Iterator<ImageReader> readers = ImageIO.getImageReadersBySuffix(getFileType(file.getName()));
        try (ImageInputStream in = new FileImageInputStream(file)) {
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                if (readers.hasNext()) {
                    LOG.warn("Found more than 1 reader of file:" + file.getName());
                }
                reader.setInput(in);
                return new int[]{reader.getWidth(0), reader.getHeight(0)};
            }
            throw new IOException("Unsupported File Type: " + file.getName());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * parse image major color for Hex RGB
     * when parse failed return empty string
     *
     * @param file must be image file
     * @return String for Hex RGB
     */
    public static String loadMajorColor(File file) {
        try {
            return findMajorColor(file);
        } catch (IOException e) {
            LOG.error("load image failed " + file.getName());
        }
        return "";
    }

    /**
     * parse image major color for Hex RGB
     *
     * @param file must be image file
     * @return String for Hex RGB
     * @throws IOException
     */
    public static String findMajorColor(File file) throws IOException {
        Map<String, Integer> colorMap = loadColorMap(file);
        return findMaxCountFromMap(colorMap);
    }

    /**
     * parse image color for Hex RGB with count
     *
     * @param file must be image file
     * @return HashMap Key - color Hex RGB, Value - color count
     * @throws IOException
     */
    public static Map<String, Integer> loadColorMap(File file) throws IOException {
        int[] imageSizes = getSize(file);
        BufferedImage image = ImageIO.read(file);
        Map<String, Integer> colorMap = new HashMap<>();
        for (int i = 0; i < imageSizes[0]; i++) {
            for (int j = 0; j < imageSizes[1]; j++) {
                // total 32 , the first 24 digits of the image
                int pixel = image.getRGB(i, j);
                String colorHex = Integer.toHexString((pixel & 0xffffff));
                if (!colorMap.containsKey(colorHex)) {
                    colorMap.put(colorHex, 0);
                }
                int colorCount = colorMap.get(colorHex) + 1;
                colorMap.put(colorHex, colorCount);
            }
        }
        return colorMap;
    }

    /**
     * 图片文件的前4字节是特征码。
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static ImageType getType(File file) throws IOException {
        assert file.isFile() : file.getAbsolutePath();
        byte[] bytes = new byte[4];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        ImageType type = ImageType.parseByBytes(bytes);
        if (type == ImageType.UNKNOWN) {
            throw new IOException("unknown image file " + file.getName());
        }
        return type;
    }

    public static void binaryImage(File file, double ratio) throws IOException {
        int[] imageSizes = getSize(file);
        BufferedImage image = ImageIO.read(file);
        int[][] greys = new int[imageSizes[0]][imageSizes[1]];
        int minGrey = 0;
        int maxGrey = 0;
        for (int i = 0; i < imageSizes[0]; i++) {
            for (int j = 0; j < imageSizes[1]; j++) {
                int thisGrey = parseGreyRGB(image.getRGB(i, j));
                greys[i][j] = thisGrey;
                if (thisGrey < minGrey) {
                    minGrey = thisGrey;
                }
                if (thisGrey > maxGrey) {
                    maxGrey = thisGrey;
                }
            }
        }
        int black = new Color(255, 255, 255).getRGB();
        int white = new Color(0, 0, 0).getRGB();
        int FZ = (int) ((maxGrey + minGrey) * ratio);
        for (int i = 0; i < imageSizes[0]; i++) {
            for (int j = 0; j < imageSizes[1]; j++) {
                if (greys[i][j] > FZ) {
                    image.setRGB(i, j, black);
                } else {
                    image.setRGB(i, j, white);
                }
            }
        }
        String typeName = getType(file).getName();
        try (FileOutputStream ops = new FileOutputStream(file)) {
            ImageIO.write(image, typeName, ops);
        }
    }


    private static int parseGreyRGB(int i) {
        String argb = Integer.toHexString(i);
        // argb - transparent,red,green,blue on 2 bits in hex
        int r = Integer.parseInt(argb.substring(2, 4), 16);
        int g = Integer.parseInt(argb.substring(4, 6), 16);
        int b = Integer.parseInt(argb.substring(6, 8), 16);
        int result = ((r + g + b) / 3);
        return result;
    }


    private static boolean hasFileType(String fileName) {
        return fileName.indexOf(".") > 0 && !fileName.endsWith(".");
    }

    private static String getFileType(String fileName) {
        assert hasFileType(fileName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private static String findMaxCountFromMap(Map<String, Integer> map) {
        int maxCount = 0;
        String majorRgb = "";
        for (String key : map.keySet()) {
            int thisCount = map.get(key);
            if (thisCount > maxCount) {
                maxCount = thisCount;
                majorRgb = key;
            }
        }
        return "#" + formatRGBHex(majorRgb);
    }

    private static String formatRGBHex(String rgbForHex) {
        final int hexLength = 6;
        if (rgbForHex.length() == hexLength) {
            return rgbForHex;
        }
        assert rgbForHex.length() <= hexLength;
        String targetStr = "";
        for (int i = 0; i < hexLength - rgbForHex.length(); i++) {
            targetStr += "0";
        }
        return targetStr + rgbForHex;
    }
}
