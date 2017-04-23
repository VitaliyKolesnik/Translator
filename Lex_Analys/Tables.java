package Lex_Analys;

import java.util.Map;

public class Tables {

    private static Map<String, Integer> key_word;
    static{
        key_word.put("PROGRAM", 401);
        key_word.put("PROCEDURE", 402);
        key_word.put("SIGNAL", 403);
        key_word.put("COMPLEX", 404);
        key_word.put("INTEGER", 405);
        key_word.put("FLOAT", 406);
        key_word.put("BLOCKFLOAT", 407);
        key_word.put("EXT", 408);
        key_word.put("BEGIN", 409);
        key_word.put("END", 410);
    }

    public static byte gets(int ch){
        if (ch == 32) return 0; //whitespace
        else
            if (ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122) return 1; //lit
            else
                if (ch >= 0 && ch <= 9) return 2; //dig
                else
                    if (ch == 59 || ch == 41 || ch == 44) return 2; // dm
                        else
                            if (ch == 40) return 3; //comment maybe
                        else return 4;
    }

    public static boolean key_tab_search(String key){
        return key_word.containsKey(key);
    }

    public static int get_key_word(String key) {
        return key_word.get(key);
    }
}
