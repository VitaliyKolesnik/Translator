package Lex_Analys;

import java.io.*;
import java.util.ArrayList;

public class Analysyer {

    private File file;
    private InputStream in;
    private OutputStream out;
    private char[] lit;
    private int[] dig;
    private char[] dm;
    private ArrayList<Character>  othet;
    private int eof;




    public Analysyer(File file) throws IOException {
        this.file = file;
        lit = new char[25];
        dig = new int[10];
        dm = new char[]{':', ';', '<', '=', '>', '-', '|', ',', '(', ')'};
        othet = new ArrayList<>();
        eof = -1;
        read();
    }

    void read() throws IOException {
        in = new FileInputStream(file);
        int symlol = in.read();

        while (symlol != eof){
            /*switch (in){
                case
            }*/
            System.out.println(symlol);
            symlol = in.read();
        }
    }
}
