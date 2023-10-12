package machine;

import java.util.HashMap;

public class TuringMachine {
    Character[] tape = new Character[500];
    int head = 0;
    int state = 0;

    protected HashMap<InstructionKey, InstructionValue> updateFunction;

    public TuringMachine(
            int initialState,
            HashMap<InstructionKey, InstructionValue> instructionMap,
            String input
    ) {
        // Fill the tape
        for (int i = 0; i < input.length(); i ++) {
            tape[i] = input.charAt(i);
        }

        this.state = initialState;
        this.updateFunction = instructionMap;
    }

    boolean run() {
        while (true) {
            step();
            display();
            try {
                Thread.sleep(2000);
            } catch (Exception ignore) {}
        }
    }

    void move(Movement m) {
        if (m == Movement.Right) head++;
        else if (m == Movement.Left) head--;

        if (head >= tape.length || head <= 0) extendTape();
    }

    void extendTape() {
        head += 10;
        Character[] newTape = new Character[tape.length + 10];
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
            System.out.println("No value found for (" + state + ", " + content + ")");
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
            if (i >= 0 && i < len) System.out.print(tape[i] + " | ");
        }

        System.out.println();
        System.out.println(" ".repeat(Math.min(diff, head) * 4) + "^");
        System.out.println("(State: " + state + ", head: " + head + ")");
    }
}

