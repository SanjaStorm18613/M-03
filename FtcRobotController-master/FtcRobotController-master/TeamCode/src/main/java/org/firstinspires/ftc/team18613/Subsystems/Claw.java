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

    private double pVel = 0, elevCtrl = 0, pPos = Constants.Claw.PITCH_UP, pTime = 0,
            pPrevPos = Constants.Claw.PITCH_UP, rPos = Constants.Claw.ROLL_UP, cTargetPos = 0,
            cont = 0, ppp = 0,


            angle = 0, pitchProgress = 0, pitchPos = Constants.Claw.PITCH_UP, lastPitchPos = Constants.Claw.PITCH_UP,
            rollPos = Constants.Claw.ROLL_UP;

    private boolean clawOpen = false, pBusy = false;
    private boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    private boolean elevNvCol = true, cIsBusy = false, init, retrain = false, elevAjt = false;

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

        init = true;

    }

    public void Control(
            boolean spin,
            boolean colVert,
            boolean colFront,
            boolean colSide,
            boolean retain,
            boolean drop,
            double angle) {

        elevNvCol = elevator.getStages() == 0;

        if (elevator.getStages() > 0 && elevator.getCurrentPos() > 1000) {
            pPos = Constants.Claw.PITCH_UP;
            rPos = rPos == Constants.Claw.ROLL_SIDE_CONE ? Constants.Claw.ROLL_UP : rPos;
            TeleOpM03.tel.addLine("AAAAAAAAAAA");
        }

        //copiloto
        if (retain && RETAIN && elevNvCol) {
            clawOpen = false;
            if (Math.abs(elevator.getTargetPosition()) > 0 && pPos == Constants.Claw.PITCH_HORIZONTAL) pPos = Constants.Claw.PITCH_UP;
            else pPos = Constants.Claw.PITCH_LOWERED;
            rPos = rPos == Constants.Claw.ROLL_SIDE_CONE ? Constants.Claw.ROLL_UP : rPos;
             retrain = true;

        }


        if (spin && SPIN) {
            rPos = rPos != Constants.Claw.ROLL_UP ? Constants.Claw.ROLL_UP : Constants.Claw.ROLL_DOWN;
            clawOpen = false;
        }

        if (drop && DROP) clawOpen = !clawOpen;

        // piloto
        if ((colVert || colFront || colSide) && elevNvCol) {
            retrain = false;
            elevAjt = false;

            if (colVert && COLVERT) {
                rPos = rPos == Constants.Claw.ROLL_SIDE_CONE ? Constants.Claw.ROLL_UP : rPos;
                clawOpen = !clawOpen;
                lastColectState = colectState;
                colectState = 1;
                pPos = Constants.Claw.PITCH_HORIZONTAL;

            } else if (colFront && COLFRONT) {
                rPos = rPos == Constants.Claw.ROLL_SIDE_CONE ? Constants.Claw.ROLL_UP : rPos;
                clawOpen = !clawOpen;
                lastColectState = colectState;
                colectState = 2;
                pPos = Constants.Claw.PITCH_LOWERED;

            } else if (colSide && COLSIDE) {
                rPos = Constants.Claw.ROLL_SIDE_CONE;
                clawOpen = !clawOpen;
                lastColectState = colectState;
                colectState = 3;
                pPos = Constants.Claw.PITCH_LOWERED;

            }
        }

        clawOpen = clawOpen || colectState != lastColectState;

        COLSIDE = !colSide;
        COLFRONT = !colFront;
        COLVERT = !colVert;
        SPIN = !spin;
        RETAIN = !retain;
        DROP = !drop;

        if (pPrevPos != pPos) {
            init = false;
            if (pPos == Constants.Claw.PITCH_LOWERED || pPrevPos == Constants.Claw.PITCH_LOWERED) {
                time.reset();
                cont = pPrevPos;
            }
        }
        pPrevPos = pPos;


        if (Math.abs(pPos-pPrevPos) == Math.abs(Constants.Claw.PITCH_UP - Constants.Claw.PITCH_LOWERED)) pTranstTime = Constants.Claw.TRANSITION_TIME;
        else pTranstTime = Constants.Claw.TRANSITION_TIME / 1.5;
        pTime = Math.min(time.time(), pTranstTime);

        if (elevNvCol && !init) pVel = Math.round(Math.min(pTime / pTranstTime, 1) * 1000) / 1000.0;
        else pVel = 1;

        pVel = Math.pow(pVel, 1.8);
        TeleOpM03.tel.addData("pVel", pVel);


        double pMid = pTranstTime / 2.0;
        elevCtrl = -Math.abs(pTime - pMid) / pMid + 1;
        elevCtrl = Math.max(elevCtrl, 0);

        TeleOpM03.tel.addData("retrain", retrain);

        if (retrain) {
            claw(clawOpen);
            arm.setControlRequirement(-1);
            elevAjt = true;

        } else if (pPos == Constants.Claw.PITCH_LOWERED) {

            if (clawOpen) {
                elevCtrl = 1;
                claw(true);

            } else if (elevator.getCurrentPos() < 80) {
                claw(false);
                elevAjt = true;
                arm.setControlRequirement(-1);

            } else if (elevator.getCurrentPos() < 700 && !elevAjt) arm.setControlRequirement(.18);

        } else {
            claw(clawOpen);
            arm.setControlRequirement(-1);
            elevAjt = false;
        }

        if (!init) {
            if (elevCtrl > 0) {
                elevator.setAjt(elevNvCol, Math.max(Constants.Claw.ELEVADOR_UP, ppp));
            } else {
                elevator.setAjt(elevNvCol, Math.max(elevAjt ? .25 : 0, ppp));
            }
        }

        if (pVel == 1) cont = pPrevPos;

        double calc = .8 * Math.pow(angle, 2.5) * (Constants.Claw.PITCH_HORIZONTAL - Constants.Claw.PITCH_UP);

        if (init) sPitch.setPosition(Constants.Claw.PITCH_UP + arm.getPos() + (pPos == Constants.Claw.PITCH_LOWERED ? 0 : calc));
        else sPitch.setPosition(cont + (pPos - cont) * pVel + arm.getPos() + (pPos == Constants.Claw.PITCH_LOWERED ? 0 : calc));

        if (pVel == 1 || !elevNvCol || init || sPitch.getPosition() > .4 && sPitch.getPosition() < .6) sRoll.setPosition(rPos);

        //telemetry.addData("pitch", pitch.getPosition());
        //telemetry.addData("roll", roll.getPosition());

        TeleOpM03.tel.addData("p1", cont + (pPos - cont) * pVel);
        TeleOpM03.tel.addData("p2", (cont + (pPos - cont) * pVel) + arm.getPos());

        TeleOpM03.tel.addData("pPos", pPos);
        TeleOpM03.tel.addData("getTargetPosition", elevator.getTargetPosition());

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

    public void setElevator(double p) {
        ppp = p;
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

    public void angulationDrop(double angle){
        this.angle = elevator.onColletionStage() ? 0 : angle;
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

        if (loweredCollectMove()) {
            if (clawOpen) {
                openClaw();
            } else {
                closeClaw();
            }
        } else {
            closeClaw();
        }

        velPitchUpdate();
        loweredCollectMove();

        if (pitchProgress < 1) {
            sPitch.setPosition(angle + lastPitchPos + (pitchPos - lastPitchPos) * pitchProgress);
        } else {
            sPitch.setPosition(pitchPos);
            lastPitchPos = pitchPos;
            pBusy = false;
        }

        if (pitchProgress == 1) {
            sRoll.setPosition(rollPos);
        }

    }

    private void velPitchUpdate() {

        if (!pBusy && (pitchPos == Constants.Claw.PITCH_LOWERED || lastPitchPos == Constants.Claw.PITCH_LOWERED)){
            time.reset();
            pBusy =  true;
            pitchProgress = 0;
        } else {
            pitchProgress = Math.min(time.time()/Constants.Claw.TRANSITION_TIME, 1);
            pitchProgress = Math.pow(pitchProgress, 1.5);
        }

    }

    private boolean loweredCollectMove() {

        if (!elevator.onColletionStage()) return true;

        if (pitchProgress != 1 && pitchProgress != 0 || clawOpen) {
            elevator.addControl(Constants.Claw.ELEVADOR_UP);
        } else {
            elevator.removeControl();
        }

        if (!clawOpen) {
            if (elevator.getCurrentPos() < 80) {
                arm.removeControl();
                return true;

            } else if (elevator.getCurrentPos() < 600) {
                arm.addControl(.18);
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







