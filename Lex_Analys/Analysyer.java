package Lex_Analys;

import java.io.*;

public class Analysyer {

    private File in_file;
    private File out_file;
    private Key_Words key_words;
    private Idn_Words idn_words;
    private FileWriter out;
    private int row;
    private int column;

    public Analysyer(File in_file, File out_file) throws IOException {
        this.in_file = in_file;
        this.out_file = out_file;
        key_words = new Key_Words();
        idn_words = new Idn_Words();
        parsing();
    }

    private void parsing() throws IOException {
        FileInputStream in = new FileInputStream(in_file);
        out = new FileWriter(out_file);
        int ch;
        String buffer;
        int lex_code;
        boolean suppress_output;
        row = 1;
        column = 1;
        if (in.available() == 0)
            System.out.println("Empty file");
            ch =in.read();
        do {
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
                    while (in.available() > 0 && (gets(ch) == 4 || gets(ch) == 1) && ch != 13) {
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
                    output(buffer, lex_code);
                    column++;
                    if (ch == 13) {
                        ++row;
                        column = 1;
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
                                if (ch == 41)
                                    suppress_output = true;
                            }
                        } else
                            output("(", (int) '(');
                            column++;
                            lex_code = 40;
                    }
                }
                break;

                case 3: {      //delimiter
                    lex_code = ch;
                    output(String.valueOf((char) ch), ch);
                    if (in.available() > 0) ch = in.read();
                    column++;
                    if (ch == 13) {
                        ++row;
                        column = 1;
                    }
                }
                break;
                default: {     //Error
                    //System.out.print("Illegal symbol ");
                    if (in.available() > 0) ch = in.read();
                }
                    break;
            }

            if (!suppress_output)
                System.out.print(lex_code + " ");
            if (in.available() == 0) {
                output(String.valueOf((char) ch), ch);
                System.out.print(ch + " ");
            }
        } while (in.available() > 0);

        in.close();
        out.close();
    }

    private byte gets(int ch){
        if (ch == 32 || ch == 13 || ch == 10) return 0; //whitespace
        else if (ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122) return 1; //lit
        else if (ch == 40) return 2; //comment maybe
        else if (ch == 46 || ch == 58 || ch == 59 || ch == 41 || ch == 44) return 3; //dm
        else if (ch >= 48 && ch <= 57) return 4; //dig
        else return 5; //err
    }

    private void output(String lex, int lex_code) throws IOException {
        out.write(String.format("%12d", lex_code));
        out.write(String.format("%12d", row));
        out.write(String.format("%12d", column));
        out.write(String.format("%12s", lex));
        out.write("\n");

    }
}
