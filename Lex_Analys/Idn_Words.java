package Lex_Analys;

import java.util.HashMap;
import java.util.Map;

class Idn_Words {
    private int value;
    private Map<String, Integer> idn;

    Idn_Words() {
        value = 1000;
        idn = new HashMap<>();
    }

    boolean search(String key){
        return idn.containsKey(key);
    }

    int get_index(String key){
        return idn.get(key);
    }

    int getValue() {
        return value;
    }

    void update(String key){
        idn.put(key, ++value);
    }
}
