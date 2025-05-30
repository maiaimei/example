package org.example.autoconfigure;

import lombok.Data;

/**
 * Properties for configuring a data source.
 * <p>
 * 用于配置数据源的属性类
 * </p>
 *
 * <p>
 * This class contains properties for the data source, including the name, URL, username, password, driver class name, type, and
 * connection pool configuration.
 * </p>
 */
@Data
public class DataSourceProperties {

  /**
   * The name of the data source.
   * <p>
   * 数据源的名称
   * </p>
   */
  private String name;

  /**
   * The URL of the database to connect to.
   * <p>
   * 数据库连接的URL
   * </p>
   */
  private String url;

  /**
   * The username for the database connection.
   * <p>
   * 数据库连接的用户名
   * </p>
   */
  private String username;

  /**
   * The password for the database connection.
   * <p>
   * 数据库连接的密码
   * </p>
   */
  private String password;

  /**
   * The fully qualified class name of the JDBC driver.
   * <p>
   * JDBC驱动的全限定类名
   * </p>
   */
  private String driverClassName;

  /**
   * The type of the connection pool.
   * <p>
   * 连接池类型的全限定类名
   * </p>
   */
  private String type;

  /**
   * The connection pool configuration.
   * <p>
   * 连接池配置
   * </p>
   */
  private Pool pool = new Pool();

  @Data
  public static class Pool {

    /**
     * The name of the connection pool.
     * <p>
     * 连接池的名称
     * </p>
     */
    private String poolName;

    /**
     * The initial number of connections that are created when the pool is started.
     * <p>
     * 连接池启动时创建的初始连接数
     * </p>
     */
    private int initialPoolSize = 5;

    /**
     * The minimum number of idle connections that maintain in the pool, including both idle and in-use connections.
     * <p>
     * 连接池维持的最小连接数，包括空闲和正在使用的连接。
     * <p>
     * 这个值可以帮助提高性能，因为它预先创建了连接，避免了频繁创建连接的开销。
     */
    private int minimumPoolSize = 5; // Default: 5 connections 默认值：5个连接

    /**
     * The maximum size that the pool is allowed to reach, including both idle and in-use connections. When the pool reaches this
     * size, and no idle connections are available, calls to getConnection() will block until a connection is available or the
     * connectionTimeoutMs is reached.
     * <p>
     * 连接池允许达到的最大连接数，包括空闲和正在使用的连接。
     * <p>
     * 当池达到这个大小且没有空闲连接时，获取连接的请求将阻塞等待，直到有可用连接 或达到连接超时时间。
     */
    private int maximumPoolSize = 20; // Default: 20 connections 默认值：20个连接

    /**
     * The maximum number of milliseconds that a client will wait for a connection from the pool. If this time is exceeded without a
     * connection becoming available, a SQLException will be thrown.
     * <p>
     * 客户端从连接池获取连接的最大等待时间（毫秒）。
     * <p>
     * 如果超过这个时间仍未获取到可用连接，将抛出SQLException异常。
     */
    private long connectionTimeoutMs = 60000; // Default: 60 seconds 默认值：60秒

    /**
     * The maximum amount of time (in milliseconds) that a connection is allowed to sit idle in the pool. Connections that are idle
     * for longer than this period will be removed by the idle connection checker.
     * <p>
     * 连接池中连接允许空闲的最大时间（毫秒）。
     * <p>
     * 超过这个时间的空闲连接会被空闲连接检测线程清理掉。
     */
    private long connectionIdleTimeoutMs = 300000; // Default: 5 minutes 默认值：5分钟

    /**
     * The time interval in milliseconds between two consecutive runs of the idle connection checker. This checker removes idle
     * connections that exceed the configured idle timeout.
     * <p>
     * 空闲连接检测线程运行的时间间隔（毫秒）。
     * <p>
     * 检测线程会定期检查并清理超过最大空闲时间的连接。
     */
    private long idleConnectionCheckIntervalMs = 60000; // Default: 60 seconds 默认值：60秒
  }
}
