package Lex_Analys;

import sun.misc.ASCIICaseInsensitiveComparator;

import java.io.*;
import java.util.ArrayList;

public class Analysyer {

    private File file;
    private InputStream in;
    private int ch;
    private String buffer;
    private int lex_code;
    private boolean suppress_output;
    private OutputStream out;
    private int[] lit;
    private int[] dig;
    private int[] dm;
    private int[] err;
    private int eof;




    public Analysyer(File file) throws IOException {
        this.file = file;
        /*lit = new int[52];      //{A..Z, a..z}  ASCII{65..90, 97..122}
        dig = new int[10];      //{0..9}        ASCII{48..57}
        dm = new int[]{':', ';', '<', '=', '>', '-', '|', ',', '(', ')'}; //ASCII
        err = new int[32];      //              ASCII{0..32}
        eof = -1;

        for (int i = 0; i < 26; i++) {
            lit[i] = 65 + i;
            lit[i + 26] = 97 + i;
        }

        for (int i = 0; i < 10; i++) {
            dig[i] = 48 + i;
        }

        for (int i = 0; i < 31; i++) {
            err[i] = i;
        }
        */
        read();
    }

    public void read() throws IOException {
        in = new FileInputStream(file);

        while (in.available() > 0){
            ch = (byte) in.read();
            buffer = "";
            suppress_output = false;
            lex_code =0;
            switch (Tables.gets(ch)){
                case 0 :{
                    while (in.available() > 0){
                        ch = in.read();
                        if (Tables.gets(ch) != 0)
                            break;
                    }
                    suppress_output = true;
                }
                    break;
                case 1 :{
                    while (in.available() > 0 && (Tables.gets(ch) == 2 || Tables.gets(ch) == 1)){
                        buffer += (char) ch;
                        ch = in.read();
                    }
                    if (Tables.key_tab_search(buffer))
                        lex_code = Tables.get_key_word(buffer);
                    else{
                        lex_code =
                    }
                }
            }
        }
        /*byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        for (int i = 0; i < buffer.length; i++) {

        }

        for (int i = 0; i < buffer.length; i++) {
            System.out.println(buffer[i]);
        }*/
    }
}
