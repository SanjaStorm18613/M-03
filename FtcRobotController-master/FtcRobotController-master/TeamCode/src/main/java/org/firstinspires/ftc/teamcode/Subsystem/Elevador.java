package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Elevador {

    DcMotor Elv;

    boolean UPCtr = true, UP = true, ODWCtr = true, DOW = true;
    double ajt = 0, niv = 0, nv0 = 0, nv1 = 1, nv2 = 2, nv3 = 3, nv4 = 4;

    public Elevador(HardwareMap hardwareMap) {

        Elv = hardwareMap.get(DcMotor.class, "Elevador");

        Elv.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Elv.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }


    public void nivel(boolean upCtr, boolean dowCtr, boolean up, boolean dow) {

        if (upCtr) { if (UPCtr) UPCtr = false; ajt += 0.25; }
        else UPCtr = true;

        if (dowCtr) { if (ODWCtr) ODWCtr = false; ajt -= 0.25; }
        else ODWCtr = true;


        if (up) {

            if (UP) {
                UP = false;
                if      (niv == nv0) niv = nv1;
                else if (niv == nv1) niv = nv2;
                else if (niv == nv2) niv = nv3;
                else if (niv == nv3) niv = nv4;
            }

        } else UP = true;

        if (dow) {

            if (DOW) {
                DOW = false;
                if      (niv == nv4) niv = nv3;
                else if (niv == nv3) niv = nv2;
                else if (niv == nv2) niv = nv1;
                else if (niv == nv1) niv = nv0;
            }

        } else DOW = true;

        Elv.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Elv.setTargetPosition((int) ((niv + ajt) * 100));
        Elv.setPower(0.5);

    }

    public int ElvPos() { return Elv.getCurrentPosition(); }
}


