package com.demo.util;

/**
 * 图片的格式识别用枚举
 * @author dominate
 */
public enum ImageType {
    /**
     * 未知类型
     */
    UNKNOWN(),

    /**
     * JPG格式图片
     */
    JPEG("FF D8 FF","jpg"),

    /**
     * PNG格式图片
     */
    PNG("89 50 4E 47","png"),

    /**
     * GIF格式图片
     */
    GIF("47 49 46 38","gif"),

    /**
     * BMP格式图片
     */
    BMP("42 4D","bmp"),

    /**
     * TIFF格式图片
     */
    TIFF("4D 4D", new String[]{"49 49"},"tiff");

    final private String hexSign;
    final private String[] otherHexSigns;
    final private String name;

    ImageType() {
        this.hexSign = "";
        this.otherHexSigns = new String[]{};
        this.name = "";
    }

    ImageType(String hexString, String name) {
        this.hexSign = hexString;
        this.otherHexSigns = new String[0];
        this.name = name;
    }

    ImageType(String hexString, String[] otherHexStrings, String name) {
        this.hexSign = hexString;
        this.otherHexSigns = otherHexStrings;
        this.name = name;

    }


    public String getHexSign() {
        return hexSign;
    }

    public String[] getOtherHexSigns() {
        return otherHexSigns;
    }

    public String getName() {
        return name;
    }

    public static ImageType parseByBytes(byte[] bytes){
        StringBuilder hexForCheck = new StringBuilder();
        for (int n = 0; n < bytes.length; n++) {
            String strHex = Integer.toHexString(bytes[n] & 0xFF);
            // each byte need two char if not enough is need "0"
            hexForCheck.append(((strHex.length() == 1) ? "0" + strHex : strHex).toUpperCase());
            hexForCheck.append(" ");
        }
        String forCheckStr = hexForCheck.toString();
        for (ImageType one : ImageType.values()) {
            if (one == UNKNOWN) {
                continue;
            }

            if (forCheckStr.startsWith(one.hexSign)) {
                return one;
            }

            for (String item : one.otherHexSigns) {
                if (forCheckStr.startsWith(item)) {
                    return one;
                }
            }
        }
        return ImageType.UNKNOWN;
    }



}
