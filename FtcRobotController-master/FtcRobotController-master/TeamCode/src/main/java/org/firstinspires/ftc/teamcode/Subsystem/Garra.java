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
            pBracoUp = Constantis.Garra.BRACO_UP,
            rUpPos = Constantis.Garra.ROLL_UP,
            rDwPos = Constantis.Garra.ROLL_DOWN,
            rSide = Constantis.Garra.ROLL_SIDE_CONE;

    boolean cOpen = false, pUp = true, rUp = true, pColet = false, rColet = false, pGrand = false;

    boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    boolean elevNvCol = true, statsCol = false;

    Servo roll, claw, pitch;
    Elevador elev;
    Braco braco;
    ElapsedTime time;
    Telemetry telemetry;

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
            pGrand = true;
            cOpen = false;
            pColet = false;
            rColet = false;

        }

        //#region ENTREGA

        if (!elevNvCol) {
            pColet = false;
            rColet = false;
            pGrand = false;

            if (spin && SPIN) rUp = !rUp;
            if (drop && DROP) cOpen = !cOpen;

        }

        //#endregion
        //#region COLETA

        if ((colVert || colFront || colSide) && elevNvCol) {
            pUp = false;
            rUp = true;
            pGrand = false;

            if (colVert && COLVERT) {
                pColet = false;
                rColet = false;
                cOpen = !cOpen;

            } else if (colFront && COLFRONT) {
                pColet = true;
                rColet = false;
                cOpen = !cOpen;

            } else if (colSide && COLSIDE) {
                pColet = true;
                rColet = true;
                cOpen = !cOpen;

            }
        }


        if (pColet && elevNvCol) {
            braco.setAjt(pBracoUp);
            //time.reset();
        } else {
            braco.setAjt(0);
        }


        COLSIDE = !colSide;
        COLFRONT = !colFront;
        COLVERT = !colVert;
        SPIN = !spin;
        RETAIN = !retain;
        DROP = !drop;

        claw.setPosition(cOpen ? cOpn : cCls);
        roll.setPosition((rUp ? rUpPos : rDwPos) + (rColet ? rSide : 0));

        if (/*time.time() > 1500 || elev.getNv() != 0*/ true) {
            pitch.setPosition(-(pGrand ? 0.2 : 0) + (pUp ? pDrop : pHorz) + (pColet ? pFlln : 0) + braco.getPos());
        }

        telemetry.addData("claw", claw.getPosition());
        telemetry.addData("roll", roll.getPosition());
        telemetry.addData("pitch", pitch.getPosition());
        telemetry.addData("pGrand", pGrand);

    }

    public void setPos(double p, double r, double c) {

        claw.setPosition(c);
        roll.setPosition(r);
        pitch.setPosition(p);

    }

}







