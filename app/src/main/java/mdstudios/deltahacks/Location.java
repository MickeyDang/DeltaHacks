package mdstudios.deltahacks;

/**
 * Created by mickeydang on 2018-01-27.
 */

public class Location {

    private String mName;
    private int mPeople;
    private String mCapacity;
    private int mResourceID;

    public Location (String name, int people, String cap, int res) {
        mName = name;
        mPeople = people;
        mCapacity = cap;
        mResourceID = res;
    }

    public String getName() {
        return mName;
    }

    public int getPeople() {
        return mPeople;
    }

    public String getCapacity() {
        return mCapacity;
    }

    public int getResourceID() {
        return mResourceID;
    }
}
