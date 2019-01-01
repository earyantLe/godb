package com.earyant.database.explain.service.impl;


import com.earyant.database.exception.DbException;
import com.earyant.database.explain.CmdType;
import com.earyant.database.explain.CreateDbScheme;
import com.earyant.database.explain.CreateTableScheme;
import com.earyant.database.explain.FieldTypeEnum;
import com.earyant.database.explain.InsertDataScheme;
import com.earyant.database.explain.TableField;
import com.earyant.database.explain.service.ExplainService;
import com.earyant.database.util.DbUtil;
import com.earyant.database.util.GsonUtils;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lirui23
 */
@Service
public class ExplainServiceImpl implements ExplainService {
    String selectMatch = "^select\b[^\b]+\bas\b[^\b]+\bfrom\b[^\b]+\bwhere\b[^\b]+\b=[0-9]+\b$";
    String createMatch = "CREATE TABLE\b['][^\b]+\bas\b[^\b]+\bfrom\b[^\b]+\bwhere\b[^\b]+\b=[0-9]+\b$'";

    @Override
    public Integer explainType(String cmd) {
        cmd = cmd.strip().toUpperCase();
        if (cmd.startsWith("CREATE TABLE")) {
            return CmdType.createTable.getValue();
        } else if (cmd.startsWith("CREATE DATABASE")) {
            return CmdType.createDb.getValue();
        } else if (cmd.startsWith("INSERT INTO")) {
            return CmdType.insertData.getValue();
        }
        return 1;
    }

    @Override
    public CreateDbScheme explainCreateDb(String cmd) {
        // CREATE DATABASE database
        cmd = cmd.strip();
        if (!cmd.startsWith("CREATE DATABASE")) {
            throw new DbException("建库语句错误");
        }
        var cmds = cmd.split(" ");
        var dbName = cmds[cmds.length - 1];
        CreateDbScheme createDbScheme = new CreateDbScheme();
        createDbScheme.setDbName(dbName);
        return createDbScheme;
    }

    @Override
    public CreateTableScheme explainCreateTable(String dbName, String cmd) {
        List<TableField> tableFields = new ArrayList<>();
        //    CREATE TABLE `feature_conf` (
        //  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
        //  PRIMARY KEY (`id`)
        //)
        // 去掉收尾空格
        cmd = cmd.strip();
        cmd = cmd.toUpperCase();

        //var reg = "(\\s)+CREATE TABLE(\\s)+()";
        //if (!cmd.matches(reg)) {
        //    throw new DbException("建表语句不正常");
        //}

        if (!cmd.startsWith("CREATE TABLE")) {
            throw new DbException("建表语句不正常");
        }
        cmd = cmd.substring(12).strip();
        int fieldIndex = cmd.indexOf("(");
        var tableName = cmd.substring(0, fieldIndex).replaceAll("'", "").replaceAll("`", "").replaceAll(" ", "");
        System.out.println("tableName: " + tableName);
        cmd = cmd.substring(fieldIndex + 1, cmd.lastIndexOf(")")).strip();

        Arrays.stream(cmd.split(",")).forEach(fieldLine -> {
            if (fieldLine.length() < 5) {
                return;
            }

            var fileds = fieldLine.strip().split(" ");
            if (fieldLine.strip().startsWith("PRIMARY KEY")) {
                var fieldName =
                        fileds[2].substring(fileds[2].indexOf("("), fileds[2].indexOf(")")).replaceAll("\'",
                                                                                                       "");
                tableFields.forEach(tableField -> {
                    if (tableField.getFieldName().equals(fieldName)) {
                        tableField.setPrimary(true);
                    }
                });
                return;
            }


            String filedName = fileds[0].replaceAll("\'", "");
            String typeContent = fileds[1];

            String typeChar = typeContent.substring(0, typeContent.indexOf("("));
            Integer length = Integer.valueOf(typeContent.substring(typeContent.indexOf("(") + 1,
                                                                   typeContent.lastIndexOf(")")));
            FieldTypeEnum type = FieldTypeEnum.of(typeChar);
            if (type == null) {
                throw new DbException("建表语句不正常");
            }
            String defaultContent = fileds[3];
            TableField tableField = new TableField();
            tableField.setDefaultContent(defaultContent);
            tableField.setFieldName(filedName);
            tableField.setLength(length);
            tableField.setType(type.code);
            tableFields.add(tableField);
        });
        CreateTableScheme createDbScheme = new CreateTableScheme();
        createDbScheme.setFiles(tableFields);
        createDbScheme.setTableName(tableName);
        createDbScheme.setType(CmdType.createTable.getValue());
        createDbScheme.setDatabaseName(dbName);

        System.out.println(createDbScheme);
        return createDbScheme;
    }


