package com.wjh.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 信息脱敏工具类
 *
 * @author
 */
public class InfoHiddenUtil {
    /**
     * 姓名脱敏
     *
     * @param userName
     * @return
     */
    public static String hiddenUserName(String userName) {
        StringBuffer hiddenUserName = new StringBuffer();
        if (StringUtils.isNotBlank(userName) && userName.length() > 1) {
            hiddenUserName = hiddenUserName.append(userName.substring(0, 1));
            for (int i = 0; i < userName.length() - 1; i++) {
                hiddenUserName.append("*");
            }
        }
        return hiddenUserName.toString();
    }

    /**
     * 身份证号脱敏
     *
     * @param idCardNo
     * @return
     */
    public static String hiddenIdCard(String idCardNo) {
        StringBuffer hiddenIdCard = new StringBuffer();
        if (StringUtils.isNotBlank(idCardNo) && idCardNo.length() == 18) {
            hiddenIdCard.append(idCardNo.substring(0, 3)).append("***********").append(idCardNo.substring(14, 18));
        }
        if (StringUtils.isNotBlank(idCardNo) && idCardNo.length() == 15) {
            hiddenIdCard.append(idCardNo.substring(0, 3)).append("*********").append(idCardNo.substring(12, 15));
        }
        return hiddenIdCard.toString();
    }

    /****
     * 隐藏房屋地址
     *
     * @param houseAddress
     * @return
     */
    public static String hiddenAddress(String houseAddress) {
        StringBuffer hiddenHousAddress = new StringBuffer();
        if (StringUtils.isNotBlank(houseAddress) && houseAddress.length() > 2) {
            for (int i = 0; i < houseAddress.length(); i++) {
                char a = houseAddress.charAt(i);
                if (i > 2 && i != houseAddress.length() - 1) {
                    hiddenHousAddress.append("*");
                } else {
                    hiddenHousAddress.append(a);
                }
            }
        } else {
            hiddenHousAddress = new StringBuffer(houseAddress);
        }
        return hiddenHousAddress.toString();
    }

    /**
     * 手机号脱敏
     *
     * @param mobile
     * @return
     */
    public static String hiddenMobile(String mobile) {
        StringBuffer hiddenMobile = new StringBuffer();
        if (StringUtils.isNotBlank(mobile) && mobile.length() > 8) {
            hiddenMobile.append(mobile.substring(0, mobile.length() - 8)).append("****").append(mobile.substring(mobile.length() - 4, mobile.length()));
        }
        return hiddenMobile.toString();
    }

    /**
     * 车牌号脱敏
     *
     * @param license
     * @return
     */
    public static String hiddenCarLicense(String license) {
        StringBuffer hiddenLicense = new StringBuffer();
        if (StringUtils.isNotBlank(license) && license.length() == 7) {
            hiddenLicense.append(license.substring(0, 4)).append("**").append(license.substring(6, 7));
        }
        return hiddenLicense.toString();
    }

    /***
     * 银行卡号脱敏
     *
     * @param bankCardNo
     * @return
     */
    public static String hiddenBankCard(String bankCardNo) {
        StringBuffer hiddenBankCardNo = new StringBuffer();
        if (StringUtils.isNotBlank(bankCardNo) && bankCardNo.length() > 4) {
            for (int i = 0; i < bankCardNo.length() - 4; i++) {
                hiddenBankCardNo.append("*");
            }
            hiddenBankCardNo.append(bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length()));
        }
        return hiddenBankCardNo.toString();
    }
}
