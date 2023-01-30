package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;

public class DriveMove extends Command {

    DTMecanum drive;
    double x, y;
    public DriveMove(DTMecanum drive, double x, double y) {
        this.drive = drive;
        this.x = x;
        this.y = y;

    }

    @Override
    public void run(){
        drive.setMove(x, y);
    }

}
