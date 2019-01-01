package com.earyant.database.explain.service;

import com.earyant.database.explain.CreateDbScheme;
import com.earyant.database.explain.CreateTableScheme;
import com.earyant.database.explain.InsertDataScheme;

public interface ExplainService {
    Integer explainType(String cmd);

    CreateDbScheme explainCreateDb(String cmd);

    CreateTableScheme explainCreateTable(String dbName, String cmd);

    InsertDataScheme explainInsertData(String dbName, String cmd);
}
