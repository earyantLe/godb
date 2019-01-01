package com.earyant.database.explain;

import lombok.Data;

@Data
public class TableField {
    String fieldName;
    Integer type;
    Integer length;
    String defaultContent;
    Boolean primary;
}
