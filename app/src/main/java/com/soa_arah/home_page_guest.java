package com.soa_arah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home_page_guest extends AppCompatActivity {

    private Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_guest_activity);

        button = (Button)findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(new Intent(getApplicationContext(), home_page_register.class));
            }
        });
    }
}
