package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Yaw {

    int yLimit = Constantis.Yaw.LIMIT,
        convr = Constantis.Yaw.CONVR;

    double ajuste = Constantis.Yaw.AJUST;

    DcMotor yaw;
    Elevador elev;
    Garra garra;
    Braco braco;
    Telemetry telemetry;

    double ang = 0, ajt = 0, posAnt;
    int pos = 0;

    boolean CW = false, CCW = true, CWCTRL = true, CCWCTRL = true;
    boolean mxLimt = false, mnLimt = false;

    public Yaw(Telemetry t, HardwareMap hardwareMap, Elevador e, Garra g) {

        yaw = hardwareMap.get(DcMotor.class, "Yaw");
        yaw.setDirection(DcMotorSimple.Direction.REVERSE);
        yaw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yaw.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        elev = e;
        telemetry = t;
        garra = g;

    }

    public void Control(boolean cw, boolean ccw, boolean cwCtrl, boolean ccwCtrl) {

        if (cwCtrl && CWCTRL && !mxLimt && elev.getNv() > 1) ajt += ajuste;

        if (ccwCtrl && CCWCTRL && !mnLimt && elev.getNv() > 1) ajt -= ajuste;

        posAnt = ang;

        if (cw && CW && !mxLimt) {
            ang++;
            ajt = 0;
        }


        if (ccw && CCW && !mnLimt) {
            ang--;
            ajt = 0;

        }

        CWCTRL = !cwCtrl;
        CCWCTRL = !ccwCtrl;
        CW = !cw;
        CCW = !ccw;


        pos = (int) ((ang + ajt) * convr);

        pos = Math.min(yLimit, pos);
        mxLimt = pos >= yLimit;

        pos = Math.max(-yLimit, pos);
        mnLimt = pos <= -yLimit;

        if (elev.getNv() == 0) {
            yaw.setTargetPosition(0);
            //braco braco braco

        } else {
            yaw.setTargetPosition(pos);

        }

        yaw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        yaw.setPower(elev.getBusy() ? 0 : 1);


        //garra.setAjt(yaw.isBusy() && (posAnt != pos)); setAjt

    }

    public boolean getBusy() {
        return yaw.isBusy();
    }


}
