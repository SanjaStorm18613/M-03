package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Garra {

    Servo roll, clawD, clawE, pitch;
    Elevador elev;
    Braco braco;
    ElapsedTime time;
    Telemetry telemetry;

    double  pDropPos = Constantis.Garra.PITCH_DROP,
            pFallenPos = Constantis.Garra.PITCH_FALLEN,
            pTranstTime = Constantis.Garra.TRANSITION_TIME;

    double pVel = 0, elevCtrl = 0, pPos = Constantis.Garra.PITCH_DROP, pTime = 0, pPrevPos = Constantis.Garra.PITCH_DROP, rPos = Constantis.Garra.ROLL_UP, cTargetPos = 0, cont = 0;

    boolean cOpen = false;
    boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    boolean elevNvCol = true, cIsBusy = false, init, retrain = false, elevAjt = false;

    int cStatus = 0, cPrevStatus = 0;

    public Garra(Telemetry t, HardwareMap hardwareMap, Elevador e, Braco b) {

        roll  = hardwareMap.get(Servo.class, "Roll");
        clawD = hardwareMap.get(Servo.class, "GarraD");
        clawE = hardwareMap.get(Servo.class, "GarraE");
        pitch = hardwareMap.get(Servo.class, "Pitch");

        clawD.setDirection(Servo.Direction.FORWARD);
        clawE.setDirection(Servo.Direction.REVERSE);

        roll.setDirection(Servo.Direction.FORWARD);
        pitch.setDirection(Servo.Direction.FORWARD);

        elev = e;
        telemetry = t;
        braco = b;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        init = true;

    }

    public void Control(boolean spin, boolean colVert, boolean colFront, boolean colSide, boolean retain, boolean drop, double angle) {
        elevNvCol = elev.getNv() == 0;

        if (elev.getNv() > 0 && elev.getCorrentPos() > 1000) {
            pPos = pDropPos;
            rPos = rPos == Constantis.Garra.ROLL_SIDE_CONE ? Constantis.Garra.ROLL_UP : rPos;

            if (retain && RETAIN) cOpen = !cOpen;

        }

        //#region COPILOTO
        if (retain && RETAIN && elevNvCol) {
            cOpen = false;
            if (Math.abs(elev.getTargetPosition()) > 0 && pPos == Constantis.Garra.HORIZONTAL) pPos = pDropPos;
            else pPos = pFallenPos;
            rPos = rPos == Constantis.Garra.ROLL_SIDE_CONE ? Constantis.Garra.ROLL_UP : rPos;
            retrain = true;

        }

        //if (drop && DROP) cOpen = !cOpen;

        if (spin && SPIN) {
            rPos = rPos != Constantis.Garra.ROLL_UP ? Constantis.Garra.ROLL_UP : Constantis.Garra.ROLL_DOWN;
            cOpen = false;
        }
        //#endregion

        //#region PILOTO
        if ((colVert || colFront || colSide) && elevNvCol) {
            retrain = false;
            elevAjt = false;

            if (colVert && COLVERT) {
                rPos = rPos == Constantis.Garra.ROLL_SIDE_CONE ? Constantis.Garra.ROLL_UP : rPos;
                cOpen = !cOpen;
                cPrevStatus = cStatus;
                cStatus = 1;
                pPos = Constantis.Garra.HORIZONTAL;

            } else if (colFront && COLFRONT) {
                rPos = rPos == Constantis.Garra.ROLL_SIDE_CONE ? Constantis.Garra.ROLL_UP : rPos;
                cOpen = !cOpen;
                cPrevStatus = cStatus;
                cStatus = 2;
                pPos = pFallenPos;

            } else if (colSide && COLSIDE) {
                rPos = Constantis.Garra.ROLL_SIDE_CONE;
                cOpen = !cOpen;
                cPrevStatus = cStatus;
                cStatus = 3;
                pPos = pFallenPos;

            }
        }
        //#endregion

        cOpen = cOpen || cStatus != cPrevStatus;

        COLSIDE = !colSide;
        COLFRONT = !colFront;
        COLVERT = !colVert;
        SPIN = !spin;
        RETAIN = !retain;
        DROP = !drop;

        if (pPrevPos != pPos) {
            init = false;
            if (pPos == pFallenPos || pPrevPos == pFallenPos) {
                time.reset();
                cont = pPrevPos;
            }
        }
        pPrevPos = pPos;

        if (Math.abs(pPos-pPrevPos) == Math.abs(pDropPos-pFallenPos)) pTranstTime = Constantis.Garra.TRANSITION_TIME;
        else pTranstTime = Constantis.Garra.TRANSITION_TIME / 1.5;
        pTime = Math.min(time.time(), pTranstTime);

        if (elevNvCol && !init) pVel = Math.round(Math.min(pTime / pTranstTime, 1) * 1000) / 1000.0;
        else pVel = 1;

        pVel = Math.pow(pVel, 1.8);
        telemetry.addData("pVel", pVel);


        double pMid = pTranstTime / 2.0;
        elevCtrl = -Math.abs(pTime - pMid) / pMid + 1;
        elevCtrl = Math.max(elevCtrl, 0);

        telemetry.addData("retrain", retrain);

        if (retrain) {
            claw(cOpen);
            braco.setAjt(-1);
            elevAjt = true;

        } else if (pPos == pFallenPos) {

            if (cOpen) {
                elevCtrl = 1;
                claw(true);

            } else if (elev.getCorrentPos() < 80) {
                claw(false);
                elevAjt = true;
                braco.setAjt(-1);

            } else if (elev.getCorrentPos() < 700 && !elevAjt) braco.setAjt(.18);

        } else {
            claw(cOpen);
            braco.setAjt(-1);
            elevAjt = false;
        }

        if (!init) {
            if (elevCtrl > 0) {
                elev.setAjt(elevNvCol, Constantis.Garra.ELEVADOR_UP);
            } else {
                elev.setAjt(elevNvCol, elevAjt ? .25 : 0);
            }
        }

        if (pVel == 1) cont = pPrevPos;

        if (init) pitch.setPosition(Constantis.Garra.PITCH_DROP);
        else pitch.setPosition(cont + (pPos - cont) * pVel + (elevNvCol ? 0 : .8 * angle * (Constantis.Garra.HORIZONTAL - Constantis.Garra.PITCH_DROP)) + braco.getPos());

        if (pVel == 1 || !elevNvCol || init || pitch.getPosition() > .4 && pitch.getPosition() < .6) roll.setPosition(rPos);

        //telemetry.addData("pitch", pitch.getPosition());
        //telemetry.addData("roll", roll.getPosition());
        telemetry.addData("time", time.time());
        telemetry.addData("pPos", pPos);
        telemetry.addData("getTargetPosition", elev.getTargetPosition());

    }

    public void setClaw(double c, int t) {

        claw(c == 1.0);

        roll.setPosition(Constantis.Garra.ROLL_UP);
        pitch.setPosition(pDropPos + 0.1);

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
        clawD.setPosition(p ? Constantis.Garra.CLAW_OPEN : Constantis.Garra.CLAW_CLOSE);
        clawE.setPosition(p ? Constantis.Garra.CLAW_OPEN : Constantis.Garra.CLAW_CLOSE);

    }
}






