package com.googlecodelabs.example.backupexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoggedInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // You do not need to edit this activity

        setContentView(R.layout.logged_in_activity);

        TextView prefsDetails = findViewById(R.id.prefs_details);
        prefsDetails.setText(PrefUtils.getDebugText(this));

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefUtils.logout(LoggedInActivity.this);
                Intent intent = new Intent(LoggedInActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // You do not need to edit this activity
    }
}
