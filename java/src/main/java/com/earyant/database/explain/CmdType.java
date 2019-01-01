package com.earyant.database.explain;

public enum CmdType {
    createTable(0),
    createDb(1),
    insertData(2),
    ;

    Integer value;
    CmdType(int i) {
        this.value = i;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
