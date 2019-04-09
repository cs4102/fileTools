package com.cs.filetools;

import com.cs.utils.RegexUtils;

import java.io.*;
import java.util.Arrays;

/**
 * This is easy tool for Folder and Files copy by including and excluding names you want
 * or you don't want...
 * Start with copyFolders or copyFiles!
 * NOTICE when copy FOLDER, The target folder better to be EMPTY to avoid duplicates.
 *        File copy is fine, because it will rename files automatically...
 * NOTICE that it's NOT thread safe! So do not use multi Thread
 *
 * @author chens
 * @version 1.1
 * @updateTime 2019-04-08
 */
@SuppressWarnings("Duplicates")
public class CopyTool {

    private static String originalPath = "E:\\HELLOYA";                            //source folders
    private static String originalCopyToPath = "E:\\COPYtO";                       //target folders
    private static String regex = ".*((资料)|(素材)|(笔记)).*";                       //contains
    private static String regexMustNotContains = ".*((视频)).*";                     //should not contains
    private static String originalPathRegex;                                        //PathRegex for creating files
    private static byte[] bytes = new byte[8 * 1024];                               //buffer
    private static boolean excludeRulesForChildren = false;    //excluding rules should be used for children folders or not

    /**
     * test for tools
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String[] includes = new String[]{"AB"};
        String[] namesMustNotContains = new String[]{"副本"};
        copyFiles(originalPath, originalCopyToPath, includes, namesMustNotContains);
        //copyFolders(originalPath, originalCopyToPath, regex, regexMustNotContains);
    }

    /**
     * the entrance of the tools
     *
     * @param originalPath         source folders
     * @param originalCopyToPath   target folders
     * @param regex                regex that the fileNames of your folders that you want to copyFolder
     *                             希望拷贝的文件夹名字所匹配的正则表达式
     * @param regexMustNotContains regex that the fileNames of your folders that you want to exclude
     *                             希望排除的文件夹名字所匹配的正则表达式
     * @param excludeRulesForChildren if child folders should be exclude by regexMustNotContains param
     * @throws Exception
     */
    public static void copyFolders(String originalPath, String originalCopyToPath, String regex, String regexMustNotContains,boolean excludeRulesForChildren) throws Exception {
        CopyTool.excludeRulesForChildren = excludeRulesForChildren;
        init(originalPath, originalCopyToPath, regex, regexMustNotContains);
        originalPathRegex = CopyTool.originalPath.replaceAll("\\\\", "\\\\\\\\");
        //regexTest();
        copyFoldersHelper(originalPath);
    }

    /**
     * the entrance of the tools
     *
     * @param originalPath         source folders
     * @param originalCopyToPath   target folders
     * @param regex                regex that the fileNames of your folders that you want to copyFolder
     *                             希望拷贝的文件夹名字所匹配的正则表达式
     * @param regexMustNotContains regex that the fileNames of your folders that you want to exclude
     *                             希望排除的文件夹名字所匹配的正则表达式
     * @throws Exception
     */
    public static void copyFolders(String originalPath, String originalCopyToPath, String regex, String regexMustNotContains) throws Exception {

        init(originalPath, originalCopyToPath, regex, regexMustNotContains);
        originalPathRegex = CopyTool.originalPath.replaceAll("\\\\", "\\\\\\\\");
        //regexTest();
        copyFoldersHelper(originalPath);
    }

    /**
     * the entrance of the tools by string arrays
     *
     * @param originalPath         source folders
     * @param originalCopyToPath   target folders
     * @param includes             String array that the fileNames of your folders that you want to copyFolder
     *                             希望拷贝的文件夹名字包含的字符串的数组
     * @param namesMustNotContains String array that the fileNames of your folders that you want to exclude
     *                             希望排除的文件夹名字包含的字符串的数组
     * @throws Exception
     */
    public static void copyFolders(String originalPath, String originalCopyToPath, String[] includes, String[] namesMustNotContains) throws Exception {

        String regex = RegexUtils.getRegexByStringArray(includes);
        String regexMustNotContains = RegexUtils.getRegexByStringArray(namesMustNotContains);

        copyFolders(originalPath, originalCopyToPath, regex, regexMustNotContains);
    }

