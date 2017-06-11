package Code_Generator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Code_Generation {
    private FileWriter code;
    private BufferedReader tree;
    private List<String> list;

    public Code_Generation( String tree, String code) throws IOException {
        this.tree = new BufferedReader(new FileReader(new File(tree)));
        this.code = new FileWriter(new File(code));
        list = new ArrayList<>();
        generate();
    }

    private void generate() throws IOException {

        String buf;
        while (tree.ready()){
            buf = tree.readLine();
            list.add(buf.substring(buf.lastIndexOf(" ") + 1));
        }
    }
}
