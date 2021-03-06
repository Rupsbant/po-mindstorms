package penoplatinum.pacman;

/**
 * PacmanRobot
 * 
 * Implementation of a Robot, implementing a complete Pac-Man
 * 
 * @author: Team Platinum
 */

public class PacmanRobot {}

// TODO: this should become a pacman-robot/navigator
//
// public class PacmanAgent extends MovingAgent {
//   private boolean blocked = false;
// 
//   public PacmanAgent() {
//     super( "pacman" );
//   }
// 
//   public boolean isTarget() { return true; }
//   public int     getValue() { return 10000; }
// 
//   // we can't reuse the standard !moving implementation, because when possible
//   // pacman will not be moving, but will nog be blocked
//   public boolean isHolding() {
//     return this.blocked;
//   }
// 
//   public void move(int[] values) {
//     int move = -1;
// 
//     if( values[Bearing.N] < 0 && values[Bearing.E] < 0 && 
//         values[Bearing.S] < 0 && values[Bearing.W] < 0 )
//     {
//       this.blocked = true;
//     } else {
//       this.go(this.chooseBestMove(values));
//     }
//     
//     this.continueActiveMovement();
//   }
// 
//   private int chooseBestMove(int[] values) {
//     int move = -1;
//     
//     // get the positions of all agents and try to move away from them as 
//     // far as possible
//     // first calc current distance
//     int bestDist = this.getDistance(this.getLeft(), this.getTop());
// 
//     if( values[Bearing.N] > 0 ) {
//       int dist = this.getDistance(this.getLeft(), this.getTop()-1);
//       if( dist > bestDist ) { bestDist = dist; move = Bearing.N; }
//     }
// 
//     if( values[Bearing.E] > 0 ) {
//       int dist = this.getDistance(this.getLeft()+1, this.getTop());
//       if( dist > bestDist ) { bestDist = dist; move = Bearing.E; }
//     }
//     
//     if( values[Bearing.S] > 0 ) {
//       int dist = this.getDistance(this.getLeft(), this.getTop()+1);
//       if( dist > bestDist ) { bestDist = dist; move = Bearing.S; }
//     }
//     
//     if( values[Bearing.W] > 0 ) {
//       int dist = this.getDistance(this.getLeft()-1, this.getTop());
//       if( dist > bestDist ) { bestDist = dist; move = Bearing.W; }
//     }
//     
//     return move;
//   }
// 
//   private int getDistance(int left, int top) {
//     int smallestDist = 1000;
//     for(Agent agent: this.getSector().getGrid().getAgents()) {
//       if( agent != this ) {
//         int dist= Math.abs(agent.getLeft() - left)
//                 + Math.abs(agent.getTop()  - top);
//         if(dist < smallestDist) { smallestDist = dist; }
//       }
//     }
//     return smallestDist;
//   }
// }
