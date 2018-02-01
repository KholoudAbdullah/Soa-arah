package com.soa_arah;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Lama on 31/01/18.
 */

public class home_page_register extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_register_activity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.viewaccount) {
            startActivity(new Intent(getApplicationContext(), view_account_register.class));
        } else if (item.getItemId() == R.id.editaccount) {
            startActivity(new Intent(getApplicationContext(), edit_account_register.class));
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
