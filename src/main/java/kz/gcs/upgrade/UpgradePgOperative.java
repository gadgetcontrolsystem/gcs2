package kz.gcs.upgrade;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
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
  static InputStream inputStream;
  public static void upgrade() throws Exception {

    Properties prop = new Properties();
    try {
      String propFileName = System.getProperty("user.home") + "/gcs.config/postgres.properties";

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
}
