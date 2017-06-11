package Syn_Analys;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Syntax {

    private ArrayList<Integer> row_with_lexem;
    private int counter;                            //индекс в строчке с лексемами
    private FileWriter tree;                        //поток для работы с таблицой идентификаторов для генератора кода
    private String tab;                             //отступ в файле с деревом разбора
    private int ts;                                 //лексема со строки лексем
    private Map<String, Integer> key_words;         //таблица ключевых лексем
    private Map<String, Integer> idn_words;         //таблица с идентификаторами
    private ArrayList<String> proc_idents;          //массив с процедурными идентификаторами
    private ArrayList<String> variable_idents;      //массив с параметрами процедуры
    private HashMap<String, String> type_idents;    //пары идентификаторов и их типов


    public Syntax(ArrayList<Integer> row_with_lexem, Map<String, Integer> key_words, Map<String, Integer> idn_words) throws Syntax_Exeption, IOException {
        tree = new FileWriter(new File("tree.txt"));
        tab = "    ";
        this.row_with_lexem = row_with_lexem;
        this.key_words = key_words;
        this.idn_words = idn_words;
        proc_idents = new ArrayList<>();
        variable_idents = new ArrayList<>();
        type_idents = new HashMap<>();
        counter = 0;
        ts = get_lex();

        Sig_Prog();

        FileWriter identifier = new FileWriter(new File("identifier.txt"));
        for (String key : type_idents.keySet()) {
            identifier.write(String.format("%12s", key));
            identifier.write(String.format("%12s", type_idents.get(key)) + "\n");


        }
        for (String map : proc_idents){
            identifier.write((String.format("%12s", map)) + String.format("%13s", "PROCEDURE\n"));
        }
        tree.close();
        identifier.close();
    }

    private void Sig_Prog() throws Syntax_Exeption, IOException {   //<signal-program>
        tree.write("<signal-program>\n");
        tab += "    ";
        Prog();
        //tab = tab.substring(4);
    }
    private void Prog() throws Syntax_Exeption, IOException {       //<program>
        if (ts != 401)
            Err();
        tree.write(tab + "<program>\n");
        tab += "    ";
        ts = get_lex();
        tree.write(tab + "PROGRAM\n");
        Proc_ident();
        if (ts != 59) Err();
        tree.write(tab + ";\n");
        ts = get_lex();
        Block();
        if (ts != 59) Err();
        tree.write(tab + ";\n");
    }
    private void Block() throws Syntax_Exeption, IOException {      //<block>
        tree.write(tab + "<block>\n");
        tab += "    ";
        Dec_s();
        if (ts != 409)
            Err();
        tree.write(tab + "BEGIN\n");
        tab += "    ";
        ts = get_lex();
        State_list();
        if (ts != 410)
            Err();
        tab = tab.substring(4);
        tree.write(tab + "END\n");
        ts = get_lex();
        tab = tab.substring(4);
    }
    private void State_list() throws IOException, Syntax_Exeption {                  //<statements-list>
        tree.write(tab + "<statements-list>\n");
        tab += "    ";
        State();
        tab = tab.substring(4);
    }
    private void State() throws IOException, Syntax_Exeption {      //<statement>
        if (ts == 410) {
            Empty();
            tab = tab.substring(8);
            return;
        }
        tree.write(tab + "<statement>\n");
        tab += "    ";
        if (ts != 411)
            Err();
        ts = get_lex();
        tree.write(tab + "IF\n");
        Cond();
        if (ts != 412)
            Err();
        ts = get_lex();
        tree.write(tab + "THEN\n");
        Dec_list();
        tab = tab.substring(4);
        State();
    }
    private void Cond() throws IOException, Syntax_Exeption{        //<cond>
        tree.write(tab + "<cond>\n");
        tab += "    ";
        Var_id();
        if (ts != 63)
            Err();
        ts = get_lex();
        tree.write(tab + "?\n");
        Var_id();
        tab = tab.substring(4);
    }

    private void Var_id() throws IOException {                                          //<var-id>
        tree.write(tab + "<var-id>\n");
    }
    private void Dec_s() throws Syntax_Exeption, IOException {      //<declarations>
        tree.write(tab + "<declarations>\n");
        tab += "    ";
        Proc_Dec();
        tab = tab.substring(4);
    }
    private void Proc_Dec() throws Syntax_Exeption, IOException {   //<procedure-declarations>
        if (ts == 409) {
            tab += "    ";
            Empty();
            tab = tab.substring(4);
            return;
        }
        tree.write(tab + "<procedure-declarations>\n");
        tab += "    ";
        Proc();
        Proc_Dec();
        tab = tab.substring(4);

    }
    private void Proc() throws Syntax_Exeption, IOException {       //<procedure>
        tree.write(tab + "<procedure>\n");
        if (ts != 402)
            Err();
        tree.write(tab + "PROCEDURE\n");
        tab += "    ";
        ts = get_lex();
        Proc_ident();
        Param_list();
        if (ts != 59) Err();
        tree.write(tab + ";\n");
        ts = get_lex();
        tab = tab.substring(4);
    }
    private void Param_list() throws Syntax_Exeption, IOException { //<parameters-list>
        if (ts == 59) {
            tab += "    ";
            Empty();
            tab = tab.substring(4);
            return;
        }
        tree.write(tab + "<parameters-list>\n");
        tab += "    ";
        if (ts != 40)
            Err();
        tree.write(tab + "(\n");
        ts = get_lex();
        Dec_list();
        if (ts != 41)
            Err();
        tree.write(tab + ")\n");
        ts = get_lex();
        tab = tab.substring(4);
    }
    private void Dec_list() throws Syntax_Exeption, IOException {
        String stack = Thread.currentThread().getStackTrace()[2].getMethodName();//<declarations-list>
        if (ts == 41 && (stack.equals("Param_list") || stack.equals("Dec_list"))  || ((ts == 410 || ts == 411) && (stack.equals("State") || stack.equals("Dec_list")))) {
            tab += "    ";
            Empty();
            tab = tab.substring(4);
            return;
        }
        tree.write(tab + "<declarations-list>\n");
        tab += "    ";
        Dec();
        tab = tab.substring(4);
        Dec_list();

    }
    private void Dec() throws Syntax_Exeption, IOException {        //<declaration>
        tree.write(tab + "<declaration>\n");
        tab += "    ";
        Varia_ident();
        Ident_list();
        if (ts != 58)
            Err();
        tree.write(tab + ":\n");
        ts = get_lex();
        Atr();
        Atr_list();
        if (variable_idents.size() > 0) {
            String buff = "";
            for (String key : key_words.keySet()) {
                if (Objects.equals(key_words.get(key), row_with_lexem.get(counter - 3))) {
                    buff = key;
                    break;
                }
            }
            while (variable_idents.size() > 0) {
                type_idents.put(variable_idents.get(0), buff);
                variable_idents.remove(0);
            }
        }
        tree.write(tab + ";\n");
        tab = tab.substring(4);
    }
    private void Ident_list() throws Syntax_Exeption, IOException { //<identifiers-list>
        if (ts == 58) {
            Empty();
            return;
        }
        tree.write(tab + "<identifiers-list>\n");
        tab += "    ";
        if (ts != 44)
            Err();
        tree.write(tab + ",\n");
        ts = get_lex();
        Varia_ident();
        Ident_list();
        tab = tab.substring(4);
    }
    private void Atr_list() throws Syntax_Exeption, IOException {   //<attributes-list>
        if (ts == 59) {
            tab += "    ";
            Empty();
            ts = get_lex();
            tab = tab.substring(4);
            return;
        }
        tree.write(tab + "<attribute-list>\n");
        tab += "    ";
        Atr();
        Atr_list();
        tab = tab.substring(4);
    }
    private void Atr() throws Syntax_Exeption, IOException {        //<attribute>
        tree.write(tab + "<attribute>\n");
        tab += "    ";
        if (ts != 403)
            Err();
        ts = get_lex();
        for ( String key: key_words.keySet()) {
            if (key_words.get(key) == ts) {
                tree.write(tab + key + "\n");
                ts = get_lex();
                if (variable_idents.size() > 0){
                    type_idents.put( variable_idents.get(0), key);
                    variable_idents.remove(0);
                }
                tab = tab.substring(4);
                return;
            }
        }
        Err();
    }
    private void Varia_ident() throws Syntax_Exeption, IOException {//<variable-identifier>
        tree.write(tab + "<variable-identifier>\n");
        tab += "    ";
        Ident();
        tab = tab.substring(4);
    }
    private void Proc_ident() throws Syntax_Exeption, IOException { //<procedure-identifier>
        tree.write(tab + "<procedure-identifier>\n");
        tab += "    ";
        Ident();
        tab = tab.substring(4);
    }
    private void Ident() throws Syntax_Exeption, IOException {      //<identidier>
        tree.write(tab + "<identifier>\n");
        tab += "    ";
        boolean flag = false;
        for (String key: idn_words.keySet())
            if (idn_words.get(key) == ts){
                if (Thread.currentThread().getStackTrace()[2].getMethodName().equals("Proc_ident"))
                    proc_idents.add(key);
                else
                    variable_idents.add(key);
                tree.write(tab + "<string>\n");
                tab += "    ";
                tree.write(tab + "'" + key + "'" + "\n");
                Empty();
                tab = tab.substring(4);
                flag = true;
                break;
            }
        if (!flag)
            Err();
        ts = get_lex();
        tab = tab.substring(4);
    }

    private void Empty() throws IOException {
        tree.write(tab + "<empty>\n");
    }
    private void Err() throws Syntax_Exeption, IOException {        //Error
        tree.write(tab + "АЛЯРМА!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        tree.close();
        throw new Syntax_Exeption("Виникла помилка");
    }

    private int get_lex(){                                          //слудующая лексема
        return row_with_lexem.get(counter++);
    }

    public class Syntax_Exeption extends Exception{                 //синтаксическая ошибка
        Syntax_Exeption(String message) {
            super(message);
        }
    }
}
