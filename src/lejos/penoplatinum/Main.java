package penoplatinum;

import penoplatinum.util.Utils;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.bluetooth.RobotGatewayClient;
import penoplatinum.driver.GhostDriver;
import penoplatinum.gateway.BluetoothConnection;
import penoplatinum.model.Reporter;
import penoplatinum.pacman.DashboardReporter;
import penoplatinum.pacman.GhostRobot;
import penoplatinum.pacman.GhostNavigator;
import penoplatinum.sensor.IRSeekerV2;

public class Main {

  public static void main(String[] args) throws Exception {
    GhostRobot robot = new GhostRobot("Michiel");
    robot.useNavigator(new GhostNavigator());
    robot.useDriver(new GhostDriver());

    final AngieEventLoop angie = new AngieEventLoop(robot);
    RobotBluetoothConnection conn = new RobotBluetoothConnection();
    conn.initializeConnection();
    Utils.EnableRemoteLogging(conn);
    BluetoothConnection conn2 = new BluetoothConnection(conn);

    final RobotGatewayClient robotBluetoothAgent = new RobotGatewayClient();
    robotBluetoothAgent.useConnection(conn2);

    robot.useGatewayClient(robotBluetoothAgent);
    
    Reporter reporter = new DashboardReporter();
    reporter.useGatewayClient(robotBluetoothAgent).setRobot(robot);
    //robotBluetoothAgent.run(); This is started in robot.useGatewayClient(agent)
//        initializeAgent(angie);
    Runnable runnable = new Runnable() {

      public void run() {
        Utils.Log("Started!");
        angie.runEventLoop();
      }
    };

    runnable.run();
  }

  private static boolean startMeasurement(IRSeekerV2 seeker, int[] angles, Motor m, int startAngle) {
    int count = 0;
    while (!Button.ESCAPE.isPressed()) {
      for (int i = 0; i < angles.length; i++) {
        m.rotateTo(startAngle + angles[i], false);
        int dir = seeker.getDirection();
        String str = count + "," + dir;
        for (int j = 1; j < 6; j++) {
          str += "," + seeker.getSensorValue(j);
        }
        Utils.Log(str);
        count++;
        Utils.Sleep(1000);
      }
    }
    return false;
  }
  static byte[] buf = new byte[1];

}