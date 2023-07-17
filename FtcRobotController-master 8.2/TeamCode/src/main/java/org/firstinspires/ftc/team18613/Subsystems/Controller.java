package org.firstinspires.ftc.team18613.Subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.team18613.Command;
import org.firstinspires.ftc.team18613.Utils.Function;
import org.firstinspires.ftc.team18613.Utils.Pair;
import org.firstinspires.ftc.team18613.Utils.Supplier;
import org.firstinspires.ftc.team18613.Utils.SupplierPair;

import java.util.ArrayList;
import java.util.Arrays;

public class Controller {

    public static final int
              a = 0
            , b = 1
            , x = 2
            , y = 3
            , up = 4
            , down = 5
            , left = 6
            , right = 7
            , left_bumper = 8
            , right_bumper = 9
            , left_stick_button = 10
            , right_stick_button = 11
            , left_stick_y_not_zero = 12
            , left_stick_x_not_zero = 13
            , right_stick_y_not_zero = 14
            , right_stick_x_not_zero = 15
            , left_trigger_not_zero = 16
            , right_trigger_not_zero = 17
            , left_stick_y = 0
            , left_stick_x = 1
            , right_stick_y = 2
            , right_stick_x = 3
            , left_trigger = 4
            , right_trigger = 5;

    private final ArrayList<Pair<Command, Integer>> onPressedRegisters = new ArrayList<>();
    private final ArrayList<Pair<Command, Integer>> onReleasedRegisters = new ArrayList<>();
    private final ArrayList<Pair<Command, Integer>> whilePressedRegisters = new ArrayList<>();
    private final ArrayList<Pair<Function<Supplier<Float>, Command>, Integer>>
                                                    stickAndTriggerRegisters = new ArrayList<>();
    private final ArrayList<Pair<Function<SupplierPair<Float>,Command>,
                                                        int[]>> sticksRegisters = new ArrayList<>();


    private final boolean[] last_values = new boolean[18];

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
                        , () -> gamepad.left_stick_y != 0
                        , () -> gamepad.left_stick_x != 0
                        , () -> gamepad.right_stick_y != 0
                        , () -> gamepad.right_stick_x != 0
                        , () -> gamepad.left_trigger != 0
                        , () -> gamepad.right_trigger != 0
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

    public void registerAction (int id, Command cc, Actions actions) {

        switch (actions){
            case ON_PRESSED:
                onPressedRegisters.add(new Pair<>(cc, id));
                break;
            case ON_RELEASED:
                onReleasedRegisters.add(new Pair<>(cc, id));
                break;
            case WHILE_PRESSED:
                whilePressedRegisters.add(new Pair<>(cc, id));
                break;
        }
    }

    public void registerAction (int[] ids, Function<SupplierPair<Float>,Command> fcc) {
        sticksRegisters.add(new Pair<>(fcc, ids));
    }

    public void registerAction (Integer ids, Function<Supplier<Float>,Command> fcc) {
        stickAndTriggerRegisters.add(new Pair<>(fcc, ids));
    }

    private void updateOnPressed() {

        for (Pair<Command, Integer> i : onPressedRegisters) {
            int id = i.secondValue();
            Command cc = i.firstValue();

            boolean current = boolSuppliers.get(id).get();

            if (current && !last_values[id]) {
                cc.run();
            }

            last_values[id] = current;
        }

    }

    private void updateOnRelease() {

        for (Pair<Command, Integer> i : onReleasedRegisters) {
            int id = i.secondValue();
            Command cc = i.firstValue();

            boolean current = boolSuppliers.get(id).get();

            if(!current && last_values[id]){
                cc.run();
            }

            last_values[id] = current;
        }
    }

    private void updateWhilePressed() {

        for (Pair<Command, Integer> i : whilePressedRegisters) {
            int id = i.secondValue();
            Command cc = i.firstValue();

            boolean current = boolSuppliers.get(id).get();

            if (current) {
                cc.run();
            }
        }
    }

    private void updateStickAndTrigger() {
        for (Pair<Function<Supplier<Float>,Command>,Integer> i : stickAndTriggerRegisters) {
            Function<Supplier<Float>,Command> fcc = i.firstValue();
            int id = i.secondValue();

            Supplier<Float> current = floatSupplier.get(id);

            fcc.apply(current).run();
        }
    }

    private void updateSticks() {
        for (Pair<Function<SupplierPair<Float>,Command>, int[]> i : sticksRegisters) {
            Function<SupplierPair<Float>,Command> fcc = i.firstValue();
            int id0 = i.secondValue()[0], id1 = i.secondValue()[1];

            SupplierPair<Float> current = new SupplierPair<>(floatSupplier.get(id0),
                                                                            floatSupplier.get(id1));

            fcc.apply(current).run();
        }
    }

    public enum Actions {
        ON_PRESSED
        , ON_RELEASED
        , WHILE_PRESSED
    }

    public void updateCommands(){
        if (onPressedRegisters.size() > 0) {
            updateOnPressed();
        }
        if (onReleasedRegisters.size() > 0) {
            updateOnRelease();
        }
        if (whilePressedRegisters.size() > 0) {
            updateWhilePressed();
        }
        if (stickAndTriggerRegisters.size() > 0) {
            updateStickAndTrigger();
        }
        if (sticksRegisters.size() > 0) {
            updateSticks();

        }
    }

}
