package com.demo.util;

public enum ImageType {
    UNKNOWN(),

    JPEG("FF D8 FF"),

    PNG("89 50 4E 47"),

    GIF("47 49 46 38"),

    BMP("42 4D"),

    TIFF("4D 4D", new String[]{
            "49 49"
    });

    final private String hexSign;
    final private String[] otherHexSigns;

    ImageType() {
        hexSign = "";
        otherHexSigns = new String[]{};
    }

    ImageType(String hexString) {
        this.hexSign = hexString;
        this.otherHexSigns = new String[0];
    }

    ImageType(String hexString, String[] otherHexStrings) {
        this.hexSign = hexString;
        this.otherHexSigns = otherHexStrings;
    }


    public String getHexSign() {
        return hexSign;
    }

    public String[] getOtherHexSigns() {
        return otherHexSigns;
    }

    public static ImageType parseByHex(String hex) {
        for (ImageType one : ImageType.values()) {
            if (one == UNKNOWN) {
                continue;
            }

            if (hex.startsWith(one.hexSign)) {
                return one;
            }

            for (String item : one.otherHexSigns) {
                if (hex.startsWith(item)) {
                    return one;
                }
            }
        }
        return ImageType.UNKNOWN;
    }

}
