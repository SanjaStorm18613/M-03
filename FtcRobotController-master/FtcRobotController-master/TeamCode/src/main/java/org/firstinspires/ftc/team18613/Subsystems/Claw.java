package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Claw extends Subsystem {

    private final Servo roll, clawD, clawE, pitch;
    private final Elevator elev;
    private final Arm braco;
    private final ElapsedTime time;

    private double pTranstTime = Constants.Claw.TRANSITION_TIME;

    private double pVel = 0, elevCtrl = 0, pPos = Constants.Claw.PITCH_UP, pTime = 0,
            pPrevPos = Constants.Claw.PITCH_UP, rPos = Constants.Claw.ROLL_UP, cTargetPos = 0,
            cont = 0, ppp = 0,
            pitchVel = 0, pitchPos = Constants.Claw.PITCH_UP, lastPitchPos = Constants.Claw.PITCH_UP,
            rollPos = Constants.Claw.ROLL_UP;

    private boolean clawOpen = false;
    private boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    private boolean elevNvCol = true, cIsBusy = false, init, retrain = false, elevAjt = false;

    int colectState = 0, lastColectState = 0;

    public Claw(Elevator elev, Arm braco) {

        roll  = TeleOpM03.hm.get(Servo.class, "Roll");
        clawD = TeleOpM03.hm.get(Servo.class, "GarraD");
        clawE = TeleOpM03.hm.get(Servo.class, "GarraE");
        pitch = TeleOpM03.hm.get(Servo.class, "Pitch");

        clawD.setDirection(Servo.Direction.FORWARD);
        clawE.setDirection(Servo.Direction.REVERSE);

        roll.setDirection(Servo.Direction.FORWARD);
        pitch.setDirection(Servo.Direction.FORWARD);

        this.elev = elev;
        this.braco = braco;

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

        elevNvCol = elev.getStages() == 0;

        if (elev.getStages() > 0 && elev.getCurrentPos() > 1000) {
            pPos = Constants.Claw.PITCH_UP;
            rPos = rPos == Constants.Claw.ROLL_SIDE_CONE ? Constants.Claw.ROLL_UP : rPos;
        }

        //if (drop && DROP) cOpen = !cOpen;
        //copiloto
        if (retain && RETAIN && elevNvCol) {
            clawOpen = false;
            if (Math.abs(elev.getTargetPosition()) > 0 && pPos == Constants.Claw.PITCH_HORIZONTAL) pPos = Constants.Claw.PITCH_UP;
            else pPos = Constants.Claw.PITCH_LOWERED;
            rPos = rPos == Constants.Claw.ROLL_SIDE_CONE ? Constants.Claw.ROLL_UP : rPos;
             retrain = true;

        }

        if (drop && DROP) clawOpen = !clawOpen;

        if (spin && SPIN) {
            rPos = rPos != Constants.Claw.ROLL_UP ? Constants.Claw.ROLL_UP : Constants.Claw.ROLL_DOWN;
            clawOpen = false;
        }

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
            braco.setAjt(-1);
            elevAjt = true;

        } else if (pPos == Constants.Claw.PITCH_LOWERED) {

            if (clawOpen) {
                elevCtrl = 1;
                claw(true);

            } else if (elev.getCurrentPos() < 80) {
                claw(false);
                elevAjt = true;
                braco.setAjt(-1);

            } else if (elev.getCurrentPos() < 700 && !elevAjt) braco.setAjt(.18);

        } else {
            claw(clawOpen);
            braco.setAjt(-1);
            elevAjt = false;
        }

        if (!init) {
            if (elevCtrl > 0) {
                elev.setAjt(elevNvCol, Math.max(Constants.Claw.ELEVADOR_UP, ppp));
            } else {
                elev.setAjt(elevNvCol, Math.max(elevAjt ? .25 : 0, ppp));
            }
        }

        if (pVel == 1) cont = pPrevPos;

        double calc = .8 * Math.pow(angle, 2.5) * (Constants.Claw.PITCH_HORIZONTAL - Constants.Claw.PITCH_UP);

        if (init) pitch.setPosition(Constants.Claw.PITCH_UP + braco.getPos() + (pPos == Constants.Claw.PITCH_LOWERED ? 0 : calc));
        else pitch.setPosition(cont + (pPos - cont) * pVel + braco.getPos() + (pPos == Constants.Claw.PITCH_LOWERED ? 0 : calc));

        if (pVel == 1 || !elevNvCol || init || pitch.getPosition() > .4 && pitch.getPosition() < .6) roll.setPosition(rPos);

        //telemetry.addData("pitch", pitch.getPosition());
        //telemetry.addData("roll", roll.getPosition());

        TeleOpM03.tel.addData("p1", cont + (pPos - cont) * pVel);
        TeleOpM03.tel.addData("p2", (cont + (pPos - cont) * pVel) + braco.getPos());

        TeleOpM03.tel.addData("pPos", pPos);
        TeleOpM03.tel.addData("getTargetPosition", elev.getTargetPosition());

    }

    public void setClaw(double c, int t) {

        claw(c == 1.0);

        roll.setPosition(Constants.Claw.ROLL_UP);
        pitch.setPosition(Constants.Claw.PITCH_UP + 0.1);

        if (cTargetPos != clawD.getPosition() && !cIsBusy) {
            time.reset();
            cIsBusy = true;
        }
        cTargetPos = clawD.getPosition();

        cIsBusy = time.time() < t;

    }

    public boolean getBusy(){
        return cIsBusy;
    }

    public void claw(boolean p) {
        clawD.setPosition(p ? Constants.Claw.CLAW_OPEN : Constants.Claw.CLAW_CLOSE);
        clawE.setPosition(p ? Constants.Claw.CLAW_OPEN : Constants.Claw.CLAW_CLOSE);

    }

    public void openClaw(){
        clawD.setPosition(Constants.Claw.CLAW_OPEN);
        clawD.setPosition(Constants.Claw.CLAW_OPEN);
    }

    public void closeClaw(){
        clawD.setPosition(Constants.Claw.CLAW_CLOSE);
        clawD.setPosition(Constants.Claw.CLAW_CLOSE);
    }

    public void horizontalColect() {
        pitchPos = Constants.Claw.PITCH_HORIZONTAL;

        if (rollPos == Constants.Claw.ROLL_SIDE_CONE){
            rollPos = Constants.Claw.ROLL_UP;
        }
        colectState = 1;
        periodic();

    }

    public void loweredFrontColect() {
        pitchPos = Constants.Claw.PITCH_LOWERED;
        if (rollPos == Constants.Claw.ROLL_SIDE_CONE){
            rollPos = Constants.Claw.ROLL_UP;
        }
        colectState = 2;
        periodic();

    }

    public void loweredSideColect() {
        pitchPos = Constants.Claw.PITCH_LOWERED;
        rollPos = Constants.Claw.ROLL_SIDE_CONE;
        colectState = 3;
        periodic();

    }

    private void updateClaw() {
        if (colectState != lastColectState){
            clawOpen = true;
        } else {
            clawOpen = !clawOpen;
        }
    }

    private void periodic() {

        updateClaw();
        lastColectState = colectState;

        if (clawOpen) {
            openClaw();
        } else {
            closeClaw();
        }

        velPitchUpdate();

        if (pitchVel < 1) {
            pitch.setPosition(lastPitchPos + (pitchPos - lastPitchPos) * pitchVel);
        } else {
            pitch.setPosition(pitchPos);
            lastPitchPos = pitchPos;
        }

        if (pitchVel == 1) {
            roll.setPosition(rollPos);
        }

    }

    public void velPitchUpdate() {

        if (pitchPos == Constants.Claw.PITCH_LOWERED || lastPitchPos == Constants.Claw.PITCH_LOWERED){
            time.reset();
        }

        pitchVel = Math.min(time.time()/Constants.Claw.TRANSITION_TIME, 1);
        pitchVel = Math.pow(pitchVel, 2);
    }
    ////
    public void elevatorUp (){

        double pMid = pTranstTime / 2.0;
        elevCtrl = -Math.abs(pTime - pMid) / pMid + 1;
        elevCtrl = Math.max(elevCtrl, 0);
    }

    public void setElev (double p) {
        ppp = p;
    }

}







