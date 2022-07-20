import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws Exception {
        String URL = "jdbc:mysql://localhost:3306/";
        Connection connection = DriverManager.getConnection(URL);
        try {

            connection.setAutoCommit(false);
            String addEvent = "INSERT INTO event VALUES('hurricane','A tropical cyclone is a " +
                    "rapidly rotating storm system')";
            String addCity = "INSERT INTO city VALUES('New Orleans','Louisiana USA',384000)";
            String addDisaster = "INSERT INTO disaster VALUES('New Orleans',2021,'hurricane',50)";
            Statement statement = connection.createStatement();
            statement.execute(addEvent);
            statement.execute(addCity);
            statement.execute(addDisaster);
            connection.commit();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {}
        }
    }
}
