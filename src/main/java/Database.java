import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection = null;
    private Statement statement = null;
    private static String url = "jdbc:mysql://localhost:3306/checkers";
    private static Database database = null;

    public Database() {
        try {
            connection = DriverManager.getConnection(url,"root","zubur1");
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            connection.commit();
        } catch (
                SQLException sqlException) {
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

    public String getConnectionStatus(){
        String connected = "Connection to DB is opened.";
        boolean closed = true;
        try{
            closed = connection.isClosed();
        } catch(
                SQLException sqlException) {
            sqlException.printStackTrace();
        }
        if (!closed)
            return connected;
        return "Connection to DB is closed.";
    }

    public void execution(String query){
        try {
            getStatement().execute(query);
            getConnection().commit();
        } catch (
                SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    public String toString() {
        return url + "\n" + getConnectionStatus();
    }

}
