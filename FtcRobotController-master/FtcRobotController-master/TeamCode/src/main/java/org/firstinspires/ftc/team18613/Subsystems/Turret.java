package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Turret extends Subsystem {

    private final DcMotor turret;
    private final Elevator elev;
    private final ElapsedTime time;
    private final Claw garra;

    //private double y = 0, x = 0;
    private double var = 0;

    boolean elevColt = true;

    public Turret(Elevator elev, Claw garra) {

        turret = TeleOpM03.hm.get(DcMotor.class, "Yaw");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.elev = elev;
        this.garra = garra;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public boolean getBusy() { return turret.isBusy(); }

    public void setPos(double pos, double vel) {
        turret.setTargetPosition((int) (pos * Constants.Yaw.CONVR));
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(vel);
    }

    public boolean getInverted() {
        return false;
    }

    public void turn (boolean isClockwise) {

        double var = turret.getCurrentPosition() / 800.0;
        var -= Math.round(var);

        TeleOpM03.tel.addData("var", var);
        TeleOpM03.tel.addData("CorrentPos", turret.getCurrentPosition());

        if (turret.getCurrentPosition() >= 1760 || turret.getCurrentPosition() <= -1760) {
            stop();
            return;
        }

        if (elev.onColletionStage()) {
            //Implementar: Se chegar no limite levantar elevador para continuar rotação
            if (isClockwise && var <= .15) {
                turret.setPower(Constants.Yaw.SPEED);

            } else if (!isClockwise && var >= -.15){
                turret.setPower(-Constants.Yaw.SPEED);

            } else {
                stop();
            }
        } else {
            turret.setPower(Constants.Yaw.SPEED * (isClockwise ? 1 : -1));
        }

    }

    public void stop () {
        turret.setPower(0);
    }

}
