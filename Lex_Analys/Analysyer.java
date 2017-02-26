package Lex_Analys;

import java.io.*;
import java.util.ArrayList;

public class Analysyer {

    private File file;
    private InputStream in;
    private OutputStream out;
    private int[] lit;
    private int[] dig;
    private int[] dm;
    private int[] err;
    private int eof;




    public Analysyer(File file) throws IOException {
        this.file = file;
        lit = new int[52];      //{A..Z, a..z}  ASCII{65..90, 97..122}
        dig = new int[10];      //{0..9}        ASCII{48..57}
        dm = new int[]{':', ';', '<', '=', '>', '-', '|', ',', '(', ')'};
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

        read();
    }

    void read() throws IOException {
        in = new FileInputStream(file);
        int symlol = in.read();

        while (symlol != eof) {
            switch (symlol){
                case
            }

            System.out.println(symlol);
            symlol = in.read();
        }
    }
}
