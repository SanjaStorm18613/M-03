package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;

public class ElevatorAdjustStage extends Command {
    Elevator elevator;
    boolean up;

    public ElevatorAdjustStage(Elevator elevator, boolean up) {
        this.elevator = elevator;
        this.up = up;
    }

    @Override
    public void run() {
        elevator.adjustHeight(up);
    }
}
