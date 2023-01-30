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
    private final DcMotorEx FE, FD, TE, TD, encE, encD;
    private final Gyro gyro;
    private final ElapsedTime accTime;
    private final Turret yaw;

    private boolean sOdmActv = false, moveIsBusy = false;
    private double pos, setPoint = 0, direction = 0, acc = 0, x = 0, y = 0, turn = 0, slow = 0;

    public DTMecanum(Turret yaw) {

        gyro = new Gyro();
        this.yaw = yaw;

        sOdmE = TeleOpM03.hm.get(Servo.class, "odmE");
        sOdmD = TeleOpM03.hm.get(Servo.class, "odmD");

        encE = TeleOpM03.hm.get(DcMotorEx.class, "encE");
        encD = TeleOpM03.hm.get(DcMotorEx.class, "encD");

        encE.setDirection(DcMotorSimple.Direction.FORWARD);
        encD.setDirection(DcMotorSimple.Direction.REVERSE);

        //Cria motores
        FE = TeleOpM03.hm.get(DcMotorEx.class, "FE");
        FD = TeleOpM03.hm.get(DcMotorEx.class, "FD");
        TE = TeleOpM03.hm.get(DcMotorEx.class, "TE");
        TD = TeleOpM03.hm.get(DcMotorEx.class, "TD");

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
            pos = (TD.getCurrentPosition() - FD.getCurrentPosition());
        } else {
            pos = (FE.getCurrentPosition() + FD.getCurrentPosition());
        }
        pos /= 2.0;

        erro = setPoint - pos;
        yawErro = encE.getCurrentPosition() - encD.getCurrentPosition();//enc


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
            pos = (TD.getCurrentPosition() - FD.getCurrentPosition());
        } else {
            pos = (FE.getCurrentPosition() + FD.getCurrentPosition());
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
        FD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FD.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FE.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        TD.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TD.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        TE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TE.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void tankDrive(double p) {
        FE.setPower(p);
        FD.setPower(p);
        TE.setPower(p);
        TD.setPower(p);
    }

    private void tankDrive(double p, double g, boolean side) {

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

    public void getTelemetry() {
        TeleOpM03.tel.addData("FE", FE.getPower());
        TeleOpM03.tel.addData("FD", FD.getPower());
        TeleOpM03.tel.addData("TE", TE.getPower());
        TeleOpM03.tel.addData("TD", TD.getPower());
    }






    public void setMove(double x, double y) {
        this.x = roundPrecision(x);
        this.y = roundPrecision(-y);

        periodic();

    }

    public void setTurn(double turn) {
        this.turn = turn;
        periodic();

    }

    public void setSlow(double slow) {
        this.slow = 1 - slow / 1.5;
        periodic();
    }

    private void periodic() {
        retractOdometry();
        updateAcceleration(Math.abs(x) < 0.1 && Math.abs(y) < 0.1 && Math.abs(turn) < 0.1);

        double vel = slow * acc * Constants.DTMecanum.SPEED;
        FE.setPower((y + x + turn) * vel);
        FD.setPower((y - x - turn) * vel);
        TE.setPower((y - x + turn) * vel);
        TD.setPower((y + x - turn) * vel);
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
