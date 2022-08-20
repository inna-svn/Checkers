import java.sql.*;


public class Database {
    private Connection connection = null;
    private Statement statement = null;
    private static final String url = "jdbc:mysql://localhost:3306/checkers";
    private static Database database = null;

    public Database() {
        try {
            connection = DriverManager.getConnection(url,"root","zubur1");
            connection.setAutoCommit(true);
            statement = connection.createStatement();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static Database getDatabase(){
        if (database == null)
            database = new Database();
        return database;
    }

    public Statement getStatement(){
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }


}
