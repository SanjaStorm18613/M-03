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
    double lastError = 0, integral = 0, erro = 0, autoPos = 0, autoVel = 0;
    ElapsedTime timeIntegral, blinkTime, timeLimitDetection;
    boolean init = true, blinkStatus = false, trackerIsBusy = false, useSetPos = false;

    private boolean enable = false, isClockwise = true, enableTracking = false;

    public Turret(OpMode opMode, Elevator elev, TrackingJunction detector) {

        this.opMode = opMode;
        RSL = opMode.hardwareMap.get(DcMotor.class, "RSL");
        RSL.setDirection(DcMotorSimple.Direction.FORWARD);

        turret = opMode.hardwareMap.get(DcMotor.class, "Yaw");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        this.elevator = elev;

        this.detector = detector;

        timeIntegral = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timeIntegral.reset();

        blinkTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        blinkTime.reset();

        timeLimitDetection = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timeLimitDetection.reset();

    }

    public Turret(OpMode opMode, Elevator elev) {

        this.opMode = opMode;
        RSL = opMode.hardwareMap.get(DcMotor.class, "RSL");
        RSL.setDirection(DcMotorSimple.Direction.FORWARD);

        turret = opMode.hardwareMap.get(DcMotor.class, "Yaw");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        this.elevator = elev;

        timeIntegral = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timeIntegral.reset();

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
                timeIntegral.reset();
                init = false;
            }

            turret.setPower(trackingCorrection());

            if (Math.abs(getTrackingError()) > Constants.Turret.TRACKING_ERROR_TOLERANCE) {

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
        correction = Math.signum(error) * Math.pow(Math.abs(error)/300.0, 2);

        if (Math.abs(error) < (lastError/2.) && detector.getCenterJunction() != 0 && detector.getDetected()) {
            lastError = Math.abs(error);
            timeIntegral.reset();
        }

        integral = Constants.Turret.TRACKING_CORRECTION_I * error * (timeIntegral.time());

        if (Math.abs(error) < 80) {
            correction += integral;
        } else {
            timeIntegral.reset();
        }

        return correction;
    }

    public double getIntegral() {
        return integral;
    }
    public double getTimeIntegral(){
        return timeIntegral.time();
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
        /*turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setTargetPosition((int) (pos * Constants.Turret.CONVERSION));
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(vel);*/

        autoPos = pos;
        autoVel = vel;
        useSetPos = true;
    }

    public void autoPeriodic() {
        if (useSetPos) {
            erro = autoPos * Constants.Turret.CONVERSION - turret.getCurrentPosition();
            turret.setPower(Math.signum(erro) * Math.min(Math.abs(erro * 0.01), autoVel));
        } else {
            erro = 0;
        }

    }

    public void setInitTracker(double limitTime) {
        //turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        useSetPos = false;
        double trackingError = getTrackingError();

        if (init) timeLimitDetection.reset();

        if (detector.getDetected() && detector.getCenterJunction() != 0
                && Math.abs(trackingError) > Constants.Turret.TRACKING_ERROR_TOLERANCE
                && timeLimitDetection.time() <= limitTime) {

            erro = 0;
            trackerIsBusy = true;

            if (init) {
                timeIntegral.reset();
                init = false;
            }

            turret.setPower(trackingCorrection());

            if (Math.abs(trackingError) > Constants.Turret.TRACKING_ERROR_TOLERANCE) {

                if (blinkTime.time() > 1000) {
                    blinkStatus = !blinkStatus;
                    blinkTime.reset();
                }

                if (blinkStatus)
                    RSL.setPower(Constants.Turret.RSL_POWER * (1 - blinkTime.time() / 1000.0));
                else RSL.setPower(0);

            } else {
                turret.setPower(0);
                if (Math.abs(trackingError) > Constants.Turret.TRACKING_ERROR_TOLERANCE) {
                    RSL.setPower(0);

                } else {
                    RSL.setPower(Constants.Turret.RSL_POWER * 2);

                }
            }

        } else {

            turret.setPower(0);
            RSL.setPower(0);
            init = true;
            trackerIsBusy = false;

        }
    }

    private boolean getNotLimit(boolean isClockwise) {

        return !(turret.getCurrentPosition() > Constants.Turret.LIMIT
                || turret.getCurrentPosition() < -Constants.Turret.LIMIT
                || (isClockwise && getRelativePos() > Constants.Turret.CHASSIS_OPENING + .015)
                || (!isClockwise && getRelativePos() < -(Constants.Turret.CHASSIS_OPENING + .015)))
                || !elevator.getTargetPosLowStage();

    }

    public boolean getBusy() {
        return Math.abs(erro) > 10;

    }

    public boolean getTrackerBusy() {
        return trackerIsBusy;
    }

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
