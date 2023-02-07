package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Arm extends Subsystem {

    private final Servo sLeft, sRight;
    private final Elevator elevator;
    private  final OpMode opMode;

    private double controlRequirement = -1;
    private double automaticPos = 0;

    public Arm(OpMode opMode, Elevator elevator) {

        this.opMode = opMode;
        //Servos direito e esquerdo do braço
        sLeft = opMode.hardwareMap.get(Servo.class, "E");
        sRight = opMode.hardwareMap.get(Servo.class, "D");

        sRight.setDirection(Servo.Direction.FORWARD);
        sLeft.setDirection(Servo.Direction.REVERSE);

        this.elevator = elevator;
    }

    public double getPos(){
        return automaticPos - Constants.Braco.MIN_POS;
    }

    public void setControlRequirement(double p) {
        controlRequirement = p;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public void periodic(){
        double var;
        updateAutomaticPos();
        if (controlRequirement == -1){
            var = automaticPos;
        } else {
            var = controlRequirement;
        }
        sLeft.setPosition(var);
        sRight.setPosition(var);

        //opMode.telemetry.addData("braco", sLeft.getPosition());

    }

    public double addControl(double pos) {
        controlRequirement = Math.max(pos, controlRequirement);
        return controlRequirement;
    }

    public void removeControl(){
        controlRequirement = -1;
    }

    private void updateAutomaticPos() {
        double posRangeBraco = Constants.Braco.MAX_POS - Constants.Braco.MIN_POS;
        double posPercentElev = (elevator.getCurrentPos() / ((Constants.Elevador.NV_3 * 0.8) * Constants.Elevador.CONVR));
        double pos = (posPercentElev * posRangeBraco) + Constants.Braco.MIN_POS;

        pos = Math.min(pos, Constants.Braco.MAX_POS);
        pos = Math.max(pos, Constants.Braco.MIN_POS);

        automaticPos = pos;

    }

}