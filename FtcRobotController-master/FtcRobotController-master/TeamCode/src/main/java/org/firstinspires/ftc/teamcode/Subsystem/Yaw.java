package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Yaw {

    int yLimit = Constantis.Yaw.LIMIT,
        convr = Constantis.Yaw.CONVR;

    double  ajuste = Constantis.Yaw.AJUST,
            bracoUp = Constantis.Yaw.BRACO_UP,
            ctrl = Constantis.Yaw.CONTROL;

    DcMotor yaw;
    Elevador elev;
    Garra garra;
    Braco braco;
    Telemetry telemetry;

    double ang = 0, ajt = 0, posAnt = 0;
    int pos = 0;

    boolean CW = false, CCW = true, CWCTRL = true, CCWCTRL = true;
    boolean mxLimt = false, mnLimt = false, elevColt = true;

    public Yaw(Telemetry t, HardwareMap hardwareMap, Elevador e, Garra g, Braco b) {

        yaw = hardwareMap.get(DcMotor.class, "Yaw");
        yaw.setDirection(DcMotorSimple.Direction.REVERSE);
        yaw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yaw.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        elev = e;
        braco = b;
        garra = g;
        telemetry = t;

    }

    public void Control(boolean cw, boolean ccw, boolean cwCtrl, boolean ccwCtrl) {

        elevColt = elev.getNv() == 0;

        if (cwCtrl && CWCTRL && !mxLimt && !elevColt) ajt += ajuste;
        if (ccwCtrl && CCWCTRL && !mnLimt && !elevColt) ajt -= ajuste;

        if (cw && CW && !mxLimt && !elevColt) ajt += ctrl;
        if (ccw && CCW && !mnLimt && !elevColt) ajt -= ctrl;

        if (elevColt) {
            ajt = 0;
        }

/*
        posAnt = ang;

        if (cw && CW && !mxLimt) {
            ang++;
            ajt = 0;
        }


        if (ccw && CCW && !mnLimt) {
            ang--;
            ajt = 0;

        }

        CW = !cw;
        CCW = !ccw;
*/

        CWCTRL = !cwCtrl;
        CCWCTRL = !ccwCtrl;
        CW = !cw;
        CCW = !ccw;

        //pos = (int) ((ang + ajt) * convr);
        pos = (int) (ajt * convr);

        pos = Math.min(yLimit, pos);
        mxLimt = pos >= yLimit;

        pos = Math.max(-yLimit, pos);
        mnLimt = pos <= -yLimit;


        if (Math.abs(yaw.getCurrentPosition()) > 100) {
            braco.setAjt(bracoUp);
            elev.setAjt(true, 0.35);
        } else {
            braco.setAjt(0);
            elev.setAjt(false, 0);

        }

        yaw.setTargetPosition(elevColt ? 0 : pos);
        yaw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        yaw.setPower(1);

        telemetry.addData("yaw", yaw.getTargetPosition());

    }

}
