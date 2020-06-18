package com.wjh.utils.redis;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Classname RedisSerializer
 * @Description redis序列化
 * @Date 2020/6/17 下午2:32
 * @Created by wjh
 */
public interface RedisSerializer {
    public final static Charset CHARSET = Charset.forName("utf-8");

    byte[] serialize(Object object);

    <T> T deserialize(byte[] byteArray, Class<T> clazz);

    <E> List<E> deserializeForList(byte[] byteArray, Class<E> itemClazz);
}
