package penoplatinum.actions;

import penoplatinum.model.processor.GapModelProcessor;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.RobotAPI;

/**
 *
 * @author: Team Platinum
 */
public class GapDetectionRestoreAction extends BaseAction {
  private GapModelProcessor proc;
  int state = -1;

  public GapDetectionRestoreAction(RobotAPI api, Model model) {
    super(model);
    proc = new GapModelProcessor(null);
    proc.setModel(getModel());
  }

  @Override
  public int getNextAction() {
    if (getModel().getSensorPart().isTurning()) {
      return Navigator.NONE;
    }
    state++;
    return getStateStart();
  }

  private int getStateStart() {
    if (state == 0) {
      proc.performGapDetectionOnBuffer();
      if (getModel().getGapPart().isGapFound()) {
        int diff = (getModel().getGapPart().getGapStartAngle() + getModel().getGapPart().getGapEndAngle()) / 2;
        setAngle(diff);
        return Navigator.TURN;
      }
    }
    return Navigator.STOP;
  }

  @Override
  public boolean isComplete() {
    return state > 0;
  }

  @Override
  public String getKind() {
    return "Align to gap";
  }

  @Override
  public String getArgument() {
    return "";
  }
}