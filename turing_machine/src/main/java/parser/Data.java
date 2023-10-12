package parser;

import machine.InstructionValue;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;

public class Data {
    String path;

    public Data(String path) {
        this.path = path;
    }

    public Instruction[] loadYaml() {
        Yaml yaml = new Yaml();

        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(path);

        List<String> data = yaml.load(inputStream);
        System.out.println(data);

        return new Instruction[]{};
    }
}

class Instruction {
    machine.InstructionKey key;
    machine.InstructionValue value;
}