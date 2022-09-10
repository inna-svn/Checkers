import com.google.common.base.Preconditions;

// Was not in project plan
public record Location(int x, int y) {

    enum Color {
        WHITE,
        BLACK
    }

    public Location.Color getColor() {
        if ((x + y) % 2 == 0) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    public Location {
        Preconditions.checkArgument(x >= 0 && x < 8);
        Preconditions.checkArgument(y >= 0 && y < 8);
    }

}