    @Override
    public InsertDataScheme explainInsertData(String dbName, String cmd) {
        // insert into feature_conf(id) values(1);
        cmd = cmd.strip().toUpperCase();
        if (!cmd.startsWith("INSERT INTO")) {
            throw new DbException("插入数据库语法不正确");
        }
        cmd = cmd.replaceAll("INSERT INTO", "");
        //System.out.println(cmd);
        var tableNameAndFields = cmd.substring(0, cmd.indexOf("VALUE")).strip();
        //System.out.println(table_name_and_fields);
        var values = cmd.substring(cmd.indexOf("VALUE")).strip();
        //System.out.println(values);
        String tableName;
        String fields;
        if (tableNameAndFields.contains("(")) {
            tableName = tableNameAndFields.substring(0, tableNameAndFields.indexOf("("));

        } else {
            tableName = tableNameAndFields;
        }
        String schemeFile = DbUtil.getSchemePath(dbName, tableName);
        String fieldDatas = DbUtil.readTxtFile(schemeFile);
        List<TableField> tableFields = GsonUtils.fromJsonList(fieldDatas, TableField.class);
        List<String> dbFields = tableFields.stream().map(tableField -> tableField.getFieldName().strip().replaceAll("`",
                                                                                                                    "")).collect(Collectors.toList());
        List<String> insertField;
        if (tableNameAndFields.contains("(")) {
            fields = tableNameAndFields.substring(tableNameAndFields.indexOf("(") + 1,
                                                  tableNameAndFields.indexOf(")"));
            for (String s : fields.split(",")) {
                boolean flag = false;
                for (String dbField : dbFields) {
                    if (Objects.equals(dbField, s)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    throw new DbException("表中不存在该字段列，请确认");
                }
            }
            insertField = Arrays.asList(fields.split(","));
        } else {
            insertField = dbFields;
        }


        System.out.println(tableName);
        System.out.println(values);
        System.out.println(insertField);

        //     parse value
        List<String> realyVlues;
        if (values.startsWith("VALUES")) {
            values = values.replaceAll("VALUES", "");
            realyVlues =
                    Arrays.asList(values.replaceAll("\\)\\(", "###").replaceAll("\\(", "").replaceAll("\\)", "").split(
                            ","));
        } else {
            values = values.replaceAll("VALUE", "");
            realyVlues = Arrays.asList(values.replaceAll("\\(", "").replaceAll("\\)", "").split(","));
        }
        List<HashMap<String, String>> fieldValues = Lists.newArrayList();
        realyVlues.forEach(realyVlue -> {
            String[] value = realyVlue.split(",");
            if (!Objects.equals(insertField.size(), value.length)) {
                throw new DbException("插入的值和字段长度不一致，请检查");
            }
            HashMap<String, String> jsonObject = new HashMap<>();
            for (int i = 0; i < value.length; i++) {
                String v = value[i];
                var field = insertField.get(i);
                //     todo 判断长度是否超过了
                jsonObject.put(field, v);
            }
            fieldValues.add(jsonObject);
        });
        InsertDataScheme insertDataScheme = new InsertDataScheme();
        insertDataScheme.setDbName(dbName);
        insertDataScheme.setTableName(tableName);
        insertDataScheme.setValues(fieldValues);
        return insertDataScheme;

    }


    public static void main(String[] args) {
        String cmd = "CREATE TABLE `feature_conf` (\n" +
                "          `id` varchar(255) DEFAULT NULL,\n" +
                "          PRIMARY KEY (`id`)\n" +
                ")";
        ExplainServiceImpl explainService = new ExplainServiceImpl();
        explainService.explainCreateTable("db", cmd);

        //System.out.println(cmd.replaceAll(" ",""));
    }
}
