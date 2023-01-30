package org.firstinspires.ftc.team18613.Subsystems;


import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.utils.FloatPair;
import org.firstinspires.ftc.team18613.utils.Pair;
import org.firstinspires.ftc.team18613.utils.Supplier;
import org.firstinspires.ftc.team18613.utils.Function;

import java.util.ArrayList;
import java.util.Arrays;


public class Controller {

    public static final int
            a = 0,
            b = 1,
            x = 2,
            y = 3,
            up = 4,
            down = 5,
            left = 6,
            right = 7,
            left_bumper = 8,
            right_bumper = 9,
            left_stick_button = 10,
            right_stick_button = 11,
            left_stick_y = 0,
            left_stick_x = 1,
            right_stick_y = 2,
            right_stick_x = 3,
            left_trigger = 4,
            right_trigger = 5;


    private final boolean[] last_values = new boolean[12];

    private final ArrayList<Supplier<Boolean>> boolSuppliers;
    private final ArrayList<Supplier<Float>> floatSupplier;


    public Controller(Gamepad gamepad) {
        for (int i = 0; i < 12; i++) {
            last_values[i] = false;
        }

        boolSuppliers = new ArrayList<>(
                Arrays.asList(
                          () -> gamepad.a
                        , () -> gamepad.b
                        , () -> gamepad.x
                        , () -> gamepad.y
                        , () -> gamepad.dpad_up
                        , () -> gamepad.dpad_down
                        , () -> gamepad.dpad_left
                        , () -> gamepad.dpad_right
                        , () -> gamepad.left_bumper
                        , () -> gamepad.right_bumper
                        , () -> gamepad.left_stick_button
                        , () -> gamepad.right_stick_button
                )
        );

        floatSupplier = new ArrayList<>(
                Arrays.asList(
                          () -> gamepad.left_stick_y
                        , () -> gamepad.left_stick_x
                        , () -> gamepad.right_stick_y
                        , () -> gamepad.right_stick_x
                        , () -> gamepad.left_trigger
                        , () -> gamepad.right_trigger
                )
        );
    }


    public void onPressed (int id, Command cc) {
        boolean current = boolSuppliers.get(id).get();

        if(current && !last_values[id]){
            cc.run();
        }

        last_values[id] = current;
    }

    public void onReleased (int id, Command cc) {
        boolean current = boolSuppliers.get(id).get();

        if(!current && last_values[id]){
            cc.run();
        }

        last_values[id] = current;
    }

    public void whilePressed (int id, Command cc) {
        boolean current = boolSuppliers.get(id).get();

        if (current) {
            cc.run();
        }

    }

    public void trigger(int id, Function<Float,Command> fcc) {
        float current = floatSupplier.get(id).get();
        fcc.apply(current).run();

    }

    public void stick(int id1, int id2, Function<FloatPair,Command> fcc) {
        FloatPair current = new FloatPair(floatSupplier.get(id1).get(), floatSupplier.get(id2).get());

        fcc.apply(current).run();
    }

    public void stick(int id, Function<Float,Command> fcc) {
        float current = floatSupplier.get(id).get();

        fcc.apply(current).run();
    }

}
