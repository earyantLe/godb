package com.earyant.database.database.service.impl;


import com.earyant.database.database.service.CreateDataBaseService;
import com.earyant.database.exception.DbException;
import com.earyant.database.explain.CreateDbScheme;
import com.earyant.database.explain.CreateTableScheme;
import com.earyant.database.explain.TableField;
import com.earyant.database.explain.service.ExplainService;
import com.earyant.database.util.DbUtil;
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
        if (!DbUtil.exsitDbDir(createDbScheme.getDatabaseName())) {
            throw new DbException("不存在该数据库");
        }
        String tableDirPath = DbUtil.mkTableDir(createDbScheme.getDatabaseName(), createDbScheme.getTableName());
        String schemeFile = tableDirPath + "/" + "scheme.json";
        List<TableField> fields = createDbScheme.getFiles();
        //DbUtil.saveFile()
    }

    @Override
    public void createDb(CreateDbScheme createDbScheme) {
        DbUtil.mkDbDir(createDbScheme.getDbName());
    }
}
