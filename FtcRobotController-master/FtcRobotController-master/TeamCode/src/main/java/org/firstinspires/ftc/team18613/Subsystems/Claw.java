package org.firstinspires.ftc.team18613.Subsystems;

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

    private double pTranstTime = Constants.Claw.TRANSITION_TIME;

    private double cTargetPos = 0,
            angle = 0, pitchProgress = 0, pitchPos = Constants.Claw.PITCH_UP, lastPitchPos = Constants.Claw.PITCH_UP,
            rollPos = Constants.Claw.ROLL_UP, b = 0;

    private boolean clawOpen = false, pBusy = false;

    private boolean cIsBusy = false, a =false;

    private int colectState = 0, lastColectState = 0;

    public Claw(Elevator elev, Arm braco) {

        sRoll = TeleOpM03.hm.get(Servo.class, "Roll");
        sClawD = TeleOpM03.hm.get(Servo.class, "GarraD");
        sClawE = TeleOpM03.hm.get(Servo.class, "GarraE");
        sPitch = TeleOpM03.hm.get(Servo.class, "Pitch");

        sClawD.setDirection(Servo.Direction.FORWARD);
        sClawE.setDirection(Servo.Direction.REVERSE);

        sRoll.setDirection(Servo.Direction.FORWARD);
        sPitch.setDirection(Servo.Direction.FORWARD);

        this.elevator = elev;
        this.arm = braco;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);


    }

    public void setClaw(double c, int t) {

        claw(c == 1.0);

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

    public void claw(boolean p) {
        sClawD.setPosition(p ? Constants.Claw.CLAW_OPEN : Constants.Claw.CLAW_CLOSE);
        sClawE.setPosition(p ? Constants.Claw.CLAW_OPEN : Constants.Claw.CLAW_CLOSE);

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
        if (elevator.getTargetPos() > 300) {
            pitchPos = Constants.Claw.PITCH_UP;
        } else {
            pitchPos = Constants.Claw.PITCH_LOWERED;
        }
        colectState = 4;
        clawOpen = false;

    }
//*/
    public void angulationDrop(double angle){
        this.angle = arm.getPos() + (elevator.onColletionStage() ? 0 : angle);
    }

    public void updateClaw() {
        if (colectState != lastColectState){
            clawOpen = true;
        } else {
            clawOpen = !clawOpen;
        }
        lastColectState = colectState;
    }

    public void periodic() {

        if (!elevator.onColletionStage()) {
            pitchPos = Constants.Claw.PITCH_UP;
        }

        if (loweredCollectMove()) {
            if (clawOpen) {
                openClaw();
            } else {
                closeClaw();
            }
        }

        velPitchUpdate();
        eee();

        if (pitchProgress < 1) {
            sPitch.setPosition(angle + lastPitchPos + (pitchPos - lastPitchPos) * pitchProgress);
        } else {
            sPitch.setPosition(pitchPos);
            lastPitchPos = pitchPos;
            pBusy = false;
            sRoll.setPosition(rollPos);

        }

        elevator.addControl(b);
        if (a) {
            arm.addControl(.18);
        }
        TeleOpM03.tel.addData("clawOpen", clawOpen);
        TeleOpM03.tel.addData("pitchProgress", pitchProgress);
        TeleOpM03.tel.addData("lastPitchPos", lastPitchPos);
        TeleOpM03.tel.addData("pitchPos", pitchPos);
    }


    private void velPitchUpdate() {

        if (!pBusy && (pitchPos == Constants.Claw.PITCH_LOWERED || lastPitchPos == Constants.Claw.PITCH_LOWERED) && pitchPos != lastPitchPos){
            time.reset();
            pitchProgress = 0;
            pBusy =  true;
        }

        pitchProgress = Math.pow(Math.min(time.time()/Constants.Claw.TRANSITION_TIME, 1), 2);

    }

    private void eee() {
        if ((pitchProgress != 1 && pitchProgress != 0) || (clawOpen && pitchPos == Constants.Claw.PITCH_LOWERED)) {
            b = Constants.Claw.ELEVADOR_UP;
        } else {
            b = 0;
        }
    }

    private boolean loweredCollectMove() {

        if (!elevator.onColletionStage() || colectState < 2) {
            a = false;
            return true;
        }

        if (!clawOpen) {
            b = 0;
            a = true;
            if (elevator.getCurrentPos() < 80) {
                a = false;
                return true;

            } else if (elevator.getCurrentPos() < 500) {
                //
            }
            return false;

        } else return true;

    }


    public void getTelemetry () {
        TeleOpM03.tel.addData("pitch.getPosition", sPitch.getPosition());
        TeleOpM03.tel.addData("pitchVel", pitchProgress);
        TeleOpM03.tel.addData("pitchVelPow", Math.pow(pitchProgress, 2));
        TeleOpM03.tel.addData("lastPitchPos", lastPitchPos);

      TeleOpM03.tel.addData("pitchPos", pitchPos);

    }

}