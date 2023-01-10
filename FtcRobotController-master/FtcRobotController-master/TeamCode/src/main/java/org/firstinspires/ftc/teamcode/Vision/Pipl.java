package org.firstinspires.ftc.teamcode.Vision;

import android.widget.Switch;

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

public class Pipl extends OpenCvPipeline {


    Scalar low0, up0, low1, up1, low2, up2, green;
    Mat input, mat, mat2;
    Size s;
    MatOfPoint objtDetc;
    ArrayList<ArrayList<MatOfPoint>> elemsArr;
    ArrayList<MatOfPoint> contourArr;
    int ObjtDetcColor = -1;

    public Pipl() {

        mat = new Mat();
        mat2 = new Mat();
        input = new Mat();

        green = new Scalar(0, 255, 0);

        //0-180
        low0 = new Scalar(40, 64, 100);
        up0 = new Scalar(90, 192, 255);//green

        low1 = new Scalar(25, 64, 127);
        up1 = new Scalar(35, 192, 255);//cian

        low2 = new Scalar(0, 0, 0);
        up2 = new Scalar(180, 30, 30);//black

        s = new Size(3, 3);
        elemsArr = new ArrayList<>();
        contourArr = new ArrayList<>();

    }


    @Override
    public Mat processFrame(Mat orgmInpt) {

        Imgproc.cvtColor(orgmInpt, input, Imgproc.COLOR_BGR2HLS);

        elemsArr.clear();

        elemsArr.add(colorFilter(low0, up0));
        elemsArr.add(colorFilter(low1, up1));
        //elemsArr.add(colorFilter(low2, up2));

        orgmInpt.copyTo(input);

        ObjtDetcColor = -1;
        objtDetc = null;

        int i = 0;
        for (ArrayList<MatOfPoint> elms : elemsArr) {

            for (MatOfPoint item : elms) {

                if (objtDetc == null ||(Imgproc.contourArea(item) > Imgproc.contourArea(objtDetc))) {
                    ObjtDetcColor = i;
                    objtDetc = item;
                }
            }
            i++;
        }


        if (objtDetc != null && Imgproc.contourArea(objtDetc) > 50) {

            Rect rectRange = Imgproc.boundingRect(objtDetc);
            Point supDir = new Point(rectRange.x, rectRange.y);
            Point botEsc = new Point(rectRange.x + rectRange.width,
                    rectRange.y + rectRange.height);

            Imgproc.rectangle(input, supDir, botEsc, green, 5);
        }


        return input;

    }

    public ArrayList<MatOfPoint> colorFilter(Scalar low, Scalar up) {

        Core.inRange(input, low, up, mat);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, s);

        Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_OPEN, kernel);
        Imgproc.GaussianBlur(mat, mat, s, 10);

        contourArr.clear();
        Imgproc.findContours(mat, contourArr, mat2, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        mat.release();
        mat2.release();
        return(ArrayList<MatOfPoint>) contourArr.clone();
    }

    public double getcontourArea() {
        if (objtDetc == null) {
            return 0;
        }

        return Imgproc.contourArea(objtDetc);

    }

    public String getColorDetected() {

        switch (ObjtDetcColor) {
            case 0:
                return "Green";
            case 1:
                return "Yellow";
            default:
                return "Black";
        }

    }
}


