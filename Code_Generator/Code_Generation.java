package Code_Generator;

import Lex_Analys.Idn_Words;
import Lex_Analys.Key_Words;
import Lex_Analys.Lexical;
import Syn_Analys.Syntax;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Code_Generation {
    private FileWriter code;
    private BufferedReader tree;
    private List<String> list;
    private Idn_Words idn_words;
    private Key_Words key_words;
    private Map<String, String> type_idents;
    private ArrayList<String> proc_idents;

    public Code_Generation( Syntax syntax, Lexical lexical) throws IOException {
        this.tree = new BufferedReader(new FileReader(new File("tree.txt")));
        this.code = new FileWriter(new File("result_code.txt"));
        list = new ArrayList<>();
        this.key_words = lexical.getKey_words();
        this.idn_words = lexical.getIdn_words();
        this.type_idents = syntax.getType_idents();
        this.proc_idents = syntax.getProc_idents();
        generate();
        this.tree.close();
        this.code.close();
    }

    private void generate() throws IOException {

        String buf;
        while (tree.ready()){
            buf = tree.readLine();
            list.add(buf.substring(buf.lastIndexOf(" ") + 1));
        }

        int i = 0;
        while (i != list.size()){
            if (Lexical.gets(list.get(i).charAt(0)) != 1)
                list.remove(i);
            else
                i++;
        }
        for (String str: list) {
            System.out.println(str);
        }
        i = 0;
        String buffer = "";
        while (true){
            buffer = list.get(i);
            if (buffer.equals("PROGRAM")) {
                code.write(";" + list.get(i + 1) + ".asm\ndata segment\n");
                i++;
            }
            else if (buffer.equals("BEGIN"))
                code.write("data ends\ncode segment\nassume cs:code ds:dats\norg 100h\nbegin:\n");
            else if (buffer.equals("END")) {
                code.write("code ends\nend begin\n");
                break;
            }
            else if (key_words.search(buffer)){}
            else {
                for (String key :type_idents.keySet()) {
                    if (buffer.equals(key)) {
                        code.write(buffer + " " + "equ" + type_idents.get(buffer) + "\n");
                        break;
                    }
                }
                for (String str: proc_idents){
                    if (buffer.equals(str))
                        code.write(buffer + " " + "equ" + "procedure\n");
                }
            }
            i++;
        }
    }
}
