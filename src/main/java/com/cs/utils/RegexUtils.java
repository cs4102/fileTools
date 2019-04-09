package com.cs.utils;

import java.io.File;

public class RegexUtils {

    /**
     * get regex through that String arrays that include including and excluding keywords
     *
     * @param keywords the include and exclude keywords to generate regex expression
     * @return
     */
    public static String getRegexByStringArray(String[] keywords) {
        String regex = ".*(";
        if (keywords.length > 0) {
            for (int i = 0; i < keywords.length - 1; i++) {
                regex += "(" + keywords[i] + ")|";
            }
        }
        regex += "(" + keywords[keywords.length - 1] + ")).*";
        return regex;
    }

    /**
     * get regex of orgin paths, notice that when you want to get the original path
     * regex, you should replace all the '\\' into '\\\\' for regex expression
     * (more specifically, string.replaceAll() function needs regex expression)
     *
     * @param paths original paths
     * @return
     */

    public static String getOriginRegexByOriginPath(String paths) {
        return paths.replaceAll("\\\\", "\\\\\\\\");
    }


    /**
     * if could meet the regex expressions , includes and excludes.
     *
     * @param fileChild
     * @return
     */
    public static boolean isSpecificFolder(File fileChild, String regex, String regexMustNotContains) {
        return fileChild.isDirectory() && fileChild.getName().matches(regex) && !fileChild.getName().matches(regexMustNotContains);
    }

    /**
     * if could meet the regex expressions , includes and excludes.
     *
     * @param fileChild
     * @return
     */
    public static boolean isSpecificFile(File fileChild, String regex, String regexMustNotContains) {
        return fileChild.isFile() && fileChild.getName().matches(regex) && !fileChild.getName().matches(regexMustNotContains);
    }

}
