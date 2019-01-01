package com.earyant.database.explain;

import org.springframework.util.ObjectUtils;

public enum FieldTypeEnum {
    integet("INT", 0),
    charset("VARCHAR", 1),
    ;

   public String name;
    public Integer code;

    FieldTypeEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public static  FieldTypeEnum of(String name) {
        for (FieldTypeEnum value : FieldTypeEnum.values()) {
            if (ObjectUtils.nullSafeEquals(value.name, name)) {
                return value;
            }
        }
        return null;
    }
}
