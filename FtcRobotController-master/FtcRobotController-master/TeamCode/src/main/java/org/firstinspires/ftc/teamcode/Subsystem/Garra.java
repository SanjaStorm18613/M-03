package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Garra {

    boolean SPIN = true, COLVERT = true, COLFRONT = true, COLSIDE = true, RETAIN = true, DROP = true;
    boolean ElevNvCol = true, statsCol = false;
    double spinPos = 0, clawPos = 0, inclPos = 0.18, antInclPos = 0.18;

    Servo roll, garra, pitch;
    Elevador Elev;
    ElapsedTime time;
    Telemetry telemetry;

    public Garra(HardwareMap hardwareMap, Elevador e, Telemetry t) {

        roll = hardwareMap.get(Servo.class, "Roll");
        garra = hardwareMap.get(Servo.class, "Garra");
        pitch = hardwareMap.get(Servo.class, "Pitch");

        garra.setDirection(Servo.Direction.REVERSE);

        Elev = e;
        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.startTime();

        telemetry = t;
    }

    public void Control(boolean spin, boolean colVert, boolean colFront, boolean colSide, boolean retain, boolean drop) {

        ElevNvCol = Elev.ElvPos() == 0;

        //#region ENTREGA

        if (!ElevNvCol) {

            if (spin && SPIN) {
                spinPos = spinPos == 0 ? 0.35 : 0;
            }

            if (retain && RETAIN) {

                if (Elev.ElvPos() > 2) {

                    inclPos = inclPos != 0.8 ? 0.8 : 0.5;

                } else if (Elev.ElvPos() == 2) {

                    inclPos = inclPos != 0.7 ? 0.7 : 0.4;

                } else {

                    inclPos = inclPos != 0.1 ? 0.1 : 0.3;

                }
            }

            if (drop && DROP) {
                clawPos = clawPos == 0 ? 1 : 0;
            }
        }
        //#endregion
        //#region COLETA

        if ((colVert || colFront || colSide || retain) && ElevNvCol) {

            if (retain && RETAIN) {
                statsCol = false;
                clawPos = 0;
                spinPos = 0;
                inclPos = 0.18;

            }
            //Apos coletado recolhe a garra
            if (statsCol) {

                if ((colVert && COLVERT) || (colFront && COLFRONT) || (colSide && COLSIDE)) {
                    statsCol = false;
                    clawPos = clawPos == 0 ? 1 : 0;

                }

            } else {

                if (colVert && COLVERT) {
                    statsCol = true;
                    spinPos = 0;
                    inclPos = 0.65;
                    clawPos = clawPos == 0 ? 1 : 0;
                } else if (colFront && COLFRONT) {
                    statsCol = true;
                    spinPos = 0;
                    inclPos = 0.9;
                    clawPos = clawPos == 0 ? 1 : 0;
                } else if (colSide && COLSIDE) {
                    statsCol = true;
                    spinPos = 0.18;
                    inclPos = 0.1;
                    clawPos = clawPos == 0 ? 1 : 0;

                }
            }
        }

        if ((inclPos == 0.9) && ElevNvCol) {
            Elev.setAjt(true, 1);
        }

        COLSIDE = !colSide;
        COLFRONT = !colFront;
        COLVERT = !colVert;
        SPIN = !spin;
        RETAIN = !retain;
        DROP = !drop;

        telemetry.addData("time", time.time());
        telemetry.addData("getBusy", Elev.getBusy());

        garra.setPosition(clawPos);
        roll.setPosition(spinPos + 0.1);
        if (time.time() >= 1200 || !Elev.getBusy()) {
            pitch.setPosition(inclPos);
            time.reset();
        }

        telemetry.addData("spinPos", spinPos);
        telemetry.addData("inclPos", inclPos + 0.1);
        telemetry.addData("clawPos", clawPos);


    }

}







