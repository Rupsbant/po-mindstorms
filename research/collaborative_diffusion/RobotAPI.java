// copied from original RobotAPI
// added support for requesting a sweep
// modified move() signature to return boolean
public interface RobotAPI {
  // moves the robot in a straigth line for a distance expressed in meters
  public boolean move( double distance );
  
  // turns the robot on its spot by an angle expressed in degrees
  public void turn( int angle );

  // stop the robot immediately
  public void stop();
  
  // returns the current values for the sensors
  public int[] getSensorValues();
  
  // sets the speed for one of the motors
  public void setSpeed(int motor, int speed);
  
  // beeps once
  public void beep();

  // NEW
  public void sweep(int[] angles);
  public boolean sweepInProgress();
  public int[] getSweepResult();
}
