package ru.deathman.rpgportal.sql;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Виктор
 */
@SuppressWarnings("deprecation")
public class SchemaGenerator {

    private String outputDir = "src/main/resources/ddl";
    private String outputFile = "schema.ddl";
    private NamingStrategy namingStrategy = ImprovedNamingStrategy.INSTANCE;

    private File configFile;

    public SchemaGenerator(List<String> packageNames) throws Exception {
        configFile = getConfigFile(packageNames);
    }

    public void generateDdl(Dialect dialect) {
        Configuration configuration = new Configuration().configure(configFile);
        configuration.setProperty(AvailableSettings.DIALECT, dialect.getDialectClass());
        configuration.setNamingStrategy(namingStrategy);
        SchemaExport export = new SchemaExport(configuration);
        File directory = new File(outputDir + "/" + dialect.name().toLowerCase());
        if (!directory.exists()) {
            directory.mkdir();
        }
        export.setDelimiter(";");
        export.setOutputFile(new File(directory, outputFile).getPath());
        export.setFormat(true);
        export.execute(true, false, false, false);

        configFile.delete();
    }

    private File getConfigFile(List<String> packageNames) throws Exception {
        File configFile = new File("hibernate.cfg.xml");
        StringBuilder content = new StringBuilder();
        content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        content.append("<!DOCTYPE hibernate-configuration SYSTEM \"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd\">");
        content.append("<hibernate-configuration><session-factory>");
        for (String packageName : packageNames) {
            for (Class clazz : getClasses(packageName)) {
                content.append("<mapping class=\""+ clazz.getName() + "\"/>");
            }
        }
        content.append("</session-factory></hibernate-configuration>");
        BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
        writer.write(content.toString());
        writer.close();
        return configFile;
    }


    @SuppressWarnings("rawtypes")
    private List<Class> getClasses(String packageName) throws Exception {
        File directory = null;
        try {
            ClassLoader cld = getClassLoader();
            URL resource = getResource(packageName, cld);
            directory = new File(resource.getFile());
        } catch (NullPointerException ex) {
            throw new ClassNotFoundException(packageName + " (" + directory + ") does not appear to be a valid package");
        }
        return collectClasses(packageName, directory);
    }

    private ClassLoader getClassLoader() throws ClassNotFoundException {
        ClassLoader cld = Thread.currentThread().getContextClassLoader();
        if (cld == null) {
            throw new ClassNotFoundException("Can't get class loader.");
        }
        return cld;
    }

    private URL getResource(String packageName, ClassLoader cld) throws ClassNotFoundException {
        String path = packageName.replace('.', '/');
        URL resource = cld.getResource(path);
        if (resource == null) {
            throw new ClassNotFoundException("No resource for " + path);
        }
        return resource;
    }

    @SuppressWarnings("rawtypes")
    private List<Class> collectClasses(String packageName, File directory) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (directory.exists()) {
            String[] files = directory.list();
            for (String file : files) {
                if (file.endsWith(".class")) {
                    // removes the .class extension
                    classes.add(Class.forName(packageName + '.' + file.substring(0, file.length() - 6)));
                }
            }
        } else {
            throw new ClassNotFoundException(packageName + " is not a valid package");
        }
        return classes;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(NamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public enum Dialect {
        ORACLE("org.hibernate.dialect.Oracle10gDialect"),
        MYSQL("org.hibernate.dialect.MySQLDialect"),
        HSQL("org.hibernate.dialect.HSQLDialect"),
        H2("org.hibernate.dialect.H2Dialect"),
        POSTGRESQL("org.hibernate.dialect.PostgreSQLDialect");

        private String dialectClass;

        Dialect(String dialectClass) {
            this.dialectClass = dialectClass;
        }

        public String getDialectClass() {
            return dialectClass;
        }
    }

}
