package org.firstinspires.ftc.team18613.utils;

public class Trio<T, S, V> {

    T a;
    S b;
    V c;

    public Trio(T a, S b, V c) {

        this.a = a;
        this.b = b;
        this.c = c;

    }

    public T firstValue() {
        return a;
    }

    public S secondValue() {
        return b;
    }

    public V thirdValue(){
        return  c;
    }
}
