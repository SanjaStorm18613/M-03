package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Yaw {

    int yLimit = Constantis.Yaw.LIMIT,
        convr = Constantis.Yaw.CONVR;

    double  ajuste = Constantis.Yaw.AJUST,
            ctrl = Constantis.Yaw.CONTROL;

    DcMotor yaw;
    Elevador elev;
    Telemetry telemetry;
    ElapsedTime time;

    double ajt = 0, relationPos = 0, t = 0;
    int pos = 0;

    boolean CW = false, CCW = true, CWCTRL = true, CCWCTRL = true;
    boolean mxLimt = false, mnLimt = false, elevColt = true;

    public Yaw(Telemetry t, HardwareMap hardwareMap, Elevador e) {

        yaw = hardwareMap.get(DcMotor.class, "Yaw");
        yaw.setDirection(DcMotorSimple.Direction.REVERSE);
        yaw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yaw.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        yaw.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        elev = e;
        telemetry = t;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public void Control(boolean cw, boolean ccw, boolean cwCtrl, boolean ccwCtrl) {

        elevColt = elev.getNv() == 0;

        if (cwCtrl && CWCTRL && !mxLimt && !elevColt) ajt += ajuste;
        if (ccwCtrl && CCWCTRL && !mnLimt && !elevColt) ajt -= ajuste;

        if (cw && CW && !mxLimt && !elevColt) ajt += ctrl;
        if (ccw && CCW && !mnLimt && !elevColt) ajt -= ctrl;

        if (!cw && !ccw && !cwCtrl && !ccwCtrl) {
            time.reset();
            t = 0;
        } else if (t + 100 < time.time()) {
            t += 100;

            if (cwCtrl) ajt += ajuste;
            else if (ccwCtrl) ajt -= ajuste;
            else if (cw) ajt += ctrl;
            else ajt -= ctrl;
        }

        CWCTRL = !cwCtrl;
        CCWCTRL = !ccwCtrl;
        CW = !cw;
        CCW = !ccw;

        pos = (int) (ajt * convr);

        pos = Math.min(yLimit, pos);
        mxLimt = pos >= yLimit;

        pos = Math.max(-yLimit, pos);
        mnLimt = pos <= -yLimit;

        if (getInverted()) relationPos = elev.getCorrentPos() - ctrl * 4;
        else relationPos = elev.getCorrentPos();
        relationPos = Math.abs(relationPos);

        if (relationPos > 100) elev.setAjt(true, 1.0);
        else elev.setAjt(false, 0);


        if (elevColt) {
            if (getInverted()) yaw.setTargetPosition((int) ctrl * 4);
            else yaw.setTargetPosition(0);

        } else yaw.setTargetPosition(pos);

        yaw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        yaw.setPower(yaw.isBusy() ? 1 : 0);

        telemetry.addData("yaw", yaw.getTargetPosition());
        telemetry.addData("posyaw", pos);

    }

    public boolean getBusy() { return yaw.isBusy(); }

    public void setPos(double p, double v) {

        yaw.setTargetPosition((int) (p * convr));
        yaw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        yaw.setPower(v);

    }

    public boolean getInverted() {
        return Math.abs(yaw.getCurrentPosition()) > ctrl * 2 && Math.abs(yaw.getCurrentPosition()) < ctrl * 6;

    }

}
