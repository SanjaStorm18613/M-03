package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Turret extends Subsystem {

    private final DcMotor turret;
    private final Elevador elev;
    private final ElapsedTime time;
    private final Garra garra;

    //private double y = 0, x = 0;
    private double var = 0;

    boolean elevColt = true;

    public Turret(Elevador elev, Garra garra) {

        turret = TeleOpM03.hm.get(DcMotor.class, "Yaw");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        turret.setDirection(DcMotorSimple.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.elev = elev;
        this.garra = garra;

        time = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public void Control(boolean cw, boolean ccw) {

        elevColt = elev.getNiveis() == 0;


        double vel;
        if ((!cw && !ccw) || ((var > 0.15 && var < 0.86) && elev.getCurrentPos() < 1700)) {
            time.reset();
            vel = 0;
        } else {
            vel = Math.min(time.time() / 1500, 1);
        }
        TeleOpM03.tel.addData("yaw", turret.getCurrentPosition());


        if (cw && turret.getCurrentPosition() > -1600) turret.setPower(-1);
        else if (ccw && turret.getCurrentPosition() < 1600) turret.setPower(1);
        else turret.setPower(0);


        if (var > 0.12 && var < 0.89) {
            garra.setElev(1.6);
        } else garra.setElev(0);

        TeleOpM03.tel.addData("yaw", turret.getCurrentPosition());
        TeleOpM03.tel.addData("var", var);
    }

    public boolean getBusy() { return turret.isBusy(); }

    public void setPos(double pos, double vel) {
        turret.setTargetPosition((int) (pos * Constants.Yaw.CONVR));
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(vel);
    }

    public boolean getInverted() {
        return var >= 0.86;
    }

    public double[] getCorrectionCoord () {
        return new double[] {1, -1};
    }

    public void turn (boolean isClockwise) {

        int var = turret.getCurrentPosition() / 1600;

        if (isClockwise) {
            if (var >= .  || var >= -) {

            }

        } else {
            if (var <= .16  || var >= -.16) {

            }
        }


        turret.setPower(Constants.Yaw.SPEED * (isClockwise ? 1 : -1));
    }

    public void stop () {
        turret.setPower(0);
    }

}
