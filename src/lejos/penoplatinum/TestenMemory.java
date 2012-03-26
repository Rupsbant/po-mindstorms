package penoplatinum;

import penoplatinum.grid.Sector;
import penoplatinum.grid.SimpleGrid;
import penoplatinum.util.Utils;

public class TestenMemory {

  public static void main() {
    
//    byte[] b = new byte[55 * 1024];
//    testCountMemory();
    //Runtime.getRuntime().gc();
    //testGridMemory();
//    if (0 == 0) {
//      return;
//    }
    
    System.out.println(Runtime.getRuntime().freeMemory());
    Sector s = new Sector();
    System.out.println(Runtime.getRuntime().freeMemory());
    int xMax = 10;
    int yMax = 10;
    System.out.println(Runtime.getRuntime().freeMemory());
    SimpleGrid g = new SimpleGrid();
    System.out.println(Runtime.getRuntime().freeMemory());
    for (int i = 0; i < xMax; i++) {
      for (int j = 0; j < yMax; j++) {
        g.addSector(new Sector().setCoordinates(i, j));
      }
    }
    System.out.println(Runtime.getRuntime().freeMemory());
    System.gc();
    System.out.println(Runtime.getRuntime().freeMemory());
    int xStart = 10;
    for (int i = xStart; i < xStart + xMax; i++) {
      for (int j = 0; j < yMax; j++) {
        g.addSector(new Sector().setCoordinates(i, j));
      }
    }
    System.out.println(Runtime.getRuntime().freeMemory());
    System.gc();
    System.out.println(Runtime.getRuntime().freeMemory());
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
}
