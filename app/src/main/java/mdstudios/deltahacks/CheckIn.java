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
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                cc.textToServerPhone(makePI(), prefs.getString("name", "anon"));
            }
        });

    }

    private PendingIntent makePI() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent intent = new Intent();
        intent.putExtra("name", prefs.getString("name", "Mickey"));
        intent.putExtra("signedIn", true);
        return PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
    }
}
