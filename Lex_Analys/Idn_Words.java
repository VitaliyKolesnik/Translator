package Lex_Analys;

import java.util.HashMap;
import java.util.Map;

public class Idn_Words {
    private int value;
    private Map<String, Integer> idn;

    Idn_Words() {
        value = 1000;
        idn = new HashMap<>();
    }

    public boolean search(String key){
        return idn.containsKey(key);
    }

    public int get_index(String key){
        return idn.get(key);
    }

    public int getValue() {
        return value;
    }

    void update(String key){
        idn.put(key, ++value);
    }

    public Map<String, Integer> getIdn() {
        return idn;
    }
}