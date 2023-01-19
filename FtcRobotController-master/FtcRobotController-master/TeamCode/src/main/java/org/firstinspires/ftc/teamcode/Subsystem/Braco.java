package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Braco {

    double  mnP   = Constantis.Braco.MIN_POS,
            mxP   = Constantis.Braco.MAX_POS;

    Servo E, D;
    Elevador elev;
    Telemetry telemetry;

    double pos = 0, ajt = -1, calPos = 0;
    int ePosAnt = 0;

    public Braco(Telemetry t, HardwareMap hardwareMap, Elevador e) {

        telemetry = t;

//Servos direito e esquerdo do braço
        E = hardwareMap.get(Servo.class, "E");
        D = hardwareMap.get(Servo.class, "D");

        D.setDirection(Servo.Direction.FORWARD);
        E.setDirection(Servo.Direction.REVERSE);

        elev = e;
    }

    public void Control() {

        pos = (elev.getCorrentPos() / ((Constantis.Elevador.NV_3/0.9) * Constantis.Elevador.CONVR)) * (mxP - mnP) + mnP;

        pos = Math.min(pos, mxP);
        pos = Math.max(pos, mnP);

        pos = Math.round(pos * 1000.0) / 1000.0;

        pos = ajt == -1 ? pos : ajt;

        calPos = pos;

        E.setPosition(pos);
        D.setPosition(pos);

        ePosAnt = elev.getNv();
        telemetry.addData("braco", E.getPosition());
/*
        telemetry.addData("pos", pos);
        telemetry.addData("getPos", getPos());
// */
    }

    public double getPos(){
        return calPos - mnP;
    }


    public void setAjt(double p) {
        ajt = p;
    }

    public double getCorrentPos(){
        return E.getPosition();
    }

}