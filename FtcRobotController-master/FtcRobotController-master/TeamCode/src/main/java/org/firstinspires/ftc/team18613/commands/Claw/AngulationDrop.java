package org.firstinspires.ftc.team18613.commands.Claw;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Claw;

public class AngulationDrop extends Command {
    private final Claw claw;
    private final double angle;

    public AngulationDrop(Claw claw, double angle) {
        this.claw = claw;
        this.angle = angle;
    }

    @Override
    public void run() {
        claw.angulationDrop(angle);
    }
}
