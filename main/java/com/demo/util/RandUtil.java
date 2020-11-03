package com.demo.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 随机工具
 * @author dominate
 */
public class RandUtil {

    private static final Random BASE_RANDOM = new Random();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static int getRandInt(int min, int max) {
        return BASE_RANDOM.nextInt(max - min + 1) + min;
    }

    public static double getRandDouble() {
        return BASE_RANDOM.nextDouble();
    }

    public static boolean getRandBoolean() {
        return BASE_RANDOM.nextBoolean();
    }

    public static int getSecureRandInt(int min, int max) {
        return SECURE_RANDOM.nextInt(max - min + 1) + min;
    }
    public static double getSecureRandDouble() {
        return SECURE_RANDOM.nextDouble();
    }

    public static boolean getSecureRandBoolean() {
        return SECURE_RANDOM.nextBoolean();
    }

}
