package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;

public class Arm extends Subsystem {

    private final Servo sLeft, sRight;
    private final Elevator elevator;
    private  final OpMode opMode;

    private double controlRequirement = -1;
    private double automaticPos = 0;

    public Arm(OpMode opMode, Elevator elevator) {

        this.opMode = opMode;

        sLeft = opMode.hardwareMap.get(Servo.class, "E");
        sRight = opMode.hardwareMap.get(Servo.class, "D");

        sRight.setDirection(Servo.Direction.REVERSE);
        sLeft.setDirection(Servo.Direction.FORWARD);

        this.elevator = elevator;
    }

    public void periodic(){
        double var;
        updateAutomaticPos();
        if (controlRequirement == -1){
            var = automaticPos;
        } else {
            var = controlRequirement;
        }

        var = Math.round(var * 1000) / 1000.0;

        sLeft.setPosition(var);
        sRight.setPosition(var);

    }

    public void addControl(double pos) {
        controlRequirement = Math.max(pos, controlRequirement);
    }

    public void removeControl() {
        controlRequirement = -1;
    }

    private void updateAutomaticPos() {
        double posRangeBraco = Constants.Braco.MAX_POS - Constants.Braco.MIN_POS;
        double posPercentElev = (elevator.getCurrentPos() / ((Constants.Elevator.NV_3) * Constants.Elevator.CONVR));
        double pos = (Math.pow(posPercentElev, 3) * posRangeBraco) + Constants.Braco.MIN_POS;

        pos = Math.max(controlRequirement, pos);

        pos = Math.min(pos, Constants.Braco.MAX_POS);
        pos = Math.max(pos, Constants.Braco.MIN_POS);

        automaticPos = pos;

    }

    public double getPos(){
        return sLeft.getPosition() - Constants.Braco.MIN_POS;
    }

}