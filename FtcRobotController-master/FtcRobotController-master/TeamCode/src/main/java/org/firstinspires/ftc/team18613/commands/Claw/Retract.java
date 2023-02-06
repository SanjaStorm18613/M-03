package org.firstinspires.ftc.team18613.commands.Claw;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Claw;

public class Retract extends Command {

    private final Claw claw;

    public Retract(Claw claw) {
        this.claw = claw;

    }

    @Override
    public void run(){
        claw.retract();
    }
}
