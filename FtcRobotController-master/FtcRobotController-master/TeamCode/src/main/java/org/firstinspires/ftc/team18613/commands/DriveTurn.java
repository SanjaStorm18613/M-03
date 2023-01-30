package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;

public class DriveTurn extends Command {

    DTMecanum drive;
    double turn;

    public DriveTurn(DTMecanum drive, double turn) {
        this.drive = drive;
        this.turn = turn;

    }

    @Override
    public void run(){
        drive.setTurn(turn);
    }
}
