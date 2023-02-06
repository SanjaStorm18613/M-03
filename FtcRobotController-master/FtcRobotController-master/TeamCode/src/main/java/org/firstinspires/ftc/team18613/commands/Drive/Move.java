package org.firstinspires.ftc.team18613.commands.Drive;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.utils.SupplierPair;

public class Move extends Command {
    private final DTMecanum drive;
    private final SupplierPair<Float> stick;

    public Move(DTMecanum drive, SupplierPair<Float> stick) {
        this.drive = drive;
        this.stick = stick;

    }

    @Override
    public void run(){
        drive.setMove(stick.firstValue().get(), stick.secondValue().get());
    }

}
