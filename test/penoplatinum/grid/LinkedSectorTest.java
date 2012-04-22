package penoplatinum.grid;

/**
 * SectorTest
 * 
 * Tests Sector class.
 * 
 * @author: Team Platinum
 */
import junit.framework.*;
import static org.mockito.Mockito.*;

import penoplatinum.util.Bearing;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;

public class LinkedSectorTest extends TestCase {

  private Grid mockedGrid;

  public LinkedSectorTest(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mockedGrid = createMockedGrid();
  }

  public void testAddNeighbourSimple() {
    LinkedSector s1 = new LinkedSector();
    LinkedSector s2 = new LinkedSector();
    s1.addNeighbour(s2, Bearing.E);

    assertEquals(s2, s1.getNeighbour(Bearing.E));
    assertEquals(true, s1.hasNeighbour(Bearing.E));

    assertEquals(s1, s2.getNeighbour(Bearing.W));
    assertEquals(true, s2.hasNeighbour(Bearing.W));
  }

  public void testAddNeighbourComplex() {
    // Layout:
    // s2 | s3
    //    ----
    // s1 | s4


    Sector s1 = new LinkedSector();
    Sector s2 = new LinkedSector();
    Sector s3 = new LinkedSector();
    Sector s4 = new LinkedSector();

    s1.setWall(Bearing.E).clearWall(Bearing.E);
    s2.setWall(Bearing.E);
    s3.setWall(Bearing.S);


    s1.addNeighbour(s2, Bearing.N);
    assertEquals(s2, s1.getNeighbour(Bearing.N));
    assertEquals(s1, s2.getNeighbour(Bearing.S));
    assertTrue(s1.hasNoWall(Bearing.N));
    assertTrue(s2.hasNoWall(Bearing.S));
    assertTrue(s1.knowsWall(Bearing.N));
    assertTrue(s2.knowsWall(Bearing.S));

    s2.addNeighbour(s3, Bearing.E);
    assertEquals(s2, s3.getNeighbour(Bearing.W));
    assertEquals(s3, s2.getNeighbour(Bearing.E));
    assertTrue(s2.hasWall(Bearing.E));
    assertTrue(s3.hasWall(Bearing.W));
    assertTrue(s2.knowsWall(Bearing.E));
    assertTrue(s3.knowsWall(Bearing.W));



    s1.addNeighbour(s4, Bearing.E);
    assertEquals(s4, s1.getNeighbour(Bearing.E));
    assertEquals(s1, s4.getNeighbour(Bearing.W));
    assertEquals(s3, s4.getNeighbour(Bearing.N));
    assertEquals(s4, s3.getNeighbour(Bearing.S));
    assertTrue(s1.hasWall(Bearing.E));
    assertTrue(s4.hasWall(Bearing.W));
    assertTrue(s1.knowsWall(Bearing.E));
    assertTrue(s4.knowsWall(Bearing.W));

    assertTrue(s3.hasWall(Bearing.S));
    assertTrue(s4.hasWall(Bearing.N));
    assertTrue(s3.knowsWall(Bearing.S));
    assertTrue(s4.knowsWall(Bearing.N));

  }
  
  
  public void testSectorCopyRetainsWallInfo() {
    Sector original = this.createSectorWithWallsNW();
    Sector copy = new LinkedSector(original);

    assertTrue("copied Sector doesn't have N wall.", copy.hasWall(Bearing.N));
    assertTrue("copied Sector doesn't have W wall.", copy.hasWall(Bearing.W));
    assertFalse("copied Sector has wall E.", copy.hasWall(Bearing.E));
    assertFalse("copied Sector has wall S.", copy.hasWall(Bearing.S));
  }

  public void testSectorRotationRotatesWalls() {
    Sector sector = this.createSectorWithWallsNW();

    sector.putOn(mockedGrid);

    when(mockedGrid.getTransformation()).thenReturn(TransformationTRT.fromRotation(Rotation.R90));

    assertTrue("90 degree rotated sector doesn't have N wall.",
            sector.hasWall(Bearing.N));
    assertTrue("90 degree rotated sector doesn't have E wall.",
            sector.hasWall(Bearing.E));
    assertFalse("90 degree rotated sector has W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("90 degree rotated sector has S wall.",
            sector.hasWall(Bearing.S));

    when(mockedGrid.getTransformation()).thenReturn(TransformationTRT.fromRotation(Rotation.R180));

    assertTrue("180 degree rotated sector doesn't have E wall.",
            sector.hasWall(Bearing.E));
    assertTrue("180 degree rotated sector doesn't have S wall.",
            sector.hasWall(Bearing.S));
    assertFalse("180 degree rotated sector has W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("180 degree rotated sector has N wall.",
            sector.hasWall(Bearing.N));

    when(mockedGrid.getTransformation()).thenReturn(TransformationTRT.fromRotation(Rotation.R270));

    assertTrue("270 degree rotated sector doesn't have S wall.",
            sector.hasWall(Bearing.S));
    assertTrue("270 degree rotated sector doesn't have W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("270 degree rotated sector has E wall.",
            sector.hasWall(Bearing.E));
    assertFalse("270 degree rotated sector has N wall.",
            sector.hasWall(Bearing.N));

    when(mockedGrid.getTransformation()).thenReturn(TransformationTRT.fromRotation(Rotation.R360));

    assertTrue("360 degree rotated sector doesn't have N wall.",
            sector.hasWall(Bearing.N));
    assertTrue("360 degree rotated sector doesn't have W wall.",
            sector.hasWall(Bearing.W));
    assertFalse("360 degree rotated sector has E wall.",
            sector.hasWall(Bearing.E));
    assertFalse("360 degree rotated sector has S wall.",
            sector.hasWall(Bearing.S));
  }

  

  // utility methods to setup basic components
  private Sector createSectorWithWallsNW() {
    /* result looks like this:
     *    +--+
     *    |
     *    +
     */
    return new LinkedSector().addWall(Bearing.N).removeWall(Bearing.E).removeWall(Bearing.S).addWall(Bearing.W);
  }

  private Grid createMockedGrid() {
    Grid ret = mock(Grid.class);

    return ret;
  }
}
