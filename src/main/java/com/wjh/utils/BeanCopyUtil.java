package com.wjh.utils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Classname BeanCopyUtil
 * @Description bean拷贝工具
 * @Date 2020/2/24 下午3:56
 * @Created by wjh
 */
public class BeanCopyUtil {

    /**
     * 将src对象的属性拷贝到dst对象，内部实现使用BeanCopier，主要用于减少使用BeanCopier的代码
     * <p>只有当src和dst都不为null的时候才会进行拷贝，否则原对象不做任何改变</p>
     * <p><b>注意：</b>该方法只能复制相同类型相同名称的数据</p>
     *
     * @param src 原
     * @param dst 目标
     */
    public static void copy(Object src, Object dst) {
        if (src != null && dst != null) {
            BeanCopier.create(src.getClass(), dst.getClass(), false).copy(src, dst, null);
        }
    }

    /**
     * 转换List
     *
     * @param srcList
     * @param dstClz
     * @param <T>
     * @return
     */
    public static <T> List<T> convertList(List srcList, Class<T> dstClz) {
        if (CollectionUtils.isEmpty(srcList)) return new ArrayList<>();

        List<T> dstList = new ArrayList<>();
        for (Object src : srcList) {
            try {
                T dst = dstClz.newInstance();
                BeanCopyUtil.copy(src, dst);
                dstList.add(dst);
            } catch (Exception e) {
//                throw new BizException("类型转换异常");
            }
        }
        return dstList;
    }

    public static <E, T> List<E> transferList(List<? extends T> source, Class<E> clz) {
        List<E> result = new LinkedList<>();
        if (null == source)
            return result;

        for (T item : source) {
            if (item.getClass().isAssignableFrom(clz)) {
                try {
                    E instance = clz.newInstance();
                    BeanUtils.copyProperties(item, instance);
                    result.add(instance);
                } catch (InstantiationException e) {
                    break;
                } catch (IllegalAccessException e) {
                    break;
                }
            }

        }
        return result;
    }

    public static <E, T> E transfer(T source, Class<E> clz) {
        try {
            E instance = clz.newInstance();
            if (source.getClass().isAssignableFrom(clz))
                BeanUtils.copyProperties(source, instance);
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}
