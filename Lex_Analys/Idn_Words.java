package Lex_Analys;

import java.util.HashMap;
import java.util.Map;

public class Idn_Words {
    private static int value = 1000;
    private static Map idn;
    static {
        idn = new HashMap<String, Integer>();
    }

    public static boolean search(String key){
        return idn.containsKey(key);
    }

    public static int get_index(String key){
        return (int) idn.get(key);
    }

    public static int getValue() {
        return value;
    }

    public static void update(String key){
        idn.put(key, ++value);
    }
}
