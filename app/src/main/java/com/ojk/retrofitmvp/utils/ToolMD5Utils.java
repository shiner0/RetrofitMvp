package com.ojk.retrofitmvp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ToolMD5Utils {
    private static char[] DigitLower = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static char[] DigitUpper = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public ToolMD5Utils() {
    }

    public static String getMD5Lower(String srcStr) throws NoSuchAlgorithmException {
        String sign = "lower";
        return processStr(srcStr, sign);
    }

    protected static String getMD5Upper(String srcStr) throws NoSuchAlgorithmException {
        String sign = "upper";
        return processStr(srcStr, sign);
    }

    private static String processStr(String srcStr, String sign) throws NoSuchAlgorithmException, NullPointerException {
        String algorithm = "MD5";
        String result = "";
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(srcStr.getBytes());
        byte[] byteRes = digest.digest();
        int length = byteRes.length;

        for(int i = 0; i < length; ++i) {
            result = result + byteHEX(byteRes[i], sign);
        }

        return result;
    }

    private static String byteHEX(byte bt, String sign) {
        char[] temp = null;
        if (sign.equalsIgnoreCase("lower")) {
            temp = DigitLower;
        } else {
            if (!sign.equalsIgnoreCase("upper")) {
                throw new RuntimeException("加密缺少必要的条件");
            }

            temp = DigitUpper;
        }

        char[] ob = new char[]{temp[bt >>> 4 & 15], temp[bt & 15]};
        return new String(ob);
    }

    public static String getMD5(String content) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(content.getBytes("UTF-8"));
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for(int i = 0; i < byteArray.length; ++i) {
            if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    public static void main(String[] args) {
        String content = getMD5("ailuobi");
        System.out.println(content);
    }
}
