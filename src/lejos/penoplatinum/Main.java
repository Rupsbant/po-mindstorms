package penoplatinum;

import java.io.File;
import penoplatinum.util.Utils;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.bluetooth.RobotBluetoothGatewayClient;
import penoplatinum.driver.ManhattanDriver;
import penoplatinum.driver.behaviour.BarcodeDriverBehaviour;
import penoplatinum.driver.behaviour.FrontProximityDriverBehaviour;
import penoplatinum.driver.behaviour.LineDriverBehaviour;
import penoplatinum.driver.behaviour.SideProximityDriverBehaviour;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.navigator.GhostNavigator;
import penoplatinum.navigator.Navigator;
import penoplatinum.reporter.DashboardReporter;
import penoplatinum.robot.GhostRobot;

public class Main {

  public static void main(String[] args) throws Exception {
    GhostRobot robot = new GhostRobot("PLATINUM");
    ManhattanDriver manhattan = new ManhattanDriver(0.4)
	    .addBehaviour(new FrontProximityDriverBehaviour())
            .addBehaviour(new SideProximityDriverBehaviour())
            .addBehaviour(new BarcodeDriverBehaviour())
            .addBehaviour(new LineDriverBehaviour());
    robot.useDriver(manhattan);

    Navigator navigator = new GhostNavigator();
    robot.useNavigator(navigator);

    GatewayClient gateway = new RobotBluetoothGatewayClient();
    robot.useGatewayClient(gateway);
    
    //MessageModelPart.from(robot.getModel()).setProtocolHandler(new GhostProtocolHandler);
    
    robot.useReporter(new DashboardReporter());

    robot.handleActivation();

    final AngieEventLoop angie = new AngieEventLoop(robot);
    RobotBluetoothConnection conn = new RobotBluetoothConnection();
    conn.initializeConnection();
    Utils.EnableRemoteLogging(conn);

    Runnable runnable = new Runnable() {
      public void run() {
//        Utils.Log("Started!");
        angie.runEventLoop();
      }
    };

    runnable.run();
    Utils.Sleep(10000);
  }
}