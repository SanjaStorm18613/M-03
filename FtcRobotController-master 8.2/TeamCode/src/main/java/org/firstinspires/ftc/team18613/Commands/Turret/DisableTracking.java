package org.firstinspires.ftc.team18613.Commands.Turret;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Turret;

public class DisableTracking extends Command {
    private final Turret turret;

    public DisableTracking(Turret turret) {
        this.turret = turret;
    }

    @Override
    public void run() {
        turret.disableTracking();
    }

}
