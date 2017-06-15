import Code_Generator.Code_Generation;
import Lex_Analys.Lexical;
import Syn_Analys.Syntax;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, Syntax.Syntax_Exeption, Code_Generation.Syntax_Exeption {
        Lexical lexical = new Lexical("program.txt","result.txt");
        Syntax syntax = new Syntax(lexical);
        Code_Generation code = new Code_Generation(syntax, lexical);
    }

}