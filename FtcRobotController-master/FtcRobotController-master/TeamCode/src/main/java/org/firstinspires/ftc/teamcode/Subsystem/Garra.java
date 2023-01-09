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
            rSide = Constantis.Garra.ROLL_SIDE_CONE;

    Servo roll, claw, pitch;
    Elevador elev;
    Braco braco;
    ElapsedTime time;
    Telemetry telemetry;

    boolean cOpen = false, pUp = true, rUp = true, pColet = false, rColet = false;

    boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    boolean elevNvCol = true, pColetSts = false;

    double pVel = 0, elevAjst = 0, pPos = 0, pMid = 0, pTime = 0;

    int stausC = 0, prevStatusC = 0;

    public Garra(Telemetry t, HardwareMap hardwareMap, Elevador e, Braco b) {

        roll = hardwareMap.get(Servo.class, "Roll");
        claw = hardwareMap.get(Servo.class, "Garra");
        pitch = hardwareMap.get(Servo.class, "Pitch");

        claw.setDirection(Servo.Direction.FORWARD);
        roll.setDirection(Servo.Direction.FORWARD);
        pitch.setDirection(Servo.Direction.FORWARD);

        elev = e;
        telemetry = t;
        braco = b;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.startTime();


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


        if (pColetSts == !pColet) time.reset();
        pColetSts = pColet;

        pTime = Math.min(time.time(), 3000);

        pVel = Math.round((Math.min(pTime, 2000)/2000.0) * 1000)/1000.0;
        pVel = elevNvCol ? pVel : 1;

        pPos = (pFlln - pHorz) * Math.abs((pColet ? 0 : 1) - pVel);
        pPos += (pUp ? pDrop : (pHorz-(!elevNvCol ? 0.3 : 0)));
        pPos += braco.getPos() * 0.9;

        pMid = (pFlln - pHorz)/2.0;
        elevAjst = 1-Math.round(Math.abs((pPos - (pHorz + pMid)) / pMid) * 1000) / 1000.0;
        elevAjst = Math.max(elevAjst, 0);

        telemetry.addData("elevAjst", elevAjst);

/*
        if (pColet) {

            if (cOpen) {

                elev.setAjt(elevNvCol, pElevUp);

            } else {

                elev.setAjt(elevNvCol, elevAjst * 2);

            }


        }
//*/

        roll.setPosition((rUp ? rUpPos : rDwPos) + (rColet ? rSide : 0));
        pitch.setPosition(pPos);

        if (!elev.getBusy() || !pColet || !cOpen) {

            claw.setPosition(cOpen ? cOpn : cCls);

        }


        //telemetry.addData("claw", claw.getPosition());
        //telemetry.addData("roll", roll.getPosition());
        //telemetry.addData("pitch", pitch.getPosition());
        telemetry.addData("pVel", pVel);

    }

    public void setPos(double p, double r, double c) {

        claw.setPosition(c);
        roll.setPosition(r);
        pitch.setPosition(p);

    }

}
/*
((pColet ? pFlln : 0) -
(pUp ? pDrop : (pHorz-(!elevNvCol ? 0.3 : 0)))) +
braco.getPos() * 0.8
 */






