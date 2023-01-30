package org.firstinspires.ftc.team18613.utils;

public class Pair <T, S> {

    T a;
    S b;

    public Pair(T a, S b) {

        this.a = a;
        this.b = b;

    }

    public T firstValue() {
        return a;
    }

    public S secondValue() {
        return b;
    }
}
