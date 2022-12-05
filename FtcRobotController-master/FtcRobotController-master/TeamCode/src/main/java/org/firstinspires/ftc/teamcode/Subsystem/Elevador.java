package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Elevador {

    DcMotor Elev;
    Telemetry telemetry;

    boolean UPCtr = true, UP = true, ODWCtr = true, DOW = true;
    double ajt = 0, vel = 0, setAjt = 0;
    double[] nv = {0, 0, 1.9, 3.3, 4.2};
    int count = 0, pos = 0;
    int convert = 1480;
    boolean mxLimt = false, mnLimt = false;

    public Elevador(HardwareMap hardwareMap, Telemetry t) {

        Elev = hardwareMap.get(DcMotor.class, "Elevador");

        Elev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Elev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry = t;

    }


    public void Control(boolean up, boolean dow, boolean upCtr, boolean dowCtr) {

        if (upCtr) {
            if (UPCtr && !mxLimt) {
                UPCtr = false;
                ajt += 0.25;
            }
        } else UPCtr = true;

        if (dowCtr) {
            if (ODWCtr && !mnLimt) {
                ODWCtr = false;
                ajt -= 0.25;
            }
        } else ODWCtr = true;


        if (up) {
            if (UP) {
                UP = false;
                count++;
                count = Math.min(4, count);
                ajt = 0;
                vel = 0.7;
            }
        } else UP = true;

        if (dow) {
            if (DOW) {
                DOW = false;
                count--;
                count = Math.max(0, count);
                ajt = 0;
                vel = 0.5;
            }
        } else DOW = true;


        pos = (int) (Math.max(nv[count] + ajt, setAjt) * convert);


        pos = Math.min(pos, (int) (nv[4] * convert));
        mxLimt = pos > (nv[4] * convert);

        pos = Math.max(pos, (int) (nv[0] * convert));
        mnLimt = pos < (nv[0] * convert);


        Elev.setTargetPosition(pos);
        Elev.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Elev.setPower(vel);

    }

    public int pos() {
        return count;
    }

    public void setAjt(boolean activ, double ajt) {
        if (activ) {
            vel = 0.7;
            setAjt = ajt;
        } else {
            vel = 0.5;
            setAjt = 0;
        }
    }

    public void setPos(double p, double v) {

        Elev.setTargetPosition((int) (p * convert));
        Elev.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Elev.setPower(v);

    }

    public boolean getBusy() {
        return Elev.isBusy();
    }

}


