package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Turret extends Subsystem {

    private final DcMotor turret;
    private final Elevator elevator;

    //private double y = 0, x = 0;
    private final double var = 0;

    private boolean enable = false, isClockwise = true;

    public Turret(Elevator elev, Claw garra) {

        turret = TeleOpM03.hm.get(DcMotor.class, "Yaw");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.elevator = elev;

        ElapsedTime time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public boolean getBusy() { return turret.isBusy(); }

    public void setPos(double pos, double vel) {
        turret.setTargetPosition((int) (pos * Constants.Yaw.CONVR));
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(vel);
    }

    public boolean getForward() {
        return !(Math.abs(turret.getCurrentPosition()) > 400 && Math.abs(turret.getCurrentPosition()) < 1200);
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

        if (elevator.onColletionStage()) {
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

    private boolean notLimit (boolean isClockwise) {

        return !(turret.getCurrentPosition() > 1760
                || turret.getCurrentPosition() < -1760
                || (isClockwise && getRelativePos() > .15)
                || (!isClockwise && getRelativePos () < -.15))
                || elevator.getTargetPosition() >= Constants.Elevador.NV_1;

    }

    private double getRelativePos () {
        double var = turret.getCurrentPosition() / 800.0;
        var -= Math.round(var);
        return var;
    }

    public void periodic() {

        if (notLimit(isClockwise)){
            if (enable) {
                turret.setPower(Constants.Yaw.SPEED * (isClockwise ? 1 : -1));
            } else {
                turret.setPower(0);
            }
        } else {
            turret.setPower(runAllowedPoint());
        }

    }

    public void enable(boolean isClockwise) {
        this.isClockwise = isClockwise;
        enable = true;

    }

    public void stop () {
        enable = false;
        turret.setPower(0);

    }

    private double runAllowedPoint () {
        double setPoint;
        double currentPos = turret.getCurrentPosition();
        double sig = Math.signum(currentPos) == 0 ? 1 : Math.signum(currentPos);

        if (!getForward()) {
                setPoint = 800 * sig;
            } else if (Math.abs(currentPos) >= 1200) {
                setPoint = 1600 * sig;
            } else {
                setPoint = 0;
            }
        return (setPoint - currentPos) * 0.005;
    }

}
