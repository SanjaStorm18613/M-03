package org.firstinspires.ftc.team18613.commands.Claw;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.utils.Supplier;

public class AngulationDrop extends Command {
    private final Claw claw;
    private final Supplier<Float> angle;

    public AngulationDrop(Claw claw, Supplier<Float> angle) {
        this.claw = claw;
        this.angle = angle;
    }

    @Override
    public void run() {
        claw.angulationDrop(angle.get());
    }
}
