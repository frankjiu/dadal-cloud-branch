package com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-11-07
 */
public class GroupRegexTest {
    public static void main(String args[]) {
        // String to be scanned to find the pattern.
        //String line = "This order was placed for QT3000! OK?";
        String line = "This order was placed for QT3000! OK?";
        String pattern = "(.*)(\\d+)(.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            System.out.println("Found value: " + m.group(0));
            System.out.println("Found value: " + m.group(1));
            System.out.println("Found value: " + m.group(2));
            System.out.println("Found value: " + m.group(3));
        } else {
            System.out.println("NO MATCH");
        }

        /*String pattern = "(\\d+)-(\\d+)-(\\d+)";
        String word = "1990-10-14";
        final Pattern compile = Pattern.compile(pattern);
        final Matcher matcher = compile.matcher(word);
        if(matcher.find()){
            final int i = matcher.groupCount();
            System.out.println("共"+i+"组");
            for (int j = 0; j <= i; j++) {
                System.out.println("第"+j+"组："+matcher.group(j));

            }
        }*/

        /*String url = "https://hfdfs/gdfsf/{marketId}/find";
        String marketId = "ab-cd";
        String reg = "(\\w+)";
        final Pattern compile = Pattern.compile(reg);
        final Matcher matcher = compile.matcher(url);
        while(matcher.find()){
            if (matcher.group().equals("marketId")) {
                System.out.println(matcher.group());
                String newUrl = url.replace("{" + matcher.group() + "}", marketId);
                System.out.println(newUrl); // https://hfdfs/gdfsf/ab-cd/find
            }
        }*/

    }
}


