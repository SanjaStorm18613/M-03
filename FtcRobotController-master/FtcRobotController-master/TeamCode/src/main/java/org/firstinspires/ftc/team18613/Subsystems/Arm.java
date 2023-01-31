package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.team18613.Constants;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Arm extends Subsystem {

    private final Servo sLeft, sRight;
    private final Elevator elevator;

    private double controlRequirement = -1;
    private double automaticPos = 0;

    public Arm(Elevator elevator) {

        //Servos direito e esquerdo do braço
        sLeft = TeleOpM03.hm.get(Servo.class, "E");
        sRight = TeleOpM03.hm.get(Servo.class, "D");

        sRight.setDirection(Servo.Direction.FORWARD);
        sLeft.setDirection(Servo.Direction.REVERSE);

        this.elevator = elevator;
    }

    public void control() {
        double posRangeBraco = Constants.Braco.MAX_POS - Constants.Braco.MIN_POS;
        double posPercentElev = (elevator.getCurrentPos() / ((Constants.Elevador.NV_3/0.9) * Constants.Elevador.CONVR));
        double pos = (posPercentElev * posRangeBraco) + Constants.Braco.MIN_POS;

        pos = Math.min(pos, Constants.Braco.MAX_POS);
        pos = Math.max(pos, Constants.Braco.MIN_POS);

        automaticPos = addControl(pos);

        //pos = controlRequirement == -1 ? pos : controlRequirement;

        sLeft.setPosition(automaticPos);
        sRight.setPosition(automaticPos);

        TeleOpM03.tel.addData("braco", sLeft.getPosition());
    }

    public double getPos(){
        return automaticPos - Constants.Braco.MIN_POS;
    }

    public void setControlRequirement(double p) {
        controlRequirement = p;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public void periodic(){
        updateAutomaticPos();
        sLeft.setPosition(automaticPos);
        sRight.setPosition(automaticPos);
        TeleOpM03.tel.addData("braco", sLeft.getPosition());

    }

    public double addControl(double pos) {
        controlRequirement = Math.max(pos, controlRequirement);
        return controlRequirement;
    }

    public void removeControl(){
        controlRequirement = 0;
    }

    private void updateAutomaticPos() {
        double posRangeBraco = Constants.Braco.MAX_POS - Constants.Braco.MIN_POS;
        double posPercentElev = (elevator.getCurrentPos() / ((Constants.Elevador.NV_3/0.9) * Constants.Elevador.CONVR));
        double pos = (posPercentElev * posRangeBraco) + Constants.Braco.MIN_POS;

        pos = Math.min(pos, Constants.Braco.MAX_POS);
        pos = Math.max(pos, Constants.Braco.MIN_POS);

        automaticPos = addControl(pos);
    }

}