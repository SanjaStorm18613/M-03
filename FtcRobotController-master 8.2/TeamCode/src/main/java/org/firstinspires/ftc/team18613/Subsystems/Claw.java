package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;

public class Claw extends Subsystem {

    private final Servo sRoll, sClawD, sClawE, sPitch;
    private final Elevator elevator;
    private final Arm arm;
    private final ElapsedTime time;
    private final OpMode opMode;

    private double pAutoPos = 0,
            angle = 0, pitchProgress = 0, pitchPos = Constants.Claw.PITCH_UP, lastPitchPos = Constants.Claw.PITCH_UP,
            rollPos = Constants.Claw.ROLL_UP, elevatorControl = 0, autoPitchPos = 0.0;

    private boolean clawOpen = false, pBusy = false, init;

    private boolean cIsBusy = false;

    private int collectState = 0, lastCollectState = 0;

    public Claw(OpMode opMode, Elevator elev, Arm braco) {

        this.opMode = opMode;

        sRoll = opMode.hardwareMap.get(Servo.class, "Roll");
        sClawD = opMode.hardwareMap.get(Servo.class, "GarraD");
        sClawE = opMode.hardwareMap.get(Servo.class, "GarraE");
        sPitch = opMode.hardwareMap.get(Servo.class, "Pitch");

        sClawD.setDirection(Servo.Direction.FORWARD);
        sClawE.setDirection(Servo.Direction.REVERSE);

        sRoll.setDirection(Servo.Direction.FORWARD);
        sPitch.setDirection(Servo.Direction.FORWARD);

        this.elevator = elev;
        this.arm = braco;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        init = true;

    }

    public void periodic() {

        if (!elevator.getOnCollectionStage()) {
            pitchPos = Constants.Claw.PITCH_UP;
            if (rollPos == Constants.Claw.ROLL_SIDE_CONE){
                rollPos = Constants.Claw.ROLL_UP;
            }
        }

        if (loweredCollectMove() || (elevator.getTargetPos() > 0 && collectState == 1)) {
            if (clawOpen) {
                openClaw();
            } else {
                closeClaw();
            }
        }

        elevatorControl();
        velPitchUpdate();

        if (pitchProgress < 1 && needTurning()) {
            sPitch.setPosition(angle + lastPitchPos + (pitchPos - lastPitchPos) * pitchProgress);
        } else {
            sPitch.setPosition(angle + pitchPos);
            lastPitchPos = pitchPos;
            pBusy = false;
            sRoll.setPosition(rollPos);

        }

        elevator.addControl(elevatorControl);

    }

    public void autoPeriodic(){

        sPitch.setPosition(arm.getPos() * 0.6 + autoPitchPos);
        sRoll.setPosition(Constants.Claw.ROLL_UP);

    }

    public void openClaw(){
        sClawD.setPosition(Constants.Claw.CLAW_OPEN);
        sClawE.setPosition(Constants.Claw.CLAW_OPEN);
    }

    public void closeClaw(){
        sClawD.setPosition(Constants.Claw.CLAW_CLOSE);
        sClawE.setPosition(Constants.Claw.CLAW_CLOSE);
    }

    public void horizontalCollect() {
        pitchPos = Constants.Claw.PITCH_HORIZONTAL;

        if (rollPos == Constants.Claw.ROLL_SIDE_CONE){
            rollPos = Constants.Claw.ROLL_UP;
        }

        collectState = 1;
        updateClaw(true);

    }

    public void loweredFrontCollect() {
        pitchPos = Constants.Claw.PITCH_LOWERED;
        if (rollPos == Constants.Claw.ROLL_SIDE_CONE){
            rollPos = Constants.Claw.ROLL_UP;
        }
        collectState = 2;
        updateClaw(true);

    }

    public void loweredSideCollect() {
        pitchPos = Constants.Claw.PITCH_LOWERED;
        rollPos = Constants.Claw.ROLL_SIDE_CONE;
        collectState = 3;
        updateClaw(true);

    }

    public void retract() {
        if (elevator.getTargetPos() > 0 && collectState == 1) {
            pitchPos = Constants.Claw.PITCH_UP;
        } else {
            pitchPos = Constants.Claw.PITCH_LOWERED;
        }

        collectState = 4;
        clawOpen = false;
        init = false;
    }

    public void angulationDrop(double angle){
        this.angle = arm.getPos() * 0.6 + (pitchPos == Constants.Claw.PITCH_UP ? angle * 0.4 : 0);
    }

    public void invertCone() {

        if (!clawOpen && sPitch.getPosition() >= (Constants.Claw.PITCH_UP + .23)) {
            if (rollPos != Constants.Claw.ROLL_UP) {
                rollPos = Constants.Claw.ROLL_UP;
            } else {
                rollPos = Constants.Claw.ROLL_DOWN;
            }
        }

    }

    public void updateClaw(boolean pilotControl) {

        if (pilotControl || !elevator.getOnCollectionStage()) {
            if (collectState != lastCollectState) {
                clawOpen = true;
            } else {
                clawOpen = !clawOpen;
            }
            lastCollectState = collectState;
            init = false;
        }
    }

    private void velPitchUpdate() {

        if (init || !pBusy && needTurning()){
            time.reset();
            pitchProgress = 0;
            pBusy =  true;
        } else {
            pitchProgress = Math.min(time.time()/Constants.Claw.TRANSITION_TIME, 1);
            pitchProgress = Math.pow(pitchProgress, 2);
        }
    }

    private boolean needTurning() {
        return (pitchPos == Constants.Claw.PITCH_LOWERED || lastPitchPos == Constants.Claw.PITCH_LOWERED) && pitchPos != lastPitchPos;
    }

    private void elevatorControl() {

        if (needTurning() && pitchProgress < 1){
            elevatorControl = Constants.Claw.ELEVADOR_UP;

        } else if (pitchPos == Constants.Claw.PITCH_LOWERED) {
            if (clawOpen){
                elevatorControl = Constants.Claw.ELEVADOR_UP;

            } else if (sClawD.getPosition() == Constants.Claw.CLAW_OPEN) {
                elevatorControl = 0;

            } else {
                elevatorControl = Constants.Claw.ELEVATOR_UP_RETRAIN;

            }
        } else {
            elevatorControl = 0;
        }

    }

    private boolean loweredCollectMove() {

        return (!elevator.getOnCollectionStage()
                || collectState < 2
                || pitchProgress < 1)
                || ((!clawOpen && sClawD.getPosition() == Constants.Claw.CLAW_OPEN)
                && (elevator.getCurrentPos() < 70));

    }

    public void setPitch (double pos, double t) {

        if (pAutoPos != pos) {
            time.reset();
            cIsBusy = true;
        }
        pAutoPos = pos;

        if (time.time() > t * 0.8) {
            autoPitchPos = pos;

        }

        cIsBusy = t > time.time();
    }

    public void setClaw(double pos) {
        if (pos == 1.0){
            openClaw();
        } else {
            closeClaw();
        }
    }

    public boolean getBusy(){
        return cIsBusy;
    }

}