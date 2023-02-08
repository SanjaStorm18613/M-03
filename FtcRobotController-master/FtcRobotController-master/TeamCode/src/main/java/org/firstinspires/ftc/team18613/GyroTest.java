package org.firstinspires.ftc.team18613;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team18613.Subsystems.Arm;
import org.firstinspires.ftc.team18613.Subsystems.Claw;
import org.firstinspires.ftc.team18613.Subsystems.Controller;
import org.firstinspires.ftc.team18613.Subsystems.DTMecanum;
import org.firstinspires.ftc.team18613.Subsystems.Elevator;
import org.firstinspires.ftc.team18613.Subsystems.Gyro;
import org.firstinspires.ftc.team18613.Subsystems.Turret;
import org.firstinspires.ftc.team18613.commands.Claw.AngulationDrop;
import org.firstinspires.ftc.team18613.commands.Claw.Drop;
import org.firstinspires.ftc.team18613.commands.Claw.HorizontalColect;
import org.firstinspires.ftc.team18613.commands.Claw.LoweredFrontColect;
import org.firstinspires.ftc.team18613.commands.Claw.LoweredSideColect;
import org.firstinspires.ftc.team18613.commands.Claw.Retract;
import org.firstinspires.ftc.team18613.commands.Drive.Move;
import org.firstinspires.ftc.team18613.commands.Drive.Slow;
import org.firstinspires.ftc.team18613.commands.Drive.TurnD;
import org.firstinspires.ftc.team18613.commands.Elevator.AdjustStage;
import org.firstinspires.ftc.team18613.commands.Elevator.ShiftStage;
import org.firstinspires.ftc.team18613.commands.Turret.Stop;
import org.firstinspires.ftc.team18613.commands.Turret.TurnT;
import org.firstinspires.ftc.team18613.utils.Supplier;
import org.firstinspires.ftc.team18613.utils.SupplierPair;

@TeleOp(name = "GyroTest", group = "Linear Opmode")

public class GyroTest extends LinearOpMode {

    Gyro gyro;

    public static Telemetry tel;
    public static HardwareMap hm;

    public void runOpMode() {
        tel = telemetry;
        hm = hardwareMap;
        gyro = new Gyro(this);

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("getPeriodicAngle", gyro.getPeriodicAngle());
            telemetry.addData("getContinuousAngle", gyro.getContinuousAngle());

            telemetry.update();

        }
    }
}
