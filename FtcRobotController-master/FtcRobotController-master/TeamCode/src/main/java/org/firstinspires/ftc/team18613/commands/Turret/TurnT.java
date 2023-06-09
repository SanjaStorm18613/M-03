package org.firstinspires.ftc.team18613.commands.Turret;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Turret;

public class TurnT extends Command {
    private final Turret turret;
    private final boolean isClockwise;

    public TurnT(Turret turret, boolean isClockwise) {
        this.turret = turret;
        this.isClockwise = isClockwise;
    }

    @Override
    public void run() {
        turret.enable(isClockwise);
    }

}
