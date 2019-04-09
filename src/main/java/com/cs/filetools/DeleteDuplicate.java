package com.cs.filetools;


import java.io.File;

//鉴于为tools 不愿意弄出来一个可视化界面,后续可补充.
public class DeleteDuplicate {


    public static void main(String[] args) {

        // the path you want to delete
        String path = "F:\\course\\CourseB\\course3\\abc\\新建文件夹";

        // the fileName contains the word
        String specificWord = "副本";
        deleteFile(path,specificWord);
    }

    /**
     * delete the file in the specific path.
     * including file.listFiles();
     * @param path 待删除的路径 path for delete
     * @param specificWord 待删除路径所包含的名字 the specific word that the file which you want to delete contains
     */
    private static void deleteFile(String path,String specificWord) {
        File file = new File(path);

        //children of files
        File[] files = file.listFiles();
        for (File childFile : files) {
            if (childFile.getName().contains(specificWord)){
                boolean delete = childFile.delete();
                if (!delete){
                    System.out.println(childFile.getName() + " delete error!!");
                }
            }

        }
    }


}
