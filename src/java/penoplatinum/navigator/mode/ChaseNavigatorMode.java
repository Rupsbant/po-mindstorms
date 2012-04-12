package penoplatinum.navigator.mode;

/**
 * ChaseNavigatorMode
 *
 * Implements a NavigatorMode that uses Hill Climbing to discover all Sectors
 * on a Grid.
 *
 * @author Team Platinum
 */

import java.util.List;
import java.util.Arrays;

import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;

import penoplatinum.navigator.action.NavigatorAction;


public class ChaseNavigatorMode implements NavigatorMode {

  public ChaseNavigatorMode(Model model) {
    super(model);
    // TODO : this.grids.onlyApplyCollaborateDiffusionOnPacman();
  }

  // we reached our goal if we're next to Pacman
  public boolean reachedGoal() {
    // TODO: clean this up ;-)
    Grid myGrid = this.grids.getMyGrid();
    Agent pacman = myGrid.getAgent("pacman"), // TODO: magic name constant
          me     = this.grids.getMyAgent();
    Sector pacmanSector = myGrid.getSectorOf(pacman),
           mySector     = myGrid.getSectorOf(me);

    for(Bearing atLocation : Bearing.NESW) {
      if( pacmanSector == mySector.getNeighbour(atLocation) ) { return true; }
    }

    return false;
  }
}
