package com.earyant.database.explain;

import lombok.Data;

import java.util.List;

@Data
public class CreateTableScheme {
    String tableName;
    Integer type;
    List<TableField> files;
    String databaseName;
}
