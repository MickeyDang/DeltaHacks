package mdstudios.deltahacks;

/**
 * Created by mickeydang on 2018-01-28.
 */

public class User {

    private String name;
    private String location;

    public User(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
