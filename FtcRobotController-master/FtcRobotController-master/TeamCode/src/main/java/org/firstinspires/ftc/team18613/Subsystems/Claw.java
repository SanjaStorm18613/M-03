package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Claw extends Subsystem {

    private final Servo sRoll, sClawD, sClawE, sPitch;
    private final Elevator elevator;
    private final Arm arm;
    private final ElapsedTime time;
    private final OpMode opMode;

    private double cTargetPos = 0,
            angle = 0, pitchProgress = 0, pitchPos = Constants.Claw.PITCH_UP, lastPitchPos = Constants.Claw.PITCH_UP,
            rollPos = Constants.Claw.ROLL_UP, elevatorControl = 0;

    private boolean clawOpen = false, pBusy = false, init;

    private boolean cIsBusy = false, armControl =false;

    private int colectState = 0, lastColectState = 0;

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

    public void setClaw(double c, int t) {

        if (c == 1.0){
            openClaw();
        } else {
            closeClaw();
        }

        sRoll.setPosition(Constants.Claw.ROLL_UP);
        sPitch.setPosition(Constants.Claw.PITCH_UP + 0.1);

        if (cTargetPos != sClawD.getPosition() && !cIsBusy) {
            time.reset();
            cIsBusy = true;
        }
        cTargetPos = sClawD.getPosition();

        cIsBusy = time.time() < t;

    }

    public boolean getBusy(){
        return cIsBusy;
    }


//////////////////////////////////////////////////////////////////////////////////////

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
        colectState = 1;
        updateClaw();

    }

    public void loweredFrontCollect() {
        pitchPos = Constants.Claw.PITCH_LOWERED;
        if (rollPos == Constants.Claw.ROLL_SIDE_CONE){
            rollPos = Constants.Claw.ROLL_UP;
        }
        colectState = 2;
        updateClaw();

    }

    public void loweredSideCollect() {
        pitchPos = Constants.Claw.PITCH_LOWERED;
        rollPos = Constants.Claw.ROLL_SIDE_CONE;
        colectState = 3;
        updateClaw();

    }

    public void retract() {
        if (elevator.getTargetPos() > 0 && colectState == 1) {
            pitchPos = Constants.Claw.PITCH_UP;
        } else {
            pitchPos = Constants.Claw.PITCH_LOWERED;
        }
        colectState = 4;
        clawOpen = false;
        init = false;
    }
//*/
    public void angulationDrop(double angle){
        this.angle = arm.getPos() * 0.65 + (pitchPos == Constants.Claw.PITCH_UP ? angle * 0.4 : 0);
    }

    public void invertCone() {
        if (rollPos != Constants.Claw.ROLL_UP) {
            rollPos = Constants.Claw.ROLL_UP;
        } else {
            rollPos = Constants.Claw.ROLL_DOWN;
        }
    }

    public void updateClaw() {
        if (colectState != lastColectState){
            clawOpen = true;
        } else {
            clawOpen = !clawOpen;
        }
        lastColectState = colectState;
        init = false;
    }

    public void periodic() {

        if (!elevator.onColletionStage()) {
            pitchPos = Constants.Claw.PITCH_UP;
            if (rollPos == Constants.Claw.ROLL_SIDE_CONE){
                rollPos = Constants.Claw.ROLL_UP;
            }
        }

        if (loweredCollectMove() || (elevator.getTargetPos() > 0 && colectState == 1)) {
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
        if (armControl) {
            arm.addControl(.07);
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
                elevatorControl = 0.5;

            }
        } else {
            elevatorControl = 0;
        }

    }

    private boolean loweredCollectMove() {

        if (!elevator.onColletionStage() || colectState < 2 || pitchProgress < 1) {
            armControl = false;
            return true;
        }

        if (!clawOpen && sClawD.getPosition() == Constants.Claw.CLAW_OPEN) {
            if (elevator.getCurrentPos() < 70) {
                armControl = false;
                return true;

            } else if (elevator.getCurrentPos() < 800) {
                armControl = true;
            }
            return false;

        } else {
            armControl = false;
            return true;
        }

    }


    public void getTelemetry () {
        opMode.telemetry.addData("pitch.getPosition", sPitch.getPosition());
        opMode.telemetry.addData("pitchVel", pitchProgress);
        opMode.telemetry.addData("pitchVelPow", Math.pow(pitchProgress, 2));
        //TeleOpM03.tel.addData("lastPitchPos", lastPitchPos);
        opMode.telemetry.addData("pitchPos", pitchPos);

    }

}