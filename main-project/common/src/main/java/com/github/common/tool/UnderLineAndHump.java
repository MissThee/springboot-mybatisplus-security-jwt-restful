package com.github.common.tool;

import com.sun.javafx.binding.StringFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnderLineAndHump {
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     */
    private static String lineToHump(String str) {
        if (str == null) {
            return null;
        }
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String lineToSmallHump(String str) {
        String s = lineToHump(str);
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public static String lineToBigHump(String str) {
        String s = lineToHump(str);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线,效率比上面高
     */
    public static String humpToLine(String str) {
        if (str == null) {
            return null;
        }
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        String s = sb.toString();
        if (s.startsWith("_")) {
            s = s.substring(1);
        }
        return s;
    }

}
