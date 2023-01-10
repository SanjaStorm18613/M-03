package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvWebcam;


public class VisionCtrl {

    HardwareMap hardwareMap;
    OpenCvWebcam webcam;
    //OpenCvCamera webcam;
    Pipl detector;
    LinearOpMode opMode;
    Telemetry telemetry;


    public VisionCtrl(LinearOpMode opM, HardwareMap hw, Telemetry t){

        opMode = opM;
        telemetry = t;
        hardwareMap = hw;


        int cameraMonitorViewId = opMode.hardwareMap
                                .appContext .getResources()
                                .getIdentifier("cameraMonitorViewId"
                                                , "id"
                                                , hardwareMap.appContext.getPackageName());

        webcam = OpenCvCameraFactory.getInstance().createWebcam(
                                        hardwareMap.get
                                        (WebcamName.class, "Webcam 1")
                                        , cameraMonitorViewId);
 //*/
        //webcam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        detector = new Pipl();
        initDetectionElement();
    }

    private void initDetectionElement() {

        webcam.setMillisecondsPermissionTimeout(2500);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {

            @Override
            public void onOpened() {
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
                /*
                ExposureControl ec = webcam.getExposureControl();
                ec.setMode(ExposureControl.Mode.Manual);
                ec.setExposure(1, TimeUnit.MILLISECONDS);

                 */
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Error while opening camera: ", errorCode);
            }
        });

        webcam.setPipeline(detector);

    }

    public void stopStreaming(){
        webcam.stopStreaming();
    }

    public double getcontourArea() {
        return detector.getcontourArea();
    }
    public String getColorDetected() {
        return detector.getColorDetected();
    }
}
