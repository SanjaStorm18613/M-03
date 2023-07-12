package org.firstinspires.ftc.team18613.Utils;

public class SupplierPair <T>{

    Supplier<T> a, b;

    public SupplierPair(Supplier<T> a, Supplier<T> b) {

        this.a = a;
        this.b = b;

    }

    public Supplier<T> firstValue() {
        return a;
    }

    public Supplier<T> secondValue() {
        return b;
    }
}
