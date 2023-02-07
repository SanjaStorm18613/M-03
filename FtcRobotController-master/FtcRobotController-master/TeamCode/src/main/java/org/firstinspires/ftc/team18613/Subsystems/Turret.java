package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Turret extends Subsystem {

    private final DcMotor turret;
    private final Elevator elevator;
    private final OpMode opMode;

    private boolean enable = false, isClockwise = true;

    public Turret(OpMode opMode, Elevator elev, Claw garra) {

        this.opMode = opMode;
        turret = opMode.hardwareMap.get(DcMotor.class, "Yaw");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.elevator = elev;

        ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public boolean getBusy() { return turret.isBusy(); }

    public void setPos(double pos, double vel) {
        turret.setTargetPosition((int) (pos * Constants.Turret.CONVR));
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(vel);
    }

    public boolean getForward() {
        return !(Math.abs(turret.getCurrentPosition()) > Constants.Turret.COUNTS_PER_REVOLUTION/ 4.
                && Math.abs(turret.getCurrentPosition()) < Constants.Turret.COUNTS_PER_REVOLUTION/ 1.5);
    }

    private boolean notLimit (boolean isClockwise) {

        return !(turret.getCurrentPosition() > Constants.Turret.LIMIT
                || turret.getCurrentPosition() < -Constants.Turret.LIMIT
                || (isClockwise && getRelativePos() > Constants.Turret.CHASSIS_OPENING + .015)
                || (!isClockwise && getRelativePos() < -(Constants.Turret.CHASSIS_OPENING + .015)))
                || !elevator.targetPosLowStage();

    }

    private double getRelativePos () {
        double var = turret.getCurrentPosition() / (Constants.Turret.COUNTS_PER_REVOLUTION/ 2.);
        var -= Math.round(var);
        return var;
    }

    public void periodic() {
        double limitProxPercent;
        if (elevator.targetPosLowStage()) {
            limitProxPercent = (0.15 - Math.abs(getRelativePos())) * 18;
        } else {
            limitProxPercent = 1;
        }

        if (Math.abs(getRelativePos()) > Constants.Turret.CHASSIS_OPENING - .015 && elevator.targetPosLowStage()) {
            turret.setPower(runAllowedPoint());

        } else if (notLimit(isClockwise)) {
            if (enable) {
                turret.setPower(Constants.Turret.SPEED * (isClockwise ? 1 : -1) * limitProxPercent);
            } else {
                turret.setPower(0);
            }
        }

        opMode.telemetry.addData("getRelativePos", Math.abs(getRelativePos()));
        opMode.telemetry.addData("elevator.targetPosLowStage", elevator.targetPosLowStage());

    }

    public void enable(boolean isClockwise) {
        this.isClockwise = isClockwise;
        enable = true;

    }

    public void stop() {
        enable = false;
        turret.setPower(0);

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
            elevator.addControl(Constants.Elevador.NV_1 * delta * 0.0028);

        } else {
            elevator.addControl(0);
        }

        return delta * 0.005;

    }

}
