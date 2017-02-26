package Lex_Analys;


import java.util.ArrayList;

public class TabForm {

    static int id = 0;
    static ArrayList<String[]> table;
    static int id_zona;

    static {
        table = new ArrayList<>();
    }

    public void form(String name, int line, int column){
        table.add(new String[4]);
        table.get(0)[0] = name;
        table.get(0)[1] = String.valueOf(id + id_zona);
        table.get(0)[2] = String.valueOf(line);
        table.get(0)[3] = String.valueOf(column);
    }
}