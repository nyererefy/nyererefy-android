package com.konektedi.vs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.konektedi.vs.Home.Elections.Elections;
import com.konektedi.vs.Motions.Motions;
import com.konektedi.vs.News.News;
import com.konektedi.vs.Student.Login;
import com.konektedi.vs.Student.Settings;
import com.konektedi.vs.Student.StudentPreferences;
import com.konektedi.vs.Student.StudentProfile;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    Intent intent;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        contextOfApplication = getApplicationContext();

        showFragment(new Elections());
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
        //TO UNDERSTAND WHAT IS DONE HERE
        //https://stackoverflow.com/questions/7491287/android-how-to-use-sharedpreferences-in-non-activity-class
    }

    private boolean showFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.messenger_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.title_elections:
                fragment = new Elections();
                break;
            case R.id.title_motions:
                fragment = new Motions();
                break;
            case R.id.title_news:
                fragment = new News();
                break;
        }
        return showFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                intent = new Intent(MainActivity.this, StudentProfile.class);
                startActivity(intent);
                break;
            case R.id.logout:
                StudentPreferences.clearPreferences(this);
                intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkLogin() {
        Boolean is_logged_in = StudentPreferences.getLoginPreference(this);

        if (!is_logged_in) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }

}
