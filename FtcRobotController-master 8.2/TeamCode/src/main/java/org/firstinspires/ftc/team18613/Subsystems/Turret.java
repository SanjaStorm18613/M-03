package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.Vision.TrackingJunction;

public class Turret extends Subsystem {

    private final DcMotor turret;
    private final Elevator elevator;
    private final OpMode opMode;
    TrackingJunction detector;

    private boolean enable = false, isClockwise = true, enableTracking = false;

    public Turret(OpMode opMode, Elevator elev, TrackingJunction detector) {

        this.opMode = opMode;
        turret = opMode.hardwareMap.get(DcMotor.class, "Yaw");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setTargetPosition(5);

        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.elevator = elev;

        this.detector = detector;

    }

    public Turret(OpMode opMode, Elevator elev) {

        this.opMode = opMode;
        turret = opMode.hardwareMap.get(DcMotor.class, "Yaw");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.setTargetPosition(5);

        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.elevator = elev;

    }

    public void periodic() {
        double limitProximityPercentage;

        if (elevator.getTargetPosLowStage()) {
            limitProximityPercentage = (0.15 - Math.abs(getRelativePos())) * 18;
        } else {
            limitProximityPercentage = 1;
        }

        if (enableTracking) {
            turret.setPower(320 + Constants.Turret.TRACKING_CENTER_OFFSET
                                - detector.getCenterTopJunction()
                                * Constants.Turret.TRACKING_PROPORTION_CORRECTION);

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
