package com.binarywang.solon.wxjava.open.properties;

import lombok.Data;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

import java.io.Serializable;

import static com.binarywang.solon.wxjava.open.properties.WxOpenProperties.PREFIX;
import static com.binarywang.solon.wxjava.open.properties.WxOpenProperties.StorageType.memory;


/**
 * 微信接入相关配置属性.
 *
 * @author someone
 */
@Data
@Configuration
@Inject("${"+PREFIX+"}")
public class WxOpenProperties {
  public static final String PREFIX = "wx.open";

  /**
   * 设置微信开放平台的appid.
   */
  private String appId;

  /**
   * 设置微信开放平台的app secret.
   */
  private String secret;

  /**
   * 设置微信开放平台的token.
   */
  private String token;

  /**
   * 设置微信开放平台的EncodingAESKey.
   */
  private String aesKey;

  /**
   * 存储策略.
   */
  private ConfigStorage configStorage = new ConfigStorage();


  @Data
  public static class ConfigStorage implements Serializable {
    private static final long serialVersionUID = 4815731027000065434L;

    /**
     * 存储类型.
     */
    private StorageType type = memory;

    /**
     * 指定key前缀.
     */
    private String keyPrefix = "wx:open";

    /**
     * redis连接配置.
     */
    private WxOpenRedisProperties redis = new WxOpenRedisProperties();

    /**
     * http客户端类型.
     */
    private HttpClientType httpClientType = HttpClientType.httpclient;

    /**
     * http代理主机.
     */
    private String httpProxyHost;

    /**
     * http代理端口.
     */
    private Integer httpProxyPort;

    /**
     * http代理用户名.
     */
    private String httpProxyUsername;

    /**
     * http代理密码.
     */
    private String httpProxyPassword;

    /**
     * http 请求重试间隔
     * <pre>
     *   {@link me.chanjar.weixin.mp.api.impl.BaseWxMpServiceImpl#setRetrySleepMillis(int)}
     *   {@link cn.binarywang.wx.miniapp.api.impl.BaseWxMaServiceImpl#setRetrySleepMillis(int)}
     * </pre>
     */
    private int retrySleepMillis = 1000;
    /**
     * http 请求最大重试次数
     * <pre>
     *   {@link me.chanjar.weixin.mp.api.impl.BaseWxMpServiceImpl#setMaxRetryTimes(int)}
     *   {@link cn.binarywang.wx.miniapp.api.impl.BaseWxMaServiceImpl#setMaxRetryTimes(int)}
     * </pre>
     */
    private int maxRetryTimes = 5;

  }

  public enum StorageType {
    /**
     * 内存.
     */
    memory,
    /**
     * jedis.
     */
    jedis,
    /**
     * redisson.
     */
    redisson,
    /**
     * redistemplate
     */
    redistemplate
  }

  public enum HttpClientType {
    /**
     * HttpClient.
     */
    httpclient
  }
}