    /**
     * the entrance of the tools by string arrays
     *
     * @param originalPath         source folders
     * @param originalCopyToPath   target folders
     * @param includes             String array that the fileNames of your folders that you want to copyFolder
     *                             希望拷贝的文件夹名字包含的字符串的数组
     * @param namesMustNotContains String array that the fileNames of your folders that you want to exclude
     *                             希望排除的文件夹名字包含的字符串的数组
     * @throws Exception
     */
    public static void copyFolders(String originalPath, String originalCopyToPath, String[] includes, String[] namesMustNotContains, boolean excludeRulesForChildren) throws Exception {

        String regex = RegexUtils.getRegexByStringArray(includes);
        String regexMustNotContains = RegexUtils.getRegexByStringArray(namesMustNotContains);
        copyFolders(originalPath, originalCopyToPath, regex, regexMustNotContains,excludeRulesForChildren);
    }

    /**
     * copyFolder files by String arrays
     *
     * @param originalPath         source folders
     * @param originalCopyToPath   target folders
     * @param includes             String array that the fileNames of your folders that you want to copyFolder
     *                             希望拷贝的文件夹名字包含的字符串的数组
     * @param namesMustNotContains String array that the fileNames of your folders that you want to exclude
     *                             希望排除的文件夹名字包含的字符串的数组
     * @throws Exception
     */
    public static void copyFiles(String originalPath, String originalCopyToPath, String[] includes, String[] namesMustNotContains) throws Exception {
        String regex = RegexUtils.getRegexByStringArray(includes);
        String regexMustNotContains = RegexUtils.getRegexByStringArray(namesMustNotContains);

        copyFiles(originalPath, originalCopyToPath, regex, regexMustNotContains);

    }

    private static void copyFiles(String originalPath, String originalCopyToPath, String regex, String regexMustNotContains) throws Exception {
        //set all the params for init;
        init(originalPath, originalCopyToPath, regex, regexMustNotContains);

        //regexTest();
        copyFilesHelper(originalPath);
        //for specific route..other usage, ignore it please!
        //originalPathRegex =RegexUtils.getOriginRegexByOriginPath(originalPath);
    }

    /**
     * set all the required params
     *
     * @param originalPath         source folders
     * @param originalCopyToPath   target folders
     * @param regex                String array that the fileNames of your folders that you want to copyFolder
     *                             希望拷贝的文件夹名字包含的字符串的数组
     * @param regexMustNotContains String array that the fileNames of your folders that you want to exclude
     *                             希望排除的文件夹名字包含的字符串的数组
     */
    private static void init(String originalPath, String originalCopyToPath, String regex, String regexMustNotContains) {
        CopyTool.setOriginalPath(originalPath);
        CopyTool.setOriginalCopyToPath(originalCopyToPath);
        CopyTool.setRegex(regex);
        CopyTool.setRegexMustNotContains(regexMustNotContains);
    }

