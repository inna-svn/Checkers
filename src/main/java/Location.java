import com.google.common.base.Preconditions;

// Was not in project plan
public record Location(int x, int y) {
    public Location {
        Preconditions.checkArgument(x >= 0 && x < 8);
        Preconditions.checkArgument(y >= 0 && y < 8);
    }
}
