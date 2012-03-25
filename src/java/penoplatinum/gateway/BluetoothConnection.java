package penoplatinum.gateway;

/**
 * BluetoothConnection
 * 
 * Functional wrapper around technical Bluetooth layer
 *
 * Author: Team Platinum
 */
import java.io.*;
import java.util.Scanner;


import java.util.logging.Level;
import java.util.logging.Logger;
import penoplatinum.bluetooth.*;
import penoplatinum.Config;
import penoplatinum.util.Utils;

public class BluetoothConnection implements Connection {

  private static String log4jChannel = "BluetoothConnection";
  //private static Logger logger = Logger.getLogger(log4jChannel);
  private IConnection connection;
  private QueuedPacketTransporter endPoint;
  private String nextMsg = "";
  private int nextType = 0;

  public BluetoothConnection(IConnection connection) {
    connection.initializeConnection();
    this.connection = connection;
    initTransporter();
  }

  public BluetoothConnection setName(String name) {
    // TODO: take name of robot in account when connecting
    return this;
  }

  public final void initTransporter() {
    this.endPoint = new QueuedPacketTransporter(this.connection);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_MODEL);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_WALLS);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_VALUES);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_AGENTS);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_LOG);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_GHOST_PROTOCOL);
  }

  public BluetoothConnection send(String msg, int channel) {
    try {
      this.endPoint.getSendStream().write(msg.getBytes());
      this.endPoint.SendPacket(channel);
    } catch (IOException ex) {
      //logger.error( "Could not send message to channel : " + channel );
      Utils.Log(log4jChannel + ": Could not send message:" + channel);
    }
    return this;
  }

  public boolean hasNext() {
    int packet;
    String data;
    Boolean logging = true;

    try {
      while (logging) {
        packet = this.endPoint.ReceivePacket();
        data = this.endPoint.getReceiveStream().readLine();
        if (data.length() > 10) {
          this.nextType = packet;
          this.nextMsg = data;
          return true;
        }
      }
    } catch (IOException ex) {
      Utils.Log("Endpoint throwed IOException");
    }
    this.nextType = 0;
    return false;
  }

  public int getType() {
    return this.nextType;
  }

  public String getMessage() {
    return this.nextMsg;
  }

  public IConnection getConnection() {
    return connection;
  }
}
