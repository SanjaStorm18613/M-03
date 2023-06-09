package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;

public class Elevator extends Subsystem {

    private final DcMotorEx elevator;
    private final OpMode opMode;

    private final double[] stages =
            { Constants.Elevator.NV_0
            , Constants.Elevator.NV_1
            , Constants.Elevator.NV_2
            , Constants.Elevator.NV_3 };

    private int stage = 0, adjust = 0;
    private double controlRequirement = 0;

    public Elevator(OpMode opMode) {

        this.opMode = opMode;

        elevator = opMode.hardwareMap.get(DcMotorEx.class, "Elevador");

        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        elevator.setDirection(DcMotorSimple.Direction.REVERSE);
        elevator.setTargetPositionTolerance(Constants.Elevator.TOLERANCE);
    }

    public void periodic() {
        double targetPos = stages[stage] + adjust * Constants.Elevator.AJUSTE;

        targetPos = Math.max(targetPos, controlRequirement);

        targetPos = Math.min(Constants.Elevator.NV_3, targetPos);
        targetPos = Math.max(Constants.Elevator.NV_0, targetPos);
        targetPos *= Constants.Elevator.CONVR;

        elevator.setTargetPosition((int) targetPos);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int delta = elevator.getTargetPosition() - elevator.getCurrentPosition();
        if (delta >= 0) {
            elevator.setPower(Constants.Elevator.UP_SPEED);
        } else {
            elevator.setPower(Constants.Elevator.DOWN_SPEED);
        }

        opMode.telemetry.addData("targetPos-final", targetPos);
        opMode.telemetry.addData("getCurrentPos", getCurrentPos() / ((double) Constants.Elevator.CONVR));

    }

    public void addControl(double pos) {
        controlRequirement = Math.max(pos, controlRequirement);
    }

    public void removeControl(){
        controlRequirement = 0;
    }

    public void raiseStage() {
        adjust = 0;
        stage = Math.min(3, ++stage);

    }

    public void lowerStage() {
        adjust = 0;
        stage = Math.max(0, --stage);

    }

    public void adjustHeight(boolean up) {
        double currentStage = stages[stage] + adjust * Constants.Elevator.AJUSTE;

        if (up && currentStage < Constants.Elevator.NV_3) {
            adjust++;
        }
        if (!up && currentStage > Constants.Elevator.NV_0) {
            adjust--;
        }
    }

    public void setPos(double pos, double vel) {
        elevator.setTargetPosition((int) (pos * Constants.Elevator.CONVR));
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(vel);
    }

    public boolean getOnColletionStage() {
        return stage == 0;
    }

    public double getTargetPos() {
        return elevator.getTargetPosition();
    }

    public boolean getTargetPosLowStage() {
        return elevator.getTargetPosition() < stages[1] * Constants.Elevator.CONVR * (getOnColletionStage() ? 1.17 :1);

    }
    public int getCurrentPos() {
        return elevator.getCurrentPosition();
    }

    public boolean getBusy() {
        return elevator.isBusy();
    }

}