    /**
     * judge if need to copy recursively
     *
     * @param path the path
     */
    private static void copyFilesHelper(String path) throws Exception {

        File file = new File(path);
        File[] files = file.listFiles();

        for (File fileChild : files) {
            if (RegexUtils.isSpecificFile(fileChild,regex,regexMustNotContains)) {
                try {
                    System.out.println("files copying： " + fileChild.getAbsolutePath());
                    copySingleFile(fileChild.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (fileChild.isDirectory()) {
                copyFilesHelper(fileChild.getAbsolutePath());
            }
        }
    }

    /**
     * test regex, consequences are [,\123], you need to get split[1]
     */
    private static void regexTest() {
        //System.out.println("ni\\代码\\d".matches(regex));
        String testOriginalPath = "F:\\ex";

        String testRegex = testOriginalPath.replaceAll("\\\\", "\\\\\\\\");
        System.out.println("regex are generated as: " + testRegex);
        String[] split = "F:\\ex\\123".split(testRegex);
        System.out.println(Arrays.toString(split));
    }

    /**
     * judge if need to copyFolder the folder，it can easily change to judged by files
     * imply that if the file only contains File which is not directory, recursion stops;
     * 递归出口隐含,如果为文件,不递归;
     *
     * @param path 当前访问的路径
     */
    private static void copyFoldersHelper(String path) throws Exception {
        createFileBySourcePath(path);

        File file = new File(path);
        File[] files = file.listFiles();

        for (File fileChild : files) {
            if (RegexUtils.isSpecificFolder(fileChild,regex,regexMustNotContains)) {
                try {
                    System.out.println("folders copying： " + fileChild.getAbsolutePath());
                    copySingleFile(fileChild.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (fileChild.isDirectory() && !fileChild.getName().matches(regexMustNotContains)) {
                copyFoldersHelper(fileChild.getAbsolutePath());
            }
        }
    }

    /**
     * copy the file to dest
     *
     * @param sourcePath
     */
    private static void copySingleFile(String sourcePath) throws Exception {
        File fileNeedCopy = new File(sourcePath);
        String fileNeedCopyName = fileNeedCopy.getName();

        int copies = 1;
        File fileOutPut = null;
        fileOutPut = new File(originalCopyToPath +"\\"+ fileNeedCopyName);

        //rename files for duplicates
        while (fileOutPut.exists()) {
            String ext = fileNeedCopyName.substring(fileNeedCopyName.lastIndexOf(".") + 1);
            //get the extension of a file
            String destFileNameWithoutExt = fileNeedCopyName.substring(0,fileNeedCopyName.lastIndexOf("."));
            fileOutPut = new File(originalCopyToPath +"\\"+ destFileNameWithoutExt + " (" + copies++ + ")." + ext);
        }
        //copy files
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(sourcePath));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileOutPut));
        int len = 1;
        try {
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes);
            }
            //System.out.println("file has been copyFolder to " + sourcePath);
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("!!!");
            System.out.println("error when copy files from  " + sourcePath);
        } finally {
            bufferedInputStream.close();
        }
    }

    /**
     * copyFolder files from the path to copyToPath recursively, 递归出口隐含
     *
     * @param sourcePath the folders that you want to copyFolder from
     * @throws Exception IOExceptions
     */
    public static void copyFolder(String sourcePath) throws Exception {

        File file = new File(sourcePath);
        File fileOutPut = null;
        try {
            fileOutPut = createFileBySourcePath(sourcePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int len;
        if (file.isFile()) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(sourcePath));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileOutPut));
            try {
                while ((len = bufferedInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes);
                }
                //System.out.println("file has been copyFolder to " + sourcePath);
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("!!!");
                System.out.println("error when copyFolder files from  " + sourcePath);
            } finally {
                bufferedInputStream.close();
            }
        } else {
            File[] files = file.listFiles();
            //exclude the child folders that you do not want to copy for some reasons such as IO stream...
            for (File childFiles : files) {
                if(childFiles.isDirectory() && !childFiles.getName().matches(regexMustNotContains)){
                    copyFolder(childFiles.getAbsolutePath());
                }
            }
        }
    }

    /**
     * return created File by originalPathRegex(in method copyFolders) and sourcePath
     * 依据静态正则表达式和sourcePath创建文件。
     * 如果为路径名相同文件，则舍弃不创建
     *
     * @param sourcePath 源文件路径
     * @return
     */
    private static File createFileBySourcePath(String sourcePath) throws Exception {
        String[] split = sourcePath.split(originalPathRegex);

        if (split.length > 0) {
            String copyToPath = originalCopyToPath + split[1]; //more details about the index 1 are shown in regexTest()
            File file = new File(sourcePath);
            if (file.isDirectory()) {
                return createDirectoryFile(copyToPath);
            } else {
                return createFile(copyToPath);
            }
        } else return null;
    }

    /**
     * create directory since java do not support create file automatically
     * if the file not exists.
     *
     * @param copyToPath the goal directory
     * @return
     */
    private static File createDirectoryFile(String copyToPath) {
        //create New directory, must be null
        File fileOutPut = new File(copyToPath);
        if (fileOutPut.mkdir()) {
            return fileOutPut;
        } else {
            throw new RuntimeException("error when creating folders: " + copyToPath);
        }
    }

    /**
     * create directory since java do not support create file automatically
     * if the file not exists.
     *
     * @param copyToPath the goal file.
     * @return File
     * @throws IOException
     */
    private static File createFile(String copyToPath) throws IOException {
        //create New file, must be null
        File fileOutPut = new File(copyToPath);
        if (fileOutPut.createNewFile()) {
            return fileOutPut;
        } else {
            throw new RuntimeException("error when creating files: " + copyToPath);
        }
    }

    public static String getOriginalCopyToPath() {
        return originalCopyToPath;
    }

    public static void setOriginalCopyToPath(String originalCopyToPath) {
        CopyTool.originalCopyToPath = originalCopyToPath;
    }

    public static String getOriginalPath() {
        return originalPath;
    }

    public static void setOriginalPath(String originalPath) {
        CopyTool.originalPath = originalPath;
    }

    public static String getRegex() {
        return regex;
    }

    public static void setRegex(String regex) {
        CopyTool.regex = regex;
    }

    public static String getRegexMustNotContains() {
        return regexMustNotContains;
    }

    public static void setRegexMustNotContains(String regexMustNotContains) {
        CopyTool.regexMustNotContains = regexMustNotContains;
    }
}
