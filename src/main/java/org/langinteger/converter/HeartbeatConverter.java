package org.langinteger.converter;

import org.langinteger.entity.Heartbeat;

public class HeartbeatConverter {

  public static Heartbeat deserialize(String raw) {
    String[] msgs = raw.split(":");
    switch (msgs[0]) {
      case "HeartBeat":
        Heartbeat hb = new Heartbeat();
        hb.setSenderId(msgs[1]);
        return hb;
      default:
        throw new RuntimeException(msgs[0] + "should not be here");
    }
  }

  public static String serialize(Heartbeat event) {
    return String.format("%s:%s", "HeartBeat", event.getSenderId());
  }

}
