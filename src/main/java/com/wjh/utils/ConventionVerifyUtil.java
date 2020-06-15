package com.wjh.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @Classname conventionVerifyUtil
 * @Description 常规正则校验工具
 * @Date 2020/6/15 下午1:52
 * @Created by wjh
 */
public class ConventionVerifyUtil {
    //手机号
    private static final String PHONE_VERIFY = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    //身份证号
    private static final String IDCART_VERIFY = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
    //中文
    private static final String CHINESE_VERIFY = "[\\u4e00-\\u9fa5]+";
    //正整数
    private static final String Number_VERIFY = "^[1-9]\\d*$";
    //日期格式XXXX-XX-XX
    private static final String DATE_VERIFY = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";

    /**
     *
     * @param param 参数
     * @param pattern
     * @return
     */
    public static Boolean checkPattern(String param, String pattern) {
        if (StringUtils.isNotEmpty(param) && StringUtils.isNotEmpty(pattern)) {
            return Pattern.matches(pattern, param);
        }
        return false;
    }
}
