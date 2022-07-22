import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        Game g = new CheckersGame();
        Board b = new Board();
        String URL = "jdbc:mysql://localhost:3306/";
        try (Connection connection = DriverManager.getConnection(URL)) {
            connection.setAutoCommit(false);
            String addDisaster = "INSERT INTO disaster VALUES('New Orleans',2021,'hurricane',50)";
            Statement statement = connection.createStatement();
            statement.execute(addDisaster);
            connection.commit();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
