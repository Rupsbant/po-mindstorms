package penoplatinum.model.processor;

import java.util.ArrayList;

import penoplatinum.grid.Sector;

import penoplatinum.model.GhostModel;
import penoplatinum.model.GridModelPart;

import penoplatinum.pacman.GhostAction;
import penoplatinum.pacman.ProtocolHandler;
import penoplatinum.util.Utils;

/**
 * Responsible for sending robot information to the Ghost communication channel
 * 
 * @author Team Platinum
 */

public class GhostProtocolModelProcessor extends ModelProcessor {

  public GhostProtocolModelProcessor(ModelProcessor p) {
    super(p);
  }

  public GhostProtocolModelProcessor() {
    super();
  }

  @Override
  protected void work() {
    GridModelPart grid = ((GhostModel) model).getGridPart();

    ProtocolHandler protocol = model.getMessagePart().getProtocol();
    Utils.Log("A");
    // Send changed sectors
    ArrayList<Sector> changed = model.getGridPart().getChangedSectors();
    for (int i = 0; i < changed.size(); i++) {
      
      Utils.Log("G");
      Sector current = changed.get(i);

      // for each changed sector
      protocol.sendDiscover(current);
      
      Utils.Log("H");
      // report
      this.model.getReporter().reportWalls(current);
      Utils.Log("I");
    }
    Utils.Log("B");
    
    if (!grid.hasRobotMoved()) {
      Utils.Log("D");
      return;
    }
    Utils.Log("C");

    // Send position updates
    if( grid.getLastMovement() == GhostAction.FORWARD ) {
      protocol.sendPosition();
      this.model.getReporter().reportAgent(this.model.getGridPart().getAgent());
    }
    Utils.Log("C");
    // Send pacman position updates
    if (grid.isPacmanPositionChanged()) {
      protocol.sendPacman();
    }

  }
}
