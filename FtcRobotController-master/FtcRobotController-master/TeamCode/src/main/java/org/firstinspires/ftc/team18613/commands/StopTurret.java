package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Turret;

public class StopTurret extends Command {

    private final Turret turret;

    public StopTurret (Turret turret) {
        this.turret = turret;
    }

    @Override
    public void run() {
        turret.stop();
    }

}
