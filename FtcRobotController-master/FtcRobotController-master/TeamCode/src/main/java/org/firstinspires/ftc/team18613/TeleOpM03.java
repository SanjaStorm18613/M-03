package org.firstinspires.ftc.team18613;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team18613.Subsystems.Braco;
import org.firstinspires.ftc.team18613.Subsystems.Controller;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevador;
import org.firstinspires.ftc.team18613.Subsystems.Garra;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.commands.StopTurret;
import org.firstinspires.ftc.team18613.commands.TurnTurret;

@TeleOp(name = "TeleOpM03", group = "Linear Opmode")

public class TeleOpM03 extends LinearOpMode {
    
    DTMecanum drive;
    Elevador elev;
    Turret turret;
    Garra garra;
    Braco braco;

    public static Telemetry tel;
    public static HardwareMap hm;

    public static Controller controller1, controller2;

    public void runOpMode() {
        tel = telemetry;
        hm = hardwareMap;
        //controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);

        elev  = new Elevador  ();
        braco = new Braco     (elev);
        garra = new Garra     (elev, braco);
        turret = new Turret(elev, garra);
        drive = new DTMecanum (turret);

        waitForStart();
        
        while (opModeIsActive()) {

            controller2.whilePressed(Controller.left, new TurnTurret(turret, false));
            controller2.onReleased(Controller.left, new StopTurret(turret));

            controller2.whilePressed(Controller.right, new TurnTurret(turret, true));
            controller2.onReleased(Controller.right, new StopTurret(turret));

            drive.control(
                    gamepad1.left_stick_x,
                    -gamepad1.left_stick_y,
                    gamepad1.right_stick_x,
                    gamepad1.right_trigger > 0.1);


/*
            turret.Control(
                    gamepad2.dpad_right,
                    gamepad2.dpad_left);*/

            garra.Control(
                    gamepad2.a,
                    gamepad1.a,
                    gamepad1.b,
                    gamepad1.x,
                    gamepad2.b,
                    gamepad2.right_trigger > 0.1,
                    gamepad2.left_trigger);

            braco.control();


            elev.Control(
                    gamepad2.y,
                    gamepad2.x,
                    gamepad1.dpad_up || gamepad2.dpad_up,
                    gamepad1.dpad_down || gamepad2.dpad_down);
            
            telemetry.update();
        }
    }
}
