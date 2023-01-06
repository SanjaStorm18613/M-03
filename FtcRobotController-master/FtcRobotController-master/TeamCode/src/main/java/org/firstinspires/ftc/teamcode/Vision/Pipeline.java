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
    double[] lowFlt = {40, 50, 50}, upFlt = {70, 100, 100};


    public Pipeline(Telemetry t){
        telemetry = t;
    }


    @Override
    public Mat processFrame(Mat orgmInpt) {

        ArrayList<ArrayList<MatOfPoint>> ctrss = new ArrayList<>();
        Mat input = new Mat();

        //orgmInpt.copyTo(input);
        Imgproc.cvtColor(orgmInpt, input, Imgproc.COLOR_BGR2HLS);

        //vai ser de 3 cores diferentes
        ctrss.add(0, this.filter(orgmInpt, lowFlt, upFlt));
        ctrss.add(1, this.filter(orgmInpt, lowFlt, upFlt));
        ctrss.add(2, this.filter(orgmInpt, lowFlt, upFlt));

        int elemt = -1, arrayElemt = -1;
        double sizeElemt = 0;

        for (ArrayList<MatOfPoint> ctrs: ctrss) {

            if (ctrs.size() == 0) continue;

            for (MatOfPoint ctr : ctrs){
                if (Imgproc.contourArea(ctr) > sizeElemt) {
                    arrayElemt = ctrss.indexOf(ctrs);
                    elemt = ctrs.indexOf(ctr);
                    sizeElemt = Imgproc.contourArea(ctr);
                }
            }
        }

        if (arrayElemt != -1 && elemt != -1) {

            if (sizeElemt > 50) {
                Rect biggestRect = Imgproc.boundingRect(ctrss.get(arrayElemt).get(elemt));

                Point supDir = new Point(biggestRect.x, biggestRect.y);
                Point botEsc = new Point(biggestRect.x + biggestRect.width,
                        biggestRect.y + biggestRect.height);

                Scalar green = new Scalar(0, 255, 0);

                Imgproc.rectangle(input, supDir, botEsc, green, 5);
            }
        }

        return orgmInpt;
    }

    //***
    public ArrayList<MatOfPoint> filter(Mat input, double[] low, double[] hig){
        Mat mat = new Mat();

        //0-180
        Scalar lower = new Scalar(20, 10, 10);
        Scalar upper = new Scalar(100, 100, 100);

        //***
        Core.inRange(input, lower, upper, mat);

        //Structuring element
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));


        //*** opening
        Imgproc.erode(mat, mat, kernel);
        Imgproc.dilate(mat, mat, kernel);
        Imgproc.GaussianBlur(mat, mat, new Size(3, 3), 10);
        Imgproc.threshold(mat, mat, 20, 255, Imgproc.THRESH_BINARY);

        ArrayList<MatOfPoint> contr = new ArrayList<>();
        //***deveria estar aqui?
        Mat temp = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        //trocar temp por new mat??
        Imgproc.findContours(mat, contr, temp, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        return contr;
    }
}

/*
            Core.bitwise_and(mat, mat, result, input);
            mat.release();
 //*/

