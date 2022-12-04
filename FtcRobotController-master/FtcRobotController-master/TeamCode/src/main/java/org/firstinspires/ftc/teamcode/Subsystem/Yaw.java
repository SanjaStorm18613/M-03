package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Yaw {

    DcMotor yaw;
    Elevador Elev;
    double ang = 0, ajt = 0;
    int pos = 0;

    boolean CW = false, CCW = true, CWCTRL = true, CCWCTRL = true;
    boolean mxLimt = false, mnLimt = false;

    public Yaw(HardwareMap hardwareMap, Elevador e) {

        yaw = hardwareMap.get(DcMotor.class, "Yaw");
        yaw.setDirection(DcMotorSimple.Direction.REVERSE);
        yaw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yaw.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Elev = e;

    }

    public void Control(boolean cw, boolean ccw, boolean cwCtrl, boolean ccwCtrl) {

            if (cwCtrl) {
                if (CWCTRL && !mxLimt && Elev.ElvPos() > 1) {
                    CWCTRL = false;
                    ajt += 0.25;
                }
            } else CWCTRL = true;

            if (ccwCtrl) {
                if (CCWCTRL && !mnLimt && Elev.ElvPos() > 1) {
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

                        Elev.setAjt(Elev.ElvPos() < 2);

                    }
                }

            } else CW = true;


            if (ccw) {

                if (CCW && !mnLimt) {
                    CCW = false;
                    ang--;
                    ajt = 0;

                    Elev.setAjt(Elev.ElvPos() < 2);
                }

            } else CCW = true;


            pos = (int) ((ang + ajt) * 400);

            pos = Math.min(2000, pos);
            mxLimt = pos >= 2000;

            pos = Math.max(-2000, pos);
            mnLimt = pos <= -2000;


            yaw.setTargetPosition(pos);
            yaw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            yaw.setPower(1);

            if (!yaw.isBusy()) Elev.setAjt(false);

    }

}
