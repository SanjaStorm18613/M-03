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

    double y = 0, x = 0, vel = 0;
    int pos = 0;

    boolean CW = false, CCW = true, CWCTRL = true, CCWCTRL = true;
    boolean mxLimt = false, mnLimt = false, elevColt = true;

    public Yaw(Telemetry t, HardwareMap hardwareMap, Elevador e) {

        yaw = hardwareMap.get(DcMotor.class, "Yaw");
        yaw.setDirection(DcMotorSimple.Direction.REVERSE);
        yaw.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        elev = e;
        telemetry = t;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public void Control(boolean cw, boolean ccw) {

        elevColt = elev.getNv() == 0;

        if (!cw && !ccw || elevColt) {
            time.reset();
            vel = 0;
        } else {
            vel = Math.min(time.time() / 3000, 1);
        }

        if (cw && yaw.getCurrentPosition() > -3000) yaw.setPower(1 * vel);
        else if (ccw && yaw.getCurrentPosition() < 3000) yaw.setPower(1 * -vel);
        else yaw.setPower(0);

        x = Math.abs(-Math.abs((yaw.getCurrentPosition() - 3000/4.) / 3000/4.)+2)-1;
        y = Math.abs((x-3000/2.)/3000/4.)-1;

        if (Math.abs(y) > .25 && Math.abs(y) < .75) elev.setAjt(true, 1.0);
        else elev.setAjt(false, 0);

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

    public double[] getCorrectionCoord () {
        return new double[] {1, -1};

    }

}
