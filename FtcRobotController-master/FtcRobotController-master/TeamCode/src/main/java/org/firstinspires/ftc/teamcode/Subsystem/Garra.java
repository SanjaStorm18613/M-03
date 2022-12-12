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
            pElevUp = Constantis.Garra.ELEV_UP,
            rUpPos = Constantis.Garra.ROLL_UP,
            rDwPos = Constantis.Garra.ROLL_DOWN,
            rSide = Constantis.Garra.ROLL_SIDE_CONE;

    double pAjt = 0;
    boolean cOpen = false, pUp = true, rUp = true, pColet = false, rColet = false;

    boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    boolean elevNvCol = true, statsCol = false;

    Servo roll, garra, pitch;
    Elevador elev;
    Braco braco;
    ElapsedTime time;
    Telemetry telemetry;

    public Garra(Telemetry t, HardwareMap hardwareMap, Elevador e, Braco b) {

        roll = hardwareMap.get(Servo.class, "Roll");
        garra = hardwareMap.get(Servo.class, "Garra");
        pitch = hardwareMap.get(Servo.class, "Pitch");

        garra.setDirection(Servo.Direction.FORWARD);
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

        //#region ENTREGA

        if (!elevNvCol) {

            pAjt = braco.getPos() + pDrop; //braco.getPos() * 1.3 + pDropP;

            if (spin && SPIN) rUp = !rUp;

            if (retain && RETAIN) pUp = !pUp;

            if (drop && DROP) cOpen = !cOpen;

        }

        //#endregion
        //#region COLETA

        if ((colVert || colFront || colSide || retain) && elevNvCol) {

            rUp = true;

            if (statsCol) {

                pUp = true;

                if ((colVert && COLVERT) || (colFront && COLFRONT) || (colSide && COLSIDE)) {
                    statsCol = false;
                    cOpen = !cOpen;

                }

            } else {

                pUp = false;

                if (colVert && COLVERT) {
                    statsCol = true;
                    pColet = false;
                    rColet = false;
                    cOpen = !cOpen;

                } else if (colFront && COLFRONT) {
                    statsCol = true;
                    pColet = true;
                    rColet = false;
                    cOpen = !cOpen;

                } else if (colSide && COLSIDE) {
                    statsCol = true;
                    pColet = true;
                    rColet = true;
                    cOpen = !cOpen;

                }
            }

            if (retain && RETAIN) {
                statsCol = false;
                cOpen = false;
                pColet = false;
                rColet = false;

            }
        }

        if (pColet && elevNvCol) {
            elev.setAjt(true, pElevUp);
        } else {
            elev.setAjt(false, 0);
        }

        COLSIDE = !colSide;
        COLFRONT = !colFront;
        COLVERT = !colVert;
        SPIN = !spin;
        RETAIN = !retain;
        DROP = !drop;

        garra.setPosition(cOpen ? cOpn : cCls);
        roll.setPosition((rUp ? rUpPos : rDwPos) + (rColet ? rSide : 0));

        if (!elev.getBusy()) {
            pitch.setPosition((pUp ? pDrop : pHorz) + (pColet ? pFlln : 0) + pAjt);
        }

    }

    public void setPos(double p, double r, double c) {

        garra.setPosition(c);
        roll.setPosition(r);
        pitch.setPosition(p);

    }

}







