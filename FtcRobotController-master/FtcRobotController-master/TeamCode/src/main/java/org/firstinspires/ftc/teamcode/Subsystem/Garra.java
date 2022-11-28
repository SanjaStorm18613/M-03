package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Garra {

    boolean SPIN = true, COLEVERT = true, COLFRONT = true, COLSIDE = true;
    boolean ElevNvCol = true;
    double spinPos, sPos, iPos, cPos, inclPos, clawPos;

    Servo roll, garra, pitch;
    Elevador Elev;

    public Garra(HardwareMap hardwareMap, Elevador e) {

        roll = hardwareMap.get(Servo.class, "Roll");
        garra = hardwareMap.get(Servo.class, "Garra");
        pitch = hardwareMap.get(Servo.class, "Pitch");

        Elev = e;
    }

    public void Control(boolean spin, boolean colVert, boolean colFront, boolean colSide) {

        ElevNvCol = Elev.ElvPos() == 0;

        if (spin){
            if (SPIN && ElevNvCol){
                sPos = sPos == 0 ? 1 : 0;
            }
        }

        

    }

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
}







