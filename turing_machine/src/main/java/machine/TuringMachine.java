package machine;

import parser.Data;

import java.util.HashMap;

public class TuringMachine {
    Character[] tape = new Character[500];
    int head = 0;
    int state;

    protected HashMap<InstructionKey, InstructionValue> updateFunction;

    /*
     * @throws FileNotFoundException if the file with the tm instructions could not be found
     * @throws RuntimeException if there is an error parsing instructions, or if an instruction is not fond
     * */
    public TuringMachine(
            int initialState,
            String input,
            String path
    ) throws Exception {
        updateFunction = Data.loadYaml(path);

        // Fill the tape
        for (int i = 0; i < input.length(); i++) {
            tape[i] = input.charAt(i);
        }

        // Set the initial state
        this.state = initialState;
    }


    public void run() {
        step();
        display();

        if (state != -1) run();
    }

    void move(Movement m) {
        if (m == Movement.Right) head++;
        else if (m == Movement.Left) head--;

        if (head >= tape.length || head <= 0) extendTape();
    }

    void extendTape() {
        head += 10;
        Character[] newTape = new Character[tape.length + 20];
        System.arraycopy(tape, 0, newTape, 10, tape.length);
        tape = newTape;
    }

    void step() {
        // Read the content
        Character content = tape[head];

        // Generate the key
        InstructionKey k = new InstructionKey(state, content);
        InstructionValue v = updateFunction.get(k);

        if (v == null) {
            throw new RuntimeException("No instruction found for (content: " + content + ", state: " + state + ")");
        }

        // Update the turing machine using the instructions
        tape[head] = v.mutate;
        state = v.newState;
        move(v.movement);
    }

    void display() {
        int diff = 10;
        int len = tape.length;

        for (int i = head - diff; i <= head + diff; i++) {
            if (i >= 0 && i < len) {
                Character tapeStr;
                if (tape[i] == null) tapeStr = 'B';
                else tapeStr = tape[i];
                System.out.print(tapeStr + " | ");
            }
        }

        System.out.println();
        System.out.println(" ".repeat(Math.min(diff, head) * 4) + "^");
        System.out.println("State: " + state + "\n\n\n");
    }
}

