package org.firstinspires.ftc.teamcode.Subsystem;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Yaw {

    DcMotor yaw;
    double ang = 0, ajt = 0;
    int pos = 0;

    boolean CW = false, CCW = true, CWCTRL = true, CCWCTRL = true;
    boolean mxLimt = false, mnLimt = false;

    public Yaw(HardwareMap hardwareMap) {

        yaw = hardwareMap.get(DcMotor.class, "Yaw");
        yaw.setDirection(DcMotorSimple.Direction.REVERSE);
        yaw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yaw.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void Control(boolean cw, boolean ccw, boolean cwCtrl, boolean ccwCtrl) {

        if (cwCtrl) {
            if (CWCTRL && !mxLimt) {
                CWCTRL = false;
                ajt += 0.25;
            }
        } else CWCTRL = true;

        if (ccwCtrl) {
            if (CCWCTRL && !mnLimt) {
                CCWCTRL = false;
                ajt -= 0.25;
            }
        } else CCWCTRL = true;


        if (cw) {

            if (CW && !mxLimt) {
                if (CCW) {
                    CW = false;
                    ang++;
                    ajt = 0;
                }
            }

        } else CW = true;


        if (ccw) {

            if (CCW && !mnLimt) {
                CCW = false;
                ang--;
                ajt = 0;
            }

        } else CCW = true;


        pos = (int) ((ang + ajt) * 400);

        if (pos > 2000) {
            pos = 2000;
            mxLimt = true;
        } else if (pos < -2000) {
            pos = -2000;
            mnLimt = true;
        } else {
            mxLimt = false;
            mnLimt = false;
        }

        Log.d("lim1",""+mxLimt);
        Log.d("lim2",""+mnLimt);

        yaw.setTargetPosition(pos);
        yaw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        yaw.setPower(1);

    }

}
