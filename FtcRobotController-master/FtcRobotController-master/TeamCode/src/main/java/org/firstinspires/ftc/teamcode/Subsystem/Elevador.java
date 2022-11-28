package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Elevador {

    DcMotor Elv;

    boolean UPCtr = true, UP = true, ODWCtr = true, DOW = true;
    double ajt = 0, vel = 0;
    double[] nv = {0, 1, 2, 3, 4};
    int count = 0, pos = 0;
    int convert = 1480;
    boolean mxLimt = false, mnLimt = false;

    public Elevador(HardwareMap hardwareMap) {

        Elv = hardwareMap.get(DcMotor.class, "Elevador");

        Elv.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Elv.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }


    public void Control(boolean upCtr, boolean dowCtr, boolean up, boolean dow) {

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


        pos = (int) ((nv[count] + ajt) * convert);

        if (pos > (nv[4] * convert)) {
            pos = (int) (nv[4] * convert);
            mxLimt = true;
        } else if (pos < (nv[0] * convert)) {
            pos = (int) (nv[0] * convert);
            mnLimt = true;
        } else {
            mxLimt = false;
            mnLimt = false;
        }

        Elv.setTargetPosition(pos);
        Elv.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Elv.setPower(vel);

    }

    public int ElvPos() {
        return count;
    }
}


