package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Braco {

    Servo E, D;
    Elevador elv;

    double pos;

    public Braco(HardwareMap hardwareMap, Elevador e) {

//Servos direito e esquerdo do braço
        E = hardwareMap.get(Servo.class, "L");
        D = hardwareMap.get(Servo.class, "R");

        elv = e;

    }

    public void Control() {

        pos = elv.ElvPos() == 0 ? 0.1 : 0.8;

        E.setPosition(pos);
        D.setPosition(1 - pos);
    }
}