package com.springboot.lock.utils;


import java.security.SecureRandom;
import java.util.UUID;

public class RandomUtils {

    private static final SecureRandom random = new SecureRandom();

    private static final char[] alphaNumbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};


    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    // 获取指定长度随机数
    public static String getRandomNumberic(int size) {
        String code = "";
        while (code.length() < size)
            code += (int) (Math.random() * 10);
        return code;
    }

    public static String getRandomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(alphaNumbers[random.nextInt(alphaNumbers.length)]);
        }
        return sb.toString();
    }


    public static Float getNum(Float min, Float max) {

        if (null == min && null == max) {
            return 0f;
        }
        if (null != min) {
            return min;
        }
        if (null != max) {
            return max;
        }

        Float v = max - min;
        Integer i = random.nextInt(v.intValue());
        return min + i.floatValue();


    }


    public static String getRandom(Integer num){
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<num;i++){
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String random = getRandom(5);
        System.out.println(random);
    }

}
