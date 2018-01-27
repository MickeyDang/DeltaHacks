package mdstudios.deltahacks;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;

/**
 * Created by mickeydang on 2018-01-27.
 */

public class LoginHandler {
    //make variables

    public LoginHandler () {

    }

    public String getName(EditText et) {
        return et.getText().toString();
    }

    public String getPassword(EditText et) {
        return et.getText().toString();
    }

    public void saveToSharedPrefs(Context c, String name, String password) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString("name", name).apply();
        prefs.edit().putString("password", password).apply();


    }

    public void saveToFirebase(String name, String password) {

    }

}
