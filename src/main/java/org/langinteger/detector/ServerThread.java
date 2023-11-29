package org.langinteger.detector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import org.langinteger.converter.HeartbeatConverter;
import org.langinteger.entity.Heartbeat;

public class ServerThread extends Thread {

  private final int serverHostPort;
  private final Consumer<Heartbeat> heartbeatConsumer;

  public ServerThread(int port, Consumer<Heartbeat> heartbeatConsumer) {
    this.serverHostPort = port;
    this.heartbeatConsumer = heartbeatConsumer;
  }

  public void run() {
    try (ServerSocket serverSocket = new ServerSocket(serverHostPort)) {
      System.out.println("Server listening on port " + serverHostPort);
      while (true) {
        try (Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()))) {

          String inputLine;
          while ((inputLine = in.readLine()) != null) {
            System.out.println("Received message: " + inputLine);
            Heartbeat event = HeartbeatConverter.deserialize(inputLine);
            heartbeatConsumer.accept(event);
          }
        } catch (IOException e) {
          System.out.println("Error handling client connection: " + e.getMessage());
        }
      }

    } catch (IOException e) {
      System.out.println("Error creating server socket: " + e.getMessage());
    }
  }
}