package com.soa_arah;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;



/**
 * Created by Lama on 31/03/18.
 */

public class aboutUs extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

      text=(TextView)findViewById(R.id.email);
//
//
//        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/Siddiqua.ttf");
//feedback.setText(Html.fromHtml("<a href=\"mailto:ask@me.it\">Send Feedback</a>"));
//        text.setTypeface(t);

        text.setText(Html.fromHtml("<a href=\"mailto:soaarah.swe@gmail.com\"> soaarah.swe@gmail.com</a> "));
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }





}

