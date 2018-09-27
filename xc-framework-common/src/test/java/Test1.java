import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: szz
 * @Date: 2018/9/17 下午2:49
 * @Version 1.0
 */
public class Test1 {

    public static void main(String[] args) throws Exception {

        FileReader reader = new FileReader("/Users/szz/text1.txt");
        BufferedReader br = new BufferedReader(reader);

        String str = null;
        int i=0;
        String[] strings = new String[20];
        while((str = br.readLine()) != null) {

            str.contains("you");

            int num = appearNumber(str, "you");
            str=num+":"+str;
            strings[i]=str;
            i++;

        }

        FileReader reader1 = new FileReader("/Users/szz/text2.txt");
        BufferedReader br1 = new BufferedReader(reader1);
        String str1 = null;
        while((str = br1.readLine()) != null) {
            str.contains("you");

            int num = appearNumber(str, "you");
            str=num+":"+str;
            strings[i]=str;
            i++;
        }

        String[] strings2 = new String[i];
        strings2=strings;

        strSort(strings2);
        StringBuffer sb= new StringBuffer();
        for (String string : strings2) {
            if (string!=null&&string.length()>2){
                System.out.println(string);
                sb.append(string.substring(2)+"\r\n");
            }

        }
        // write string to file
        FileWriter writer = new FileWriter("/Users/szz/text3.txt");
        BufferedWriter bw = new BufferedWriter(writer);
        System.out.println(sb);
        bw.write(sb.toString());

        bw.close();
        writer.close();

    }

    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        srcText=srcText.toLowerCase();
        findText=findText.toLowerCase();
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static void strSort(String[] str){
        for (int i = 0; i < str.length; i++) {
            for (int j = i+1; j < str.length; j++) {
                if (str[i]!=null&&str[j]!=null) {
                    if(str[i].compareTo(str[j])<0){    //对象排序用camparTo方法
                        swap(str,i,j);
                    }
                }

            }
        }

    }

    private static void swap(String[] strSort, int i, int j) {
        String t = strSort[i];
        strSort[i] = strSort[j];
        strSort[j] = t;
    }
}
