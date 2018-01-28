package mdstudios.deltahacks;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by mickeydang on 2018-01-27.
 */

public class FirebaseClient {


    public FirebaseClient() {

    }


    public void sendInfoToDB(List<Location> list) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("location");

        for (Location loc : list) {
            ref.child(loc.getName()).child("imageID").setValue(getInt(loc.getName()));
        }

    }

    private int getInt(String s) {

        if (s.equals("Communitech Area 151")) {
            return R.drawable.ic_communitech;
        } else if (s.equals("Chapters @Yonge")) {
            return R.drawable.ic_starbucks;
        } else if (s.equals("E5 6008")) {
            return R.drawable.ic_guelph_science_atrium;
        } else if (s.equals("E5 6006")) {
            return R.drawable.ic_richmond_city_centre;
        } else if (s.equals("City Public Library")) {
            return R.drawable.ic_mac_innis_lib;
        } else if (s.equals("Settlement Co @Queens")) {
            return (R.drawable.ic_settlement);
        } else if (s.equals("Science Complex")) {
            return R.drawable.ic_science_complex;
        } else {
            return 0;
        }

    }


}
