package com.demo.util;

import io.netty.util.internal.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Image processing tools
 *
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
        int startX = 0;
        int startY = 0;
        Map<String, Integer> colorMap = new HashMap<>();
        for (int i = startX; i < imageSizes[0]; i++) {
            for (int j = startY; j < imageSizes[1]; j++) {
                // 共32位 代表颜色的是前24位
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
        byte[] b = new byte[4];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(b, 0, b.length);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

        String hex = byteToHex(b);
        ImageType type = ImageType.parseByHex(hex);
        if (type == ImageType.UNKNOWN) {
            throw new IOException("unknown image type");
        }
        return type;
    }

    private static String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < bytes.length; n++) {
            String strHex = Integer.toHexString(bytes[n] & 0xFF);
            // each byte need two char if not enough is need "0"
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
        }
        return sb.toString().trim();
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
