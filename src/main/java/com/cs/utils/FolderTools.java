package com.cs.utils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderTools {

    private static List<File> fileList = new ArrayList<>();

    /**
     * deal the file in the specific path.
     * return the list
     * four params as explained up there.
     */
    public static List<File> getFolderListNeedToDealWith(String originalPath, String regex,
                                                          String regexMustNotContains, boolean excludeRulesForChildren) {
        fileList = new ArrayList<>();
         getFolderListNeedToDealWithHelper(originalPath, regex, regexMustNotContains, excludeRulesForChildren);
         return fileList;
    }

    /**
     * deal the file in the specific path.
     * return the list
     * four params as explained up there.
     * @param originalPath         source folders
     * @param regex                String array that the fileNames of your folders that you want to copyFolder
     *                             希望拷贝的文件夹名字包含的字符串的数组
     * @param regexMustNotContains String array that the fileNames of your folders that you want to exclude
     *                             希望排除的文件夹名字包含的字符串的数组
     * @param excludeRulesForChildren excluding rules should be used for children folders or not
     *                                希望排除的名字是否用在子文件夹上
     */
    private static List<File> getFolderListNeedToDealWithHelper(String originalPath, String regex,
                                                                String regexMustNotContains, boolean excludeRulesForChildren) {
        File parentFile = new File(originalPath);
        File[] files = parentFile.listFiles();
        for (File file : files) {
            if (RegexUtils.isSpecificFolder(file, regex, regexMustNotContains)) {
                fileList.add(file);
            } else if (file.isDirectory() && !file.getName().matches(regexMustNotContains)) {
                getFolderListNeedToDealWithHelper(file.getAbsolutePath(), regex, regexMustNotContains, excludeRulesForChildren);
            }
        }
        return fileList;
    }


}
