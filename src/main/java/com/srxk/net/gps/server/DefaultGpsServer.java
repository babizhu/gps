package com.srxk.net.gps.server;

import com.srxk.net.gps.handler.ChannelInitializerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by liulaoye on 17-2-17.
 * 缺省的服务器实现
 */
@Slf4j
public class DefaultGpsServer implements IServer{

    /**
     * 缺省的服务器名字
     */
    public static final String SERVER_NAME_DEFAULT = "GPS server";

    /**
     * 缺省的监听端口
     */
    public static final int PORT_DEFAULT = 9000;

    /**
     * 缺省的idle时间
     */
    public static final int IDLE_SECOND_DEFAULT = 10;



    /**
     * True when the server has already been stopped by calling {@link #stop()} or {@link #abort()}.
     */
    private final AtomicBoolean stopped = new AtomicBoolean( false );

    /**
     * 服务器名字
     */
    private final String serverName;

    private final int idleSecond;

    /**
     * Keep track of all channels created by this  server for later shutdown when the proxy is stopped.
     */
    private final ChannelGroup allChannels = new DefaultChannelGroup( "gps", GlobalEventExecutor.INSTANCE );
    private final Thread jvmShutdownHook = new Thread(() -> abort(), "GPS-SERVER-JVM-shutdown-hook" );

    //    private final int maxFramesize;
//    private final int lengthFieldOffset;
    private InetSocketAddress listenAddress;

    DefaultGpsServer(InetSocketAddress listenAddress, int idleSecond,
        String serverName){
        this.listenAddress = listenAddress;
        this.serverName = serverName;
        this.idleSecond = idleSecond;
    }


    public void stop(){

    }

    public void abort(){
        log.error("碰到无法解决的问题了，正在关闭服务器并回写数据，请不要进行任何操作！");
    }

    public InetSocketAddress getListenAddress(){
        return listenAddress;
    }

    public void setThrottle( long readThrottleBytesPerSecond, long writeThrottleBytesPerSecond ){

    }

    @Override
    public String getServerName() {
        return this.serverName;
    }

//    public int getMaxFrameSize(){
//        return maxFramesize;
//    }

    public static IServerBootstrap bootstrapFromFile( String path ){
        final File propsFile = new File( path );
        Properties props = new Properties();

        if( propsFile.isFile() ) {
            try( InputStream is = new FileInputStream( propsFile ) ) {
                props.load( is );
            } catch( final IOException e ) {
                log.warn( "Could not load props file?", e );
            }
        }

        return new GpsServerBootstrap( props );
    }

    IServer start(){

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        final ServerBootstrap b = new ServerBootstrap();
        b.group( boss, worker )
                .channel( NioServerSocketChannel.class )
//                .handler(new LoggingHandler( LogLevel.INFO))
                .childHandler( new ChannelInitializerHandler( this ) )
                .option( ChannelOption.SO_BACKLOG, 128 );

        ChannelFuture future = b.bind( listenAddress ).awaitUninterruptibly();

        Throwable cause = future.cause();
        if( cause != null ) {
            throw new RuntimeException( cause );
        }


        Runtime.getRuntime().addShutdownHook( jvmShutdownHook );
        return this;
    }


}
