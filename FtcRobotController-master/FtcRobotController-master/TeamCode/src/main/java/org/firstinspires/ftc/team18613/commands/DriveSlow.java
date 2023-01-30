package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;

public class DriveSlow extends Command {

    DTMecanum drive;
    double slow;

    public DriveSlow(DTMecanum drive, double slow) {
        this.drive = drive;
        this.slow = slow;

    }

    @Override
    public void run(){
        drive.setSlow(slow);
    }
}
