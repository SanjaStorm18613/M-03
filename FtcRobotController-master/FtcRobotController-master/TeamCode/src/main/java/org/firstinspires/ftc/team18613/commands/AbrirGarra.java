package org.firstinspires.ftc.team18613.commands;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Garra;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class AbrirGarra extends Command {

    Garra m_garra;

    public AbrirGarra(Garra garra){
        m_garra = garra;
    }

    @Override
    public void run() {
        m_garra.openClaw();
    }
}
