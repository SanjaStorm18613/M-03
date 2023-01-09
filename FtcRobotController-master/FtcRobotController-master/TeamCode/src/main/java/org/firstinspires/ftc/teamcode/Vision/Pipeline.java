package org.firstinspires.ftc.teamcode.Vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

public class Pipeline extends OpenCvPipeline {

    Telemetry telemetry;

    Scalar low1, up1, green;
    Mat mat, mat2, input;
    Size s;


    public Pipeline(Telemetry t){
        telemetry = t;


        mat = new Mat();
        mat2 = new Mat();
        input = new Mat();

        green = new Scalar(0, 255, 0);
        low1 = new Scalar(0, 100, 0); //0-180
        up1 = new Scalar(180, 180, 180);

        s = new Size(3,3);

    }


    @Override
    public Mat processFrame(Mat orgmInpt) {

        ArrayList<ArrayList<MatOfPoint>> elemsArr = new ArrayList<>();

        //orgmInpt.copyTo(input);
        Imgproc.cvtColor(orgmInpt, input, Imgproc.COLOR_BGR2HLS);

        //vai ser de 3 cores diferentes
        elemsArr.add(0, this.colorFilter(orgmInpt, low1, up1));
        //elemsArr.add(1, this.colorFilter(orgmInpt, low1, up1));
        //elemsArr.add(2, this.colorFilter(orgmInpt, low1, up1));

        //telemetry.addData("size elemsArr", elemsArr.size());

        Imgproc.cvtColor(input, input, Imgproc.COLOR_HLS2BGR);


        int elementsIdx = -1, elementssIdx = -1;
        double area = 0;

        for (ArrayList<MatOfPoint> elems: elemsArr) {

            for (MatOfPoint item : elems){

                if (Imgproc.contourArea(item) > area) {

                    elementssIdx = elemsArr.indexOf(elems);
                    elementsIdx = elems.indexOf(item);
                    area = Imgproc.contourArea(item);

                    //telemetry.addData(elementsIdx + "", elems.size());
                    //telemetry.addData("area", area);

                }
            }
        }

        if (elementsIdx != -1) {

            if (area > 50) {
                Rect rectRange = Imgproc.boundingRect(elemsArr.get(elementssIdx).get(elementsIdx));

                Point supDir = new Point(rectRange.x, rectRange.y);
                Point botEsc = new Point(rectRange.x + rectRange.width,
                        rectRange.y + rectRange.height);

                Imgproc.rectangle(input, supDir, botEsc, green, 5);
            }
        }

        return input;
    }

    //***
    public ArrayList<MatOfPoint> colorFilter(Mat input, Scalar low, Scalar up){

        Core.inRange(input, low, up, mat);

        //Structuring element
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, s);

        //*** opening
        Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_OPEN, kernel);
        Imgproc.GaussianBlur(mat, mat, s, 10);

        ArrayList<MatOfPoint> contr = new ArrayList<>();

        Imgproc.findContours(mat, contr, mat2, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        mat2.release();
        mat.release();
        return contr;
    }
}


