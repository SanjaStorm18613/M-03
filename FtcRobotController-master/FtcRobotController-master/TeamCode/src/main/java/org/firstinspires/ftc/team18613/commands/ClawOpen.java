package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Claw;

public class ClawOpen extends Command {

    Claw m_garra;

    public ClawOpen(Claw garra){
        m_garra = garra;
    }

    @Override
    public void run() {
        m_garra.openClaw();
    }
}
