package Code_Generator;

import Lex_Analys.Idn_Words;
import Lex_Analys.Key_Words;
import Lex_Analys.Lexical;
import Syn_Analys.Syntax;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Code_Generation {
    private FileWriter code;
    private BufferedReader tree;
    private List<String> list;
    private Idn_Words idn_words;
    private Key_Words key_words;
    private HashMap<String, String> type_idents;
    private ArrayList<String> proc_idents;
    private ArrayList<String> buffer_proc;
    private ArrayList<String> buffer_idn;
    private HashMap<String, ArrayList<Integer>> table;

    public Code_Generation( Syntax syntax, Lexical lexical) throws IOException, Syntax_Exeption {
        this.tree = new BufferedReader(new FileReader(new File("tree.txt")));
        this.code = new FileWriter(new File("result_code.txt"));
        list = new ArrayList<>();
        this.key_words = lexical.getKey_words();
        this.idn_words = lexical.getIdn_words();
        this.table = lexical.getTable();
        this.type_idents = syntax.getType_idents();
        this.proc_idents = syntax.getProc_idents();
        buffer_idn = new ArrayList<>();
        buffer_proc = new ArrayList<>();
        generate();
        this.tree.close();
        this.code.close();
    }

    private void generate() throws IOException, Syntax_Exeption {

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
                if (buffer_proc.contains(buffer))
                    Err(buffer);
                code.write(";" + list.get(i + 1) + ".asm\ndata segment\n");
                buffer_proc.add(buffer);
                i++;
            }
            else if (buffer.equals("BEGIN"))
                code.write("data ends\ncode segment\nassume cs:code ds:dats\norg 100h\nbegin:\n");
            else if (buffer.equals("END")) {
                code.write("end begin\ncode ends\n");
                break;
            }
            else if (buffer.equals("PROCEDURE")){}
            else if (key_words.search(buffer)){}
            else if (proc_idents.contains(buffer)){
                if (buffer_proc.contains(buffer))
                    Err(buffer);
                buffer_proc.add(buffer);
            }
            else {
                String s = type_idents.get(buffer);
                if (buffer_idn.contains(buffer))
                    Err(buffer);
                if (s.equals("COMPLEX")) {
                    code.write("DQ" + buffer);

                } else if (s.equals("INTEGER")) {
                    code.write("DW " + buffer + "\n");

                } else if (s.equals("FLOAT")) {
                    code.write("DD " + buffer + "\n");

                } else if (s.equals("BLOCKFLOAT")) {
                    code.write("DW " + buffer + "\n");

                } else if (s.equals("EXT")) {
                    code.write("DD " + buffer + "\n");

                }
                buffer_idn.add(buffer);
            }
            i++;
        }
    }

    private void Err(String buffer) throws Syntax_Exeption, IOException {        //Error
        boolean flag = true;
        if (table.containsKey(buffer)) {
            code.write("Дублювання ідентифікатора " + buffer + "\nПерше оголошення в " + table.get(buffer).get(0) + "-му рядку\n");
            code.write("Безпосереднє дублювання в " + table.get(buffer).get(1) + "-му рядку");
        }

        code.close();
        throw new Syntax_Exeption("Дублювання ідентифікатора");
    }

    public class Syntax_Exeption extends Exception{                 //синтаксическая ошибка
        Syntax_Exeption(String message) {
            super(message);
        }
    }
}
