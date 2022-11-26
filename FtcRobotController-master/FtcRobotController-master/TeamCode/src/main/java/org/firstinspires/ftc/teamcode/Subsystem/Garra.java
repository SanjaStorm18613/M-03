package org.firstinspires.ftc.teamcode.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Garra<a> {
    boolean coletar;
    boolean jjj;
    boolean kkkk;
    boolean fff;

    public Garra(HardwareMap hardwareMap) {

         Servo Roll;
         DcMotor TD;
         Servo Garra;
         Servo Pitch;
        Roll = hardwareMap.get(Servo.class, "Roll");
        TD = hardwareMap.get(DcMotor.class, "TD");
        Garra = hardwareMap.get(Servo.class, "Garra");
        Pitch = hardwareMap.get(Servo.class, "Pitch");
    }


        /**
         * This function is executed when this Op Mode is selected from the Driver Station.
         */
         public void bts(boolean a, boolean dpad_down, boolean dpad_left, boolean dpad_up) {
                // inverte roll
                if (a) {
                    Roll.setPosition(180 / 270);
                } else {
                    Roll.setPosition(0);
                }
                // botao pincas
                if (TD.getTargetPosition() == 0) {
                    dfg();
                    if (Garra.getPosition() == 0) {
                        Garra.setPosition(1);
                    } else {
                        Garra.setPosition(0);
                    }
                } else {
                    Garra.setPosition(1);
                    Pitch.setPosition(0);
                    Garra.setPosition(0);
                }
                // cone caido  de frente
                if (dpad_down) {
                    if (jjj) {
                        jjj = false;
                        Garra.setPosition(1);
                        Pitch.setPosition(135 / 270);
                        coletar = true;
                    }
                } else {
                    jjj = true;
                }
                // cone caido  de lado
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
                // cone em pe
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
                telemetry.update();
            }
        }


        /**
         * Describe this function...
         */
        // verific
        private void dfg() {
            private void dfg() {
                if (TD.getTargetPosition() == 0) {
                    if (coletar == true) {
                        Garra.setPosition(0);
                        Pitch.setPosition(0);
                    } else {
                    }
                } else {
                    Garra.setPosition(1);
                    Pitch.setPosition(0);
                    Garra.setPosition(0);
                }
        }
    }
}

