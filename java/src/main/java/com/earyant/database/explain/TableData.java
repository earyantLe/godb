package com.earyant.database.explain;

import lombok.Data;

import java.util.List;

@Data
public class TableData {

    List<TableFiles> tabelFiles;

    @Data
    public static class TableFiles {
        Integer id;
        Integer length;

    }
}
