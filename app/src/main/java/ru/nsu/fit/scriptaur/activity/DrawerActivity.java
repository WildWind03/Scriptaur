package ru.nsu.fit.scriptaur.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import pub.devrel.easypermissions.EasyPermissions;
import ru.evtushenko.english.R;
import ru.nsu.fit.scriptaur.fragments.RegistrationFragment;
import ru.nsu.fit.scriptaur.fragments.SingleVideoFragment;

import java.util.List;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new SingleVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SingleVideoFragment.VIDEO_ID_KEY, "VNqNnUJVcVs");
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_drawer, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_video) {
            Fragment fragment = new SingleVideoFragment();
            Bundle bundle = new Bundle();
            bundle.putString(SingleVideoFragment.VIDEO_ID_KEY, "VNqNnUJVcVs");
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_drawer, fragment)
                    .commit();
        } else if (id == R.id.nav_api) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_drawer, new SingleVideoFragment())
                    .commit();
        } else if (id == R.id.nav_register){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_drawer, new RegistrationFragment())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }


}
