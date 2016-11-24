package kz.gcs.util;

import kz.gcs.upgrade.UpgradePgOperative;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



/**
 * Слушатель контекста запуск и завершение приложения
 */
public class ContextListener implements ServletContextListener {
//  private final Logger log = Logger.getLogger(getClass());
  
  @Override
  public void contextInitialized(ServletContextEvent event) {
    System.out.println("Start GCS application...");
    
    System.err.println("*********************************");
    System.err.println("*********************************");
    System.err.println("** HOME USER DIR: " + System.getProperty("user.home"));
    System.err.println("*********************************");
    System.err.println("*********************************");
    
    updateDB();
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent event) {
    System.out.println("Stop GCS application...");
  }
  

  private void updateDB() {
    
    {
      File sf = new File(System.getProperty("user.home") + "/gcs.config/do.not.upgrade.db");
      if (sf.exists()) {
        System.out.println("Skip upgrading DB because exists file " + sf);
        return;
      }
    }
    
    try {
      System.out.println("Upgrade database");
      UpgradePgOperative.upgrade();
    } catch (Throwable e) {
      e.printStackTrace();
      System.err.println("Ошибка при обновлении БД");
      throw new RuntimeException("Ошибка при обновлении БД", e);
    }
  }
}