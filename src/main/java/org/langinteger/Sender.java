package org.langinteger;

import org.langinteger.sender.SendHeartbeat;

public class Sender {

  public static void main(String[] args) throws InterruptedException {

    SendHeartbeat sendHeartbeat = new SendHeartbeat("client1", 10080);
    sendHeartbeat.start();

    Thread.currentThread().join();
  }
}