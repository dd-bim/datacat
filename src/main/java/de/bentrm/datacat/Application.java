package de.bentrm.datacat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    // CALL db.index.fulltext.createNodeIndex("namesAndDescriptions",["XtdName", "XtdDescription"],["value"])

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
