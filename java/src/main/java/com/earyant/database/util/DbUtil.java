package com.earyant.database.util;

import java.io.File;

public class DbUtil {
    static final String workDir = System.getProperty("user.dir");
    static String tmp = workDir + "/" + "tmp";

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

    public static boolean exsitDbDir(String databaseName) {
        String tableFilePath = tmp + "/" + databaseName;
        File file = new File(tableFilePath);
        if (!file.exists()) {
            return false;
        }
        return true;
    }
}
