package Lex_Analys;

import java.util.HashMap;
import java.util.Map;

public class Idn_Words {
    private int value;
    private Map idn;

    public Idn_Words() {
        value = 1000;
        idn = new HashMap<String, Integer>();
    }

    public boolean search(String key){
        return idn.containsKey(key);
    }

    public int get_index(String key){
        return (int) idn.get(key);
    }

    public int getValue() {
        return value;
    }

    public void update(String key){
        idn.put(key, ++value);
    }
}
