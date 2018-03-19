package com.pokeme;

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

import com.pokeme.store.Session;


public class DashboardActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView navigation;
    private Session session;

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
