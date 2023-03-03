package org.firstinspires.ftc.team18613.Vision;

import org.firstinspires.ftc.team18613.Constants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class PipelineSizes {//extends OpenCvPipeline {
/*
    double[][] cUp = Constants.Pipeline.COLOR_UP,
            cLw = Constants.Pipeline.COLOR_LOW;

    int tolerance = Constants.Pipeline.TOLERANCE_AREA;

    Scalar low0, up0, green;
    Mat input, mat, mat2;
    Size s;
    MatOfPoint objtDetc;
    ArrayList<MatOfPoint> contourArr;


    public PipelineSizes() {

        mat = new Mat();

        green = new Scalar(0, 255, 0);

        //0-180
        low0 = new Scalar(cLw[0][0], cLw[0][1], cLw[0][2]);
        up0 = new Scalar(cUp[0][0], cUp[0][1], cUp[0][2]);//green


        s = new Size(3, 3);
        contourArr = new ArrayList<>();
    }


    @Override
    public Mat processFrame(Mat orgmInpt) {

        Imgproc.cvtColor(orgmInpt, mat, Imgproc.COLOR_BGR2HLS);

        Core.inRange(input, low0, up0, mat);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, s);
        Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_OPEN, kernel);
        kernel.release();
        Imgproc.GaussianBlur(mat, mat, s, 10);

        contourArr.clear();
        Imgproc.findContours(mat, contourArr, mat2, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        orgmInpt.copyTo(mat);

        objtDetc = null;

        for (MatOfPoint item : contourArr) {
            if (objtDetc == null || (Imgproc.contourArea(item) > Imgproc.contourArea(objtDetc))) {
                objtDetc = item;
            }
        }


        if (objtDetc != null && Imgproc.contourArea(objtDetc) > tolerance) {

            Rect rectRange = Imgproc.boundingRect(objtDetc);
            Point supDir = new Point(rectRange.x, rectRange.y);
            Point botEsc = new Point(rectRange.x + rectRange.width,
                    rectRange.y + rectRange.height);

            Imgproc.rectangle(mat, supDir, botEsc, green, 5);
        }

        return mat;
    }

    public DetectionColor getImageDetected() {

        if (objtDetc != null) {

            if (Imgproc.contourArea(objtDetc) >= 1000) return DetectionColor.BIG;
            else if (Imgproc.contourArea(objtDetc) >= 500) return DetectionColor.MIDDLE;
            else return DetectionColor.SMALL;

        } else return DetectionColor.SMALL;

    }

    public enum DetectionColor {
        SMALL,
        MIDDLE,
        BIG
    }

 */
}


