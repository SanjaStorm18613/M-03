package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Garra {

    boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    boolean elevNvCol = true, statsCol = false, update = false;
    double spinPos = 0, clawPos = 0, inclPos = 0.18, ajt = 0;
    int elevNvAnt = 0;

    Servo roll, garra, pitch;
    Elevador elev;
    Braco braco;
    ElapsedTime time;
    Telemetry telemetry;

    public Garra(HardwareMap hardwareMap, Elevador e, Telemetry t, Braco b) {

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

        elevNvCol = elev.pos() == 0;

        update = elev.getBusy() && elev.pos() != elevNvAnt;
        elevNvAnt = elev.pos();

        if (update) {

            if (elev.pos() > 2) inclPos = 0.5;
            else if (elev.pos() == 2) inclPos = 0.4;
            else inclPos = .2;

        }


        //#region ENTREGA

        if (!elevNvCol) {

            if (spin && SPIN) spinPos = spinPos == 0 ? 0.35 : 0;

            if (retain && RETAIN) {

                if (elev.pos() > 2) inclPos = inclPos != 0.8 ? 0.8 : 0.6;
                else if (elev.pos() == 2) inclPos = inclPos != 0.8 ? 0.8 : 0.4;
                else inclPos = inclPos != 0.2 ? 0.2 : 0.7;

            }

            if (drop && DROP) clawPos = clawPos == 0 ? .5 : 0;

        }

        //#endregion
        //#region COLETA

        if ((colVert || colFront || colSide || retain) && elevNvCol) {

            if (retain && RETAIN) {
                statsCol = false;
                clawPos = 0;
                spinPos = 0.01;
                inclPos = 0.2;

            }
            //Apos coletado recolhe a garra
            if (statsCol) {

                if ((colVert && COLVERT) || (colFront && COLFRONT) || (colSide && COLSIDE)) {
                    statsCol = false;
                    clawPos = clawPos == 0 ? 0.5 : 0;

                }

            } else {

                if (colVert && COLVERT) {
                    statsCol = true;

                    spinPos = 0.01;
                    inclPos = 0.73;
                    clawPos = clawPos == 0 ? 0.5 : 0;

                } else if (colFront && COLFRONT) {
                    statsCol = true;

                    spinPos = 0.01;
                    inclPos = 1.0;
                    clawPos = clawPos == 0 ? 0.5 : 0;

                } else if (colSide && COLSIDE) {
                    statsCol = true;

                    spinPos = 0.2;
                    inclPos = 1.0;
                    clawPos = clawPos == 0 ? 0.5 : 0;

                }
            }
        }

        if ((inclPos == 1) && elevNvCol) {
            elev.setAjt(true, 0.35);

        } else {
            elev.setAjt(false, 0);
        }

        COLSIDE = !colSide;
        COLFRONT = !colFront;
        COLVERT = !colVert;
        SPIN = !spin;
        RETAIN = !retain;
        DROP = !drop;

        inclPos = Math.min(ajt, inclPos);

        garra.setPosition(clawPos);
        roll.setPosition(spinPos);

        if (time.time() >= 1500 || !elev.getBusy()) {
            pitch.setPosition(inclPos);
            time.reset();
        }


    }

    public void setAjt(boolean activ) {

        ajt = activ ? 0 : 1;

    }

    public void setPos (double p, double r, double c) {

        garra.setPosition(c);
        roll.setPosition(r);
        pitch.setPosition(p);

    }

}







