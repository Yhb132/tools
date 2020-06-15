package com.wjh.action;

import java.util.BitSet;

/**
 * @Classname BitSetDome
 * @Description BitSet 使用测试：解决数据重复 是否存在
 * @Date 2020/2/25 下午2:23
 * @Created by wjh
 */
public class BitSetDome {

    /**
     * 求一个字符串包含的char
     *
     */
    public static void containChars(String str) {
        BitSet used = new BitSet();
        for (int i = 0; i < str.length(); i++)
            used.set(str.charAt(i));

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int size = used.size();
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            if (used.get(i)) {
                sb.append((char) i);
            }
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        containChars("Hi this is BitSetDome");
    }
}
