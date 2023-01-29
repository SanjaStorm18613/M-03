package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Garra;

public class FecharGarra extends Command {

    Garra m_garra;

    public FecharGarra(Garra garra){
        m_garra = garra;
    }

    @Override
    public void run() {
        m_garra.closeClaw();
    }
}
