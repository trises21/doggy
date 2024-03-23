package database;

import constants.ServerConstants;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

public class DatabaseConnection
{
  private static DataSource dataSource;
  private static GenericObjectPool connectionPool;
  private static String databaseName;
  private static int databaseMajorVersion;
  private static int databaseMinorVersion;
  private static String databaseProductVersion;
  public static final int CLOSE_CURRENT_RESULT = 1;
  public static final int KEEP_CURRENT_RESULT = 2;
  public static final int CLOSE_ALL_RESULTS = 3;
  public static final int SUCCESS_NO_INFO = -2;
  public static final int EXECUTE_FAILED = -3;
  public static final int RETURN_GENERATED_KEYS = 1;
  public static final int NO_GENERATED_KEYS = 2;

  public static synchronized void init()
  {
    if (dataSource != null) {
      return;
    }
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
    } catch (Throwable ex) {
      System.exit(1);
    }

    connectionPool = new GenericObjectPool();

    if (ServerConstants.MySQLMINCONNECTION > ServerConstants.MySQLMAXCONNECTION) {
      ServerConstants.MySQLMAXCONNECTION = ServerConstants.MySQLMINCONNECTION;
    }

    connectionPool.setMaxIdle(ServerConstants.MySQLMINCONNECTION);
    connectionPool.setMaxActive(ServerConstants.MySQLMAXCONNECTION);
    connectionPool.setTestOnBorrow(true);
    connectionPool.setMaxWait(5000L);
    try
    {
      dataSource = setupDataSource();
      Connection c = getConnection();
      DatabaseMetaData dmd = c.getMetaData();
      databaseName = dmd.getDatabaseProductName();
      databaseMajorVersion = dmd.getDatabaseMajorVersion();
      databaseMinorVersion = dmd.getDatabaseMinorVersion();
      databaseProductVersion = dmd.getDatabaseProductVersion();
      c.close();
    } catch (Exception e) {
      System.err.println("DB 초기화에 실패하였습니다. DB서버가 올바르게 켜져있는지, DB 사용자 설정은 올바른지 확인해주세요.\r\n" + e.toString());
      System.exit(1);
    }

    System.err.println("[VaconMaple] DB Version : " + databaseName + " " + databaseProductVersion);
  }

  private static DataSource setupDataSource() throws Exception {
    ConnectionFactory conFactory = new DriverManagerConnectionFactory(ServerConstants.dbHost, ServerConstants.dbUser, ServerConstants.dbPassword);

    new PoolableConnectionFactoryAE(conFactory, connectionPool, null, 1, false, true);

    return new PoolingDataSource(connectionPool);
  }

  public static synchronized void shutdown() {
    try {
      connectionPool.close();
    }
    catch (Exception e) {
    }
    dataSource = null;
  }

  public static Connection getConnection() throws SQLException {
    if (connectionPool.getNumIdle() == 0) {
      connectionPool.setMaxActive(Math.min(connectionPool.getMaxActive() + 1, 10000));
    }
    Connection con = dataSource.getConnection();

    return con;
  }

  public static int getActiveConnections() {
    return connectionPool.getNumActive();
  }

  public static int getIdleConnections() {
    return connectionPool.getNumIdle();
  }
}