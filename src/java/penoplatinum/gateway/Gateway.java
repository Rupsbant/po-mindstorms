package penoplatinum.gateway;

/**
 * Gateway
 * 
 * Connects to a Robot and dispatches all incoming information to log4j.
 *
 * Author: Team Platinum
 */

import org.apache.log4j.Logger;
import penoplatinum.bluetooth.SimulatedConnection;

public class Gateway {
  // setup some loggers
  static Logger modelLogger  = Logger.getLogger("model");  // 123
  static Logger wallsLogger  = Logger.getLogger("walls");  // 124
  static Logger valuesLogger = Logger.getLogger("values"); // 125
  static Logger agentsLogger = Logger.getLogger("agents"); // 126

  // the connection to the Robot
  BluetoothConnection source;
  private MQ mq;
  private MQMessageDispatcher mqDispatcher;

  // connects to a Robot (by bluetooth name => currently ignored)
  public Gateway connect(String name) {
    this.source = new BluetoothConnection();
    this.mqDispatcher = new MQMessageDispatcher(source.getConnection());
    return this;
  }

  // connects to a Robot (by bluetooth name => currently ignored)
  public Gateway connect(SimulatedConnection conn) {
    this.source = new BluetoothConnection(conn);
    this.mqDispatcher = new MQMessageDispatcher(conn);
    return this;
  }

  // start a loop that continues to fetch and dispatch messages
  public void start() {
    mqDispatcher.startMQDispatcher();
    System.out.println("Gateway:> Starting logging...");
    while (this.source.hasNext()) {
      String msg = source.getMessage();
      try {
        // TODO: this switch should be handled using polymorphism ;-)
        switch (source.getType()) {
          case 123:
            modelLogger.info(msg);
            break;
          case 124:
            wallsLogger.info(msg);
            break;
          case 125:
            valuesLogger.info(msg);
            break;
          case 126:
            agentsLogger.info(msg);
            break;
        }
      } catch (Exception e) {
        System.err.println("Failed to log message: " + msg);
      }
    }
  }
}