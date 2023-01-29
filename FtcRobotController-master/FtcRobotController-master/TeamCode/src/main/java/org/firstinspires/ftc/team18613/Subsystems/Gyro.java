package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.team18613.Subsystem;
import org.firstinspires.ftc.team18613.TeleOpM03;

public class Gyro extends Subsystem {

    private final BNO055IMU imu;

    private double anguloAnterior;
    private int revolucoes;


    public Gyro(){

        BNO055IMU.Parameters GYRO_imu_parameters;

        imu = TeleOpM03.hm.get(BNO055IMU.class, "imu2");

        GYRO_imu_parameters                = new BNO055IMU.Parameters();
        GYRO_imu_parameters.mode           = BNO055IMU.SensorMode.GYRONLY;
        GYRO_imu_parameters.angleUnit      = BNO055IMU.AngleUnit.DEGREES;
        GYRO_imu_parameters.loggingEnabled = false;

        imu.initialize(GYRO_imu_parameters);

        anguloAnterior = 0;
        revolucoes = 0;
    }

    public double getContinuousAngle() {

        double anguloAtual = getPeriodicAngle();

        if (180 < Math.abs(anguloAnterior - anguloAtual)) {

            if (anguloAnterior > anguloAtual) revolucoes++;
            else revolucoes--;

        }
        anguloAnterior = anguloAtual;

        return revolucoes * 360 + anguloAtual;

    }

    private double getPeriodicAngle(){

        return -imu.getAngularOrientation(AxesReference
                .INTRINSIC, AxesOrder
                .ZYX, AngleUnit
                .DEGREES)
                .firstAngle;

    }

}
