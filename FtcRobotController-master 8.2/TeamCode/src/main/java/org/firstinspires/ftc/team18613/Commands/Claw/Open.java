package org.firstinspires.ftc.team18613.Commands.Claw;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Claw;

public class Open extends Command {
    private final Claw m_garra;

    public Open(Claw garra){
        m_garra = garra;
    }

    @Override
    public void run() {
        m_garra.openClaw();
    }
}
