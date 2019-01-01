package com.earyant.database.database.service;

import com.earyant.database.explain.CreateDbScheme;
import com.earyant.database.explain.CreateTableScheme;
import com.earyant.database.explain.InsertDataScheme;

public interface CreateDataBaseService {
    void createTable(CreateTableScheme createDbScheme);

    void createDb(CreateDbScheme createDbScheme);

    void insertData(InsertDataScheme insertDataSchemes);
}
