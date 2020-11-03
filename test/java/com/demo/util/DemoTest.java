package com.demo.util;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class DemoTest {

    @Test
    public void testRand(){
        long base1 = 1L;
        long base2 = 2L;
        Random random1 = new Random(base1);
        Random random1Copy = new Random(base1);
        Random random2 = new Random(base2);
        Random randomBase = new Random();
        Random randomBaseCopy = new Random();
        int maxInt = 100;
        for (int i = 0; i < 100; i++){
            System.out.println(random1.nextInt(maxInt));
            System.out.println(random1Copy.nextInt(maxInt));
            System.out.println(random2.nextInt(maxInt));
            System.out.println(randomBase.nextInt(maxInt));
            System.out.println(randomBaseCopy.nextInt(maxInt));
            System.out.println(new Random().nextInt(maxInt));
            System.out.println("===============================");
        }
    }


    @Test
    public void testString() {
        String a = "123";
        String b = "123";
        Integer c = Integer.parseInt(a);
        int e = 123;
        System.out.println(a == b);
        System.out.println(a.equals(b));
        System.out.println(a.equals(c));
        System.out.println(a.equals(e));
        System.out.println(c.equals(a));
        System.out.println(c.equals(b));
        System.out.println(c == e);
        System.out.println(c.equals(e));
    }
}
