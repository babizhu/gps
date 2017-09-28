package com.srxk.net.gps.pojo;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 正常gps上传的数据包
 */
@Data
public class GpsDataFrame {

  private final String id;
  private final LocalDateTime dateTime;
  /**
   * 纬度
   */
  private final String atitude;
  /**
   * 电量
   */
  private final byte power;
  /**
   * 经度
   */
  private final String longitude;
  private final boolean isGps;
  private final int speed;
  private final int direction;
  /**
   * 剩余电量
   */


  private final short mcc;
  private final byte mnc;

  private final int lac;
  private final int cellId;

  private final byte recordId;

//
//  public static  LocalDateTime parseTimeStamp( byte[] timeStamp ){
//    return LocalDateTime.of(timeStamp[5] + 2000, timeStamp[4],timeStamp[3],timeStamp[0],timeStamp[1],timeStamp[2]);
//  }
  public static  LocalDateTime parseTimeStamp( String  timeStamp ){
    int year = Integer.parseInt(timeStamp.substring(10,12));
    int month = Integer.parseInt(timeStamp.substring(8,10));
    int dayOfMonth = Integer.parseInt(timeStamp.substring(6,8));
    int hour = Integer.parseInt(timeStamp.substring(0,2));
    int min = Integer.parseInt(timeStamp.substring(2,4));
    int sec = Integer.parseInt(timeStamp.substring(4,6));
    return LocalDateTime.of(year + 2000, month,dayOfMonth,hour,min,sec);
  }
}
