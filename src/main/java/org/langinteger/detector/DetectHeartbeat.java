package org.langinteger.detector;

import java.util.Timer;
import java.util.function.Consumer;
import lombok.Data;
import org.langinteger.timer.WorkTimer;
import org.langinteger.entity.Heartbeat;

@Data
public class DetectHeartbeat {

  private static final int interval = 3000;

  private final int port;
  private final Consumer<String> timeoutConsumer;
  private Timer timer;
  private ServerThread serverThread;

  public DetectHeartbeat(int port, Consumer<String> timeoutConsumer) {
    this.port = port;
    this.timeoutConsumer = timeoutConsumer;
  }

  public void start() {
    Consumer<Heartbeat> heartbeatEventConsumer = (Heartbeat event) -> {
      String senderId = event.getSenderId();
      System.out.println("receive heartbeat from: " + senderId);
      receiveHeartbeat(senderId);
    };
    this.serverThread = new ServerThread(this.port, heartbeatEventConsumer);
    serverThread.setDaemon(true);
    serverThread.start();
  }

  public synchronized void receiveHeartbeat(String id) {
    if (timer == null) {
      scheduleCheckTask(id);
    } else {
      rescheduleCheckTask(id);
    }
  }

  private void rescheduleCheckTask(String id) {
    cancelTask();
    scheduleCheckTask(id);
  }

  private void cancelTask() {
    timer.cancel();
    timer.purge();
  }

  private void scheduleCheckTask(String id) {
    this.timer = new Timer("Timer", true);
    WorkTimer workTimer = new WorkTimer(id, interval, this::timeout);
    timer.schedule(workTimer, interval);
  }

  private void timeout(String id) {
    timeoutConsumer.accept(id);
  }
}

