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

    double pVel = 0, elevAjst = 0, pPos = Constantis.Garra.PITCH_DROP, pTime = 0, pPrevPos = Constantis.Garra.PITCH_DROP, rPos = Constantis.Garra.ROLL_UP, cTargetPos = 0;

    boolean cOpen = false;
    boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    boolean elevNvCol = true, cIsBusy = false, init;

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

    public void Control(boolean spin, boolean colVert, boolean colFront, boolean colSide, boolean retain, boolean drop) {
        elevNvCol = elev.getNv() == 0;

        //#region COPILOTO
        if (retain && RETAIN) {
            cOpen = false;
            if (Math.abs(elev.getTargetPosition()) > 0) pPos = pPos != pDropPos ? pDropPos : pDropPos + .3;
            else pPos = pPos != pDropPos ? pDropPos : pFallenPos;

        }
        if (drop && DROP) cOpen = !cOpen;

        if (spin && SPIN) {
            rPos = rPos != Constantis.Garra.ROLL_UP ? Constantis.Garra.ROLL_UP : Constantis.Garra.ROLL_DOWN;
            cOpen = false;
        }
        //#endregion

        //#region PILOTO
        if ((colVert || colFront || colSide) && elevNvCol) {

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
            }
        }
        pPrevPos = pPos;

        if (Math.abs(pPos-pPrevPos) == Math.abs(pDropPos-pFallenPos)) pTranstTime = Constantis.Garra.TRANSITION_TIME;
        else pTranstTime = Constantis.Garra.TRANSITION_TIME / 1.5;
        pTime = Math.min(time.time(), pTranstTime);

        if (elevNvCol && !init) pVel = Math.round(Math.min(pTime / pTranstTime, 1) * 1000) / 1000.0;
        else pVel = 1;


        double pMid = pTranstTime / 2.0;
        elevAjst = -Math.abs(pTime - pMid) / pMid + 1;
        elevAjst = Math.max(elevAjst, 0);

        if (pPos == pFallenPos) {
            if (cOpen) {
                elevAjst = 1;
                claw(true);
            } else if (elev.getCorrentPos() < 80) claw(false);
            else if (elev.getCorrentPos() < 500) braco.setAjt(.18);
            else braco.setAjt(-1);

        } else {
            claw(cOpen);
            braco.setAjt(-1);
        }

        if (!init) elev.setAjt(elevNvCol, elevAjst * Constantis.Garra.ELEVADOR_UP);

        if (init) pitch.setPosition(Constantis.Garra.PITCH_DROP);
        else pitch.setPosition(pitch.getPosition() + (pPos - pitch.getPosition()) * pVel + braco.getPos());

        if (pTime >= pTranstTime || !elevNvCol || init) roll.setPosition(rPos);

        //telemetry.addData("pitch", pitch.getPosition());
        //telemetry.addData("roll", roll.getPosition());
        telemetry.addData("time", time.time());
        telemetry.addData("pVel", pVel);
        telemetry.addData("pPrevPos", pPrevPos);
        telemetry.addData("pPos", pPos);
        telemetry.addData("init", init);
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






