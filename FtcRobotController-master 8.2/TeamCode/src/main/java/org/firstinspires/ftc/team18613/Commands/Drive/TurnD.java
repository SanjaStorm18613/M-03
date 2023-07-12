package org.firstinspires.ftc.team18613.Commands.Drive;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Utils.Supplier;

public class TurnD extends Command {
    private final DTMecanum drive;
    private final Supplier<Float> turn;

    public TurnD(DTMecanum drive, Supplier<Float> turn) {
        this.drive = drive;
        this.turn = turn;

    }

    @Override
    public void run(){
        drive.setTurn(turn.get());
    }
}
