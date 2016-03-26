package ru.deathman.rpgportal.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Виктор
 */
public class GenerateSchemaTool {

    private static Logger logger = LoggerFactory.getLogger(GenerateSchemaTool.class);

    public static void main(String[] args) {
        try {
            logger.debug("Schema generation started");
            generateDdl();
            logger.debug("Schema generation finished");
        } catch (Exception e) {
            logger.warn("Schema generation failed");
        }
    }

    private static void generateDdl() throws Exception {
        List<String> packages = new ArrayList<>();
        packages.add("ru.deathman.rpgportal.common.domain");

        SchemaGenerator generator = new SchemaGenerator(packages);
        generator.generateDdl(SchemaGenerator.Dialect.POSTGRESQL);
    }
}
