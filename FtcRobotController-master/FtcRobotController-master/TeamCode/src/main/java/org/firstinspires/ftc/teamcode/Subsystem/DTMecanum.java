package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DTMecanum {

    Telemetry telemetry;

    double s = Constantis.DTMecanum.SPEED,
            sY = Constantis.DTMecanum.YAW_SPEED,
            acclT = Constantis.DTMecanum.ACCELERATION,
            precs = Constantis.DTMecanum.PRECISION,
            tolDist = Constantis.DTMecanum.TOLERANCE_DISTANCE,
            tolAngle = Constantis.DTMecanum.TOLERANCE_ANGLE,
            covt = Constantis.DTMecanum.COVERTION;


    Servo odmE, odmD;
    DcMotorEx FE, FD, TE, TD, encE, encD;

    boolean odmActv = false, moveIsBusy = false;
    double yaw, pos, accl = 0, setPoint = 0, direction = 0;
    ElapsedTime acelTime;

    Gyro gyro;

    public DTMecanum(Telemetry t, HardwareMap hardwareMap) {

        telemetry = t;
        gyro = new Gyro(hardwareMap);

        odmE = hardwareMap.get(Servo.class, "odmE");
        odmD = hardwareMap.get(Servo.class, "odmD");

        odmE.setDirection(Servo.Direction.FORWARD);
        odmD.setDirection(Servo.Direction.FORWARD);

//Cria motores
        FE = hardwareMap.get(DcMotorEx.class, "FE");
        FD = hardwareMap.get(DcMotorEx.class, "FD");
        TE = hardwareMap.get(DcMotorEx.class, "TE");
        TD = hardwareMap.get(DcMotorEx.class, "TD");

        DcMotor[] motors = {FE, TE, FD, TD};


        for (int m = 0; m < 4; m++) {

            motors[m].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[m].setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            motors[m].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motors[m].setDirection(m > 1 ? DcMotor.Direction.FORWARD :
                    DcMotor.Direction.REVERSE);
        }

        encE = hardwareMap.get(DcMotorEx.class, "encE");
        encD = hardwareMap.get(DcMotorEx.class, "encE");

        //resetEnc();

        acelTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        acelTime.startTime();

    }

    //Controle movimentação mecanum
    public void Control(double x, double y, double yaw) {

        if (!odmActv) {
            odmE.setPosition(1);
            odmD.setPosition(1);
            odmActv = true;
        }

        yaw *= s * sY;
        x *= s;
        y *= s;

        yaw = Math.round(yaw / precs) * precs;
        x = Math.round(x / precs) * precs;
        y = Math.round(y / precs) * precs;


        if (Math.abs(x) < 0.06 && Math.abs(y) < 0.06 && Math.abs(yaw) < 0.1) {
            accl = 0;
            acelTime.reset();
        } else {
            accl = acelTime.time();
        }

        //telemetry.addData("moveT", accl);

        accl = Math.min(1, accl / acclT);
        accl = Math.round(accl * 1000.0) / 1000.0;

//*/
        FE.setPower((y + x + yaw) * accl);
        FD.setPower((y - x - yaw) * accl);
        TE.setPower((y - x + yaw) * accl);
        TD.setPower((y + x - yaw) * accl);


    }

    public void move(boolean sideMove, double maxVel, double acelT, double propc, double dist) {

        double erro, yawErro, acT;

        propc *= covt;
        dist *= covt;

        if (dist != setPoint) {
            setPoint = dist;
            acelTime.reset();
            resetEnc();
            moveIsBusy = true;
        }

        if (sideMove) {
            pos = (TD.getCurrentPosition() - FD.getCurrentPosition());
        } else {
            pos = (FE.getCurrentPosition() + FD.getCurrentPosition());
        }
        pos /= 2.0;

        erro = setPoint - pos;
        yawErro = encE.getCurrentPosition() - encD.getCurrentPosition();//enc


        if ((Math.abs(erro) > tolDist) || yawErro > tolAngle) {

            moveIsBusy = true;

            acT = Math.min(1, acelTime.time() / acelT);

            pos = erro / propc;
            pos = Math.signum(pos) * Math.min(maxVel, Math.abs(pos));
            pos *= acT;

            yaw = yawErro / propc;
            yaw = Math.min(maxVel, yaw);
            yaw *= acT;

            setPower(pos, yaw, sideMove);

        } else {
            moveIsBusy = false;
            setPower(0);
        }
    }


    public void move(boolean sideMove, double maxVel, double acelT, double propc, double dist, double ang) {

        double erro, yawErro, acT;

        propc *= covt;
        dist *= covt;

        if (dist != setPoint) {
            setPoint = dist;
            acelTime.reset();
            resetEnc();
            moveIsBusy = true;
        }

        if (direction != direction + ang) {
            direction += ang;
            acelTime.reset();
        }

        if (sideMove) {
            pos = (TD.getCurrentPosition() - FD.getCurrentPosition());
        } else {
            pos = (FE.getCurrentPosition() + FD.getCurrentPosition());
        }
        pos /= 2.0;

        erro = setPoint - pos;
        yawErro = direction - gyro.getContinuousAngle();


        if ((Math.abs(erro) > tolDist) || yawErro > tolAngle) {

            moveIsBusy = true;

            acT = Math.min(1, acelTime.time() / acelT);

            pos = erro / propc;
            pos = Math.signum(pos) * Math.min(maxVel, Math.abs(pos));
            pos *= acT;

            yaw = yawErro / propc;
            yaw = Math.min(maxVel, yaw);
            yaw *= acT;

            setPower(pos, yaw, sideMove);

        } else {
            moveIsBusy = false;
            setPower(0);
        }
    }

    public void resetEnc() {
        /*encE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encE.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        encD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encD.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
         */
        FD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FD.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FE.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        TD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TD.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        TE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TE.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void setPower(double p) {
        FE.setPower(p);
        FD.setPower(p);
        TE.setPower(p);
        TD.setPower(p);
    }

    public void setPower(double p, double g, boolean side) {

        if (side) {
            FE.setPower(p + g);
            FD.setPower(-p - g);
            TE.setPower(-p + g);
            TD.setPower(p - g);
        } else {
            FE.setPower(p + g);
            FD.setPower(p - g);
            TE.setPower(p + g);
            TD.setPower(p - g);
        }

    }

    public boolean getBusy() {
        return moveIsBusy;
    }
/*
    public void getTelemetry() {
        telemetry.addData("FE", FE.getPower());
        telemetry.addData("FD", FD.getPower());
        telemetry.addData("TE", TE.getPower());
        telemetry.addData("TD", TD.getPower());
    }
 */

}
