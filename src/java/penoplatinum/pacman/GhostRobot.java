package penoplatinum.pacman;

import java.util.ArrayList;

import penoplatinum.Config;


import penoplatinum.driver.Driver;
import penoplatinum.driver.GhostDriver;

import penoplatinum.grid.GridView;

import penoplatinum.model.GhostModel;
import penoplatinum.model.Reporter;
import penoplatinum.model.processor.AgentWallsUpdateProcessor;
import penoplatinum.model.processor.BarcodeBlackModelProcessor;
import penoplatinum.model.processor.GhostProtocolModelProcessor;
import penoplatinum.model.processor.GridRecalcModelProcessor;
import penoplatinum.model.processor.NewSectorsUpdateProcessor;
import penoplatinum.model.processor.HistogramModelProcessor;
import penoplatinum.model.processor.IRModelProcessor;
import penoplatinum.model.processor.InboxProcessor;
import penoplatinum.model.processor.LightColorModelProcessor;
import penoplatinum.model.processor.LineModelProcessor;
import penoplatinum.model.processor.MergeGridModelProcessor;
import penoplatinum.model.processor.ModelProcessor;
import penoplatinum.model.processor.WallDetectionModelProcessor;
import penoplatinum.model.processor.WallDetectorProcessor;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.ReferencePosition;
import penoplatinum.simulator.Robot;
import penoplatinum.simulator.RobotAPI;
import penoplatinum.simulator.Navigator;

import penoplatinum.gateway.GatewayClient;
import penoplatinum.util.Utils;


public class GhostRobot implements Robot {

  protected GhostModel model;
  protected RobotAPI api;   // provided from the outside

  private Driver driver;
  private Navigator navigator;

  private int[] sweepAngles = new int[]{ -90, 0, 90 };
  private ArrayList<Integer> sweepAnglesList = new ArrayList<Integer>();
  private boolean waitingForSweep = false;

  private GatewayClient client;
  private Reporter reporter;

  private ReferencePosition initialReference = new ReferencePosition();


  public GhostRobot(String name) {
    this.setupModel(name);

    for (int i = 0; i < sweepAngles.length; i++) {
      sweepAnglesList.add(sweepAngles[i]);
    }

    useDriver(new GhostDriver()); // default
  }

  public GhostRobot(String name, GridView view) {
    this(name);
    this.model.getGridPart().displayGridOn(view);
  }

  protected void setupModel(String name) {
    this.model = new GhostModel(name);

    ModelProcessor processors =
            new LightColorModelProcessor(
            new HistogramModelProcessor(
            new BarcodeBlackModelProcessor(
            new LineModelProcessor(
            new WallDetectionModelProcessor(
            new WallDetectorProcessor(
            new InboxProcessor(
            new AgentWallsUpdateProcessor(
            new NewSectorsUpdateProcessor(
            new IRModelProcessor(
            new GridRecalcModelProcessor(
            new GhostProtocolModelProcessor(
            new MergeGridModelProcessor()))))))))))));
    this.model.setProcessor(processors);

    // make sure the messagePart can send messages through the GatewayClient,
    // using the GhostProtocolHandler
    // this is only the case for a selection of the messages, most messages
    // are stored in the outbox and send by this robot implementation at the
    // end of a step.
    model.getMessagePart()
         .setProtocol(new GhostProtocolHandler(model, 
                        new GhostProtocolModelCommandHandler(model)));
  }

  @Override
  public GhostRobot useRobotAPI(RobotAPI api) {
    this.api = api;

    this.api.setReferencePoint(initialReference);
    this.api.setSpeed(Model.M3, Config.MOTOR_SPEED_SONAR);
    this.api.setSpeed(Model.M2, Config.MOTOR_SPEED_MOVE);
    this.api.setSpeed(Model.M1, Config.MOTOR_SPEED_MOVE);

    this.linkComponents();
    return this;
  }

  public GhostRobot useNavigator(Navigator nav) {
    this.navigator = nav;
    this.linkComponents();
    return this;
  }

  public GhostRobot useDriver(Driver driver) {
    this.driver = driver;
    this.linkComponents();
    return this;
  }

  // This function links the driver, navigator and api correctly
  protected void linkComponents() {
    if( driver != null ) {
      driver.useRobotAPI(api);
      driver.useModel(model);
    }
    if( navigator != null ) {
      navigator.setModel(model);
    }
  }

  public GhostRobot useReporter(Reporter reporter) {
    this.reporter = reporter;
    this.reporter.setRobot(this);
    return this;
  }

  /**
   * incoming communication from other ghosts, used by GatewayClient to deliver
   * incoming messages from the other ghosts
   * This is thread safe
   * @param cmd 
   */
  public void processCommand(String cmd) {
    this.model.getMessagePart().addIncomingMessage(cmd);
  }

  public void step() {
    if( ! this.model.getGridPart().getAgent().isActive() ) {
      // wait until we're active
      // but we need to process incoming messages
      this.model.process();
      return;
    }
    // poll other sensors and update model
    this.model.getSensorPart().updateSensorValues(this.api.getSensorValues());
    this.model.process();

    this.model.getSensorPart().setTotalTurnedAngle(api.getRelativePosition(initialReference).getAngle());

    // let the driver do his thing
    if( this.driver.isBusy() ) {
      this.driver.step();
      return;
    }

    // we want obstacle-information based on Sonar-values
    // as long as this is in progress, we wait
    if( this.api.sweepInProgress() ) { return; }

    // if the sweep is ready ...
    if (this.waitingForSweep) {
      this.model.getSonarPart()
                .updateSonarValues(this.api.getSweepResult(), sweepAnglesList);
      this.model.process(); // TODO: double call
      this.api.setSweeping(false);
      this.waitingForSweep = false;
    } else {
      this.api.setSweeping(true);
      this.api.sweep(sweepAngles);
      this.waitingForSweep = true;
      return; // to wait for results
    }
    Utils.Log(getModel().getGridPart().getGrid().getSize()+"");

    // ask navigator what to do and ...
    // let de driver drive, manhattan style ;-)
    int action = this.navigator.nextAction();
    this.driver.perform(action);

    // send outgoing messages
    this.sendMessages();

    // report info about our internals
    if( this.reporter != null ) { this.reporter.report(); }

    System.gc();
  }

  private void sendMessages() {
    if( this.client == null ) { return; }
    for(String msg : this.model.getMessagePart().getOutgoingMessages()) {
      this.client.send(msg, Config.BT_GHOST_PROTOCOL);
    }
    this.model.getMessagePart().clearOutbox();
  }

  public Boolean reachedGoal() {
    return false;
  }

  public void stop() {
    this.api.stop();
  }

  @Override
  public GhostRobot useGatewayClient(GatewayClient client) {
    this.client = client;
    // already set by Runner
    // this.client.setRobot(this);
    this.model.getMessagePart().getProtocol().useGatewayClient(this.client);
    this.client.run();
    return this;
  }
  
  public GatewayClient getGatewayClient() {
    return this.client;
  }

  @Override
  public Model getModel() {
    return model;
  }

  public GhostModel getGhostModel() {
    return model;
  }

  @Override
  public String getName() {
    return model.getGridPart().getAgent().getName();
  }
}
