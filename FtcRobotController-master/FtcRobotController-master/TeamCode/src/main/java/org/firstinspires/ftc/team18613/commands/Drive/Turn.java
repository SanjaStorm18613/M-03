package org.firstinspires.ftc.team18613.commands.Drive;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;

public class Turn extends Command {
    private final DTMecanum drive;
    private final double turn;

    public Turn(DTMecanum drive, double turn) {
        this.drive = drive;
        this.turn = turn;

    }

    @Override
    public void run(){
        drive.setTurn(turn);
    }
}
