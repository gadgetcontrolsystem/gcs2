package kz.gcs.upgrade;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class UpgradePgOperative {
    public static void main(String[] args) throws Exception {

        System.out.println(UpgradePgOperative.class.getClassLoader().getResource("").getPath());
        upgrade();

        System.out.println("Db Upgraded");
    }

    private static String PROPFILE = "/gcs.config/postgres.properties";
    static InputStream inputStream;

    public static void upgrade() throws Exception {

        init();
        Properties prop = new Properties();
        try {
            String propFileName = System.getProperty("user.home") + PROPFILE;

            inputStream = new FileInputStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }


        Class.forName("org.postgresql.Driver");

        Connection con = DriverManager.getConnection(prop.getProperty("db.url"), prop.getProperty("db.username"),
                prop.getProperty("db.password"));

        Database database = new PostgresDatabase();
        database.setConnection(new JdbcConnection(con));
        Liquibase liquibase = new Liquibase("kz/gcs/upgrade/pg/oper/changelog-master.xml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.update("");

        con.close();

    }

    private static void init() throws Exception {
        String appHome = System.getProperty("user.home");
        {
            File f = new File(appHome + PROPFILE);
            if (!f.exists()) createDbPropertiesFile(f);
        }
    }

    private static void createDbPropertiesFile(File f) throws Exception {
        f.getParentFile().mkdirs();
        PrintStream out = new PrintStream(f, "UTF-8");

        out.println("");
        out.println("db.url=" + "gcs");
        out.println("db.username=gcs");
        out.println("db.password=gcs");
        out.println("");


        out.close();
    }
}
