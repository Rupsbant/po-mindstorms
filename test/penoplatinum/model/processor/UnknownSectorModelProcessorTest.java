package penoplatinum.model.processor;

import penoplatinum.grid.GridUtils;
import penoplatinum.util.Point;
import penoplatinum.grid.agent.Agent;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Grid;
import org.junit.Test;
import junit.framework.TestCase;
import org.junit.Before;
import penoplatinum.grid.LinkedSector;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.util.Bearing;
import static org.mockito.Mockito.*;

public class UnknownSectorModelProcessorTest extends TestCase {
  
  private Sector mockCurrentSector;
  private Grid mockGrid;
  private GridModelPart mockGridPart;
  private Model mockModel;
  private Point mockPosition;
  
  @Before
  public void setUp() {
    mockCurrentSector = mock(Sector.class);
    mockGrid = mock(Grid.class);
    mockGridPart = mock(GridModelPart.class);
    mockModel = mock(Model.class);
    when(mockModel.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(mockGridPart);
    when(mockGridPart.getMyGrid()).thenReturn(mockGrid);
    when(mockGridPart.getMySector()).thenReturn(mockCurrentSector);
    mockPosition = mock(Point.class);
    when(mockGrid.getSectorAt(mockPosition)).thenReturn(mockCurrentSector);
    when(mockGrid.getPositionOf(any(Agent.class))).thenReturn(mockPosition);
  }

  // Test of work method, of class UnknownSectorModelProcessor.
  @Test
  public void testWork() {
    UnknownSectorModelProcessor instance = new UnknownSectorModelProcessor();
    instance.setModel(mockModel);
    when(mockGridPart.hasChangedSectors()).thenReturn(true);
    when(mockCurrentSector.knowsWall(Bearing.E)).thenReturn(Boolean.TRUE);
    when(mockCurrentSector.knowsWall(Bearing.S)).thenReturn(Boolean.TRUE);
    when(mockCurrentSector.knowsWall(Bearing.W)).thenReturn(Boolean.TRUE);
    when(mockCurrentSector.hasNoWall(Bearing.E)).thenReturn(Boolean.TRUE);
    when(mockCurrentSector.hasNoWall(Bearing.S)).thenReturn(Boolean.TRUE);
    when(mockCurrentSector.hasNoWall(Bearing.W)).thenReturn(Boolean.TRUE);
    when(mockCurrentSector.hasNeighbour(Bearing.S)).thenReturn(Boolean.TRUE);
    when(mockGridPart.getMyPosition()).thenReturn(new Point(0, 0));
    instance.work();
    verify(mockGrid).add(any(LinkedSector.class), eq(new Point(1, 0)));
    verify(mockGrid).add(any(LinkedSector.class), eq(new Point(-1, 0)));
  }
}
