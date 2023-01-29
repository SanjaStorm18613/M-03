package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Elevador extends Subsystem {

    DcMotorEx elev;

    boolean UPCtr = true, UP = true, DOWNCtr = true, DOWN = true, velUp = true;
    double ajt = 0, setAjt = 0;

    double[] niveis =
            { Constants.Elevador.NV_0
            , Constants.Elevador.NV_1
            , Constants.Elevador.NV_2
            , Constants.Elevador.NV_3 };

    int nivel_atual = 0, pos = 0;
    boolean maxLimt = false, minLimt = false;

    public Elevador() {
        elev = TeleOpM03.hm.get(DcMotorEx.class, "Elevador");

        elev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elev.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        elev.setDirection(DcMotorSimple.Direction.REVERSE);
        elev.setTargetPositionTolerance(Constants.Elevador.TOLERANCE);
    }


    public void Control(boolean up, boolean down, boolean upCtr, boolean downCtr) {

        if (upCtr && UPCtr && !maxLimt) ajt += Constants.Elevador.AJUSTE;
        if (downCtr && DOWNCtr && !minLimt) ajt -= Constants.Elevador.AJUSTE;


        if (up && UP) {
                nivel_atual = Math.min(3, ++nivel_atual);
                ajt = 0;
                velUp = true;
        }
        if (down && DOWN) {
                nivel_atual = Math.max(0, --nivel_atual);
                ajt = 0;
                velUp = false;
        }


        UPCtr = !upCtr;
        DOWNCtr = !downCtr;
        UP = !up;
        DOWN = !down;

        pos = (int) (Math.max(niveis[nivel_atual] + ajt, setAjt) * Constants.Elevador.CONVR);


        pos = Math.min(pos, (int) (niveis[3] * Constants.Elevador.CONVR));
        maxLimt = pos > (niveis[3] * Constants.Elevador.CONVR);

        pos = Math.max(pos, (int) (niveis[0] * Constants.Elevador.CONVR));
        minLimt = pos < (niveis[0] * Constants.Elevador.CONVR);


        elev.setTargetPosition(pos);
        elev.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (elev.isBusy()) {
            boolean abc = 0 < elev.getTargetPosition() - elev.getCurrentPosition();
            if(abc){
                elev.setPower(Constants.Elevador.UP_SPEED);
            }
            else{
                elev.setPower(Constants.Elevador.DOWN_SPEED);
            }
        }
        else elev.setPower(0);

        //telemetry.addData("getTargetPosition", elev.getTargetPosition());
        TeleOpM03.tel.addData("elev Pos", getCurrentPos());

    }

    public int getNiveis() {
        return nivel_atual;
    }

    public double getTargetPosition() {
        return elev.getTargetPosition();
    }

    public int getCurrentPos() {
        return elev.getCurrentPosition();
    }

    public void setAjt(boolean activ, double ajt) {
        if (activ) {
            velUp = true;
            setAjt = ajt;
        } else {
            velUp = false;
            setAjt = 0;
        }
    }

    public void setPos(double pos, double vel) {
        elev.setTargetPosition((int) (pos * Constants.Elevador.CONVR));
        elev.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elev.setPower(vel);
    }

    public boolean getBusy() {
        return elev.isBusy();
    }

}


