package penoplatinum.actions;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 * This Action moves a set distance to the left.
 * @author Team Platinum
 */
public class SideWallCorrectAction extends BaseAction {
  private int phase = -1;

  public SideWallCorrectAction(Model m, float correction) {
    super(m);
      setAngle(90);
      setDistance(correction);
  }

  @Override
  public int getNextAction() {
    if (phase == -1) {
      phase++;
      return Navigator.STOP;
    }
    if (getModel().getSensorPart().isTurning() || getModel().getSensorPart().isMoving()) {
      return Navigator.NONE;
    }
    return getStartNavigatorAction();
  }

  private int getStartNavigatorAction() {
    phase++;
    switch (phase) {
      case 1:
        return Navigator.TURN;
      case 2:
        return Navigator.MOVE;
      case 3:
        setAngle(-getAngle());
        return Navigator.TURN;
    }
    return Navigator.STOP;
  }

  @Override
  public boolean isComplete() {
    return phase > 3;

  }

  @Override
  public String getKind() {
    return "SideWallCorrect";
  }

  @Override
  public String getArgument() {
    return (int) (getDistance() * 100) + "cm";
  }
}
