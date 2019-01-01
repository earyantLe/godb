package com.earyant.database;

import com.earyant.database.database.service.impl.CreateDataBaseServiceImpl;
import com.earyant.database.explain.CmdType;
import com.earyant.database.explain.CreateDbScheme;
import com.earyant.database.explain.CreateTableScheme;
import com.earyant.database.explain.InsertDataScheme;
import com.earyant.database.explain.service.impl.ExplainServiceImpl;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        String dbName = "db";

        //String cmd = "CREATE DATABASE " + dbName;
        //runShell(dbName, cmd);
        String createTableCmd = "CREATE TABLE `feature_conf` (\n          `id` varchar(255) DEFAULT NULL,\n          " +
                "PRIMARY KEY (`id`)\n)";
        runShell(dbName, createTableCmd);

        String insertCmd = "INSERT INTO feature_conf values(1)";
        runShell(dbName, insertCmd);

    }

    private static void runShell(String dbName, String cmd) {
        ExplainServiceImpl explainService = new ExplainServiceImpl();
        CreateDataBaseServiceImpl createDataBaseService = new CreateDataBaseServiceImpl();
        Integer type = explainService.explainType(cmd);
        if (Objects.equals(type, CmdType.createTable.getValue())) {
            // 建表
            CreateTableScheme createTableScheme = explainService.explainCreateTable(dbName, cmd);
            createDataBaseService.createTable(createTableScheme);
        } else if (Objects.equals(type, CmdType.createDb.getValue())) {
            //建库
            CreateDbScheme createDbScheme = explainService.explainCreateDb(cmd);
            createDataBaseService.createDb(createDbScheme);
        } else if (Objects.equals(type, CmdType.insertData.getValue())) {
            InsertDataScheme insertDataSchemes  = explainService.explainInsertData(dbName, cmd);
            createDataBaseService.insertData(insertDataSchemes);
        }
    }
}
