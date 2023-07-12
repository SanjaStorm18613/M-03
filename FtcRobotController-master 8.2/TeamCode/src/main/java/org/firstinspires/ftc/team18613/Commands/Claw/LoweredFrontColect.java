package org.firstinspires.ftc.team18613.Commands.Claw;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Claw;

public class LoweredFrontColect extends Command {
    private final Claw claw;

    public LoweredFrontColect(Claw claw) {
        this.claw = claw;
    }

    @Override
    public void run() {
        claw.loweredFrontCollect();
    }
}
