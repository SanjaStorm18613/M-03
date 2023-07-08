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

    private boolean moveIsBusy = false, sideMove = false, internalEncoder = false;
    private double setPoint = 0, direction = 0, acc = 0, x = 0, y = 0, turn = 0, slowFactor = 0,
            tolerance = Constants.DTMecanum.TOLERANCE_DISTANCE, distance = 0, adjust = 0, angle = 0, dist = 0,
            proportional = 0, maxVel = 0, timeAccel = 0;

    public DTMecanum(OpMode opMode, Turret turret) {

        gyro = new Gyro(opMode);
        this.turret = turret;
        this.opMode = opMode;

        sOdmE = opMode.hardwareMap.get(Servo.class, "odmE");
        sOdmD = opMode.hardwareMap.get(Servo.class, "odmD");

        sOdmE.setDirection(Servo.Direction.FORWARD);
        sOdmD.setDirection(Servo.Direction.FORWARD);

        setDownEncoderServo(false);

        eLeft = opMode.hardwareMap.get(DcMotorEx.class, "encE");
        eRight = opMode.hardwareMap.get(DcMotorEx.class, "encD");

        eLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        eRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        eLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        eRight.setDirection(DcMotorSimple.Direction.REVERSE);

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

        for (DcMotor m : motors) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        resetEnc();

        accTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        accTime.startTime();

    }

    public void periodic() {
        updateAcceleration(Math.abs(x) < 0.1 && Math.abs(y) < 0.1 && Math.abs(turn) < 0.1);

        setDownEncoderServo(true);

        double vel = slowFactor * Constants.DTMecanum.SPEED * acc,
                turretF = turret.getForward() ? 1 : -1;

        FL.setPower(((y + x) * turretF + turn) * vel);
        FR.setPower(((y - x) * turretF - turn) * vel);
        BL.setPower(((y - x) * turretF + turn) * vel);
        BR.setPower(((y + x) * turretF - turn) * vel);

    }

    public void autoPeriodic() {
        double error, yawError, acT, turn, pos;

        setDownEncoderServo(false);

        if (distance != setPoint) {
            setPoint = distance;
            this.accTime.reset();
            resetEnc();
            moveIsBusy = true;
        }

        if ((direction != angle) && dist == 0) {
            direction = angle;
            this.accTime.reset();
        }

        if (sideMove) {
            pos = (BR.getCurrentPosition() - FR.getCurrentPosition()) / 2.0;
        } else if (internalEncoder) {
            pos = (FL.getCurrentPosition() + FR.getCurrentPosition()) / 2.0;
        } else {
            pos = eRight.getCurrentPosition();
        }

        error = setPoint + adjust - pos;
        yawError = direction - gyro.getContinuousAngle();

        acT = Math.min(1, this.accTime.time() / timeAccel);

        pos = error * proportional;
        pos = Math.signum(pos) * Math.min(maxVel, Math.abs(pos));
        pos *= acT;

        turn = yawError * proportional;
        turn = Math.signum(turn) * Math.min(maxVel, Math.abs(turn));
        turn *= acT;


        if (dist == 0.0){
            tankDrive(0, turn, false);
        } else {
            tankDrive(pos, turn, sideMove);
        }

        moveIsBusy = (Math.abs(error) > tolerance) || (Math.abs(yawError) > Constants.DTMecanum.TOLERANCE_ANGLE);

    }

    public void setMove(boolean internalEncoder, boolean sideMove, double maxVel, double timeAccel, double prop, double dist, double ang) {

        angle = ang;
        proportional = prop;
        this.dist = dist;
        this.sideMove = sideMove;
        this.internalEncoder = internalEncoder;
        this.maxVel = maxVel;
        this.timeAccel = timeAccel;

        tolerance = Constants.DTMecanum.TOLERANCE_DISTANCE;
        distance = this.dist * Constants.DTMecanum.CONVERTION_2_EXTERNAL;

        if (internalEncoder) {
            distance = this.dist * Constants.DTMecanum.CONVERTION_2_INTERNAL;
            proportional *= 10.;

        } else if (sideMove) {
            proportional *= 12.;
            distance /= 35.;
            this.maxVel += 0.1;
        }

    }

    public void resetEnc() {
        eLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        eRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

    private double roundPrecision(double val) {
        return Math.round(val / Constants.DTMecanum.PRECISION) * Constants.DTMecanum.PRECISION;
    }

    private void updateAcceleration(boolean release) {

        if (release) {
            acc = 0;
            accTime.reset();
            return;
        }

        acc = Math.min(1, accTime.time() / Constants.DTMecanum.ACCELERATION);
        acc = Math.round(acc * 1300.0) / 1300.0;
    }

    public void setValCalibration(double val){

        adjust = val * Constants.DTMecanum.CONVERTION_2_EXTERNAL;

        if (sideMove) {
            adjust /= 35.;
        } else if (internalEncoder) {
            adjust = val * Constants.DTMecanum.CONVERTION_2_INTERNAL;
        }

    }

    public void setDownEncoderServo(boolean act){
        sOdmE.setPosition(act ? 0 : 1);
        sOdmD.setPosition(act ? 0 : 1);
    }

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

    public boolean getBusy() {
        return moveIsBusy;
    }

}
