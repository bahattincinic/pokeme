package com.pokeme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.pokeme.fragments.CreateCategoryFragment;
import com.pokeme.fragments.CreateNoteFragment;
import com.pokeme.fragments.ListFragment;
import com.pokeme.fragments.ProfileUpdateFragment;
import com.pokeme.store.Session;


public class DashboardActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView navigation;
    private Session session;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu);

        session = Session.getInstance(this);
        navigation = (NavigationView) findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;
                Fragment fragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (id) {
                    case R.id.nav_logout:
                        session.clearSession();
                        intent = new Intent(DashboardActivity.this, WelcomeActivity.class);
                        break;
                    case R.id.nav_notes:
                        fragment = new ListFragment();
                        break;
                    case R.id.nav_add_note:
                        fragment = new CreateNoteFragment();
                        break;
                    case R.id.nav_add_category:
                        fragment = new CreateCategoryFragment();
                        break;
                    case R.id.nav_profile_update:
                        fragment = new ProfileUpdateFragment();
                        break;
                }

                if (intent != null) {
                    finish();
                    startActivity(intent);
                } else if (fragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }

                drawer.closeDrawers();

                return false;
            }
        });

        // default fragment.
        getSupportFragmentManager().beginTransaction().replace(
                R.id.content_frame, new ListFragment()
        ).commit();

        LinearLayout layout = (LinearLayout)findViewById(R.id.content_layout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
                return false;
            }
        });
    }

    protected void hideKeyboard(View view) {
        /*
        Hide Keyboard by touching screen outside keyboard
        */
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
