package org.firstinspires.ftc.team18613.commands.Claw;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Subsystems.Claw;

public class Close extends Command {
    private final Claw m_garra;

    public Close(Claw garra){
        m_garra = garra;
    }

    @Override
    public void run() {
        m_garra.closeClaw();
    }
}
