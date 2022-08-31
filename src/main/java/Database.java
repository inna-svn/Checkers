import java.sql.*;


public class Database {
    private final Connection connection;
    private final Statement statement;
    private static final String url = "jdbc:mysql://localhost:3306/checkers";
    private static Database database = null;

    public Database() throws SQLException{
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            connection = DriverManager.getConnection(url,"root","zubur1");
            connection.setAutoCommit(true);
            statement = connection.createStatement();
        } catch (SQLException sqlException) {
          sqlException.printStackTrace();
          throw sqlException;
        }
    }

    public static Database getDatabase(){
        if (database == null) {
            try {
                database = new Database();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return database;
    }

    public Statement getStatement(){
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }


}
