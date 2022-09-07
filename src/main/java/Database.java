import java.sql.*;


public class Database {
    private final Connection connection;
    private final Statement statement;
    private static final String url = "jdbc:mysql://localhost:3306/checkers";
    private static Database database = null;

    public Database() throws SQLException {
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            connection = DriverManager.getConnection(url, "root", "zubur1");
            connection.setAutoCommit(true);
            statement = connection.createStatement();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw sqlException;
        }
    }

    public static Database getDatabase() {
        if (database == null) {
            try {
                database = new Database();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                throw new RuntimeException(sqlException);
            }
        }
        return database;
    }

    public Statement getStatement() {
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public void createUser(String username, String password) throws User.SignUpError {

        String query = "INSERT INTO users(userName,password) VALUES(?,?)";

        try (PreparedStatement preparedStmt = getConnection().prepareStatement(query)) {

            preparedStmt.setString(1, username);
            preparedStmt.setString(2, password);

            preparedStmt.executeUpdate();

        } catch (SQLException e) {
            throw new User.SignUpError(e.toString());
        }
    }

    public User userSignIn(String username, String password) throws User.SignInError {

        User user;

        String query = "SELECT * FROM users WHERE userName=? AND password=? limit 1";

        try {
            try (PreparedStatement preparedStmt = getConnection().prepareStatement(query)) {

                preparedStmt.setString(1, username);
                preparedStmt.setString(2, password);

                ResultSet u = preparedStmt.executeQuery();

                if (u.next()) {

                    user = new User(u.getInt("id"), username);

                    return user;
                }

                throw new User.SignInError("User not found or password does not match");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new User.SignInError(e.toString());
        }
    }

    public int getUserId(User user) {
        int id;

        String username = user.getUsername();

        String queryId = "SELECT id FROM users WHERE userName= ? ";

        try (PreparedStatement preparedStmt = getConnection().prepareStatement(queryId)) {

            preparedStmt.setString(1, username);

            ResultSet u = preparedStmt.executeQuery();

            if (u.next()) {

                id = u.getInt("id");

                return id;
            }

            throw new SQLException("User does not exists");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getGameId(String gameName) {
        int id;

        String queryId = "SELECT id FROM games WHERE name= ? ";

        try (PreparedStatement preparedStmt = getConnection().prepareStatement(queryId)) {

            preparedStmt.setString(1, gameName);

            ResultSet g = preparedStmt.executeQuery();

            if (g.next()) {

                id = g.getInt("id");

                return id;
            }

            throw new SQLException("Game does not exists");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void createScore(User user, String game, int gamesNum, int winsNum, float rate) {
        int userId = getUserId(user);

        int gameId = getGameId(game);

        String query = "INSERT INTO scores VALUES(?,?,?,?,?)";

        try (PreparedStatement preparedStmt = getConnection().prepareStatement(query)) {

            preparedStmt.setInt(1, userId);
            preparedStmt.setInt(2, gameId);
            preparedStmt.setInt(3, gamesNum);
            preparedStmt.setInt(4, winsNum);
            preparedStmt.setFloat(5, rate);

            preparedStmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void saveRate(User user, float rate) {
        int id = getUserId(user);

        String saveRate = "UPDATE scores SET rate = ? WHERE userId = ? ";

        try (PreparedStatement preparedUpdate = getConnection().prepareStatement(saveRate)) {

            preparedUpdate.setFloat(1, rate);
            preparedUpdate.setInt(2, id);

            preparedUpdate.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
