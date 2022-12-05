package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Yaw {

    DcMotor yaw;
    Elevador Elev;
    Garra garra;
    Braco braco;
    Telemetry telemetry;

    double ang = 0, ajt = 0, posAnt;
    int pos = 0, antElvPos = 0;

    boolean CW = false, CCW = true, CWCTRL = true, CCWCTRL = true;
    boolean mxLimt = false, mnLimt = false;

    public Yaw(HardwareMap hardwareMap, Elevador e, Telemetry t, Garra g) {

        yaw = hardwareMap.get(DcMotor.class, "Yaw");
        yaw.setDirection(DcMotorSimple.Direction.REVERSE);
        yaw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yaw.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Elev = e;
        telemetry = t;
        garra = g;

    }

    public void Control(boolean cw, boolean ccw, boolean cwCtrl, boolean ccwCtrl) {

        if (cwCtrl) {
            if (CWCTRL && !mxLimt && Elev.pos() > 1) {
                CWCTRL = false;
                ajt += 0.25;
            }
        } else CWCTRL = true;

        if (ccwCtrl) {
            if (CCWCTRL && !mnLimt && Elev.pos() > 1) {
                CCWCTRL = false;
                ajt -= 0.25;
            }
        } else CCWCTRL = true;


        if (cw) {

            if (CW && !mxLimt) {
                CW = false;
                ang++;
                ajt = 0;


            }

        } else CW = true;


        if (ccw) {

            if (CCW && !mnLimt) {
                CCW = false;
                ang--;
                ajt = 0;

            }

        } else CCW = true;


        antElvPos = Elev.pos();

        pos = (int) ((ang + ajt) * 400);

        pos = Math.min(2000, pos);
        mxLimt = pos >= 2000;

        pos = Math.max(-2000, pos);
        mnLimt = pos <= -2000;

        yaw.setTargetPosition(pos);
        yaw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        yaw.setPower(Elev.getBusy() ? 0 : 1);

        garra.setAjt(yaw.isBusy() && (posAnt != pos));
        posAnt = pos;

    }

    public boolean getBusy(){
        return yaw.isBusy();
    }


}
