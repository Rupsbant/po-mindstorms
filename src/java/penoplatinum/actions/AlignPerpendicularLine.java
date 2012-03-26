package penoplatinum.actions;

import penoplatinum.util.Utils;
import penoplatinum.util.LightColor;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 * This aligns the robot perpendicular to a line.
 * It measures the two angles: left to line and right to line and averages them to have a better precision.
 * @author: Team Platinum
 */
public class AlignPerpendicularLine extends BaseAction {

  public static final int TARGET_ANGLE = 20;
  public static final int SWEEP_ANGLE = 250;
  private static final int START = -1;
  private static final int FINDLINE_1_START = 0;
  private static final int FINDLINE_1_END = 1;
  private static final int CENTER_START = 2;
  private static final int CENTER_END = 3;
  private static final int FINDLINE_2_START = 4;
  private static final int FINDLINE_2_END = 5;
  private static final int ALIGN = 6;
  private static final int END = 7;
  private static final int NO_LINE = 8;

  public AlignPerpendicularLine(Model model, boolean CCW) {
    super(model);
    directionModifier = CCW ? 1 : -1;
  }
  private int directionModifier;
  private double initialAngle;
  private int state = -1;
  private int leftStart;
  private int leftEnd;
  private int rightStart;
  private int rightEnd;

  @Override
  public int getNextAction() {
    if (getModel().getSensorPart().isTurning()) {
      // processing state
      switch (state) {
        case FINDLINE_1_START:
          if (getModel().getLightPart().getCurrentLightColor() == LightColor.White) {
            leftStart = getRelativeAngle();
            state = FINDLINE_1_END;
          }
          break;
        case FINDLINE_1_END:
          if (getModel().getLightPart().getCurrentLightColor() == LightColor.Brown) {
            leftEnd = getRelativeAngle();
            state = CENTER_START;
            return Navigator.STOP;
          }
          break;
        case FINDLINE_2_START:
          if (getModel().getLightPart().getCurrentLightColor() == LightColor.White) {
            rightStart = getRelativeAngle();
            state = FINDLINE_2_END;
          }
          break;
        case FINDLINE_2_END:
          if (getModel().getLightPart().getCurrentLightColor() == LightColor.Brown) {
            rightEnd = getRelativeAngle();
            state = ALIGN;
            return Navigator.STOP;
          }
          break;
        case CENTER_START:
        case CENTER_END:
        case ALIGN:
        case END:
        default:
          //WAIT
          return Navigator.NONE;
      }
    }
    return getStateStart();
  }

  private int getRelativeAngle() {
    return (int) (getModel().getSensorPart().getTotalTurnedAngle() - initialAngle);
  }

  private int getStateStart() {
    switch (state) {
      case START:
        setAngle(-SWEEP_ANGLE * directionModifier);
        state = FINDLINE_1_START;
        return Navigator.TURN;
      case FINDLINE_1_START:
      case FINDLINE_1_END:
      case FINDLINE_2_START:
      case FINDLINE_2_END:
        //line or end of line not found.
        state = NO_LINE;
        setAngle(-getRelativeAngle());
        return Navigator.TURN;
      case CENTER_START:
        state = CENTER_END;
        setAngle(-getRelativeAngle());
        return Navigator.TURN;
      case CENTER_END:
        state = FINDLINE_2_START;
        setAngle(SWEEP_ANGLE * directionModifier);
        return Navigator.TURN;
      case ALIGN:
        // correction turn
        leftEnd = Utils.ClampLooped(leftEnd, -360, 0);
        leftStart = Utils.ClampLooped(leftStart, -360, 0);
        rightEnd = Utils.ClampLooped(rightEnd, 0, 360);
        rightStart = Utils.ClampLooped(rightStart, 0, 360);
        int left = (leftEnd + leftStart) / 2;
        int right = (rightEnd + rightStart) / 2;
        left = leftStart;
        right = rightStart;
        int corr = (left + right) / 2;
        corr -= getRelativeAngle();
        setAngle(corr);
        return Navigator.TURN;
      case NO_LINE:
        state = END;
      case END:
      default:
        return Navigator.STOP;
    }
  }

  @Override
  public boolean isComplete() {
    return state == END;
  }

  @Override
  public String getKind() {
    return "Align to line";
  }

  @Override
  public String getArgument() {
    return directionModifier > 0 ? "CCW" : "CW";
  }
}
