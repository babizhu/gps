package com.srxk.net.gps.server;

import com.srxk.net.gps.misc.PropertiesUtil;
import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * Created by liulaoye on 17-2-17. 启动类
 */
public class GpsServerBootstrap implements IServerBootstrap {

  /**
   * server 监听端口
   */
  private int port;
  private InetSocketAddress listenAddress;
  /**
   * idle 等待的时间（秒）
   */
  private int idleSecond;
  private String name;


  GpsServerBootstrap(Properties props) {
    this.withPort(PropertiesUtil.getInt(props, "port", DefaultGpsServer.PORT_DEFAULT))
        .withName(PropertiesUtil.getString(props, "name", DefaultGpsServer.SERVER_NAME_DEFAULT))
        .withIdleSecond(
            PropertiesUtil.getInt(props, "idleSecond", DefaultGpsServer.IDLE_SECOND_DEFAULT));
  }


  @Override
  public IServerBootstrap withName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public IServerBootstrap withAddress(InetSocketAddress address) {
    this.listenAddress = address;
    return this;
  }

  @Override
  public IServerBootstrap withPort(int port) {
    this.port = port;
    return this;
  }

  @Override
  public IServerBootstrap withIdleSecond(int idleConnectionTimeout) {
    idleSecond = idleConnectionTimeout;
    return this;
  }

  @Override
  public IServer start() {
    return build().start();
  }

  private DefaultGpsServer build() {
    return new DefaultGpsServer(determineListenAddress(), idleSecond,name);
  }

  private InetSocketAddress determineListenAddress() {
    if (listenAddress != null) {
      return listenAddress;
    } else {
      return new InetSocketAddress(port);
    }
  }
}
