package Lex_Analys;

import java.io.*;

public class Analysyer {

    private File file;
    private Key_Words key_words;
    private Idn_Words idn_words;
    private InputStream in;
    private int ch;
    private String buffer;
    private int lex_code;
    private boolean suppress_output;
    private OutputStream out;

    public Analysyer(File file) throws IOException {
        this.file = file;
        key_words = new Key_Words();
        idn_words = new Idn_Words();
        parsing();
    }

    public void parsing() throws IOException {
        in = new FileInputStream(file);

        if (in.available() == 0)
            System.out.println("Empty file");
        do {
            ch =in.read();
            buffer = "";
            suppress_output = false;
            lex_code =0;
            switch (gets(ch)) {
                case 0: {   //whitespace
                    while (in.available() > 0) {
                        ch = in.read();
                        if (gets(ch) != 0)
                            break;
                    }
                    suppress_output = true;
                }
                break;

                case 1: {   //identifier
                    while (in.available() > 0 && (gets(ch) == 4 || gets(ch) == 1)) {
                        buffer += (char) ch;
                        ch = in.read();
                    }
                    if (key_words.search(buffer))
                        lex_code = key_words.get_index(buffer);
                    else if (idn_words.search(buffer))
                        lex_code = idn_words.get_index(buffer);
                    else {
                        lex_code = idn_words.getValue() + 1;
                        idn_words.update(buffer);
                    }
                }
                break;

                case 2:{    //comment
                    if (in.available() == 0)
                        lex_code = 40;
                    else {
                        ch = in.read();
                        if (ch == 42) {
                            if (in.available() == 0)
                                System.out.print("(* expected but end of file found");
                            else {
                                ch = in.read();
                                do {
                                    while (in.available() > 0 && (ch != 42))
                                        ch = in.read();
                                    if (in.available() == 0) {
                                        System.out.print("*) expected but end of file found");
                                        ch = 43;
                                        break;
                                    } else
                                        ch = in.read();
                                } while (ch != 41);
                                if (ch == 42)
                                    suppress_output = true;
                                if (in.available() > 0)
                                    ch = in.read();
                            }
                        } else
                            lex_code = 40;
                    }
                }
                break;

                case 3:     //delimiter
                    lex_code = ch;
                break;
                case 5:     //Error
                    System.out.print("Illegal symbol ");
                    break;
            }
            if (!suppress_output)
                System.out.print(lex_code + " ");
        } while (in.available() > 0);
    }

    public static byte gets(int ch){
        if (ch == 32) return 0; //whitespace
        else if (ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122) return 1; //lit
        else if (ch == 40) return 2; //comment maybe
        else if (ch == 59 || ch == 41 || ch == 44) return 3; //dm
        else if (ch >= 48 && ch <= 57) return 4; //dig
        else return 5; //err
    }
}
