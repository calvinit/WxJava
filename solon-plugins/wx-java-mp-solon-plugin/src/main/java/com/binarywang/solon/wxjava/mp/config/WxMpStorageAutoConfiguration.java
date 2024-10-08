package com.binarywang.solon.wxjava.mp.config;

import com.binarywang.solon.wxjava.mp.enums.StorageType;
import com.binarywang.solon.wxjava.mp.properties.RedisProperties;
import com.binarywang.solon.wxjava.mp.properties.WxMpProperties;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.JedisWxRedisOps;
import me.chanjar.weixin.common.redis.WxRedisOps;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.WxMpHostConfig;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.core.AppContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.util.Pool;

import java.util.Set;

/**
 * 微信公众号存储策略自动配置.
 *
 * @author Luo
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WxMpStorageAutoConfiguration {
  private final AppContext applicationContext;

  private final WxMpProperties wxMpProperties;

  @Bean
  @Condition(onMissingBean=WxMpConfigStorage.class)
  public WxMpConfigStorage wxMpConfigStorage() {
    StorageType type = wxMpProperties.getConfigStorage().getType();
    WxMpConfigStorage config;
    switch (type) {
      case Jedis:
        config = jedisConfigStorage();
        break;
      default:
        config = defaultConfigStorage();
        break;
    }
    // wx host config
    if (null != wxMpProperties.getHosts() && StringUtils.isNotEmpty(wxMpProperties.getHosts().getApiHost())) {
      WxMpHostConfig hostConfig = new WxMpHostConfig();
      hostConfig.setApiHost(wxMpProperties.getHosts().getApiHost());
      hostConfig.setMpHost(wxMpProperties.getHosts().getMpHost());
      hostConfig.setOpenHost(wxMpProperties.getHosts().getOpenHost());
      config.setHostConfig(hostConfig);
    }
    return config;
  }

  private WxMpConfigStorage defaultConfigStorage() {
    WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
    setWxMpInfo(config);
    return config;
  }

  private WxMpConfigStorage jedisConfigStorage() {
    Pool<Jedis> jedisPool;
    if (wxMpProperties.getConfigStorage() != null && wxMpProperties.getConfigStorage().getRedis() != null
      && StringUtils.isNotEmpty(wxMpProperties.getConfigStorage().getRedis().getHost())) {
      jedisPool = getJedisPool();
    } else {
      jedisPool = applicationContext.getBean(JedisPool.class);
    }
    WxRedisOps redisOps = new JedisWxRedisOps(jedisPool);
    WxMpRedisConfigImpl wxMpRedisConfig = new WxMpRedisConfigImpl(redisOps,
      wxMpProperties.getConfigStorage().getKeyPrefix());
    setWxMpInfo(wxMpRedisConfig);
    return wxMpRedisConfig;
  }

  private void setWxMpInfo(WxMpDefaultConfigImpl config) {
    WxMpProperties properties = wxMpProperties;
    WxMpProperties.ConfigStorage configStorageProperties = properties.getConfigStorage();
    config.setAppId(properties.getAppId());
    config.setSecret(properties.getSecret());
    config.setToken(properties.getToken());
    config.setAesKey(properties.getAesKey());
    config.setUseStableAccessToken(wxMpProperties.isUseStableAccessToken());
    config.setHttpProxyHost(configStorageProperties.getHttpProxyHost());
    config.setHttpProxyUsername(configStorageProperties.getHttpProxyUsername());
    config.setHttpProxyPassword(configStorageProperties.getHttpProxyPassword());
    if (configStorageProperties.getHttpProxyPort() != null) {
      config.setHttpProxyPort(configStorageProperties.getHttpProxyPort());
    }
  }

  private Pool<Jedis> getJedisPool() {
    RedisProperties redis = wxMpProperties.getConfigStorage().getRedis();

    JedisPoolConfig config = new JedisPoolConfig();
    if (redis.getMaxActive() != null) {
      config.setMaxTotal(redis.getMaxActive());
    }
    if (redis.getMaxIdle() != null) {
      config.setMaxIdle(redis.getMaxIdle());
    }
    if (redis.getMaxWaitMillis() != null) {
      config.setMaxWaitMillis(redis.getMaxWaitMillis());
    }
    if (redis.getMinIdle() != null) {
      config.setMinIdle(redis.getMinIdle());
    }
    config.setTestOnBorrow(true);
    config.setTestWhileIdle(true);
    if (StringUtils.isNotEmpty(redis.getSentinelIps())) {
      Set<String> sentinels = Sets.newHashSet(redis.getSentinelIps().split(","));
      return new JedisSentinelPool(redis.getSentinelName(), sentinels);
    }

    return new JedisPool(config, redis.getHost(), redis.getPort(), redis.getTimeout(), redis.getPassword(),
      redis.getDatabase());
  }
}
