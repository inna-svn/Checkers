import com.google.common.base.Preconditions;

import java.util.stream.Stream;

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

    public static Stream<Location> stream() {
        Stream.Builder<Location> builder = Stream.builder();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                builder.add(new Location(row, col));
            }
        }
        return builder.build();
    }

}
