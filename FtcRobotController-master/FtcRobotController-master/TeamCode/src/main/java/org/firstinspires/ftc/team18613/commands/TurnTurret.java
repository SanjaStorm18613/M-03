package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Turret;

public class TurnTurret extends Command {

    private final Turret turret;
    boolean isClockwise;

    public TurnTurret (Turret turret, boolean isClockwise) {
        this.turret = turret;
        this.isClockwise = isClockwise;
    }

    @Override
    public void run() {
        turret.turn(isClockwise);
    }

}
