package org.firstinspires.ftc.teamcode.Vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class Pipeline extends OpenCvPipeline {

    Mat mat = new Mat(), mat2 = new Mat();
    Mat result;
    Telemetry telemetry;

    public Pipeline(Telemetry t){
        telemetry = t;
    }


        @Override
        public Mat processFrame(Mat input) {

            Imgproc.cvtColor(input, mat, Imgproc.COLOR_BGR2HLS);


            Scalar lower = new Scalar (0, 0, 0);
            Scalar upper = new Scalar (50, 255, 255);


            Core.inRange(mat, lower, upper, mat);

            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));

            Imgproc.erode(mat, mat, kernel);
            Imgproc.dilate(mat, mat, kernel);
            Imgproc.GaussianBlur(mat, mat, new Size(3, 3), 10);
            Imgproc.threshold(mat, mat, 20, 255, Imgproc.THRESH_BINARY);



            ArrayList<MatOfPoint> contours = new ArrayList<>();

            Mat temp = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
            Imgproc.findContours(mat, contours, temp, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

            if (result == null) result = new Mat();
            else result.release();

            telemetry.addData("height", mat.height());
            telemetry.addData("width", mat.width());
            telemetry.addData("input height", input.height());
            telemetry.addData("input width", input.width());

            Core.bitwise_and(input, input, result, mat);
            mat.release();

            return result;

        }

        public void ell(Mat inp, int[] low, int[] hig){



        }

}

