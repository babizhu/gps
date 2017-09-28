package com.srxk.net.gps.server;

import java.net.InetSocketAddress;

/**
 * Created by liulaoye on 17-2-17.
 * 启动辅助类接口
 */
public interface IServerBootstrap{
    /**
     * <p>
     * Give the server a name (used for naming threads, useful for logging).
     * </p>
     * <p>
     * <p>
     * Default = GPS server
     * </p>
     *
     * @param name  name
     */
    IServerBootstrap withName(String name);


    /**
     * <p>
     * Listen for incoming connections on the given address.
     * </p>
     * <p>
     * <p>
     * Default = [bound ip]:8080
     * </p>
     *
     * @param address listen Address
     */
    IServerBootstrap withAddress(InetSocketAddress address);

    /**
     * <p>
     * Listen for incoming connections on the given port.
     * </p>
     * <p>
     * <p>
     * Default = 8080
     * </p>
     *
     * @param port port
     */
    IServerBootstrap withPort(int port);


    /**
     * <p>
     * Specify the timeout after which to disconnect idle connections, in
     * seconds.
     * </p>
     * <p>
     * <p>
     * Default = 70
     * </p>
     *
     * @param idleConnectionTimeout idleConnectionTimeout
     */
    IServerBootstrap withIdleSecond(int idleConnectionTimeout);



    /**
     * <p>
     * Build and starts the server.
     * </p>
     *
     * @return the newly built and started server
     */
    IServer start();

}
