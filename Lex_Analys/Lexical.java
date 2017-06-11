package Lex_Analys;

import java.io.*;
import java.util.ArrayList;

public class Lexical {

    private File in_file;
    private File out_file;
    private Key_Words key_words;
    private Idn_Words idn_words;
    private FileWriter out;
    private FileInputStream in;
    private ArrayList<Integer> row_with_lexem;



    private int row;
    private int column;
    private int ch;

    public Lexical(String in_file, String out_file) throws IOException {
        this.in_file = new File(in_file);
        this.out_file = new File(out_file);
        key_words = new Key_Words();
        idn_words = new Idn_Words();
        row_with_lexem = new ArrayList<>();
        parsing();
    }

    private void parsing() throws IOException {
        in = new FileInputStream(in_file);
        out = new FileWriter(out_file);
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

                    row_with_lexem.add(lex_code);
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
                            row_with_lexem.add((int) '(');
                            output("(", (int) '(');
                            column++;
                            lex_code = 40;
                    }
                }
                break;

                case 3: {      //delimiter
                    lex_code = ch;
                    row_with_lexem.add(ch);
                    output(String.valueOf((char) ch), ch);
                    if (in.available() > 0) ch = in.read();
                    column++;
                    if (ch == 13) {
                        ++row;
                        column = 1;
                    }
                }
                break;

                case 5:{        //format number +38(099)123-45-67
                    buffer += (char) ch;
                    if (in.available() > 0) {
                        ch = in.read();
                        for (int i = 0; i < 2; i++) {
                            is_whitespace();
                            if (gets(ch) != 4) break;
                            buffer += (char) ch;
                            ch = in.read();
                        }
                        is_whitespace();
                        if (ch == 40)
                            buffer += (char) ch;
                        else
                            break;
                        ch = in.read();
                        for (int i = 0; i < 3; i++) {
                            is_whitespace();
                            if (gets(ch) != 4) break;
                            buffer += (char) ch;
                            ch = in.read();
                        }
                        is_whitespace();
                        if (ch == 41)
                            buffer += (char) ch;
                        else
                            break;
                        ch = in.read();
                        for (int i = 0; i < 3; i++) {
                            is_whitespace();
                            if (gets(ch) != 4) break;
                            buffer += (char) ch;
                            ch = in.read();
                        }
                        is_whitespace();
                        if (ch == 45)
                            buffer += (char) ch;
                        else
                            break;
                        ch = in.read();
                        for (int i = 0; i < 2; i++) {
                            is_whitespace();
                            if (gets(ch) != 4) break;
                            buffer += (char) ch;
                            ch = in.read();
                        }
                        is_whitespace();
                        if (ch == 45)
                            buffer += (char) ch;
                        else
                            break;
                        ch = in.read();
                        for (int i = 0; i < 2; i++) {
                            is_whitespace();
                            if (gets(ch) != 4) break;
                            buffer += (char) ch;
                            ch = in.read();
                        }
                        if (idn_words.search(buffer))
                            lex_code = idn_words.get_index(buffer);
                        else {
                            lex_code = idn_words.getValue() + 1;
                            idn_words.update(buffer);
                        }
                        row_with_lexem.add(lex_code);
                        output(buffer, lex_code);
                        column++;
                        if (ch == 13) {
                            ++row;
                            column = 1;
                        }
                    }
                    else
                        break;

                }
                break;

                default: {     //Error
                    System.out.print("Illegal symbol ");
                    suppress_output = true;
                    if (in.available() > 0) ch = in.read();
                }
                    break;
            }

            if (!suppress_output)
                System.out.print(lex_code + " ");
            if (in.available() == 0) {
                row_with_lexem.add(ch);
                output(String.valueOf((char) ch), ch);
                System.out.println(ch + " ");
            }
        } while (in.available() > 0);

        in.close();
        out.close();
    }

    public static byte gets(int ch){
        if (ch == 32 || ch == 13 || ch == 10) return 0; //whitespace or new line or new page
        else if (ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122) return 1; //lit
        else if (ch == 40) return 2; //comment maybe
        else if (ch == 46 || ch == 58 || ch == 59 || ch == 41 || ch == 44 || ch == 63) return 3; //dm
        else if (ch >= 48 && ch <= 57) return 4; //dig
        else if (ch == 43) return 5; //+
        else return 6;
    }

    private void output(String lex, int lex_code) throws IOException {
        out.write(String.format("%12d", lex_code));
        out.write(String.format("%12d", row));
        out.write(String.format("%12d", column));
        out.write(String.format("%19s", lex + "\n"));

    }

    private void is_whitespace() throws IOException {
        while (ch == 32)
            ch = in.read();

    }

    public Idn_Words getIdn_words() {
        return idn_words;
    }

    public Key_Words getKey_words() {

        return key_words;
    }

    public ArrayList<Integer> getRow_with_lexem() {
        return row_with_lexem;
    }
}