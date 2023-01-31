package org.firstinspires.ftc.team18613.commands.Drive;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;

public class Slow extends Command {
    private final DTMecanum drive;
    private final double slow;

    public Slow(DTMecanum drive, double slow) {
        this.drive = drive;
        this.slow = slow;

    }

    @Override
    public void run(){
        drive.setSlowFactor(slow);
    }
}
