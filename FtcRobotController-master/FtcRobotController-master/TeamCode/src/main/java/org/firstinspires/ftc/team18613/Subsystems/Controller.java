package org.firstinspires.ftc.team18613.Subsystems;


import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Supplier;
import org.firstinspires.ftc.team18613.TeleOpM03;

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
            right_stick_button = 11;

    private final boolean[] last_values = new boolean[12];

    private final ArrayList<Supplier<Boolean>> suppliers;


    public Controller(Gamepad gamepad) {
        for (int i = 0; i < 12; i++) {
            last_values[i] = false;
        }

        suppliers = new ArrayList<>(
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
    }


    public void onPressed(int id, Command cc) {
        boolean current = suppliers.get(id).get();

        if(current && !last_values[id]){
            cc.run();
        }

        last_values[id] = current;
    }

    public void onReleased(int id, Command cc) {
        boolean current = suppliers.get(id).get();

        if(!current && last_values[id]){
            cc.run();
        }

        last_values[id] = current;
    }

    public void whilePressed (int id, Command cc) {
        boolean current = suppliers.get(id).get();

        if (current) {
            cc.run();
        }

    }
}
