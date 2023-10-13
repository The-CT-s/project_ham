import machine.TuringMachine;
import parser.Data;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println(Arrays.toString(args));
            System.out.println(
                    "Expected 3 parameters: <inital_state> <input_string> <instructions_file>"
            );

            return;
        }

        TuringMachine add = new TuringMachine(
                Integer.parseInt(args[0]),
                args[1],
                args[2]
        );

        add.run();
    }
}
