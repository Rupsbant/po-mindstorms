package penoplatinum.pacman;

/**
 * GhostNavigator
 * 
 * Implementation of Navigator for Ghosts.
 * 
 * @author: Team Platinum
 */

import penoplatinum.model.GhostModel;
import java.util.List;
import java.util.ArrayList;

import java.util.Random;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Agent;
import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.Model;

import penoplatinum.simulator.Navigator;
import penoplatinum.util.Utils;

public class GhostNavigator implements Navigator {
  private GhostModel model;
  private List<Integer> plan = new ArrayList<Integer>();
  private Random r = new Random(1);
  
  public Navigator setModel(Model model) {
    this.model = (GhostModel)model;
    return this;
  }
  
  private void log(String msg) {
//    Utils.Log(msg);
//    System.out.printf( "[%10s] %2d,%2d / Navigator  : %s\n", 
//                       this.model.getAgent().getName(),
//                       this.model.getAgent().getLeft(),
//                       this.model.getAgent().getTop(),
//                       msg );
  }

  public int nextAction() {
    // We wait until the agent is active == everybody is there to run
    Agent  agent  = this.model.getGridPart().getAgent();
    if( ! agent.isActive() ) {
      return GhostAction.NONE;
    }

    // TODO: add check if next planned action is still valid, else also
    //       create a new plan based on the new situation
    if( this.plan.size() == 0 ) {
      this.createNewPlan();
//      this.log("Got a new Plan : " + this.plan );
    }
    if( this.plan.size() == 0 ) {
      throw new RuntimeException( "Out of plans ..." );
    }
//    this.log("Executing plan: " + this.plan);
    return this.plan.remove(0);
  }

  public Boolean reachedGoal() {
    // TODO: implement some termination, we need it to announce
    //       the CAPTURE
    return false;
  }

  public double getDistance() {
    // not used, but if it were, it would always be half a Sector = 20cm
    return 20;
  }
  
  public double getAngle() {
    // not used, but if it were, it would always be a quarter of a Sector
    return 90;
  }

  // get the values of the 4 adjacent sectors and decide were to go
  private void createNewPlan() {
    int[] values = this.getadjacentSectorInfo();

    
    // move towards the higher ground/scent/value
    int max = this.getMax(values);
    // if any of the moves brings us onto the 10000 pos, we hold our position
    if(max == PacmanAgent.VALUE){
      this.plan.add(GhostAction.NONE);
      return;
    }
    
    // retain moves with highest value
    int[] moves = {-1, -1, -1, -1};
    int count = 0;
    for( int i=Bearing.N; i<=Bearing.W; i++ ) {
      if( values[i] == max ) {
        moves[count] = i;
        count++;
      }
    }
    Utils.Log("Max: "+max);
    Utils.Log("0:"+values[0]);
    Utils.Log("1:"+values[1]);
    Utils.Log("2:"+values[2]);
    Utils.Log("3:"+values[3]);
    
    // TODO: maybe take into account the move with least turns ?
    
    // choose randomly one of the best moves and create the required actions
    int forMove = moves[(int)(r.nextDouble()*count)];
//    forMove = moves[0];
//    this.log( forMove + " out of " + Arrays.toString(moves) + " / " + count);
    
    // randomly don't do anything (20%)
    if((int)(r.nextDouble()*5)==3) { forMove = Bearing.NONE; }

    if(forMove <= Bearing.NONE) {
      this.plan.add(GhostAction.NONE);
      return;
    }
    
    this.createActions(forMove);
  }

  // get the values of all 4 adjacent Sectors (N,E,S,W)
  // return the Sector's value IF it is accessible (there must be a Sector,
  // and there shouldn't be a wall in between).
  // if there is an agent on the adjacent Sector, we don't go there...
  private int[] getadjacentSectorInfo() {
    Agent  agent  = this.model.getGridPart(). getAgent();
    Sector sector = agent.getSector();

    Boolean hasNeighbour, hasWall;

    int[] info = {-1, -1, -1, -1};
    for(int atLocation=Bearing.N; atLocation<=Bearing.W; atLocation++ ) {
      hasNeighbour = sector.hasNeighbour(atLocation);
      hasWall      = sector.hasWall(atLocation);
      hasWall      = hasWall != null && hasWall;
      info[atLocation] = ! hasNeighbour || hasWall ?
                         -1 : sector.getNeighbour(atLocation).getValue();
      // if we know there is an agent on the next sector, lower the value
      // drastically
      /*if( hasNeighbour &&   //Dit werkt niet indien er een pacman op staat, dan rijdt hij weg!
          sector.getNeighbour(atLocation).hasAgent() )
      {
        info[atLocation] = -1;
      } else {/**/
        // TEMPORARY CHEATING TO SOLVE DETECT-OTHER-AGENT-AS-WALL PROBLEM
        // we don't want to allow multiple agents on the same sector
//        Agent proxy = MiniSimulation.goalGrid.getAgent(agent.getName());
//        int proxyOrigin = proxy.getOriginalBearing();
//        int bearingOfProxy = Bearing.withOrigin(atLocation, proxyOrigin);
//        Sector neighbour = proxy.getSector().getNeighbour(bearingOfProxy);
//        if( neighbour != null && neighbour.hasAgent() ) {
//          // System.out.println( agent.getName() + " : other agent @ " + atLocation + " == " + bearingOfProxy );
//          // try { System.in.read(); } catch(Exception e) {}
//          info[atLocation] = -1;
//        }
      /*}/**/
    }

    return info;
  }
  
  private int getMax(int[] values) {
    // find max
    int max = -20000;
    for( int value : values ) {
      if( value > max ) { max = value; }
    }
    return max;
  }
  
  private void createActions(int target) {
    int current = this.model.getGridPart().getAgent().getBearing();
    
    if( target != current ) {
      int diff = target - current;
      if( Math.abs(diff) == 3 ) { diff /= -3; } // -3 => 1   3 => -1
    
      switch(diff) {
        case -2: this.plan.add(GhostAction.TURN_LEFT);
        case -1: this.plan.add(GhostAction.TURN_LEFT); break;
        case  2: this.plan.add(GhostAction.TURN_RIGHT);
        case  1: this.plan.add(GhostAction.TURN_RIGHT); break;
      }
//      this.log( "turn needed: " + current + " -> " + target + " = " + diff );
    }
    
    // after turning, move forward
    this.plan.add(GhostAction.FORWARD);
  }
}
