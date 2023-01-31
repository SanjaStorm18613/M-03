package org.firstinspires.ftc.team18613.commands.Elevator;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;

public class AdjustStage extends Command {
    private final Elevator elevator;
    private final boolean up;

    public AdjustStage(Elevator elevator, boolean up) {
        this.elevator = elevator;
        this.up = up;
    }

    @Override
    public void run() {
        elevator.adjustHeight(up);
    }
}
