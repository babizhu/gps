package com.srxk.net.gps.server;

import java.net.InetSocketAddress;

/**
 * Created by liulaoye on 17-2-17.
 */
public interface IServer{

    /**
     * Stops the server and all related clones. Waits for traffic to stop before shutting down.
     */
    void stop();

    /**
     * Stops the server and all related clones immediately, without waiting for traffic to stop.
     */
    void abort();

    /**
     * Return the address on which this proxy is listening.
     *
     * @return
     */
    InetSocketAddress getListenAddress();

    /**
     * <p>
     * Set the read/write throttle bandwidths (in bytes/second) for this proxy.
     * </p>
     *
     * @param readThrottleBytesPerSecond
     * @param writeThrottleBytesPerSecond
     */
    void setThrottle(long readThrottleBytesPerSecond, long writeThrottleBytesPerSecond);

    String getServerName();
}


