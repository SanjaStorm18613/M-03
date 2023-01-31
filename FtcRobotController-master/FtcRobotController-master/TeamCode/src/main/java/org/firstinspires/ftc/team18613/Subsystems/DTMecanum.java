package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class DTMecanum  extends Subsystem {

    private final Servo sOdmE, sOdmD;
    private final DcMotorEx FL, FR, BL, BR, eLeft, eRight;
    private final Gyro gyro;
    private final ElapsedTime accTime;
    private final Turret turret;

    private boolean sOdmActv = false, moveIsBusy = false;
    private double pos, setPoint = 0, direction = 0, acc = 0, x = 0, y = 0, turn = 0, slowFactor = 0;

    public DTMecanum(Turret turret) {

        gyro = new Gyro();
        this.turret = turret;

        sOdmE = TeleOpM03.hm.get(Servo.class, "odmE");
        sOdmD = TeleOpM03.hm.get(Servo.class, "odmD");

        eLeft = TeleOpM03.hm.get(DcMotorEx.class, "encE");
        eRight = TeleOpM03.hm.get(DcMotorEx.class, "encD");

        eLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        eRight.setDirection(DcMotorSimple.Direction.REVERSE);

        //Cria motores
        FL = TeleOpM03.hm.get(DcMotorEx.class, "FE");
        FR = TeleOpM03.hm.get(DcMotorEx.class, "FD");
        BL = TeleOpM03.hm.get(DcMotorEx.class, "TE");
        BR = TeleOpM03.hm.get(DcMotorEx.class, "TD");

        DcMotor[] motors = {FL, BL, FR, BR};


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

        accTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        accTime.startTime();


    }

    public void move(boolean sideMove, double maxVel, double acelT, double propc, double dist) {
        double erro, yawErro, acT, turn;

        propc *= Constants.DTMecanum.CONVERTION;
        dist *= Constants.DTMecanum.CONVERTION;

        if (dist != setPoint) {
            setPoint = dist;
            this.accTime.reset();
            resetEnc();
            moveIsBusy = true;
        }

        if (sideMove) {
            pos = (BR.getCurrentPosition() - FR.getCurrentPosition());
        } else {
            pos = (FL.getCurrentPosition() + FR.getCurrentPosition());
        }
        pos /= 2.0;

        erro = setPoint - pos;
        yawErro = eLeft.getCurrentPosition() - eRight.getCurrentPosition();//enc


        if ((Math.abs(erro) > Constants.DTMecanum.TOLERANCE_DISTANCE) || yawErro > Constants.DTMecanum.TOLERANCE_ANGLE) {

            moveIsBusy = true;

            acT = Math.min(1, this.accTime.time() / acelT);

            pos = erro / propc;
            pos = Math.signum(pos) * Math.min(maxVel, Math.abs(pos));
            pos *= acT;

            turn = yawErro / propc;
            turn = Math.min(maxVel, turn);
            turn *= acT;

            tankDrive(pos, turn, sideMove);

        } else {
            moveIsBusy = false;
            tankDrive(0);
        }
    }


    public void move(boolean sideMove, double maxVel, double acelT, double propc, double dist, double ang) {

        double erro, yawErro, acT;

        propc *= Constants.DTMecanum.CONVERTION;
        dist *= Constants.DTMecanum.CONVERTION;

        if (dist != setPoint) {
            setPoint = dist;
            this.accTime.reset();
            resetEnc();
            moveIsBusy = true;
        }

        if (direction != direction + ang) {
            direction += ang;
            this.accTime.reset();
        }

        if (sideMove) {
            pos = (BR.getCurrentPosition() - FR.getCurrentPosition());
        } else {
            pos = (FL.getCurrentPosition() + FR.getCurrentPosition());
        }
        pos /= 2.0;

        erro = setPoint - pos;
        yawErro = direction - gyro.getContinuousAngle();


        if ((Math.abs(erro) > Constants.DTMecanum.TOLERANCE_DISTANCE) || yawErro > Constants.DTMecanum.TOLERANCE_ANGLE) {

            moveIsBusy = true;

            acT = Math.min(1, this.accTime.time() / acelT);

            pos = erro / propc;
            pos = Math.signum(pos) * Math.min(maxVel, Math.abs(pos));
            pos *= acT;

            turn = yawErro / propc;
            turn = Math.min(maxVel, turn);
            turn *= acT;

            tankDrive(pos, turn, sideMove);

        } else {
            moveIsBusy = false;
            tankDrive(0);
        }
    }

    public void resetEnc() {
        /*encE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encE.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        encD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encD.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
         */
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void tankDrive(double p) {
        FL.setPower(p);
        FR.setPower(p);
        BL.setPower(p);
        BR.setPower(p);
    }

    private void tankDrive(double p, double g, boolean side) {

        if (side) {
            FL.setPower(p + g);
            FR.setPower(-p - g);
            BL.setPower(-p + g);
            BR.setPower(p - g);
        } else {
            FL.setPower(p + g);
            FR.setPower(p - g);
            BL.setPower(p + g);
            BR.setPower(p - g);
        }

    }

    public boolean getBusy() {
        return moveIsBusy;
    }

    public void getTelemetry() {
        TeleOpM03.tel.addData("FE", FL.getPower());
        TeleOpM03.tel.addData("FD", FR.getPower());
        TeleOpM03.tel.addData("TE", BL.getPower());
        TeleOpM03.tel.addData("TD", BR.getPower());
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setMove(double x, double y) {
        this.x = roundPrecision(x);
        this.y = roundPrecision(-y);
    }

    public void setTurn(double turn) {
        this.turn = turn;
    }

    public void setSlowFactor(double slowFactor) {
        this.slowFactor = 1 - slowFactor / 1.5;
    }

    public void periodic() {
        retractOdometry();
        updateAcceleration(Math.abs(x) < 0.1 && Math.abs(y) < 0.1 && Math.abs(turn) < 0.1);

        double vel = slowFactor * acc * Constants.DTMecanum.SPEED;
        FL.setPower((y + x + turn) * vel);
        FR.setPower((y - x - turn) * vel);
        BL.setPower((y - x + turn) * vel);
        BR.setPower((y + x - turn) * vel);
    }

    private void retractOdometry() {
        sOdmE.setPosition(1);
        sOdmD.setPosition(1);

    }

    private void updateAcceleration(boolean release) {

        if (release) {
            acc = 0;
            accTime.reset();
            return;
        }

        acc = Math.min(1, accTime.time() / Constants.DTMecanum.ACCELERATION);
        acc = Math.round(acc * 1000.0) / 1000.0;
    }

    private double roundPrecision(double val) {
        return Math.round(val / Constants.DTMecanum.PRECISION) * Constants.DTMecanum.PRECISION;
    }


 //*/

}
