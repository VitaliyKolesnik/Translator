package Lex_Analys;

import java.util.HashMap;
import java.util.Map;

public class Key_Words {
    private Map key_word;

    public Key_Words() {
        key_word = new HashMap<String, Integer>();
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

    public boolean search(String key){
        return key_word.containsKey(key);
    }

    public int get_index(String key) {
        return (int) key_word.get(key);
    }
}
