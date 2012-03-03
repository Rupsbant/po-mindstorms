public class MiniSimulationRobotAPI implements RobotAPI {
  private Agent proxy;
  
  public MiniSimulationRobotAPI(Agent proxy) {
    this.proxy = proxy;
  }
  
  public boolean move(double distance) {
    if( ! this.proxy.canMoveForward() ) {
      return false;
    }
    this.proxy.moveForward();
    return true;
  }
  
  public void turn(int angle) {
    int bearing = this.proxy.getBearing();
    switch(angle) {
      case -90: this.proxy.turnLeft(); break;
      case  90: this.proxy.turnRight(); break;
    }
  }

  public void stop() {
    // nothing to do ;-)
  }
  
  public int[] getSensorValues() {
    return new int[] {};
  }
  
  public void setSpeed(int motor, int speed) {
    // nothing to do
  }
  
  public void beep() {
    System.out.println( "BEEP" );
  }
  
  // NEW
  public void sweep(int[] bearings) {
    // nothing to do... hard-coded
  }
  
  public boolean sweepInProgress() {
    // we have everything immediately
    return false;
  }
  
  public int[] getSweepResult() {
    int left, front, right;

    int bearing = this.proxy.getBearing();
    Boolean wall, agent = false;
    wall = this.proxy.getSector().hasWall(Bearing.leftFrom(bearing));
    if(wall == null) {
      throw new RuntimeException("proxy can not determine wall to left");
    }
    left = wall ? 20 : 60;

    wall = this.proxy.getSector().hasWall(bearing);
    if(wall == null) {
      throw new RuntimeException("proxy can not determine wall in front");
    }
    front = wall ? 20 : 60;

    wall = this.proxy.getSector().hasWall(Bearing.rightFrom(bearing));
    if(wall == null) {
      throw new RuntimeException("proxy can not determine wall to right");
    }
    right = wall ? 20 : 60;

    return new int[] { left, front, right };
  }
}
