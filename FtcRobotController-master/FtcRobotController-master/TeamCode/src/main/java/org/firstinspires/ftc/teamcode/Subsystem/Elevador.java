package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Elevador {

    DcMotor FD;
    Servo L, R;

    public Elevador(HardwareMap hardwareMap) {



        FD = hardwareMap.get(DcMotor.class, "FD");
        L = hardwareMap.get(Servo.class, "L");
        R = hardwareMap.get(Servo.class, "R");
    }



    public void subir(Boolean a, Boolean x, Boolean y, Boolean b) {
        double pos = 0;
        int niv = 0;

        if (a) {
            pos = pos + 0.25;
            FD.setTargetPosition((int) (niv + pos));

        } else if (b) {
            pos = pos - 0.25;
            FD.setTargetPosition((int) (niv + pos));
        }
        if (x) {
            if (niv == 0) {
                FD.setTargetPosition(niv + 1);
                // braco no max.
                L.setPosition(-1);
                R.setPosition(1);
            } else if (niv == 1) {
                FD.setTargetPosition(niv + 1);
            }
        } else if (y) {
            if (niv == 2) {
                FD.setTargetPosition(niv - 1);
            } else if (niv == 1) {
                FD.setTargetPosition(niv - 1);
                L.setPosition(1);
                R.setPosition(-1);
            }
        } else {
            FD.setTargetPosition(niv);
        }
    }
}
