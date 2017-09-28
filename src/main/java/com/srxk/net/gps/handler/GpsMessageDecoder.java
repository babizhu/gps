package com.srxk.net.gps.handler;

import com.srxk.net.gps.pojo.GpsDataFrame;
import com.srxk.net.gps.pojo.HeartBeat;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

public class GpsMessageDecoder extends ReplayingDecoder {

  private static final int HEART_BEAT_LEN = 118;//不包含flag位
  private static final byte HEART_BEAT_END_MARK = 35;//ascii 的 #

//  private static final int GPS_DATA_FRAME_LEN = 45;

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    byte flag = in.readByte();
    if (flag == 0x24) {//gps数据包
      out.add(parseGpsDataFrame(in));
      return;

    } else if (flag == 0x2a) {//心跳包
      int pos = in.indexOf(in.readerIndex(), in.writerIndex(), HEART_BEAT_END_MARK);
      if (pos != -1) {

        int len = pos - in.readerIndex();
        len += 1;//算上分隔符自身
        final ByteBuf buf = in.readBytes(len);
        out.add(parseHeartBeat(buf));
      }

    } else {
      throw new DecoderException("无法解析");
    }
  }

  private HeartBeat parseHeartBeat(ByteBuf in) {
//    final String heartBeat = ByteBufUtil.hexDump(in, 0, 119);
//    final String charSequence = in.readCharSequence(HEART_BEAT_LEN, Charset.defaultCharset()).toString();
    return new HeartBeat(in.toString(Charset.defaultCharset()));

  }

  /**
   * 解析正常的数据包
   */
  private GpsDataFrame parseGpsDataFrame(ByteBuf in) {
    String id = ByteBufUtil.hexDump(in.readBytes(5));
    String timeBytes = ByteBufUtil.hexDump(in.readBytes(6));
//    byte[] timeBytes = new byte[6];
//    in.readBytes(timeBytes);
    final LocalDateTime dateTime = GpsDataFrame.parseTimeStamp(timeBytes);
    String atitude = ByteBufUtil.hexDump(in.readBytes(4));
    byte power = in.readByte();

    byte[] wBytes = new byte[5];
    in.readBytes(wBytes);

    String longitude = ByteBufUtil.hexDump(wBytes);
    longitude = longitude.substring(0, longitude.length() - 1);//去掉最后一位

    final byte[] bits = byteToBit(wBytes[4]);
    boolean isGps = bits[6] == 1;

    String temp = ByteBufUtil.hexDump(in.readBytes(3));
    int speed = Integer.parseInt(temp.substring(0, 2));
    int direction = Integer.parseInt(temp.substring(3, temp.length() - 1));
    in.skipBytes(12);
    short mcc = in.readShort();
    byte mnc = in.readByte();
    int lac = in.readUnsignedShort();
    int cellId = in.readUnsignedShort();
    byte recordId = in.readByte();
    return new GpsDataFrame(id, dateTime, atitude, power, longitude, isGps, speed, direction, mcc,
        mnc, lac, cellId, recordId);

  }

  private static byte[] byteToBit(byte b) {
    byte[] ret = new byte[8];
    ret[0] = (byte) ((b >> 7) & 0x1);
    ret[1] = (byte) ((b >> 6) & 0x1);
    ret[2] = (byte) ((b >> 5) & 0x1);
    ret[3] = (byte) ((b >> 4) & 0x1);
    ret[4] = (byte) ((b >> 3) & 0x1);
    ret[5] = (byte) ((b >> 2) & 0x1);
    ret[6] = (byte) ((b >> 1) & 0x1);
    ret[7] = (byte) ((b) & 0x1);
    return ret;
//    return ""
//        + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
//        + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
//        + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
//        + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
  }
}
