package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Elevador {

    Constantis.Elevador eVar;
    double  nv1 = Constantis.Elevador.NV_1,
            nv2 = Constantis.Elevador.NV_2,
            nv3 = Constantis.Elevador.NV_3,
            convr = Constantis.Elevador.CONVR,
            vUp = Constantis.Elevador.UP_S,
            vDwn = Constantis.Elevador.DOWN_S;

    DcMotor elev;
    Telemetry telemetry;

    boolean UPCtr = true, UP = true, DOWNCtr = true, DOWN = true, velUp = true;
    double ajt = 0, setAjt = 0;

    double[] nv = {0, nv1, nv2, nv3};
    int count = 0, pos = 0;
    boolean mxLimt = false, mnLimt = false;

    public Elevador(Telemetry t, HardwareMap hardwareMap) {

        elev = hardwareMap.get(DcMotor.class, "Elevador");

        elev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry = t;

    }


    public void Control(boolean up, boolean down, boolean upCtr, boolean dowCtr) {

        if (upCtr && UPCtr && !mxLimt) {
                ajt += 0.25;
        }

        if (dowCtr && DOWNCtr && !mnLimt) {
                ajt -= 0.25;
        }


        if (up && UP) {
                count++;
                count = Math.min(3, count);
                ajt = 0;
                velUp = true;
        }

        if (down && DOWN) {
                count--;
                count = Math.max(0, count);
                ajt = 0;
                velUp = false;
        }


        UPCtr = !upCtr;
        DOWNCtr = !dowCtr;
        UP = !up;
        DOWN = !down;

        pos = (int) (Math.max(nv[count] + ajt, setAjt) * convr);


        pos = Math.min(pos, (int) (nv[3] * convr));
        mxLimt = pos > (nv[3] * convr);

        pos = Math.max(pos, (int) (nv[0] * convr));
        mnLimt = pos < (nv[0] * convr);


        elev.setTargetPosition(pos);
        elev.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elev.setPower(velUp ? vUp : vDwn);

    }

    public int getNv() {
        return count;
    }

    public int getCorrentPos() {
        return elev.getCurrentPosition();
    }

    public void setAjt(boolean activ, double ajt) {
        if (activ) {
            velUp = true;
            setAjt = ajt;
        } else {
            velUp = false;
            setAjt = 0;
        }
    }

    public void setPos(double p, double v) {

        elev.setTargetPosition((int) (p * convr));
        elev.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elev.setPower(v);

    }

    public boolean getBusy() {
        return elev.isBusy();
    }

}


