package com.earyant.database.util;

import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

@Data
public class DbUtil {
    public static final String workDir = System.getProperty("user.dir");
    public static String tmp = workDir + "/" + "tmp";

    //public static String mkTableDir(String databaseName, String tableName) {
    //    mkDbDir(databaseName);
    //    var existTable = mkTableDir(databaseName, tableName);
    //    return "";
    //}

    public static String mkTableDir(String databaseName, String tableName) {
        /// Users/lirui23/Desktop/code/database
        mkDbDir(databaseName);
        String tableFilePath = tmp + "/" + databaseName + "/" + tableName;
        File file = new File(tableFilePath);
        if (file.exists()) {
            return tableFilePath;
        } else {
            file.mkdir();
            return tableFilePath;
        }
    }

    private static void mkTmp() {
        File file = new File(tmp);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static void mkDbDir(String databaseName) {
        mkTmp();
        String tableFilePath = tmp + "/" + databaseName;
        File file = new File(tableFilePath);
        if (!file.exists()) {
            file.mkdir();
        }
        //else {
        //    throw new DbException("存在该数据库了");
        //}
    }

    public static boolean exsitDbDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    public static boolean writeFile(String filePath, String data) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String readTxtFile(String filePath) {
        try {
            File file = new File(filePath);
            //判断文件是否存在
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                StringBuffer sb = new StringBuffer();
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt);
                }
                read.close();
                return sb.toString();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        return "";
    }

    public static String getSchemePath(String dbName, String tableName) {
        String tableDirPath = DbUtil.mkTableDir(dbName, tableName);
        String schemeFile = tableDirPath + "/" + "scheme.json";
        return schemeFile;
    }
}
