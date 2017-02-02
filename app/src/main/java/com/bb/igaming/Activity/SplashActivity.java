package com.bb.igaming.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.bb.igaming.Model.AnswerManager;
import com.bb.igaming.Model.Settings;
import com.bb.igaming.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private String _lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // calc scale info
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Settings.scale = dm.widthPixels / 1080.0f;
        Settings.wide = 1.0*dm.widthPixels/dm.heightPixels > 1080.0/1650;

        findViewById(R.id.ivLogo).setLayoutParams(
                new LinearLayout.LayoutParams((int) (Settings.scale * 652), (int) (Settings.scale * 817))
        );

        _lang = Locale.getDefault().getLanguage();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.ivLogo).setVisibility(View.INVISIBLE);
                findViewById(R.id.pbLoading).setVisibility(View.VISIBLE);
                loadResources();
            }
        }, 2000);
    }

    private void loadResources() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Resources");
        query.whereEqualTo("lang", _lang);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0 && !_lang.equals("en")) {
                        _lang = "en";
                        loadResources();
                        return;
                    }

                    for (ParseObject object : objects) {
                        Settings.instance.resources.put(object.getString("key"), object.getString("value"));
                    }
                }
                loadAnswers();
            }
        });
    }

    private void loadAnswers() {
        AnswerManager.instance.init(this, new AnswerManager.CallBack() {
            @Override
            public void done() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
