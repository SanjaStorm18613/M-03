package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Braco {

    double  mnP   = Constantis.Braco.MIN_POS,
            mxP   = Constantis.Braco.MAX_POS,
            nv    = Constantis.Elevador.NV_2,
            convr = Constantis.Elevador.CONVR;

    Servo E, D;
    Elevador elev;
    Telemetry telemetry;

    double pos, ajt;

    int ePosAnt = 0;

    public Braco(Telemetry t, HardwareMap hardwareMap, Elevador e) {

        telemetry = t;

//Servos direito e esquerdo do braço
        E = hardwareMap.get(Servo.class, "E");
        D = hardwareMap.get(Servo.class, "D");

        D.setDirection(Servo.Direction.REVERSE);
        E.setDirection(Servo.Direction.FORWARD);

        elev = e;

    }

    public void Control() {

        pos = (elev.getCorrentPos() / nv * convr) * (mxP - mnP) + mnP;

        pos = Math.min(pos, mxP);

        pos = Math.max(pos, ajt);

        E.setPosition(pos);
        D.setPosition(pos);

        ePosAnt = elev.getNv();

    }

    public double getPos(){
        return E.getPosition();
    }


    public void setAjt(double p) {
        ajt = p;
    }
}