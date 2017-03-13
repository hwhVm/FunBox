package com.example.administrator.baseapp.test;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;

/**
 * Created by beini on 2017/3/9.
 */

public class Tess {
    private static String Enoding = "GBK";

    public static void main(String[] args) {
//        File file = new File("C:/Users/Administrator/Desktop/201305理论部分.docx");
        getTextFromText("C:/Users/Administrator/Desktop/201305理论部分.docx", "C:/Users/Administrator/Desktop/b.txt");
    }

 //URLDecoder.decode(filePath, "UTF-8")
    public static void getTextFromText(String filePath, String detFilePath) {
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), Enoding);
            BufferedReader br = new BufferedReader(isr);
            new File(detFilePath);
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(detFilePath), Enoding);
            BufferedWriter bw = new BufferedWriter(osw);
            String temp = null;
            while ((temp = br.readLine()) != null) {
                System.out.println(" temp=" + temp);
                if (temp.contains("、") && temp.contains("( )")) {
                    bw.newLine();
                }
                bw.write(temp);
                bw.newLine();
                bw.flush();
            }
            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
