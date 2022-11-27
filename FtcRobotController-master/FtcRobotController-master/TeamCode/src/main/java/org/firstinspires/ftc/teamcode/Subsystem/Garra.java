package org.firstinspires.ftc.teamcode.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Garra {
    boolean coletar;
    boolean jjj;
    boolean kkkk;
    boolean fff;
    Servo Roll;
    DcMotor TD;
    Servo Garra;
    Servo Pitch;

    public Garra(HardwareMap hardwareMap) {


        Roll = hardwareMap.get(Servo.class, "Roll");
        TD = hardwareMap.get(DcMotor.class, "TD");
        Garra = hardwareMap.get(Servo.class, "Garra");
        Pitch = hardwareMap.get(Servo.class, "Pitch");
    }

    public void roll(boolean a) {//boolean dpad_down, boolean dpad_left, boolean dpad_up) {
        // inverte roll
        if (a) {
            Roll.setPosition(180 / 270);
        } else {
            Roll.setPosition(0);
        }
    }

    // botao pincas
    public void pincas(boolean b) {
        if (b) {
            if (TD.getTargetPosition() == 0) {
                Garra.setPosition(1);
                Garra.setPosition(0);
            } else {
                Garra.setPosition(0);
            }
        }
    }

    // cone caido  de frente
    public void ccf(boolean dpad_down) {
        if (jjj) {
            jjj = false;
            Garra.setPosition(1);
            Pitch.setPosition(135 / 270);
            coletar = true;
        }
    }


    // cone caido  de lado
    public void ccl(boolean dpad_left) {
        if (dpad_left) {
            if (kkkk) {
                jjj = true;
                Garra.setPosition(1);
                Roll.setPosition(90 / 270);
                Pitch.setPosition(135 / 270);
                coletar = true;
            }
        } else {
            jjj = true;
        }
    }
        // cone em pe
        public void ccp(boolean dpad_up){
            if (dpad_up) {
                if (fff) {
                    fff = true;
                    Garra.setPosition(1);
                    Pitch.setPosition(90 / 270);
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
                    Garra.setPosition(0);
                    Pitch.setPosition(0);

                } else {
                    Garra.setPosition(1);
                    Pitch.setPosition(0);
                    Garra.setPosition(0);
                }
            }
        }//criar funcao de cada botao. chamando para configurar...
}







