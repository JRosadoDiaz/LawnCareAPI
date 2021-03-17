package rosado.jose.lawncareproducer.configs;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
        redisConf.setHostName(System.getenv("REDIS_HOST"));
        redisConf.setPort(6379);
        redisConf.setPassword(System.getenv("REDIS_PASSWORD"));
        return new LettuceConnectionFactory(redisConf);
    }

    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager rcm = RedisCacheManager.create(redisConnectionFactory());
        rcm.setTransactionAware(true);
        return rcm;
    }
}
