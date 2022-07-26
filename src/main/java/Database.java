import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;

@ApplicationScoped
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

    private User makeUserObj(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE userName=? AND password=? limit 1";

        try (PreparedStatement preparedStmt = getConnection().prepareStatement(query)) {
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, password);
            ResultSet u = preparedStmt.executeQuery();
            if (u.next()) {

                return new User(u.getInt("id"), username);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new SQLException("User not found or password does not match");
    }

    public User createUser(String username, String password) throws User.SignUpError {
        String query = "INSERT INTO users(userName,password) VALUES(?,?)";

        try (PreparedStatement preparedStmt = getConnection().prepareStatement(query)) {
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, password);
            preparedStmt.executeUpdate();

            return makeUserObj(username, password);

        } catch (SQLException e) {
            throw new User.SignUpError(e.toString());
        }
    }

    public User getUser(String username, String password) throws User.SignInError {
        try {

            return makeUserObj(username, password);

        } catch (SQLException e) {
            throw new User.SignInError(e.toString());
        }
    }

    private int getId(String queryId, String queryParameter, String objectName) {
        try (PreparedStatement preparedStmt = getConnection().prepareStatement(queryId)) {
            preparedStmt.setString(1, queryParameter);
            ResultSet u = preparedStmt.executeQuery();
            if (u.next()) {
                return u.getInt("id");
            }
            throw new SQLException(objectName + " does not exists");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getUserId(User user) {
        return getId("SELECT id FROM users WHERE userName= ? ", user.getUsername(), "User");
    }

    public int getGameId(String gameName) {
        return getId("SELECT id FROM games WHERE name= ? ", gameName, "Game");
    }

    public void createScore(User user, Class<? extends Game> gameClass, int gamesNum, int winsNum, float rate) {
        int userId = getUserId(user);
        int gameId;
        try {
            gameId = getGameId(gameClass.getField("NAME").get(null).toString());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

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

    public void updateScore(User user, Class<? extends Game> gameClass, int gamesNum, int winsNum, float rate) {
        int userId = getUserId(user);
        int gameId;
        try {
            gameId = getGameId(gameClass.getField("NAME").get(null).toString());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        String updScores = "UPDATE scores SET gamesNum = ? , winsNum = ? , rate = ? WHERE userId = ? AND gameId = ?";

        try (PreparedStatement preparedUpdate = getConnection().prepareStatement(updScores)) {
            preparedUpdate.setInt(1, gamesNum);
            preparedUpdate.setInt(2, winsNum);
            preparedUpdate.setFloat(3, rate);
            preparedUpdate.setInt(4, userId);
            preparedUpdate.setInt(5, gameId);
            preparedUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public UserGameScore getScore(User user, Class<? extends Game> gameClass) {
        int userId = getUserId(user);
        int gameId;
        try {
            gameId = getGameId(gameClass.getField("NAME").get(null).toString());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT * FROM scores WHERE userId = ? AND gameId = ?";

        try (PreparedStatement preparedStmt = getConnection().prepareStatement(query)) {
            preparedStmt.setInt(1, userId);
            preparedStmt.setInt(2, gameId);
            ResultSet u = preparedStmt.executeQuery();
            if (u.next()) {

                return new UserGameScore(user, gameClass, u.getInt("gamesNum"), u.getInt("winsNum"), u.getFloat("rate"));

            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
