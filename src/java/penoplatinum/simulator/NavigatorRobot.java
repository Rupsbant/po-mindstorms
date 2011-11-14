package penoplatinum.simulator;

/**
 * BumperNavigatorRobot
 * 
 * This robot drives in a straight line and stops when its front-push-sensor
 * is triggered, it then turns an angle and tries to continue its journey.
 *  
 * Author: Team Platinum
 * 
 * TODO: most of this class can be extracted to an abstract RobotBase class.
 *       the logic in the constructor and the processCommand will remain, 
 *       defining the "personality" of the robot ;-)
 */

class NavigatorRobot implements Robot {

  private RobotAPI  api;
  private Navigator navigator;
  private Model     model;
  
  public NavigatorRobot() {
    // setup a model with the required ModelProcessors
    this.model     = new Model();
    this.model.setProcessor( new FrontPushModelProcessor() );
    // setup the navigator using the same model
    this.navigator = new SonarNavigator( this.model );
  }
  public NavigatorRobot(Navigator navigator) {
    // setup a model with the required ModelProcessors
    this.model     = new Model();
    this.model.setProcessor( new FrontPushModelProcessor() );
    // setup the navigator using the same model
    this.navigator = navigator;
    this.navigator.setModel(this.model);
  }

  public void useRobotAPI( RobotAPI api ) {
    this.api = api;
  }
  
  public void processCommand( String cmd ) {
    // no input required
  }
  
  public String getModelState() {
    return this.model.toString();
  }
  
  public String getNavigatorState() {
    return this.navigator.toString();
  }

  /**
   * This implements one step in the Event Loop. The Robot polls its sensors
   * updates its model and asks the navigator what to do.
   */
  public void step() {
    // get sensor data and update the model (motors are sensors too)
    this.model.updateSensorValues( this.api.getSensorValues() );

    // ask the navigator what to do next
    switch( this.navigator.nextAction() ) {
      case Navigator.MOVE:
        System.out.println("Moving: "+this.navigator.getDistance());
        this.api.move(this.navigator.getDistance());
        break;
      case Navigator.TURN:
        System.out.println("Turning: "+this.navigator.getAngle());
        this.api.turn(this.navigator.getAngle());
        break;
      case Navigator.STOP:
        this.api.stop();
        break;
      case Navigator.NONE:
      default:
        // do nothing
    }
  }

  public Boolean reachedGoal() {
    return this.navigator.reachedGoal();
  }
  
  public void stop() {
    this.api.stop();
  }
}
