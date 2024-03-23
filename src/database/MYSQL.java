package database;

import constants.ServerConstants;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class MYSQL
{
  private static final ThreadLocal<Connection> con = new ThreadLocalConnection();
  public static final int CLOSE_CURRENT_RESULT = 1;
  public static final int KEEP_CURRENT_RESULT = 2;
  public static final int CLOSE_ALL_RESULTS = 3;
  public static final int SUCCESS_NO_INFO = -2;
  public static final int EXECUTE_FAILED = -3;
  public static final int RETURN_GENERATED_KEYS = 1;
  public static final int NO_GENERATED_KEYS = 2;

  public static Connection getConnection()
  {
    return (Connection)con.get();
  }

  public static void closeAll() throws SQLException
  {
    for (Connection conn : ThreadLocalConnection.allConnections)
      conn.close();
  }

  private static final class ThreadLocalConnection extends ThreadLocal<Connection>
  {
    public static final Collection<Connection> allConnections = new LinkedList();

    protected final Connection initialValue()
    {
      try {
        Class.forName("com.mysql.jdbc.Driver");
      } catch (ClassNotFoundException e) {
        System.err.println("[오류] MYSQL 클래스를 발견할 수 없습니다.");
        if (!ServerConstants.realese) e.printStackTrace(); 
      }
      try
      {
        Connection con = DriverManager.getConnection(ServerConstants.dbHost, ServerConstants.dbUser, ServerConstants.dbPassword);

        allConnections.add(con);
        return con;
      } catch (SQLException e) {
        System.err.println("[오류] 데이터베이스 연결에 오류가 발생했습니다.");
        if (!ServerConstants.realese) e.printStackTrace(); 
      }
      return null;
    }
  }
}