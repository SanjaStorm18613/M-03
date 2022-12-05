package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Braco {

    Servo E, D;
    Elevador elv;
    Telemetry telemetry;

    double pos, ajt = 0.1;

    int ePosAnt = 0;

    public Braco(HardwareMap hardwareMap, Telemetry t, Elevador e) {

//Servos direito e esquerdo do braço
        E = hardwareMap.get(Servo.class, "E");
        D = hardwareMap.get(Servo.class, "D");

        D.setDirection(Servo.Direction.REVERSE);
        E.setDirection(Servo.Direction.FORWARD);

        elv = e;
        telemetry = t;

    }

    public void Control() {

        if (elv.pos() < 2){
            pos = 0.71;
        } else if (elv.pos() == 2) {
            pos = 0.5;
        } else {
            pos = 0.38;
        }

        pos = Math.max(pos, ajt);

        telemetry.addData("bracoPos", pos);

        if (!elv.getBusy() || ePosAnt > elv.pos()){
            E.setPosition(pos);
            D.setPosition(pos);
        }

        ePosAnt = elv.pos();

    }

    public void setAjt(boolean activ) {

        ajt = activ ? 0.5 : 0;

    }
}