package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Elevator extends Subsystem {

    DcMotorEx elev;

    int adjust = 0;

    double[] stages =
            { Constants.Elevador.NV_0
            , Constants.Elevador.NV_1
            , Constants.Elevador.NV_2
            , Constants.Elevador.NV_3 };

    int stage = 0;
    double setAjt = 0;

    public Elevator() {
        elev = TeleOpM03.hm.get(DcMotorEx.class, "Elevador");

        elev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elev.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        elev.setDirection(DcMotorSimple.Direction.REVERSE);
        elev.setTargetPositionTolerance(Constants.Elevador.TOLERANCE);
    }

    public int getStages() {
        return stage;
    }
    public boolean onColletionStage() {
        return stage == 0;
    }

    public double getTargetPosition() {
        return elev.getTargetPosition();
    }

    public int getCurrentPos() {
        return elev.getCurrentPosition();
    }

    public void setPos(double pos, double vel) {
        elev.setTargetPosition((int) (pos * Constants.Elevador.CONVR));
        elev.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elev.setPower(vel);
    }

    public boolean getBusy() {
        return elev.isBusy();
    }

    public void setAjt(boolean activ, double ajt) {
        if (activ) {
            //setAjt = ajt;
        } else {
            setAjt = 0;
        }
    }


    public void setMinPos(double pos) {
        if (pos ==-1){
            setAjt = 0;
        } else {
            setAjt = pos;
        }
    }

    public void raiseStage() {
        adjust = 0;
        stage = Math.min(3, ++stage);
        stageUpdate();

    }

    public void lowerStage() {
        adjust = 0;
        stage = Math.max(0, --stage);
        stageUpdate();

    }

    public void adjustHeight(boolean up) {
        double currentStage = stages[stage] + adjust * Constants.Elevador.AJUSTE;

        if (up && currentStage < Constants.Elevador.NV_3) {
            adjust++;
        }
        if (!up && currentStage > Constants.Elevador.NV_0) {
            adjust--;
        }
        stageUpdate();

    }

    private void stageUpdate() {
        double targetPos = stages[stage] + adjust * Constants.Elevador.AJUSTE;
        targetPos = Math.min(Constants.Elevador.NV_3, targetPos);
        targetPos = Math.max(Constants.Elevador.NV_0, targetPos);
        targetPos = targetPos * Constants.Elevador.CONVR;

        elev.setTargetPosition((int) targetPos);
        elev.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int delta = elev.getTargetPosition() - elev.getCurrentPosition();
        if (delta >= 0) {
            elev.setPower(Constants.Elevador.UP_SPEED);
        } else {
            elev.setPower(Constants.Elevador.DOWN_SPEED);

        }

        TeleOpM03.tel.addData("adjust", adjust);
        TeleOpM03.tel.addData("stage", stage);

    }

}


