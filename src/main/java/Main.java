public class Main {
    public static void main(String[] args) {
        User activeUser = new User();
        User inactiveUser = new User();
        Game g = new CheckersGame();
        g.start(activeUser,inactiveUser);
        Board b = new Board();

        //String addUser = "INSERT INTO users VALUES(4,'Tamar','23587',0)";
        Database db = new Database();
        //db.execution(addUser);
        System.out.println(db.toString());


    }
}
