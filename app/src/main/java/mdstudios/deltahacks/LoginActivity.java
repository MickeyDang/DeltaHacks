package mdstudios.deltahacks;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText mName;
    EditText mPassword;
    Button mSubmit;
    private static final int MY_PERMISSIONS_REQUEST_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mName = findViewById(R.id.name);
        mSubmit = findViewById(R.id.signIn);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,
                            Manifest.permission.BLUETOOTH},
                    MY_PERMISSIONS_REQUEST_SMS);
        }

        final LoginHandler handler = new LoginHandler();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(handler.allFieldsFilled(mName)) {
                    handler.saveToSharedPrefs(getApplicationContext(),
                            handler.getName(mName));
                    goToCheckout();


                } else {
                    Toast.makeText(getApplicationContext(), "Not all fields filled!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
    }

    private void goToCheckout() {
        Intent intent = new Intent(this, LobbyActivity.class);
        startActivity(intent);
    }
}
