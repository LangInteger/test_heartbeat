package org.langinteger;

import java.util.function.Consumer;
import org.langinteger.detector.DetectHeartbeat;

public class Detector {

  public static void main(String[] args) throws InterruptedException {

    Consumer<String> timeoutEventConsumer = clientId -> {
      System.out.println("client timeout: " + clientId);
    };
    DetectHeartbeat detectHeartbeat = new DetectHeartbeat(10080, timeoutEventConsumer);
    detectHeartbeat.start();

    Thread.currentThread().join();
  }
}