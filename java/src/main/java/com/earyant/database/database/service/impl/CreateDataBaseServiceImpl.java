package com.earyant.database.database.service.impl;


import com.earyant.database.database.service.CreateDataBaseService;
import com.earyant.database.exception.DbException;
import com.earyant.database.explain.CreateDbScheme;
import com.earyant.database.explain.CreateTableScheme;
import com.earyant.database.explain.InsertDataScheme;
import com.earyant.database.explain.TableData;
import com.earyant.database.explain.TableField;
import com.earyant.database.explain.service.ExplainService;
import com.earyant.database.util.DbUtil;
import com.earyant.database.util.GsonUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lirui23
 */
@Service
public class CreateDataBaseServiceImpl implements CreateDataBaseService {
    @Autowired
    ExplainService explainService;

    @Override
    public void createTable(CreateTableScheme createDbScheme) {
        String dbPath = DbUtil.tmp + "/" + createDbScheme.getDatabaseName();

        if (!DbUtil.exsitDbDir(dbPath)) {
            throw new DbException("不存在该数据库");
        }
        //if (DbUtil.exsitDbDir(DbUtil.tmp + "/" + createDbScheme.getDatabaseName() + "/" + createDbScheme.getTableName())) {
        //    throw new DbException("已存在该表");
        //}
        String schemeFile = DbUtil.getSchemePath(createDbScheme.getDatabaseName(), createDbScheme.getTableName());
        List<TableField> fields = createDbScheme.getFiles();

        DbUtil.writeFile(schemeFile, GsonUtils.toJson(fields));
    }

    @Override
    public void createDb(CreateDbScheme createDbScheme) {
        String dbPath = DbUtil.tmp + "/" + createDbScheme.getDbName();

        if (DbUtil.exsitDbDir(dbPath)) {
            throw new DbException("已经存在该数据库了");
        }
        DbUtil.mkDbDir(createDbScheme.getDbName());
    }

    @Override
    public void insertData(InsertDataScheme insertDataSchemes) {
        String dbPath = DbUtil.tmp + "/" + insertDataSchemes.getDbName();

        if (!DbUtil.exsitDbDir(dbPath)) {
            throw new DbException("不存在该数据库");
        }
        if (!DbUtil.exsitDbDir(DbUtil.tmp + "/" + insertDataSchemes.getDbName() + "/" + insertDataSchemes.getTableName())) {
            throw new DbException("不存在该表");
        }

        //     没有数据配置文件，就写一个
        TableData tableData;
        String tableDataPath =
                DbUtil.tmp + "/" + insertDataSchemes.getDbName() + "/" + insertDataSchemes.getTableName() +
                        "/TableData.json";
        if (DbUtil.exsitDbDir(tableDataPath)) {
            String dataJson =
                    DbUtil.readTxtFile(tableDataPath);
            tableData = GsonUtils.fromJson(dataJson, TableData.class);
        } else {
            tableData = new TableData();
            //TableData.TableFiles tableFiles = new TableData.TableFiles();
            //tableFiles.setId(0);
            //tableFiles.setLength(0);
            tableData.setTabelFiles(Lists.newArrayList());
        }
        Integer id = null;
        Integer maxzid = -1;
        for (TableData.TableFiles tabelFile : tableData.getTabelFiles()) {
            if (tabelFile.getLength() < 1000 && tabelFile.getLength() + insertDataSchemes.getValues().size() < 1000) {
                id = tabelFile.getId();
                break;
            }
            maxzid = tabelFile.getId();
        }
        List<String> lines;
        String filePath;
        if (id == null) {
            maxzid = maxzid + 1;
            filePath = DbUtil.tmp + "/" + insertDataSchemes.getDbName() + "/" + insertDataSchemes.getTableName() +
                    "/data" + maxzid + ".json";
            lines = Lists.newArrayList();
            TableData.TableFiles tableFiles = new TableData.TableFiles();
            tableFiles.setId(0);
            tableFiles.setLength(0);
            tableData.setTabelFiles(Lists.newArrayList(tableFiles));
            id = 0;
        } else {
            filePath = DbUtil.tmp + "/" + insertDataSchemes.getDbName() + "/" + insertDataSchemes.getTableName() +
                    "/data" + id + ".json";
            String linesDatas = DbUtil.readTxtFile(filePath);
            lines = GsonUtils.fromJsonList(linesDatas, String.class);
        }


        insertDataSchemes.getValues().forEach(fieldValue -> {
            String line = GsonUtils.toJson(fieldValue);
            lines.add(line);
        });

        DbUtil.writeFile(filePath, GsonUtils.toJson(lines));
        tableData.getTabelFiles().get(id).setLength(tableData.getTabelFiles().get(id).getLength() + insertDataSchemes.getValues().size());
        DbUtil.writeFile(tableDataPath, GsonUtils.toJson(tableData));
    }
}
