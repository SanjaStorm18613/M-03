package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;

public class ElevatorShiftStage extends Command {

    Elevator elevator;
    boolean up;

    public ElevatorShiftStage(Elevator elevator, boolean up) {
        this.elevator = elevator;
        this.up = up;
    }

    @Override
    public void run() {
        if (up){
            elevator.raiseStage();
        } else {
            elevator.lowerStage();
        }
    }
}
