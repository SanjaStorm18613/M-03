package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;

public class DTMecanum  extends Subsystem {

    private final Servo sOdmE, sOdmD;
    private final DcMotorEx FL, FR, BL, BR, eLeft, eRight;
    private final Gyro gyro;
    private final ElapsedTime accTime;
    private final Turret turret;
    private final OpMode opMode;

    private boolean moveIsBusy = false;
    private double pos, setPoint = 0, direction = 0, acc = 0, x = 0, y = 0, turn = 0, slowFactor = 0;

    public DTMecanum(OpMode opMode, Turret turret) {

        gyro = new Gyro(opMode);
        this.turret = turret;
        this.opMode = opMode;

        sOdmE = opMode.hardwareMap.get(Servo.class, "odmE");
        sOdmD = opMode.hardwareMap.get(Servo.class, "odmD");

        sOdmE.setDirection(Servo.Direction.FORWARD);
        sOdmD.setDirection(Servo.Direction.FORWARD);

        eLeft = opMode.hardwareMap.get(DcMotorEx.class, "encE");
        eRight = opMode.hardwareMap.get(DcMotorEx.class, "encD");

        eLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        eRight.setDirection(DcMotorSimple.Direction.FORWARD);

        //Cria motores
        FL = opMode.hardwareMap.get(DcMotorEx.class, "FE");
        FR = opMode.hardwareMap.get(DcMotorEx.class, "FD");
        BL = opMode.hardwareMap.get(DcMotorEx.class, "TE");
        BR = opMode.hardwareMap.get(DcMotorEx.class, "TD");

        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);
        FR.setDirection(DcMotor.Direction.FORWARD);
        BR.setDirection(DcMotor.Direction.FORWARD);

        DcMotor[] motors = {FL, BL, FR, BR};


        for (int m = 0; m < 4; m++) {

            motors[m].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }



        //resetEnc();

        accTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        accTime.startTime();


    }
    public void encoderServo(boolean activated){
        if (activated) {
            sOdmE.setPosition(0);
            sOdmD.setPosition(0);
        } else {
            sOdmE.setPosition(1);
            sOdmD.setPosition(1);
        }
    }

    public void move(boolean sideMove, double maxVel, double acelT, double propc, double dist) {
        double erro, yawErro, acT, turn, tolerance;

        encoderServo(false);

        opMode.telemetry.addData("eLeft", eLeft.getCurrentPosition());
        opMode.telemetry.addData("eRight", eRight.getCurrentPosition());

        if (sideMove) {
            propc *= Constants.DTMecanum.CONVERTION/35.;
            dist *= Constants.DTMecanum.CONVERTION/35.;
            tolerance = Constants.DTMecanum.TOLERANCE_DISTANCE/35.;

        } else {
            propc *= Constants.DTMecanum.CONVERTION;
            dist *= Constants.DTMecanum.CONVERTION;
            tolerance = Constants.DTMecanum.TOLERANCE_DISTANCE;
        }

        if (dist != setPoint) {
            setPoint = dist;
            this.accTime.reset();
            resetEnc();
            moveIsBusy = true;
        }

        yawErro = eLeft.getCurrentPosition() - eRight.getCurrentPosition();//enc

        if (sideMove) {
            pos = (BR.getCurrentPosition() - FR.getCurrentPosition());
            yawErro /= 35.;
        } else {
            pos = (eLeft.getCurrentPosition() + eRight.getCurrentPosition());
        }
        pos /= 2.0;

        erro = setPoint - pos;


        if ((Math.abs(erro) > tolerance) || yawErro > Constants.DTMecanum.TOLERANCE_ENCODER_DIFERENCE) {

            moveIsBusy = true;

            acT = Math.min(1, this.accTime.time() / acelT);

            pos = erro / propc;
            pos = Math.signum(pos) * Math.min(maxVel, Math.abs(pos));
            pos *= acT;

            turn = yawErro / propc;
            turn = Math.min(maxVel, turn);
            turn *= acT;

            tankDrive(pos, -turn, sideMove);

        } else {
            moveIsBusy = false;
            tankDrive(0);
        }
    }


    public void move(boolean sideMove, double maxVel, double acelT, double propc, double dist, double ang) {
        double erro, yawErro, acT, turn, tolerance;

        encoderServo(false);

        if (sideMove) {
            propc *= Constants.DTMecanum.CONVERTION/35.;
            dist *= Constants.DTMecanum.CONVERTION/35.;
            tolerance = Constants.DTMecanum.TOLERANCE_DISTANCE/35.;

        } else {
            propc *= Constants.DTMecanum.CONVERTION;
            dist *= Constants.DTMecanum.CONVERTION;
            tolerance = Constants.DTMecanum.TOLERANCE_DISTANCE;
        }

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
            pos = (eLeft.getCurrentPosition() + eRight.getCurrentPosition());
        }

        pos /= 2.0;

        erro = setPoint - pos;
        yawErro = direction - gyro.getContinuousAngle();


        if ((Math.abs(erro) > tolerance) || yawErro > Constants.DTMecanum.TOLERANCE_ANGLE) {

            moveIsBusy = true;

            acT = Math.min(1, this.accTime.time() / acelT);

            pos = erro / propc;
            pos = Math.signum(pos) * Math.min(maxVel, Math.abs(pos));
            pos *= acT;

            turn = yawErro / propc;
            turn = Math.min(maxVel, turn);
            turn *= acT;

            tankDrive(pos, -turn, sideMove);

        } else {
            moveIsBusy = false;
            tankDrive(0);
        }
        opMode.telemetry.addData("yawErro", yawErro);
    }

    public void resetEnc() {
        eLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        eRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
         //*/
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
        opMode.telemetry.addData("FE", FL.getPower());
        opMode.telemetry.addData("FD", FR.getPower());
        opMode.telemetry.addData("TE", BL.getPower());
        opMode.telemetry.addData("TD", BR.getPower());
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
        updateAcceleration(Math.abs(x) < 0.1 && Math.abs(y) < 0.1 && Math.abs(turn) < 0.1);

        encoderServo(true);

        double vel = slowFactor * Constants.DTMecanum.SPEED * acc;
        vel *= turret.getForward() ? 1 : -1;

        /*FL.setPower(sensitivReduction((y + x - turn)) * vel);
        FR.setPower(sensitivReduction((y - x + turn)) * vel);
        BL.setPower(sensitivReduction((y - x - turn)) * vel);
        BR.setPower(sensitivReduction((y + x + turn)) * vel);*/

        FL.setPower((y + x + turn) * vel);
        FR.setPower((y - x - turn) * vel);
        BL.setPower((y - x + turn) * vel);
        BR.setPower((y + x - turn) * vel);

        opMode.telemetry.addData("getForward", turret.getForward());
    }

    public double sensitivReduction(double a) {
        return  Math.signum(a) * Math.pow(Math.abs(a), 2);

    }

    private void updateAcceleration(boolean release) {

        if (release) {
            acc = 0;
            accTime.reset();
            return;
        }

        acc = Math.min(1, accTime.time() / Constants.DTMecanum.ACCELERATION);
        acc = Math.round(acc * 1500.0) / 1500.0;
    }

    private double roundPrecision(double val) {
        return Math.round(val / Constants.DTMecanum.PRECISION) * Constants.DTMecanum.PRECISION;
    }


}
