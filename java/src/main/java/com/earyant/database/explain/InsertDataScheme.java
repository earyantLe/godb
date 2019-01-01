package com.earyant.database.explain;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class InsertDataScheme {
    String dbName;
    String tableName;
    List<HashMap<String, String>> values;
}
