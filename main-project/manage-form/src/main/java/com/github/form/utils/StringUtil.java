package com.github.form.utils;

public class StringUtil {
    //截取字符串中第一个大写字母之前的字符，并将第一个字母变为大写
    public static String getWordBeforeFirstUpperCase(String key) {
        String prefix = "";
        for (int i = 0; i < key.length(); i++) {
            if (Character.isUpperCase(key.charAt(i))) {
                prefix = key.substring(0, i);
                prefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
                break;
            }
        }
        return prefix;
    }
}
