package org.firstinspires.ftc.team18613.Utils;

@FunctionalInterface
public interface Function<T, R> {
    R apply(T var1);

    default <V> java.util.function.Function<V, R> compose(java.util.function.Function<? super V, ? extends T> before) {
        throw new RuntimeException("Stub!");
    }

    default <V> java.util.function.Function<T, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
        throw new RuntimeException("Stub!");
    }

    static <T> java.util.function.Function<T, T> identity() {
        throw new RuntimeException("Stub!");
    }
}
