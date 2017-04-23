import Lex_Analys.Analysyer;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new Analysyer(new File("program.txt"), new File("result.txt"));
    }

}
