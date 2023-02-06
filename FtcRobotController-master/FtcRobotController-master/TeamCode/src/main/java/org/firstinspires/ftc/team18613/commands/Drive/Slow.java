package org.firstinspires.ftc.team18613.commands.Drive;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.utils.Supplier;

public class Slow extends Command {
    private final DTMecanum drive;
    private final Supplier<Float> slow;

    public Slow(DTMecanum drive, Supplier<Float> slow) {
        this.drive = drive;
        this.slow = slow;

    }

    @Override
    public void run(){
        drive.setSlowFactor(slow.get());
    }
}
