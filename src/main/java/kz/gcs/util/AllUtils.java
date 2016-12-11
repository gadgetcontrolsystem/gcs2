package kz.gcs.util;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AllUtils {
  public static final long MILLISECONDS_PER_DAY = 1000L * 60L * 60L * 24L;
  
  private static final BigDecimal C_100() {
    return new BigDecimal("100");
  }
  
  private static final BigDecimal C_0_5() {
    return new BigDecimal("0.5");
  }
  
  public static final SimpleDateFormat DMY() {
    return new SimpleDateFormat("dd/MM/yyyy");
  }
  
  public static final SimpleDateFormat DMY2() {
    return new SimpleDateFormat("dd/MM/yy");
  }
  
  public static final SimpleDateFormat YMD() {
    return new SimpleDateFormat("yyyy-MM-dd");
  }
  
  public static String toYMD(Date date, String onNullValue) {
    if (date == null) return onNullValue;
    return YMD().format(date);
  }
  
  public static final SimpleDateFormat _YMD_() {
    return new SimpleDateFormat("yyyyMMdd");
  }
  
  public static final SimpleDateFormat DMYP() {
    return new SimpleDateFormat("dd.MM.yyyy");
  }
  
  public static final SimpleDateFormat DMYHHMMSSP() {
    return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  }
  
  public static final SimpleDateFormat W3CDTF() {
    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  }
  
  public static final SimpleDateFormat FOR_FILE_NAME() {
    return new SimpleDateFormat("yyyyMMdd_HHmmss");
  }
  
  public static final SimpleDateFormat DMY_HMS() {
    return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  }
  
  public static final SimpleDateFormat YMD_HMS() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  }
  
  public static final SimpleDateFormat HMS() {
    return new SimpleDateFormat("HH:mm:ss");
  }
  
  private static long nextTraceID = 1L;
  private static final Object NEW_TRACE_ID_SYNCER = new Object();
  
  public static final long newTraceID() {
    synchronized (NEW_TRACE_ID_SYNCER) {
      return nextTraceID++;
    }
  }
  
  /**
   * Преобразует дату в вормат W3CDTF
   * 
   * @param date
   *          исходная дата
   * @return строка даты в формате W3CDTF, или пустая строка если на вход пришел
   *         null
   */
  public static String toW3CDTF(Date date) {
    if (date == null) return null;
    return W3CDTF().format(date);
  }
  
  /**
   * Преобразует дату в строку в формате DD/MM/YYYY
   * 
   * @param date
   *          преобразуемая дата
   * @param defaultValue
   *          возвращаемое значение на случай если date == null
   * @return полученная строка, или defaultValue, если date == null
   */
  public static String dateToStr(Date date, String defaultValue) {
    if (date == null) return defaultValue;
    return DMY().format(date);
  }
  
  /**
   * Преобразует логическое значение в строку в формате Да/Нет
   * 
   * @param b
   *          логическое значение
   * @return полученная строка
   */
  public static String boolToStr(boolean b) {
    return b ? "Да" :"Нет";
  }
  
  /**
   * Преобразует дату в строку в формате DD.MM.YYYY
   * 
   * @param date
   *          преобразуемая дата
   * @param defaultValue
   *          возвращаемое значение на случай если date == null
   * @return полученная строка, или defaultValue, если date == null
   */
  public static String dateToStrP(Date date, String defaultValue) {
    if (date == null) return defaultValue;
    return DMYP().format(date);
  }
  
  /**
   * Преобразует дату в строку в формате dd.MM.yyyy HH:mm:ss
   * 
   * @param date
   *          преобразуемая дата
   * @param defaultValue
   *          возвращаемое значение на случай если date == null
   * @return полученная строка, или defaultValue, если date == null
   */
  public static String dateToStrDateTimeP(Date date, String defaultValue) {
    if (date == null) return defaultValue;
    return DMYHHMMSSP().format(date);
  }
  
  /**
   * Преобразует время в строку в формате HH:mm:ss
   * 
   * @param date
   *          преобразуемая дата
   * @param defaultValue
   *          возвращаемое значение на случай если date == null
   * @return полученная строка, или defaultValue, если date == null
   */
  public static String timeToStrP(Date date, String defaultValue) {
    if (date == null) return defaultValue;
    return HMS().format(date);
  }
  

  
  public static void appendToSB(InputStream in, StringBuilder sb) {
    try {
      appendToSB0(in, sb);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public static void appendToSB0(InputStream in, StringBuilder sb) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    String line;
    while ((line = br.readLine()) != null) {
      sb.append(line);
      sb.append(System.getProperty("line.separator"));
    }
    br.close();
  }
  
  public static String streamToStr(InputStream in) {
    StringBuilder sb = new StringBuilder();
    appendToSB(in, sb);
    return sb.toString();
  }
  
  public static String streamToStr0(InputStream in) throws Exception {
    StringBuilder sb = new StringBuilder();
    appendToSB0(in, sb);
    return sb.toString();
  }
  
  /**
   * Вырезает из полного пути с именем файла только его имя. Если в конце слэш,
   * то он игнорируется
   * <p/>
   * Например: <code>
   * asd/asd/wow.xml -> wow.xml
   * /wow/asd/dsa/uu/mama/ -> mama
   * </code>
   * 
   * @param fullName
   *          полный путь к файлу или папке
   * @return только имя файла или папки
   */
  public static String extractBaseName(String fullName) {
    if (fullName.endsWith("/")) {
      fullName = fullName.substring(0, fullName.length() - 1);
    }
    int index = fullName.lastIndexOf('/');
    if (index < 0) return fullName;
    return fullName.substring(index + 1);
  }
  
  /**
   * Удаляет слэш из начала строки, если он там есть, иначе ни чего не делает
   * 
   * @param s
   *          исходная строка
   * @return результирующая строка
   */
  public static String killFirstSlash(String s) {
    if (s == null) return null;
    if (!s.startsWith("/")) return s;
    return s.substring(1);
  }
  

  
  public static void copyStreams(InputStream in, OutputStream out, int bufferSize)
      throws IOException {
    byte buffer[] = new byte[bufferSize];
    int readBytes;
    while ((readBytes = in.read(buffer)) != -1) {
      out.write(buffer, 0, readBytes);
    }
  }
  
  /**
   * Копирует данные из входного потока в выходной через буфер размером 2048
   * байт
   * 
   * @param in
   *          входной поток
   * @param out
   *          выходной поток
   * @throws IOException
   *           происходт в случае ошибки ввода/вывода
   */
  public static void copyStreams(InputStream in, OutputStream out) throws IOException {
    copyStreams(in, out, 2048);
  }
  
  /**
   * Извлекает из даты год
   * 
   * @param date
   *          исходная дата
   * @return год (или 0, если <code>date == null</code>)
   */
  public static int extractYear(Date date) {
    if (date == null) return 0;
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.get(Calendar.YEAR);
  }
  
  /**
   * Проверяют даты на то, указывают ли они на один и тотже день или нет
   * 
   * @param d1
   *          одна проверяемая дата
   * @param d2
   *          другая проверяемая дата
   * @return <true> - даты указывают на один и тотже день (т.е. у них одинаковый
   *         год, месяц и день), <code>false</code> - иначе
   */
  public static boolean isSameDay(Date d1, Date d2) {
    Calendar c = Calendar.getInstance();
    c.setTime(d1);
    int year1 = c.get(Calendar.YEAR);
    int month1 = c.get(Calendar.MONTH);
    int day1 = c.get(Calendar.DAY_OF_MONTH);
    c.setTime(d2);
    return year1 == c.get(Calendar.YEAR) && month1 == c.get(Calendar.MONTH)
        && day1 == c.get(Calendar.DAY_OF_MONTH);
  }
  
  /**
   * Проверяет, находиться ли одна дата между двумя другими (время всех дат
   * игнорируется)
   * 
   * @param date
   *          проверяемая дата
   * @param from
   *          начало периода
   * @param to
   *          конец периода
   * @return <code>from &lt;= date and date &lt;= to</code>
   */
  public static boolean between(Date date, Date from, Date to) {
    if (isSameDay(date, from)) return true;
    if (isSameDay(date, to)) return true;
    if (date.compareTo(from) < 0) return false;
    if (date.compareTo(to) > 0) return false;
    return true;
  }
  
  /**
   * Переводить дату-время на начало дня (0 ч. 0 мин. 0 сек. 0 мсек.)
   * 
   * @param date
   *          исходная дата
   * @return переведенная дата
   */
  public static Date toBeginDay(Date date) {
    if (date == null) return null;
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }
  
  /**
   * Переводить дату-время на конец дня (23 ч. 59 мин. 59 сек. 0 мсек.)
   * 
   * @param date
   *          исходная дата
   * @return переведенная дата
   */
  public static Date toEndDay(Date date) {
    if (date == null) return null;
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }
  
  public static Date strToDate_DMY_HMS(String str) {
    if (str == null) return null;
    str = str.trim();
    if (str.length() == 0) return null;
    try {
      if (str.indexOf(':') > -1) {
        return DMY_HMS().parse(str);
      }
      return DMY().parse(str);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static Date strToDate_DMY2(String str) {
    if (str == null) return null;
    str = str.trim();
    if (str.length() == 0) return null;
    try {
      return DMY2().parse(str);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static Date strToDate_DMYP(String str) {
    if (str == null) return null;
    str = str.trim();
    if (str.length() == 0) return null;
    try {
      return DMYP().parse(str);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static BigDecimal strToBD(String s) {
    if (s == null) return null;
    s = s.trim();
    if (s.length() == 0) return null;
    s = s.replace(',', '.');
    return new BigDecimal(s);
  }
  
  //	public static OrganizationUnit extractBranch(User u) {
  //		OrganizationUnit branch = u.getOrgUnit();
  //		while (branch != null
  //			&& !OrganizationUnit.Type.BRANCH.equals(branch.getType())) {
  //			branch = branch.getParent();
  //		}
  //		return branch;
  //	}
  
  public static int rnd(int max) {
    return (int)(Math.random() * max);
  }
  
  public static <T> T getRndElem(T[] mas) {
    return mas[rnd(mas.length)];
  }
  
  public static <T> T getRndElem(List<T> list) {
    return list.get(rnd(list.size()));
  }
  
  /**
   * Округляет число до двух знаков после запятой
   * 
   * @param x
   *          исходное число
   * @return округленное число
   */
  public static BigDecimal round2(BigDecimal x) {
    if (x == null) return null;
    boolean negative = false;
    if (x.compareTo(BigDecimal.ZERO) < 0) {
      x = x.negate();
      negative = true;
    }
    x = x.multiply(C_100());
    x = x.add(C_0_5());
    x = new BigDecimal(x.toBigInteger());
    x = x.divide(C_100());
    if (negative) x = x.negate();
    return x;
  }
  
  /**
   * Возвращает количество дней между датами (d1 - d2)
   * 
   * @param d1
   *          первая исходная дата
   * @param d2
   *          вторая исходная дата
   * @return разница в днях (если d1 раньше чем d2, то возвращается < 0)
   */
  public static int daysBetween(Date d1, Date d2) {
    long d1Days = d1.getTime() / MILLISECONDS_PER_DAY;
    long d2Days = d2.getTime() / MILLISECONDS_PER_DAY;
    return (int)(d1Days - d2Days);
  }
  
  static final long ONE_HOUR = 60 * 60 * 1000L;
  
  public static int daysBetweenTimeIndifferent(Date d1, Date d2) {
    d1 = toBeginDay(d1);
    d2 = toBeginDay(d2);
    return (int)((d1.getTime() - d2.getTime() + ONE_HOUR) / (ONE_HOUR * 24));
  }
  
  public static boolean pingPort(String host, int port) {
    try {
      new Socket(host, port).close();
    } catch (Exception e) {
      return false;
    }
    return true;
  }
  
  public static String resourceToStr(Class<?> cl, String resourceName) {
    try {
      StringBuilder ret = new StringBuilder();
      BufferedReader br = new BufferedReader(new InputStreamReader(
          cl.getResourceAsStream(resourceName), "UTF-8"));
      String line;
      boolean first = true;
      while ((line = br.readLine()) != null) {
        if (first) first = false;
        else
          ret.append('\n');
        ret.append(line);
      }
      br.close();
      return ret.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Добавляет (отнимает) к дате указанное количество дней
   * 
   * @param date
   *          дата
   * @param days
   *          дни (+ или -)
   * @return новая дата (date+days)
   */
  public static Date dateAdd(Date date, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DAY_OF_MONTH, days);
    return cal.getTime();
  }
  
  public static String getVersion() {
    Package p = AllUtils.class.getPackage();
    return p.getSpecificationVersion();
  }
  
  /**
   * Глубокое копирование объекта.
   * 
   * @param o
   *          Объект копирования
   * @return копия объекта
   */
  @SuppressWarnings("unchecked")
  public static <T> T deepCopy(T o) {
    T newObj;
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(o);
      ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bis);
      
      newObj = (T)ois.readObject();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return newObj;
  }
  
  /**
   * Computes an md5 hash of a string.
   * 
   * @param text
   *          the hashed string
   * @return the string hash
   * @throws NullPointerException
   *           if text is null
   */
  public static byte[] md5(String text) {
    // arguments check
    if (text == null) {
      throw new NullPointerException("null text");
    }
    
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(text.getBytes());
      return md.digest();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      throw new RuntimeException("Cannot find MD5 algorithm");
    }
  }
  
  /**
   * Computes an md5 hash and returns the result as a string in hexadecimal
   * format.
   * 
   * @param text
   *          the hashed string
   * @return the string hash
   * @throws NullPointerException
   *           if text is null
   */
  public static String md5AsHexString(String text) {
    return toHexString(md5(text));
  }
  
  /**
   * Returns a string in the hexadecimal format.
   * 
   * @param bytes
   *          the converted bytes
   * @return the hexadecimal string representing the bytes data
   * @throws IllegalArgumentException
   *           if the byte array is null
   */
  public static String toHexString(byte[] bytes) {
    if (bytes == null) {
      throw new IllegalArgumentException("byte array must not be null");
    }
    
    StringBuffer hex = new StringBuffer(bytes.length * 2);
    for (int i = 0; i < bytes.length; i++) {
      hex.append(Character.forDigit((bytes[i] & 0XF0) >> 4, 16));
      hex.append(Character.forDigit((bytes[i] & 0X0F), 16));
    }
    return hex.toString();
  }
  
  /**
   * сумма массива аргументов
   * 
   * @param args
   *          аргументы
   * @return сумма
   */
  public static BigDecimal sum(BigDecimal... args) {
    BigDecimal sum = BigDecimal.ZERO;
    for (BigDecimal arg : args) {
      if (arg != null) sum = sum.add(arg);
    }
    return sum;
  }
  
  /**
   * количество месяцев в интервале между двумя датами
   * 
   * @param dateTo
   *          конец интервала
   * @param dateFrom
   *          начало интервала
   * @return количество месяцев
   */
  public static int monthBetween(Date dateTo, Date dateFrom) {
    
    if (dateTo == null) return 0;
    if (dateFrom == null) return 0;
    
    toBeginDay(dateTo);
    toBeginDay(dateFrom);
    Calendar calTo = Calendar.getInstance();
    Calendar calFrom = Calendar.getInstance();
    calTo.setTime(dateTo);
    calFrom.setTime(dateFrom);
    
    int i = 0;
    while (calTo.compareTo(calFrom) > 0) {
      calTo.add(Calendar.MONTH, -1);
      i++;
    }
    
    return i;
  }
}
