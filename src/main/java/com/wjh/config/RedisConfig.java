package com.wjh.config;

import com.wjh.utils.redis.RedisComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Configuration
@Slf4j
public class RedisConfig implements EnvironmentAware {

    private RelaxedPropertyResolver resolver;
    private static String HOST_NAME;
    private static Integer PORT;
    private static String PASSWORD;
    private static Integer MAXTOTAL;
    private static Integer MINIDLE;
    private static Integer MAXWAITMILLIS;
    private static Boolean TESTOBORROW;
    private static Boolean TESTONRETURN;
    private static Integer TIMEOUT;

    @Override
    public void setEnvironment(Environment environment) {
        this.resolver = new RelaxedPropertyResolver(environment, "spring.redis.");

        HOST_NAME = resolver.getProperty("host");
        PORT = Integer.valueOf(resolver.getProperty("port"));
        PASSWORD = resolver.getProperty("password");
        TIMEOUT = Integer.valueOf(resolver.getProperty("timeout"));
        MAXTOTAL = Integer.valueOf(resolver.getProperty("pool.max-total"));
        MINIDLE = Integer.valueOf(resolver.getProperty("pool.min-idle"));
        MAXWAITMILLIS = Integer.valueOf(resolver.getProperty("pool.max-wait-millis"));
        TESTOBORROW = Boolean.valueOf(resolver.getProperty("pool.test-on-borrow"));
        TESTONRETURN = Boolean.valueOf(resolver.getProperty("pool.test-on-return"));

    }

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAXTOTAL);
        config.setMaxIdle(MAXTOTAL);
        config.setMinIdle(MINIDLE);
        config.setMaxWaitMillis(MAXWAITMILLIS);
        config.setTestOnBorrow(TESTOBORROW);
        config.setTestOnReturn(TESTONRETURN);
        return config;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() throws Exception {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName(HOST_NAME);
        redisConnectionFactory.setPort(PORT);
        redisConnectionFactory.setPassword(PASSWORD);
        redisConnectionFactory.setTimeout(TIMEOUT);
        redisConnectionFactory.setUsePool(true);
        return redisConnectionFactory;
    }

    @Autowired
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Autowired
    @Bean(name = "jedisPool")
    public JedisPool jedis(JedisPoolConfig config) {
        return new JedisPool(config, HOST_NAME, PORT, TIMEOUT, PASSWORD);
    }

    @Autowired
    @Bean(name = "shareJedisPool")
    public ShardedJedisPool shareJedis(JedisPoolConfig config) {
        JedisShardInfo shardInfo = new JedisShardInfo(HOST_NAME, PORT, TIMEOUT);
        shardInfo.setPassword(PASSWORD);
        List<JedisShardInfo> list = new ArrayList<>();
        list.add(shardInfo);
        return new ShardedJedisPool(config, list);
    }

    @Bean(name = "redisComponent", destroyMethod = "destory")
    public RedisComponent redisComponent(ShardedJedisPool shareJedisPool) {
        RedisComponent redisComponent = new RedisComponent();
        redisComponent.setPool(shareJedisPool);
        return redisComponent;
    }
}
