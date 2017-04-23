package Lex_Analys;

import java.io.*;

public class Analysyer {

    private File file;
    private InputStream in;
    private int ch;
    private String buffer;
    private int lex_code;
    private boolean suppress_output;
    private OutputStream out;

    public Analysyer(File file) throws IOException {
        this.file = file;
        read();
    }

    public void read() throws IOException {
        in = new FileInputStream(file);

        while (in.available() > 0){
            ch = (byte) in.read();
            buffer = "";
            suppress_output = false;
            lex_code =0;
            switch (gets(ch)){
                case 0 :{
                    while (in.available() > 0){
                        ch = in.read();
                        if (gets(ch) != 0)
                            break;
                    }
                    suppress_output = true;
                }
                    break;
                case 1 :{
                    while (in.available() > 0 && (gets(ch) == 2 || gets(ch) == 1)){
                        buffer += (char) ch;
                        ch = in.read();
                    }
                    if (Key_Words.search(buffer))
                        lex_code = Key_Words.get_index(buffer);
                    else
                        if (Idn_Words.search(buffer))
                            lex_code = Idn_Words.get_index(buffer);
                        else {
                            lex_code = Idn_Words.getValue() + 1;
                            Idn_Words.update(buffer);
                        }
                }
                break;
                case 2 :{

                }
            }
        }

    }

    public static byte gets(int ch){
        if (ch == 32) return 0; //whitespace
        else
        if (ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122) return 1; //lit
        else
        if (ch == 40) return 2; //comment maybe
        else
        if (ch == 59 || ch == 41 || ch == 44) return 2; // dm
        else
        if (ch >= 0 && ch <= 9) return 3; //dig
        else return 4;
    }
}
