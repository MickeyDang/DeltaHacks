package mdstudios.deltahacks;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckIn extends AppCompatActivity {

    Button mCheckIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);


        mCheckIn = findViewById(R.id.checkIn);
        final CommunicationClient cc = new CommunicationClient();


        mCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cc.textToServerPhone(makeBundle());
            }
        });

    }

    private Bundle makeBundle() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString(Utils.NAME_KEY, prefs.getString("name", "Mickey"));
        bundle.putString(Utils.STATUS_KEY, "true");
        bundle.putString(Utils.LOC_KEY, "E5 Room 6008");
        return bundle;
    }
}
