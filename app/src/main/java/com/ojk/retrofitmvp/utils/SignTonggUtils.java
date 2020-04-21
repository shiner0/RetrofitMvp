package com.ojk.retrofitmvp.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 描述:签名代码
 */
public class SignTonggUtils {

    /**
     * 返回签名内容
     *
     * @return
     */
    public static String getSign(Map<String, String> params) {
        String sign = "";
        try {
            sign = ToolMD5Utils.getMD5(createSign(params, false) + "&Bsa=" + CommonTonggParams.DEVELOP_LAZYCARD_SECRET_KEY).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 构造签名
     *
     * @param params
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String createSign(Map<String, String> params, boolean encode) throws
            UnsupportedEncodingException {
        if (params == null) {
            return null;
        }
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if ("sign".equals(key) || "imgcont".equals(key) || "auximgstr".equals(key) ||
                    "imgFile".equals(key) || "image_action".equals(key)) {
                continue;
            }
            if (key == null || isEmpty(params.get(key))) // 参数为空不参与签名
                continue;
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = String.valueOf(value);
            }
            if (encode) {
                temp.append(URLEncoder.encode(valueString, "UTF-8"));
            } else {
                temp.append(valueString);
            }
        }

        return temp.toString();
    }

    /**
     * 获取32位随机字符串
     *
     * @return
     */
    public static String getRandom() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean isEmpty(String input) {
        return input == null || input.equals("") || input.matches("[\\s\\u00a0\\u2007\\u202f\\u0009-\\u000d\\u001c-\\u001f]+");
    }

}