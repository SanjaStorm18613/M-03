package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.team18613.AutoM03;
import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Elevator extends Subsystem {

    private final DcMotorEx elevator;
    private final OpMode opMode;

    private final double[] stages =
            { Constants.Elevador.NV_0
            , Constants.Elevador.NV_1
            , Constants.Elevador.NV_2
            , Constants.Elevador.NV_3 };

    private int stage = 0, adjust = 0;
    private double controlRequirement = 0;

    public Elevator(OpMode opMode) {

        this.opMode = opMode;

        elevator = opMode.hardwareMap.get(DcMotorEx.class, "Elevador");

        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        elevator.setDirection(DcMotorSimple.Direction.REVERSE);
        elevator.setTargetPositionTolerance(Constants.Elevador.TOLERANCE);
    }

    public boolean onColletionStage() {
        return stage == 0;
    }

    public double getTargetPos() {
        return elevator.getTargetPosition();
    }

    public boolean targetPosLowStage() {
        return elevator.getTargetPosition() < stages[1] * Constants.Elevador.CONVR * (onColletionStage() ? 1.17 :1);

    }
    public int getCurrentPos() {
        return elevator.getCurrentPosition();
    }

    public void setPos(double pos, double vel) {
        elevator.setTargetPosition((int) (pos * Constants.Elevador.CONVR));
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(vel);
    }

    public boolean getBusy() {
        return elevator.isBusy();
    }

//////aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

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
        double currentStage = stages[stage] + adjust * Constants.Elevador.AJUSTE;

        if (up && currentStage < Constants.Elevador.NV_3) {
            adjust++;
        }
        if (!up && currentStage > Constants.Elevador.NV_0) {
            adjust--;
        }
    }

    public void periodic() {
        double targetPos = stages[stage] + adjust * Constants.Elevador.AJUSTE;

        targetPos = Math.max(targetPos, controlRequirement);

        targetPos = Math.min(Constants.Elevador.NV_3, targetPos);
        targetPos = Math.max(Constants.Elevador.NV_0, targetPos);
        targetPos *= Constants.Elevador.CONVR;

        elevator.setTargetPosition((int) targetPos);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int delta = elevator.getTargetPosition() - elevator.getCurrentPosition();
        if (delta >= 0) {
            elevator.setPower(Constants.Elevador.UP_SPEED);
        } else {
            elevator.setPower(Constants.Elevador.DOWN_SPEED);
        }

        opMode.telemetry.addData("adjust", adjust);
        opMode.telemetry.addData("targetPos-final", targetPos);
        opMode.telemetry.addData("controlRequirement", controlRequirement);

    }

}


