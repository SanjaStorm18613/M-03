package org.firstinspires.ftc.team18613.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.FocusControl;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvWebcam;


public class VisionCtrl {

    private final OpenCvWebcam webcam;
    private final LinearOpMode opMode;

    public VisionCtrl(LinearOpMode opM, boolean webcamAuto){

        //OpenCvCamera webcam;
        opMode = opM;

        int cameraMonitorViewId = opM.hardwareMap
                                .appContext .getResources()
                                .getIdentifier("cameraMonitorViewId"
                                                , "id"
                                                , opMode.hardwareMap.appContext.getPackageName());

        webcam = OpenCvCameraFactory.getInstance().createWebcam(
                                        opMode.hardwareMap.get
                                        (WebcamName.class, webcamAuto ? "Webcam 1" : "Webcam 2")
                                        , cameraMonitorViewId);
 //*/
        //webcam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        initDetectionElement();
    }

    private void initDetectionElement() {

        webcam.setMillisecondsPermissionTimeout(2500);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {

            @Override
            public void onOpened() {
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

            }

            @Override
            public void onError(int errorCode) {
                opMode.telemetry.addData("Error while opening camera: ", errorCode);
            }
        });

    }

    public void setPepiline(TrackingJunction detector) {
        webcam.setPipeline(detector);
    }

    public void setPepiline(PipelineColors detector) {
        webcam.setPipeline(detector);
    }

    public void stopDetection(){
        webcam.pauseViewport();
        webcam.stopStreaming();
        webcam.closeCameraDevice();

    }
    public void stopViewport() {
        webcam.pauseViewport();
    }
}
