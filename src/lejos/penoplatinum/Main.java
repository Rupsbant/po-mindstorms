package penoplatinum;

import penoplatinum.util.Utils;
import java.io.PrintStream;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import penoplatinum.bluetooth.QueuedPacketTransporter;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.bluetooth.RobotGatewayClient;
import penoplatinum.driver.GhostDriver;
import penoplatinum.gateway.BluetoothConnection;
import penoplatinum.pacman.GhostRobot;
import penoplatinum.grid.Sector;
import penoplatinum.grid.SimpleGrid;
import penoplatinum.pacman.GhostNavigator;
import penoplatinum.sensor.IRSeekerV2;

public class Main {

  public static void main(String[] args) throws Exception {
//    byte[] b = new byte[55 * 1024];
//    testCountMemory();
    //Runtime.getRuntime().gc();
    //testGridMemory();
//    if (0 == 0) {
//      return;
//    }

    GhostRobot robot = new GhostRobot("Michiel");
    //robot.useNavigator(new LeftFollowingGhostNavigator(robot.getGhostModel()));
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

  public static void testCountMemory() {
    int step = 1024 * 3;
    int size = step;
    byte[] buffer;
    for (int i = 0; i < 1000; i++) {
      buffer = new byte[size];
      System.out.println(buffer.length + " " + Runtime.getRuntime().freeMemory());
      Utils.Sleep(1000);

      size += step;
      buffer = null;
      System.gc();
    }
  }

  public static void testGridMemory() {


    SimpleGrid[] grids = new SimpleGrid[500];
    int num = 0;

    for (int k = 0; k < grids.length; k++) {
      grids[k] = new SimpleGrid();

      for (int i = 0; i < 12; i++) {
        for (int j = 0; j < 12; j++) {
          Sector s = new Sector(grids[k]).setCoordinates(i, j);
          int ff = s.getLeft() + 3;
          grids[k].addSector(s);

          num++;
          System.out.println("Wee! " + num + " " + k + " " + i + " " + j);


        }
      }
    }
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

  private static void initializeAgent(final AngieEventLoop angie) {

    RobotBluetoothConnection connection = new RobotBluetoothConnection();
    connection.initializeConnection();
    Utils.EnableRemoteLogging(connection);

    if (0 == 0) {
      return;
    }
    final QueuedPacketTransporter transporter = new QueuedPacketTransporter(connection);
    connection.RegisterTransporter(transporter, 123);
    final PrintStream stream = new PrintStream(transporter.getSendStream());


    Runnable communication = new Runnable() {

      public void run() {
        try {
          while (true) {
            String state = angie.fetchState();
            stream.println(state);

            transporter.SendPacket(123);
            Utils.Sleep(30);
          }
        } catch (Exception e) {
          Utils.Log("Comm crashed!");
        }
      }
    };
    Thread t = new Thread(communication);
    t.start();

  }
}