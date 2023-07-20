package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.Vision.TrackingJunction;

public class Turret extends Subsystem {

    private final DcMotor turret;
    DcMotor RSL;
    private final Elevator elevator;
    private final OpMode opMode;
    TrackingJunction detector;
    double lastError, integral = 0;
    ElapsedTime time, blinkTime;
    boolean init = true, blinkStatus = false;

    private boolean enable = false, isClockwise = true, enableTracking = false;

    public Turret(OpMode opMode, Elevator elev, TrackingJunction detector) {

        this.opMode = opMode;
        RSL = opMode.hardwareMap.get(DcMotor.class, "RSL");
        RSL.setDirection(DcMotorSimple.Direction.FORWARD);

        turret = opMode.hardwareMap.get(DcMotor.class, "Yaw");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setTargetPosition(5);

        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        this.elevator = elev;

        this.detector = detector;
        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.reset();

        blinkTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        blinkTime.reset();

    }

    public Turret(OpMode opMode, Elevator elev) {

        this.opMode = opMode;
        RSL = opMode.hardwareMap.get(DcMotor.class, "RSL");
        RSL.setDirection(DcMotorSimple.Direction.FORWARD);

        turret = opMode.hardwareMap.get(DcMotor.class, "Yaw");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setTargetPosition(5);

        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        this.elevator = elev;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        time.reset();

        blinkTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        blinkTime.reset();

    }

    public void periodic() {
        double limitProximityPercentage;



        if (elevator.getTargetPosLowStage()) {
            limitProximityPercentage = (0.15 - Math.abs(getRelativePos())) * 18;
        } else {
            limitProximityPercentage = 1;
        }

        if (enableTracking && detector.getDetected() && detector.getCenterJunction() != 0) {
            if (init) {
                time.reset();
                init = false;
            }

            turret.setPower(trackingCorrection());

            if (Math.abs(getTrackingError()) > 20) {

                if (blinkTime.time() > 1000) {
                    blinkStatus = !blinkStatus;
                    blinkTime.reset();
                }

                if (blinkStatus) RSL.setPower(Constants.Turret.RSL_POWER * (1-blinkTime.time()/1000.0));
                else RSL.setPower(0);

            } else {
                turret.setPower(0);
                RSL.setPower(Constants.Turret.RSL_POWER * 2);
            }

        } else {

            if (Math.abs(getRelativePos()) > Constants.Turret.CHASSIS_OPENING
                                        - .015 && elevator.getTargetPosLowStage()) {
                turret.setPower(runAllowedPoint());

            } else if (getNotLimit(isClockwise)) {
                if (enable) {
                    turret.setPower(Constants.Turret.SPEED * (isClockwise ? 1 : -1)
                                                            * limitProximityPercentage);
                } else {
                    turret.setPower(0);
                }
            }

            RSL.setPower(0);
            init = true;
        }
    }

    public void enable(boolean isClockwise) {
        this.isClockwise = isClockwise;
        enable = true;

    }

    public void stop() {
        enable = false;
        turret.setPower(0);

    }

    public void enableTracking() {
        enableTracking = true;
    }

    public void disableTracking() {
        enableTracking = false;
    }

    public double trackingCorrection() {
        double error, correction;
        error = getTrackingError();
        correction = Math.signum(error) * Math.pow(Math.abs(error)/320.0, 2);

        if (Math.abs(error) < (lastError/2.) && detector.getCenterJunction() != 0 && detector.getDetected()) {
            lastError = Math.abs(error);
            time.reset();
        }

        integral = Constants.Turret.TRACKING_CORRECTION_I * error * (Math.round(time.time() * 100)/100.);

        if (Math.abs(error) < 80) {
            correction += integral;
        } else {
            time.reset();
        }

        return correction;
    }

    public double getIntegral() {
        return integral;
    }
    public double getTime(){
        return time.time();
    }

    public double getTrackingError(){
        if (detector.getDetected()) return (320 - Constants.Turret.TRACKING_CENTER_OFFSET - detector.getCenterJunction());
        else return 0.0;
    }

    private double runAllowedPoint() {
        double setPoint;
        double currentPos = turret.getCurrentPosition();
        double sig = Math.signum(currentPos) == 0 ? 1 : Math.signum(currentPos);

        if (!getForward()) {
                setPoint = Constants.Turret.COUNTS_PER_REVOLUTION / 2. * sig;
            } else if (Math.abs(currentPos) >= Constants.Turret.COUNTS_PER_REVOLUTION / 1.5) {
                setPoint = Constants.Turret.COUNTS_PER_REVOLUTION * sig;
            } else {
                setPoint = 0;
            }

        double delta = setPoint - currentPos;

        if (Math.abs(getRelativePos()) > Constants.Turret.CHASSIS_OPENING){
            elevator.addControl(Constants.Elevator.NV_1 * delta * 0.0029);

        } else {
            elevator.addControl(0);
        }

        return delta * 0.005;

    }

    public void setPos(double pos, double vel) {
        turret.setTargetPosition((int) (pos * Constants.Turret.CONVERSION));
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(vel);
    }

    private boolean getNotLimit(boolean isClockwise) {

        return !(turret.getCurrentPosition() > Constants.Turret.LIMIT
                || turret.getCurrentPosition() < -Constants.Turret.LIMIT
                || (isClockwise && getRelativePos() > Constants.Turret.CHASSIS_OPENING + .015)
                || (!isClockwise && getRelativePos() < -(Constants.Turret.CHASSIS_OPENING + .015)))
                || !elevator.getTargetPosLowStage();

    }

    public boolean getBusy() { return turret.isBusy(); }

    public boolean getForward() {
        return !(Math.abs(turret.getCurrentPosition()) > Constants.Turret.COUNTS_PER_REVOLUTION / 4.
                && Math.abs(turret.getCurrentPosition()) < Constants.Turret.COUNTS_PER_REVOLUTION
                                                                                            / 1.5);
    }

    private double getRelativePos () {
        double var = turret.getCurrentPosition() / (Constants.Turret.COUNTS_PER_REVOLUTION / 2.);
        var -= Math.round(var);
        return var;
    }

}
