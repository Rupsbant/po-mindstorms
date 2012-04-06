/**
 * RunAllTests
 *
 * Suite for all tests.
 *
 * @author Team Platonum
 */

import penoplatinum.util.RotationTest;
import penoplatinum.util.BearingTest;
import penoplatinum.util.PointTest;
import penoplatinum.util.TransformationTRTTest;
import penoplatinum.util.ScannerTest;
import penoplatinum.util.ColorTest;
import penoplatinum.util.BitwiseOperationsTest;
import penoplatinum.grid.LinkedSectorTest;
import penoplatinum.protocol.GhostProtocolHandlerTest;
import penoplatinum.driver.action.MoveDriverActionTest;
import penoplatinum.driver.action.TurnDriverActionTest;
import penoplatinum.driver.action.CombinedDriverActionTest;
import penoplatinum.driver.ManhattanDriverTest;

import junit.framework.*;


public class RunAllTests {
  public static Test suite() {
    Class[] testClasses = { 
      penoplatinum.util.RotationTest.class,
      penoplatinum.util.BearingTest.class,
      penoplatinum.util.PointTest.class,
      penoplatinum.util.TransformationTRTTest.class,
      penoplatinum.util.ScannerTest.class,
      penoplatinum.util.ColorTest.class,
      penoplatinum.util.BitwiseOperationsTest.class,
      penoplatinum.grid.LinkedSectorTest.class,
      penoplatinum.protocol.GhostProtocolHandlerTest.class,
      penoplatinum.driver.action.MoveDriverActionTest.class,
      penoplatinum.driver.action.TurnDriverActionTest.class,
      penoplatinum.driver.action.CombinedDriverActionTest.class,
      penoplatinum.driver.ManhattanDriverTest.class
    };
    TestSuite suite = new TestSuite(testClasses);
    return suite;
  }
}
