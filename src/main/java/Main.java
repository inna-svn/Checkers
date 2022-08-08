public class Main {
    public static void main(String[] args) {
        Game g = new CheckersGame();
        Board b = new Board();

        String addUser = "UPDATE users SET userName=Tamara where id=4";
        Database db = new Database();
        //db.execution(addUser);
        try {
            System.out.println(""+db.executeQuery(addUser));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(db.toString());


    }
}
