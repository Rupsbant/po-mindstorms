package penoplatinum.util.debugging;

import java.io.File;
import penoplatinum.util.Utils;
import penoplatinum.bluetooth.PCBluetoothConnection;
import penoplatinum.bluetooth.RemoteFileLogger;

/**
 * This is a basic listener to bluetooth chatter.
 * @author: Team Platinum
 */
public class MainLogger {
    public static void main(String[] args) {
        PCBluetoothConnection conn = new PCBluetoothConnection();
        conn.initializeConnection();
        RemoteFileLogger logger = new RemoteFileLogger(conn, "RobotLog", new File("logs"));
        logger.startLogging();
        while (true) {
            Utils.Sleep(1000);
        }
    }
}
