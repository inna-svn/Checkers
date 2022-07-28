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
        try (
                Connection connection = DriverManager.getConnection(url,"root","zubur1")) {
            connection.setAutoCommit(false);
            //String addUser = "INSERT INTO users VALUES(1,'Inna','12345',0)";
            Statement statement = connection.createStatement();
            System.out.println("Hello from Database()");
            //statement.execute(addUser);
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

    public void execution(String query){
        try
                {
            //getConnection().setAutoCommit(false);
            //String addUser = "INSERT INTO users VALUES(1,'Inna','12345',0)";
            //Statement statement = connection.createStatement();
            getStatement().execute(query);
            getConnection().commit();
        } catch (
                SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    public String toString() {
        return toString();
    }

}
