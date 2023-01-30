package org.firstinspires.ftc.team18613;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.Controller;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.commands.DriveMove;
import org.firstinspires.ftc.team18613.commands.DriveSlow;
import org.firstinspires.ftc.team18613.commands.DriveTurn;
import org.firstinspires.ftc.team18613.commands.ElevatorAdjustStage;
import org.firstinspires.ftc.team18613.commands.ElevatorShiftStage;
import org.firstinspires.ftc.team18613.commands.StopTurret;
import org.firstinspires.ftc.team18613.commands.TurnTurret;
import org.firstinspires.ftc.team18613.utils.FloatPair;

@TeleOp(name = "TeleOpM03", group = "Linear Opmode")

public class TeleOpM03 extends LinearOpMode {
    
    DTMecanum drive;
    Elevator elev;
    Turret turret;
    Claw garra;
    Arm braco;

    public static Telemetry tel;
    public static HardwareMap hm;

    public static Controller pilot, copilot;

    public void runOpMode() {
        tel = telemetry;
        hm = hardwareMap;
        pilot = new Controller(gamepad1);
        copilot = new Controller(gamepad2);

        elev  = new Elevator();
        braco = new Arm(elev);
        garra = new Claw(elev, braco);
        turret = new Turret(elev, garra);
        drive = new DTMecanum (turret);



        waitForStart();
        
        while (opModeIsActive()) {

            copilot.whilePressed(Controller.left, new TurnTurret(turret, false));
            copilot.onReleased(Controller.left, new StopTurret(turret));

            copilot.whilePressed(Controller.right, new TurnTurret(turret, true));
            copilot.onReleased(Controller.right, new StopTurret(turret));

            copilot.onPressed(Controller.y, new ElevatorShiftStage(elev, true));
            copilot.onPressed(Controller.x, new ElevatorShiftStage(elev, false));

            copilot.onPressed(Controller.up, new ElevatorAdjustStage(elev, true));
            copilot.onPressed(Controller.down, new ElevatorAdjustStage(elev, false));

            pilot.stick(Controller.left_stick_x, Controller.left_stick_y, (FloatPair val) -> new DriveMove(drive, val.firstValue(), val.secondValue()));
            pilot.stick(Controller.right_stick_x, (Float val) -> new DriveTurn(drive, val));
            pilot.trigger(Controller.right_trigger, (Float val) -> new DriveSlow(drive, val));

//            drive.control(
//                    gamepad1.left_stick_x,
//                    -gamepad1.left_stick_y,
//                    gamepad1.right_stick_x,
//                    gamepad1.right_trigger > 0.1);
/*
            garra.Control(
                    gamepad2.a,
                    gamepad1.a,
                    gamepad1.b,
                    gamepad1.x,
                    gamepad2.b,
                    gamepad2.right_trigger > 0.1,
                    gamepad2.left_trigger);


 */
            braco.control();

            drive.getTelemetry();

            telemetry.update();
        }
    }
}
