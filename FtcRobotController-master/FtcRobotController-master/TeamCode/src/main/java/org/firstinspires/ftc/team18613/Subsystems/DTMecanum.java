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

    private boolean moveIsBusy = false, sideMove = false, intenalEncoder = false;
    private double setPoint = 0, direction = 0, acc = 0, x = 0, y = 0, turn = 0, slowFactor = 0,
            valCalibration = 0, tolerance = 0, distance = 0, adjust = 0, angle = 0, dist = 0,
            proportional = 0, maxVel = 0, timeAccelt = 0;

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

        double vel = slowFactor * Constants.DTMecanum.SPEED * acc;
        vel *= turret.getForward() ? 1 : -1;

        FL.setPower((y + x + turn) * vel);
        FR.setPower((y - x - turn) * vel);
        BL.setPower((y - x + turn) * vel);
        BR.setPower((y + x - turn) * vel);

        opMode.telemetry.addData("getForward", turret.getForward());
    }

    public void autoPeriodic() {
        double erro, yawErro, acT, turn, pos;

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
        } else if (intenalEncoder) {
            pos = (FL.getCurrentPosition() + FR.getCurrentPosition()) / 2.0;
        } else {
            pos = eRight.getCurrentPosition();
        }

        erro = setPoint + adjust - pos;
        yawErro = direction - gyro.getContinuousAngle();

        acT = Math.min(1, this.accTime.time() / timeAccelt);

        pos = erro * proportional;
        pos = Math.signum(pos) * Math.min(maxVel, Math.abs(pos));
        pos *= acT;

        turn = yawErro * proportional;
        turn = Math.signum(turn) * Math.min(maxVel, Math.abs(turn));
        turn *= acT;


        if (dist == 0.0){
            tankDrive(0, turn, false);
        } else {
            tankDrive(pos, turn, sideMove);
        }

        moveIsBusy = (Math.abs(erro) > tolerance) || (yawErro > Constants.DTMecanum.TOLERANCE_ANGLE);

    }

    public void setMove(boolean intenalEncoder, boolean sideMove, double maxVel, double timeAccelt, double propc, double dist, double ang) {

        angle = ang;
        proportional = propc;
        this.dist = dist;
        this.sideMove = sideMove;
        this.intenalEncoder = intenalEncoder;
        this.maxVel = maxVel;
        this.timeAccelt = timeAccelt;

        tolerance = Constants.DTMecanum.TOLERANCE_DISTANCE;
        distance = this.dist * Constants.DTMecanum.CONVERTION_2_EXTERNAL;

        if (sideMove) {
            proportional *= 1.6;
            distance /= 35.;
            tolerance /= 35.;

        } else if (intenalEncoder) {
            distance = this.dist * Constants.DTMecanum.CONVERTION_2_INTERNAL;
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
        } else if (intenalEncoder) {
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

    public void getTelemetry() {
        opMode.telemetry.addData("FE", FL.getPower());
        opMode.telemetry.addData("FD", FR.getPower());
        opMode.telemetry.addData("TE", BL.getPower());
        opMode.telemetry.addData("TD", BR.getPower());
    }

}
