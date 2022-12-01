package org.firstinspires.ftc.teamcode.Subsystem;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Garra {

    boolean SPIN = true, COLEVERT = true, COLFRONT = true, COLSIDE = true, CLAW = true;
    boolean ElevNvCol = true, statsCol = false;
    double spinPos, clawPos, inclPos;

    Servo roll, garra, pitch;
    Elevador Elev;
    ElapsedTime time;

    public Garra(HardwareMap hardwareMap, Elevador e) {

        roll = hardwareMap.get(Servo.class, "Roll");
        garra = hardwareMap.get(Servo.class, "Garra");
        pitch = hardwareMap.get(Servo.class, "Pitch");

        Elev = e;
        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.startTime();
    }

    public void Control(boolean spin, boolean colVert, boolean colFront, boolean colSide, boolean claw) {

        ElevNvCol = Elev.ElvPos() == 0;

        //#region ENTREGA

        //Inverte cone se estiver em entrega
        if (spin && !ElevNvCol) {
            if (SPIN) {
                spinPos = spinPos == 0 ? 1 : 0;
            }
        }

        //Solta e recolhe garra se estiver em entrega
        if (!ElevNvCol) {

            statsCol = false;
            clawPos = 0;
            inclPos = 0.5;

            if (colFront || colSide || colVert) {

                clawPos = 1;
                inclPos = 0;
                spinPos = 0;

            }

        }

        //#endregion


        //#region COLETA

        if (colVert || colFront || colSide && ElevNvCol) {

            //Apos coletado recolhe a garra
            if (statsCol) {

                if (COLFRONT && COLSIDE && COLEVERT) {

                    statsCol = false;
                    clawPos = 0;
                    spinPos = 0;
                    inclPos = 0;

                    Elev.setAjt(true);
                }

            } else {

                //Posiciona a garra para coleta
                statsCol = true;
                clawPos = 1;
                spinPos = 0;
                inclPos = 0.5;


                if (colFront && COLFRONT) {
                    inclPos = 1;

                }

                if (colSide && COLSIDE) {
                    inclPos = 1;
                    spinPos = 0.5;

                }

            }
        }

        if (claw && !ElevNvCol) {
            if (CLAW) {
                clawPos = clawPos == 0 ? 1 : 0;
            }
        }

        COLSIDE = !colSide;
        COLFRONT = !colFront;
        COLEVERT = !colVert;
        SPIN = !spin;
        CLAW = !claw;



        garra.setPosition(clawPos);
        if (time.time() == 700){
            roll.setPosition(spinPos);
        } else if(time.time() == 1300) {
            pitch.setPosition(inclPos);
        } else if (time.time() == 2000) {
            time.reset();
            Elev.setAjt(false);
        }


    }


    /*
    public void roll(boolean a) {//boolean dpad_down, boolean dpad_left, boolean dpad_up) {
        // inverte roll
        if (a) {
            roll.setPosition(180 / 270.0);
        } else {
            roll.setPosition(0);
        }
    }

    // botao pincas
    public void pincas(boolean b) {
        if (b) {
            if (TD.getTargetPosition() == 0) {
                garra.setPosition(1);
                garra.setPosition(0);
            } else {
                garra.setPosition(0);
            }
        }
    }

    // cone caido  de frente
    public void ccf(boolean dpad_down) {
        if (jjj) {
            jjj = false;
            garra.setPosition(1);
            pitch.setPosition(135 / 270.0);
            coletar = true;
        }
    }


    // cone caido  de lado
    public void ccl(boolean dpad_left) {
        if (dpad_left) {
            if (kkkk) {
                jjj = true;
                garra.setPosition(1);
                roll.setPosition(90 / 270.0);
                pitch.setPosition(135 / 270.0);
                coletar = true;
            }
        } else {
            jjj = true;
        }
    }

    // cone em pe
    public void ccp(boolean dpad_up) {
        if (dpad_up) {
            if (fff) {
                fff = false;
                garra.setPosition(1);
                pitch.setPosition(90 / 270.0);
                coletar = true;
            }
        } else {
            fff = true;
        }
    }

    // verific
    public void vg(boolean dpad_left, boolean dpad_up, boolean dpad_down) {

        if (TD.getTargetPosition() == 0) {
            if (coletar == true) {
                garra.setPosition(0);
                pitch.setPosition(0);

            } else {
                garra.setPosition(1);
                pitch.setPosition(0);
                garra.setPosition(0);
            }
        }
    }//criar funcao de cada botao. chamando para configurar...
    //*/
}







