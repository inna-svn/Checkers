public class Main {
    public static void main(String[] args) {
        Game g = new CheckersGame();
        Board b = new Board();

        String updateUser = "UPDATE users SET userName='Tamara' where id=1";
        String selectQuery = "select * from users where userName='Tamaraffff';";
        String selectQueryNegativeTesting = "select * from users where userName='Tamara';";

        Database db = new Database();
        try {
        db.updateQuery(updateUser);
        System.out.println("result from select query is : " +db.executeQuery(selectQuery));
        System.out.println("result from select query is : " +db.executeQuery(selectQueryNegativeTesting));

            //     System.out.println(""+db.executeQuery(updateUser));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(db.toString());


    }
}
