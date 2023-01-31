package org.firstinspires.ftc.team18613.commands.Drive;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;

public class Move extends Command {
    private final DTMecanum drive;
    private final double x, y;

    public Move(DTMecanum drive, double x, double y) {
        this.drive = drive;
        this.x = x;
        this.y = y;

    }

    @Override
    public void run(){
        drive.setMove(x, y);
    }

}
