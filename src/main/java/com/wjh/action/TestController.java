package com.wjh.action;

import com.wjh.utils.ConventionVerifyUtil;

/**
 * @Classname TestController
 * @Description test
 * @Date 2020/2/24 上午10:54
 * @Created by wjh
 */
public class TestController {
    private static final String DATE_VERIFY = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";

    public static void main(String[] args) {
        System.out.println(ConventionVerifyUtil.checkPattern("2020-02-28", DATE_VERIFY));
    }
}
