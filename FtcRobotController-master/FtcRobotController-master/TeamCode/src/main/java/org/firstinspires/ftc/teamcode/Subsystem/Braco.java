package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Braco {

    double  mnP   = Constantis.Braco.MIN_POS,
            mxP   = Constantis.Braco.MAX_POS,
            nv    = Constantis.Elevador.NV_3,
            convr = Constantis.Elevador.CONVR;

    Servo E, D;
    Elevador elev;
    Telemetry telemetry;

    double pos = 0, ajt = 0;
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

        pos = (elev.getCorrentPos() / (nv * convr)) * (mxP - mnP) + mnP;

        pos = Math.min(pos, mxP);

        pos = Math.max(pos, ajt);

        pos = (double) Math.round(pos * 1000)/1000;

        E.setPosition(pos);
        D.setPosition(pos);

        ePosAnt = elev.getNv();

        telemetry.addData("braco", E.getPosition());
        telemetry.addData("pos", pos);
        telemetry.addData("getPos", getPos());

    }

    public double getPos(){
        return E.getPosition() - mnP;
    }


    public void setAjt(double p) {
        ajt = p;
    }
}