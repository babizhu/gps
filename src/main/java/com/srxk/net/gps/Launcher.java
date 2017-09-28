package com.srxk.net.gps;

import com.srxk.net.gps.server.DefaultGpsServer;
import com.srxk.net.gps.server.IServer;
import com.srxk.net.gps.server.IServerBootstrap;
import lombok.extern.slf4j.Slf4j;

//
//final class Foo extends AbstractConstant<Foo> {
//
//  Foo(int id, String name) {
//    super(id, name);
//  }
//}
//
//final class MyConstants {
//
//  private static final ConstantPool<Foo> pool = new ConstantPool<Foo>() {
//    @Override
//    protected Foo newConstant(int id, String name) {
//      return new Foo(id, name);
//    }
//  };
//
//  public static Foo valueOf(String name) {
//    return pool.valueOf(name);
//  }
//
//  public static final Foo A = valueOf("A");
//  public static final Foo B = valueOf("B");
//}
//
//final class YourConstants {
//
//  public static final Foo C = MyConstants.valueOf("C");
//  public static final Foo D = MyConstants.valueOf("D");
//}
@Slf4j
public class Launcher {

  public static void main(String[] args) {

    log.info( "Beging to running GPS server\n" );
    IServerBootstrap bootstrap = DefaultGpsServer.bootstrapFromFile( "./server.properties" );

    final IServer server = bootstrap.start();
    log.info( server.getServerName() + server.getListenAddress() );
    System.out.println( "------------------------------------------------------------" );
    System.out.println( "------------------------------------------------------------" );
  }
}
