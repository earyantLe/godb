package com.earyant.database;

import com.earyant.database.database.service.CreateDataBaseService;
import com.earyant.database.explain.CmdType;
import com.earyant.database.explain.CreateDbScheme;
import com.earyant.database.explain.CreateTableScheme;
import com.earyant.database.explain.service.ExplainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author lirui23
 */
@SpringBootApplication
@RestController
@RequestMapping
public class DataBaseApplication {
    @Autowired
    ExplainService explainService;
    @Autowired
    CreateDataBaseService createDataBaseService;

    public static void main(String[] args) {
        SpringApplication.run(DataBaseApplication.class, args);
    }

    @GetMapping
    public Object connect(String cmd) {
        Integer type = explainService.explainType(cmd);
        if (Objects.equals(type, CmdType.createTable.getValue())) {
            CreateTableScheme createTableScheme = explainService.explainCreateTable("db", cmd);
            createDataBaseService.createTable(createTableScheme);
        } else if (Objects.equals(type, CmdType.createDb.getValue())) {
            CreateDbScheme createDbScheme = explainService.explainCreateDb(cmd);
            createDataBaseService.createDb(createDbScheme);
        }
        System.out.println(System.getProperty("user.dir"));
        return "";
    }
}

