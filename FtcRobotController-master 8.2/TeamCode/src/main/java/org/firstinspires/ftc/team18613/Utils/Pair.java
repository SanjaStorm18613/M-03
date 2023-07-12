package org.firstinspires.ftc.team18613.Utils;

public class Pair <T, S> {

    T a;
    S b;

    public Pair(T a, S d) {

        this.a = a;
        this.b = d;

    }

    public T firstValue() {
        return a;
    }

    public S secondValue() {
        return b;
    }

}
