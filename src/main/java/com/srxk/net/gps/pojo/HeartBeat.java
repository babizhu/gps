package com.srxk.net.gps.pojo;

import lombok.Data;


/**
 * 心跳包
 */
@Data
public class HeartBeat {

  private final String heartBeat;

  public HeartBeat(String heartBeat) {
    this.heartBeat = heartBeat;
  }
}
