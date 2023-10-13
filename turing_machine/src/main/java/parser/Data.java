package parser;


import machine.InstructionKey;
import machine.InstructionValue;
import machine.Movement;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Data {

    public static HashMap<InstructionKey, InstructionValue> loadYaml(String path) throws Exception {
        InputStream inputStream = new FileInputStream(path);
        Yaml yaml = new Yaml();


        Map<String, Map<String, Object>> data = yaml.load(inputStream);

        HashMap<InstructionKey, InstructionValue> map = new HashMap<>();

        data.forEach((k, v) -> {
            // Make the InstructionKey
            InstructionKey ik = keyStringToInstructionKey(k);
            InstructionValue iv = null;

            try {
                iv = valueMapToInstructionValue(ik, v);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            map.put(ik, iv);
        });

        return map;
    }

    private static InstructionKey keyStringToInstructionKey(String str) {
        Character content;
        int state;

        String[] keys = str
                .substring(1, str.length() - 1)
                .replace(" ", "")
                .split(",");

        if (Objects.equals(keys[0], "null")) content = null;
        else content = keys[0].charAt(0);

        state = Integer.parseInt(keys[1]);

        return new InstructionKey(state, content);
    }

    private static InstructionValue valueMapToInstructionValue(
            InstructionKey key,
            Map<String, Object> h
    ) throws Exception {
        Character mutate;
        if (h.containsKey("mutate")) {
            String mutateOrNull = (String) h.get("mutate");
            if (Objects.equals(mutateOrNull, "null")) mutate = null;
            else mutate = mutateOrNull.charAt(0);
        } else mutate = key.content;

        int state;
        if (h.containsKey("newState")) state = (int) h.get("newState");
        else state = key.state;

        String strMovement = ((String) h.get("move")).toLowerCase();
        Movement movement;

        if (strMovement.charAt(0) == 'r') movement = Movement.Right;
        else if (strMovement.charAt(0) == 'l') movement = Movement.Left;
        else if (strMovement.equals("halt")) movement = Movement.Halt;
        else throw new RuntimeException("Could not parse movement " + strMovement);

        return new InstructionValue(mutate, state, movement);
    }
}

