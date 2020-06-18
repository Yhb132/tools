package com.wjh.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * @Classname ByteSerializer
 * @Description ByteSerializer
 * @Date 2020/6/17 下午2:33
 * @Created by wjh
 */
public class ByteSerializer implements RedisSerializer {

    private final static Logger logger = LoggerFactory.getLogger(ByteSerializer.class);

    /**
     * @param object
     * @return
     * @see com.wjh.utils.redis.RedisSerializer#serialize(java.lang.Object)
     */
    @Override
    public byte[] serialize(Object object) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            logger.error("redis序列化对象失败", ex);
        }
        return bytes;
    }

    /**
     * @param byteArray
     * @param clazz
     * @return
     * @see com.wjh.utils.redis.RedisSerializer#deserialize(byte[], java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] byteArray, Class<T> clazz) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
            return (T) obj;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @param byteArray
     * @param itemClazz
     * @return
     * @see com.wjh.utils.redis.RedisSerializer#deserializeForList(byte[], java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <E> List<E> deserializeForList(byte[] byteArray, Class<E> itemClazz) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
            return (List<E>) obj;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
