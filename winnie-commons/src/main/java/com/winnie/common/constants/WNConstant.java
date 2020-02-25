package com.winnie.common.constants;

import lombok.Data;

@Data
public class WNConstant {
    public static final String BRAND_IMG_FILE="D:\\it\\leyou\\nginx\\nginx-1.16.0\\html\\brand-logo";

    public static final String BRAND_IMG_URL="http://localhost/brand-logo/";

    public static final String CHECK_CODE_PRE = "CHECK_CODE_PRE";

    public static final String USER_ID_HEADER = "UserIdHeader";

    /**
     * 用户在redis中的购物车对象的key的前缀
     */
    public static final String CART_PRE = "CART_PRE";
    public static final String PAY_URL_PER = "PAY_URL_PER";
}
