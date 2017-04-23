package Lex_Analys;

import java.util.Map;

public class Idn_Words {
    private static int value = 1000;
    private static Map<String, Integer> idn;

    public static boolean search(String key){
        return idn.containsKey(key);
    }

    public static int get_index(String key){
        return idn.get(key);
    }

    public static int getValue() {
        return value;
    }

    public static void update(String key){
        idn.put(key, ++value);
    }
}
