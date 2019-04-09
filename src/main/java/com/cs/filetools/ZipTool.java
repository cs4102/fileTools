package com.cs.filetools;

import com.cs.utils.FolderTools;
import com.cs.utils.RegexUtils;

import java.io.*;
import java.util.List;
import java.util.zip.*;

/**
 * used for packaging files that contains some name and exclude some name in the folder
 * NOT thread Safe!
 * for better IO when faced with lots of small files;
 */
public class ZipTool {
    private static String originalPathRegex;                                        //PathRegex for creating files
    private static boolean excludeRulesForChildren = false;    //excluding rules should be used for children folders or not
    private static final String ext = ".zip";


    /**
     * @param originalPath
     * @param originalCopyToPath
     * @param includes
     * @param namesMustNotContains
     * @param excludeRulesForChildren
     */
    public static void zip(String originalPath, String originalCopyToPath, String[] includes, String[] namesMustNotContains, boolean excludeRulesForChildren) throws Exception {

        originalPathRegex = RegexUtils.getOriginRegexByOriginPath(originalPath);

        String regex = RegexUtils.getRegexByStringArray(includes);
        String regexMustNotContains = RegexUtils.getRegexByStringArray(namesMustNotContains);

        List<File> folderList = FolderTools.getFolderListNeedToDealWith(originalPath, regex, regexMustNotContains, excludeRulesForChildren);

        for (File file : folderList) {

            String[] split = file.getAbsolutePath().split(originalPathRegex);
            String folderNameWithRelativePath = "";
            if (split != null && split.length > 0) {
                folderNameWithRelativePath = split[1];
            }

            //the dest folders outputStream
            String destFileName = originalCopyToPath + folderNameWithRelativePath + ext;

            //if parent do not exists, then create it recursively!
            if (parentisFile(destFileName)) continue;

            FileOutputStream fileOutputStream = new FileOutputStream(destFileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

            System.out.println("zip packaging to:"+ destFileName);
            compressZip(zipOutputStream, file, file.getAbsolutePath());
            zipOutputStream.close();
            bufferedOutputStream.close();
            fileOutputStream.close();
        }

    }

    /**
     * create if not exists,
     * @param destFileName
     * @return if parent is file, return false
     */
    private static boolean parentisFile(String destFileName) {
        File destParent = new File(destFileName).getParentFile();
        if (!destParent.exists()){
            parentisFile(destParent.getAbsolutePath());
            destParent.mkdir();
        }
        else if (destParent.isFile()){
            return true;
        }
        return false;
    }

    /**
     * @param zipOutput ZipoutputStream
     * @param file      directory need to be compress
     * @param base      the name of the file
     * @throws Exception
     */
    private static void compressZip(ZipOutputStream zipOutput, File file, String base) throws Exception {

        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();// 列出所有的文件

            for (File fi : listFiles) {
                if (fi.isDirectory()) {
                    compressZip(zipOutput, fi, base + "/" + fi.getName());
                } else {
                    zipFiles(zipOutput, fi, base);
                }
            }
        } else {
            zipFiles(zipOutput, file, base);
        }
    }

    /**
     * @param zipOutput
     * @param file
     * @param base
     * @throws Exception
     */
    public static void zipFiles(ZipOutputStream zipOutput, File file, String base) throws Exception {
        ZipEntry zEntry = new ZipEntry(base + File.separator + file.getName());
        zipOutput.putNextEntry(zEntry);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = bis.read(buffer)) != -1) {
            zipOutput.write(buffer, 0, read);
        }
        bis.close();
    }

    public static void main(String[] args) throws Exception {
        zip("F:\\course\\CourseB\\course6 品优购", "E:\\temp",
                new String[]{"代码"}, new String[]{"分加哦微积分拉萨的"}, true);
    }


}
