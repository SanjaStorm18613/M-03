package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Elevador {

    DcMotor Elev;

    boolean UPCtr = true, UP = true, ODWCtr = true, DOW = true;
    double ajt = 0, vel = 0;
    double[] nv = {0, 0, 2, 3, 4};
    int count = 0, pos = 0, setAjt = 0;
    int convert = 1480;
    boolean mxLimt = false, mnLimt = false;

    public Elevador(HardwareMap hardwareMap) {

        Elev = hardwareMap.get(DcMotor.class, "Elevador");

        Elev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Elev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

    public int ElvPos() {
        return count;
    }

    public void setAjt(boolean activ) {

        setAjt = activ ? 2 : 0;

    }

    public boolean getBusy() {
        return Elev.isBusy();
    }

}


