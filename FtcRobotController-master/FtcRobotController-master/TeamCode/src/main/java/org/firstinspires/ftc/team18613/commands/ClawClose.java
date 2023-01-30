package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Claw;

public class ClawClose extends Command {

    Claw m_garra;

    public ClawClose(Claw garra){
        m_garra = garra;
    }

    @Override
    public void run() {
        m_garra.closeClaw();
    }
}
