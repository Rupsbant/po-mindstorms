package penoplatinum.bluetooth;

/**
 * SimulatedGatewayClient
 * 
 * Implements the GatewayClient interface for use in the Simulator.
 * On the real Robot, this implementation contains logic to send everything
 * over the bluetooth connection to the actual Gateway. Here we short-cut this
 * step and directly integrate with the Gateway, using a simulated connection.
 * 
 * @author: Team Platinum
 */
import penoplatinum.gateway.Connection;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.gateway.MessageReceiver;
import penoplatinum.simulator.Robot;

public class RobotGatewayClient implements GatewayClient, MessageReceiver {
  // the robot we're the GatewayClient for

  private Robot robot;
  // we have a SimulatedConnection and a (Simulated)MQ
  private Connection connection;

  public RobotGatewayClient setRobot(Robot robot) {
    this.robot = robot;
    return this;
  }

  public GatewayClient useConnection(Connection conn) {
    this.connection = conn;
    return this;
  }

  public void run() {
    // we don't start a thread or a loop ... everything runs synchronously ?!
  }

  public void receive(String cmd) {
    this.robot.processCommand(cmd);
  }

  public void send(String msg, int channel) {
    this.connection.send(msg, channel);
  }
}
