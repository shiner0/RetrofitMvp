package com.ojk.retrofitmvp.utils;

import java.util.Map;
import java.util.UUID;

public class FixedTonggUtils {


    public static Map<String, String> sign(Map<String, String> map) {
        map.put(CommonTonggParams.PARAMS_NONCE, SignTonggUtils.getRandom());
        map.put(CommonTonggParams.PARAMS_SIGN, SignTonggUtils.getSign(map));
        return map;
    }

}
