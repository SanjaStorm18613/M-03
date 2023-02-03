package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Elevator extends Subsystem {

    private DcMotorEx elevator;

    private int adjust = 0;

    private final double[] stages =
            { Constants.Elevador.NV_0
            , Constants.Elevador.NV_1
            , Constants.Elevador.NV_2
            , Constants.Elevador.NV_3 };

    private int stage = 0;
    private double setAjt = 0, priorityPos = 0, controlRequirement = 0;

    private boolean UPCtr = true, UP = true, DOWNCtr = true, DOWN = true, velUp = true;
    private double ajt = 0;
    private boolean mxLimt = false, mnLimt = false;
    private int pos = 0;


    public Elevator() {
        elevator = TeleOpM03.hm.get(DcMotorEx.class, "Elevador");

        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        elevator.setDirection(DcMotorSimple.Direction.REVERSE);
        elevator.setTargetPositionTolerance(Constants.Elevador.TOLERANCE);
    }

    public void Control(boolean up, boolean down, boolean upCtr, boolean dowCtr) {

        if (upCtr && UPCtr && !mxLimt) ajt += Constants.Elevador.AJUSTE;

        if (dowCtr && DOWNCtr && !mnLimt) ajt -= Constants.Elevador.AJUSTE;


        if (up && UP) {
            stage++;
            stage = Math.min(3, stage);
            ajt = 0;
            velUp = true;
        }

        if (down && DOWN) {
            stage--;
            stage = Math.max(0, stage);
            ajt = 0;
            velUp = false;
        }


        UPCtr = !upCtr;
        DOWNCtr = !dowCtr;
        UP = !up;
        DOWN = !down;

        pos = (int) (Math.max(stages[stage] + ajt, setAjt) * Constants.Elevador.CONVR);


        pos = Math.min(pos, (int) (stages[3] * Constants.Elevador.CONVR));
        mxLimt = pos == (stages[3] * Constants.Elevador.CONVR);

        pos = Math.max(pos, (int) (stages[0] * Constants.Elevador.CONVR));
        mnLimt = pos == (stages[0] * Constants.Elevador.CONVR);


        elevator.setTargetPosition(pos);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (elevator.isBusy()) elevator.setPower(velUp ? Constants.Elevador.UP_SPEED : Constants.Elevador.DOWN_SPEED);
        else elevator.setPower(0);

        //telemetry.addData("getTargetPosition", elev.getTargetPosition());

    }

    public int getStages() {
        return stage;
    }
    public boolean onColletionStage() {
        return stage == 0;
    }

    public double getTargetPos() {
        return elevator.getTargetPosition();
    }

    public boolean targetPosLowStage() {
        return elevator.getTargetPosition() < stages[1] * Constants.Elevador.CONVR;
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

    public void setAjt(boolean activ, double ajt) {
        if (activ) {
            setAjt = ajt;
        } else {
            setAjt = 0;
        }
    }
//////aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

    public double addControl(double pos) {
        controlRequirement = Math.max(pos, controlRequirement);
        return controlRequirement;
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

        TeleOpM03.tel.addData("adjust", adjust);
        TeleOpM03.tel.addData("targetPos-final", targetPos);
        TeleOpM03.tel.addData("controlRequirement", controlRequirement);

    }

}


