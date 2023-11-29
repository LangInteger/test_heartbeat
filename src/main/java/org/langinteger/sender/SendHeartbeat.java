package org.langinteger.sender;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import lombok.Data;
import org.langinteger.converter.HeartbeatConverter;
import org.langinteger.timer.WorkTimer;
import org.langinteger.entity.Heartbeat;

@Data
public class SendHeartbeat {

  private static final int interval = 2000;
  private final int targetPort;
  private final String clientId;
  private final String targetAddress = "127.0.0.1";
  private Timer timer;

  public SendHeartbeat(String id, int targetPort) {
    this.clientId = id;
    this.targetPort = targetPort;
  }

  public void start() {
    scheduleSendTask();
  }

  private void rescheduleCheckTask() {
    cancelTask();
    scheduleSendTask();
  }

  private void cancelTask() {
    timer.cancel();
    timer.purge();
  }

  private void scheduleSendTask() {
    this.timer = new Timer("Timer", true);
    WorkTimer workTimer = new WorkTimer(clientId, interval, this::sendAndReschedule);
    System.out.println("schedule send task");
    timer.schedule(workTimer, interval);
  }

  private void sendAndReschedule(String id) {
    send(id);
    rescheduleCheckTask();
  }

  private void send(String id) {
    Heartbeat heartbeat = new Heartbeat();
    heartbeat.setSenderId(id);
    String data = HeartbeatConverter.serialize(heartbeat);

    try (Socket socket = new Socket(targetAddress, targetPort)) {
      OutputStream outputStream = socket.getOutputStream();
      PrintWriter printWriter = new PrintWriter(outputStream);
      System.out.println("send heartbeat by " + id);
      printWriter.write(data);
      printWriter.flush();
    } catch (Exception ex) {
      System.out.println("exception happen: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}

