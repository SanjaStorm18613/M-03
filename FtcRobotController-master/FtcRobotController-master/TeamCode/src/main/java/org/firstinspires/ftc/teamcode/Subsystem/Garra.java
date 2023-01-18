package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Garra {

    double cCls = Constantis.Garra.CLAW_CLOSE,
            cOpn = Constantis.Garra.CLAW_OPEN,
            pDrop = Constantis.Garra.PITCH_DROP,
            pHorz = Constantis.Garra.HORIZONTAL,
            pFlln = Constantis.Garra.PITCH_FALLEN,
            pElevUp = Constantis.Garra.ELEVADOR_UP,
            rUpPos = Constantis.Garra.ROLL_UP,
            rDwPos = Constantis.Garra.ROLL_DOWN,
            rSide = Constantis.Garra.ROLL_SIDE_CONE,
            pTTT = Constantis.Garra.TRANSITION_TIME;

    Servo roll, clawD, clawE, pitch;
    Elevador elev;
    Braco braco;
    ElapsedTime acelT;
    Telemetry telemetry;

    boolean cOpen = false, pUp = true, rUp = true, pColet = false, rColet = false;

    boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    boolean elevNvCol = true, pColetSts = false, init;

    double pVel = 0, elevAjst = 0, pPos = 0, pMid = 0, pTime = 0;

    int stausC = 0, prevStatusC = 0;

    double cCorrentPos = 0;
    boolean cIsBusy = true;

    public Garra(Telemetry t, HardwareMap hardwareMap, Elevador e, Braco b) {

        roll = hardwareMap.get(Servo.class, "Roll");
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

        acelT = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        acelT.startTime();

        init = true;

    }

    public void Control(boolean spin, boolean colVert, boolean colFront, boolean colSide, boolean retain, boolean drop) {

        elevNvCol = elev.getNv() == 0;

        if (retain && RETAIN) {
            pUp = !pUp;
            cOpen = false;
            pColet = false;
            rColet = false;

        }
        if (drop && DROP) cOpen = !cOpen;

        //#region ENTREGA

        if (!elevNvCol) {
            pColet = false;
            rColet = false;

            if (spin && SPIN) rUp = !rUp;

        }

        //#endregion
        //#region COLETA

        if ((colVert || colFront || colSide) && elevNvCol) {
            pUp = false;
            rUp = true;


            if (colVert && COLVERT) {
                pColet = false;
                rColet = false;
                cOpen = !cOpen;
                prevStatusC = stausC;
                stausC = 1;

            } else if (colFront && COLFRONT) {
                pColet = true;
                rColet = false;
                cOpen = !cOpen;
                prevStatusC = stausC;
                stausC = 2;

            } else if (colSide && COLSIDE) {
                pColet = true;
                rColet = true;
                cOpen = !cOpen;
                prevStatusC = stausC;
                stausC = 3;

            }

        }

        cOpen = cOpen || stausC != prevStatusC;

        COLSIDE = !colSide;
        COLFRONT = !colFront;
        COLVERT = !colVert;
        SPIN = !spin;
        RETAIN = !retain;
        DROP = !drop;


        if (pColetSts == !pColet) {
            acelT.reset();
            init = false;
        }
        pColetSts = pColet;

        pTTT = Constantis.Garra.TRANSITION_TIME;
        if (pUp) pTTT += 1000;

        pTime = Math.min(acelT.time(), pTTT);

        if (elevNvCol && !init) {
            pVel = Math.round(Math.min(pTime / pTTT, 1) * 1000)/1000.0;

            if (!pColet) pVel = 1 - pVel;

        } else pVel = 0;


        pPos = (pFlln - pHorz) * pVel;

        if (pUp) pPos += pDrop + pHorz * pVel;
        else if (elevNvCol) pPos += pHorz;
        else pPos += pHorz -.3;

        pPos += braco.getPos() * 0.9;

        pitch.setPosition(pPos);

        pMid = pTTT / 2.0;
        elevAjst = -Math.abs(pTime - pMid) / pMid + 1;
        elevAjst = Math.max(elevAjst, 0);


        if (pColet) {
            if (cOpen) {
                elevAjst = pElevUp;
                claw(true);
            }
            else if (elev.getCorrentPos() < 100) {
                claw(false);
            }
        } else claw(cOpen);

        roll.setPosition((rUp ? rUpPos : rDwPos) + (rColet ? rSide : 0));

        if (!init) elev.setAjt(elevNvCol, elevAjst * pElevUp);



        telemetry.addData("elevAjst", elevAjst);
        telemetry.addData("cOpen", cOpen);
        telemetry.addData("pVel", pVel);
        telemetry.addData("claw", clawD.getPosition());
        telemetry.addData("roll", roll.getPosition());
        telemetry.addData("pitch", pitch.getPosition());

    }

    public void setClaw(double c, int t) {

        claw(c == 1.0);

        roll.setPosition(rUpPos);
        pitch.setPosition(pDrop + 0.1);

        if (cCorrentPos != clawD.getPosition() && !cIsBusy) {
            acelT.reset();
            cIsBusy = true;
        }
        cCorrentPos = clawD.getPosition();

        cIsBusy = acelT.time() < t;

        telemetry.addData("cCorrentPos", cCorrentPos);
        telemetry.addData("time", acelT.time());

    }

    public boolean getBusy(){
        return cIsBusy;
    }

    public void claw(boolean p) {
        clawD.setPosition(p ? cOpn : cCls);
        clawE.setPosition(p ? cOpn : cCls);

    }
}
/*
((pColet ? pFlln : 0) -
(pUp ? pDrop : (pHorz-(!elevNvCol ? 0.3 : 0)))) +
braco.getPos() * 0.8
 */






