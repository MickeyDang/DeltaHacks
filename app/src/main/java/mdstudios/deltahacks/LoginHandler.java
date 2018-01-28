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

    public void saveToSharedPrefs(Context c, String name) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().putString("name", name).apply();

    }

    public boolean allFieldsFilled(EditText et1) {
        return(!et1.getText().toString().isEmpty());
    }

    public void saveToFirebase(String name, String password) {

    }

}
