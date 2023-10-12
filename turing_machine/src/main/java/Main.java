import parser.Data;

public class Main {
    public static void main(String[] args) {
        Data d = new Data("main/java/parser/list.yml");
        d.loadYaml();
    }
}
