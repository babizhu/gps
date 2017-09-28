package com.srxk.net.gps.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.srxk.net.gps.pojo.GpsDataFrame;
import com.srxk.net.gps.pojo.HeartBeat;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class GpsMessageDecoderTest {

  @Test
  public void testFramesDecoded() {
    //                                               日期|     纬度   |电|      经度     | 速度方向|          保留                      | mcc |nc| lac| id   |序列号
    String content = "24 61 70 78 44 87 02 57 20 27 09 17 29 37 06 75 03 10 62 98 06 1E 00 00 00 FB FF BB FF 00 00 00 00 00 00 00 00 01 CC 00 33 38 81 E5 0C";
    String hearBeat =   "2A 48 51 2C 36 31 37 30 37 38 34 34 38 37 2C 56 "
        +               "36 2C 30 33 31 37 34 32 2C 56 2C 32 39 33 37 2E "
        +               "31 32 30 37 2C 4E 2C 31 30 36 32 39 2E 37 38 32 "
        +               "38 2C 45 2C 30 30 30 2E 30 30 2C 30 30 30 2C 32 "
        +               "37 30 39 31 37 2C 46 42 46 46 42 42 46 46 2C 34 "
        +               "36 30 2C 30 30 2C 31 33 31 31 32 2C 33 33 32 35 "
        +               "33 2C 38 39 38 36 30 32 42 39 31 39 31 37 39 30 "
        +               "33 30 36 35 34 34 23";

    String heartBeat1 = "2a 48 51 2c 36 31 37 30 37 38 34 34 38 37 2c 56 "
        +               "31 2c 30 36 35 39 30 36 2c 56 2c 32 39 33 37 2e "
        +               "30 38 33 31 2c 4e 2c 31 30 36 32 39 2e 38 32 30 "
        +               "33 2c 45 2c 30 30 30 2e 30 30 2c 30 30 30 2c 32 "
        +               "38 30 39 31 37 2c 46 46 46 46 42 42 46 46 2c 30 "
        +               "30 30 2c 30 30 2c 30 30 30 30 30 2c 30 30 30 30 "
        +               "23";
//    System.out.println(hearBeat.split(" ").length);
//    String str = "2481680000080436021007152234927306113543980E000000FBFFBBFF000000000000000001CC00286610BB00";
//    String str = "2461707844870257202709172937067503106298061E000000FBFFBBFF000000000000000001CC00333881E50C";
//    String content = str.substring(0,str.length()-1);
    ByteBuf buf = Unpooled.buffer();
//    byte[] input =

    for (String t : content.split(" ")) {
      short s = Short.parseShort(t, 16);
      buf.writeByte(s);
    }
    for (String t : heartBeat1.split(" ")) {
      short s = Short.parseShort(t, 16);
      buf.writeByte(s);
    }

    System.out.println(ByteBufUtil.hexDump(buf));
//    ByteBuf buf = Unpooled.buffer();
//    for (int i = 0; i < 9; i++) {
//      buf.writeByte(i);
//    }
    ByteBuf input = buf.duplicate();
    EmbeddedChannel channel = new EmbeddedChannel(new GpsMessageDecoder());
//
// write bytes
    assertTrue(channel.writeInbound(input));
//
    assertTrue(channel.finish());
//// read messages
    final GpsDataFrame o = channel.readInbound();
    log.debug(o.toString());


    assertEquals("6170784487",o.getId());
    assertEquals(LocalDateTime.parse("2017-09-27T02:57:20"),o.getDateTime());
    assertEquals("29370675",o.getAtitude());
    assertEquals(3,o.getPower());
    assertEquals("106298061",o.getLongitude());
    assertEquals(true,o.isGps());
    assertEquals(0,o.getSpeed());
    assertEquals(0,o.getDirection());
    assertEquals(460,o.getMcc());
    assertEquals(0,o.getMnc());
    assertEquals(13112,o.getLac());
    assertEquals(33253,o.getCellId());
    assertEquals(12,o.getRecordId());

    System.out.println("======================================================");
    final HeartBeat heartBeat = channel.readInbound();
    log.debug(heartBeat.toString());
    assertNull(channel.readInbound());
  }

  @Test
  public void ByteBufIndexOfTest(){
    ByteBuf buf = Unpooled.buffer();
    for (byte i = 0; i < 10; i++) {
      buf.writeByte(i);
    }
    final byte one = buf.readByte();
    final byte two = buf.readByte();
    final byte three = buf.readByte();
    System.out.println(one+" " +two+" " +three);
    final int pos = buf.indexOf(buf.readerIndex(), buf.writerIndex(), (byte) 7) - buf.readerIndex();
    final ByteBuf byteBuf = buf.readBytes(pos+1);
    while (byteBuf.isReadable()){
      System.out.println( byteBuf.readByte());
    }
  }

}