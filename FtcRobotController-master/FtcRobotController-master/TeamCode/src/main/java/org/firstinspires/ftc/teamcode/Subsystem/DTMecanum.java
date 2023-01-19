package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DTMecanum {

    Telemetry telemetry;

    double speed = Constantis.DTMecanum.SPEED,
            precs = Constantis.DTMecanum.PRECISION,
            tolDist = Constantis.DTMecanum.TOLERANCE_DISTANCE,
            tolAngle = Constantis.DTMecanum.TOLERANCE_ANGLE,
            covert = Constantis.DTMecanum.COVERTION;


    Servo sOdmE, sOdmD;
    DcMotorEx FE, FD, TE, TD, encE, encD;
    Gyro gyro;
    ElapsedTime acelT;

    boolean sOdmActv = false, moveIsBusy = false;
    double turn, pos, accl = 0, setPoint = 0, direction = 0;


    public DTMecanum(Telemetry t, HardwareMap hardwareMap) {

        telemetry = t;
        gyro = new Gyro(hardwareMap);

        sOdmE = hardwareMap.get(Servo.class, "odmE");
        sOdmD = hardwareMap.get(Servo.class, "odmD");

        encE = hardwareMap.get(DcMotorEx.class, "encE");
        encD = hardwareMap.get(DcMotorEx.class, "encE");

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

        sOdmE.setDirection(Servo.Direction.FORWARD);
        sOdmD.setDirection(Servo.Direction.FORWARD);

        //resetEnc();

        acelT = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        acelT.startTime();

    }

    //Controle movimentação mecanum
    public void Control(double x, double y, double t) {

        if (!sOdmActv) {
            sOdmE.setPosition(1);
            sOdmD.setPosition(1);
            sOdmActv = true;
        }

        t *= speed * Constantis.DTMecanum.YAW_SPEED;
        x *= speed;
        y *= speed;

        t = Math.round(t / precs) * precs;
        x = Math.round(x / precs) * precs;
        y = Math.round(y / precs) * precs;


        if (Math.abs(x) < 0.06 && Math.abs(y) < 0.06 && Math.abs(t) < 0.1) {
            accl = 0;
            acelT.reset();
        } else {
            accl = acelT.time();
        }

        //telemetry.addData("moveT", accl);

        accl = Math.min(1, accl / Constantis.DTMecanum.ACCELERATION);
        accl = Math.round(accl * 1000.0) / 1000.0;

//*/
        FE.setPower((y + x + t) * accl);
        FD.setPower((y - x - t) * accl);
        TE.setPower((y - x + t) * accl);
        TD.setPower((y + x - t) * accl);


    }

    public void move(boolean sideMove, double maxVel, double acelT, double propc, double dist) {

        double erro, yawErro, acT;

        propc *= covert;
        dist *= covert;

        if (dist != setPoint) {
            setPoint = dist;
            this.acelT.reset();
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

            acT = Math.min(1, this.acelT.time() / acelT);

            pos = erro / propc;
            pos = Math.signum(pos) * Math.min(maxVel, Math.abs(pos));
            pos *= acT;

            turn = yawErro / propc;
            turn = Math.min(maxVel, turn);
            turn *= acT;

            setPower(pos, turn, sideMove);

        } else {
            moveIsBusy = false;
            setPower(0);
        }
    }


    public void move(boolean sideMove, double maxVel, double acelT, double propc, double dist, double ang) {

        double erro, yawErro, acT;

        propc *= covert;
        dist *= covert;

        if (dist != setPoint) {
            setPoint = dist;
            this.acelT.reset();
            resetEnc();
            moveIsBusy = true;
        }

        if (direction != direction + ang) {
            direction += ang;
            this.acelT.reset();
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

            acT = Math.min(1, this.acelT.time() / acelT);

            pos = erro / propc;
            pos = Math.signum(pos) * Math.min(maxVel, Math.abs(pos));
            pos *= acT;

            turn = yawErro / propc;
            turn = Math.min(maxVel, turn);
            turn *= acT;

            setPower(pos, turn, sideMove);

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
