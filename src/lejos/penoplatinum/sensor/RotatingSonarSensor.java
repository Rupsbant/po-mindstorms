package penoplatinum.sensor;

import java.util.ArrayList;
import java.util.List;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.util.Utils;

/**
 * Abstraction for a new type of sensor: a sonar mounted on top of a motor.
 * @author: Team Platinum
 */
public class RotatingSonarSensor {

    private Motor motor;
    private final UltrasonicSensor sensor;
    private int startTacho;
    private int endTacho;
    private int forwardTacho;
    private boolean first;
    private boolean continuousSweeping = false;

    public RotatingSonarSensor(Motor motor, UltrasonicSensor sensor) {
        this.motor = motor;
        this.sensor = sensor;
        motor.smoothAcceleration(true);

        if (motor.getTachoCount() != 0) {
            Sound.playNote(Sound.PIANO, 220, 1);
            Utils.Log("Initial tacho of the motor should've been 0!");
            motor.resetTachoCount();
        }

        int range = 110;

        forwardTacho = motor.getTachoCount();
        startTacho = motor.getTachoCount() + range;
        endTacho = motor.getTachoCount() - range;
    }

    public float getDistance() {
        int dist = sensor.getDistance();

        return dist;

    }

    public void updateSonarMovement() {
        if (continuousSweeping) {
            updateSonarMovementContinuousl();
        } else {
            updateSonarMovementOnDemand();

        }
    }

    private void updateSonarMovementContinuousl() {
        if (motor.isMoving()) {
            return;
        }
        first = !first;
        if (first) {
            motor.rotateTo(endTacho, true);
        } else {
            motor.rotateTo(startTacho, true);

        }
    }

    public Motor getMotor() {
        return motor;
    }

    public UltrasonicSensor getSensor() {
        return sensor;
    }

    public boolean isContinuousSweeping() {
        return continuousSweeping;
    }

    public void setContinuousSweeping(boolean continuousSweeping) {
        this.continuousSweeping = continuousSweeping;
    }
    int[] currentSweepAngles;
    int currentSweepAngleIndex;
    List<Integer> resultBuffer = new ArrayList<Integer>();

    public boolean sweepInProgress() {
        return currentSweepAngles != null;
    }

    public void sweep(int[] i) {
        currentSweepAngles = i;
        currentSweepAngleIndex = 0;
        //WARNING: the result list can be used after the next sweep is requested! resultBuffer.clear();
        resultBuffer = new ArrayList<Integer>();
        //resultBuffer.clear();
    }

    public List<Integer> getSweepResult() {
        return resultBuffer;
    }

    private void updateSonarMovementOnDemand() {
        if (!sweepInProgress()) {
            return;
        }
        if (motor.isMoving()) {
            return;
        }

        int currentTacho = motor.getTachoCount() - forwardTacho;

        int currentAngle = currentSweepAngles[currentSweepAngleIndex] - forwardTacho;

        if (currentTacho != currentAngle) {
            motor.rotateTo(currentAngle, true);
            return;
        }
        
        resultBuffer.add((int) getDistance());
        currentSweepAngleIndex++;

        if (currentSweepAngleIndex >= currentSweepAngles.length) {
            motor.rotateTo(currentSweepAngles[0] - forwardTacho, true);
            currentSweepAngles = null;

        }

    }
}
