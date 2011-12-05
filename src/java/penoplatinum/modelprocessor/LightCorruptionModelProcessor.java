/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.modelprocessor;

import penoplatinum.modelprocessor.ColorInterpreter.Color;
import penoplatinum.simulator.Line;
import penoplatinum.simulator.Model;

/**
 *
 * @author Thomas
 */
public class LightCorruptionModelProcessor extends ModelProcessor {

  private ColorInterpreter colorInterpreter;

  public LightCorruptionModelProcessor() {
    super();
    this.colorInterpreter = new ColorInterpreter();
  }

  public LightCorruptionModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
    this.colorInterpreter = new ColorInterpreter();
  }

  @Override
  public void setModel(Model model) {
    super.setModel(model);
    colorInterpreter.setModel(model);
  }
  private int lightDataCorruption = 0;

  @Override
  protected void work() {
    model.setLine(Line.NONE);
    if (model.isTurning() && !colorInterpreter.isColor(Color.Brown)) {
      lightDataCorruption = 3;

    }
    if (lightDataCorruption > 0) {
      model.setLightCorruption(true);
      if (colorInterpreter.isColor(Color.Brown)) {
        lightDataCorruption--;
      }
    }
    else
    {
      model.setLightCorruption(false);
    }
  }
}