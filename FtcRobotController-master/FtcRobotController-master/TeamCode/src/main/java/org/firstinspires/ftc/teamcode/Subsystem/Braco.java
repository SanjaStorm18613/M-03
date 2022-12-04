package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Braco {

    Servo E, D;
    Elevador elv;

    double pos;

    int ePosAnt = 0;

    public Braco(HardwareMap hardwareMap, Elevador e) {

//Servos direito e esquerdo do braço
        E = hardwareMap.get(Servo.class, "E");
        D = hardwareMap.get(Servo.class, "D");

        D.setDirection(Servo.Direction.REVERSE);

        elv = e;

    }

    public void Control() {

        if (elv.ElvPos() < 2){
            pos = 0.5;
        } else if (elv.ElvPos() == 2) {
            pos = 0.25;
        } else {
            pos = 0.15;
        }

        if (!elv.getBusy() || ePosAnt > elv.ElvPos()){
            E.setPosition(pos);
            D.setPosition(pos);
        }

        ePosAnt = elv.ElvPos();

    }
}