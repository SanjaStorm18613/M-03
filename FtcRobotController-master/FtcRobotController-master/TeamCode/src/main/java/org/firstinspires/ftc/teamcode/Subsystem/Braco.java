package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Braco {

    Servo E, D;
    Elevador elv;
    double ajs;
    double pos;

    public Braco (HardwareMap hardwareMap, Elevador e){

        E = hardwareMap.get(Servo.class, "L");
        D = hardwareMap.get(Servo.class, "R");

        elv = e;

    }

    public void contl(){

        if (elv.pos = 0) {

            pos = 0.1 + ajs;

            if (pos > 0.8) pos = 0.8;
            if (pos < 0.1) pos = 0.1;

            E.setPosition(pos);
            D.setPosition(1 - pos);

        } else {

            pos = 0.8 + ajs;

            if (pos > 0.8) pos = 0.8;
            if (pos < 0.1) pos = 0.1;

            E.setPosition(0.8 + ajs);
            D.setPosition(1 - 0.8 + ajs);

        }
    }

    public void Ajuste (Boolean up, Boolean dow){

        ajs += up ? 0.1 : dow ? -0.1 : 0;

    }
}