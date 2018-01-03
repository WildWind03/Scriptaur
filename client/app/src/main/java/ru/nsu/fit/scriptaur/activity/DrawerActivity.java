package ru.nsu.fit.scriptaur.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.fragments.VideoListFragment;
import ru.nsu.fit.scriptaur.network.entities.Video;

import java.util.ArrayList;
import java.util.Arrays;
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

        switchToFragment(R.id.nav_video);
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

    private void switchToFragment(int fragmentId) {
        switch (fragmentId) {
            case R.id.nav_video: {
                Fragment fragment = new VideoListFragment();
                Bundle bundle = new Bundle();

                ArrayList<Video> videos = new ArrayList<>();
                // Todo: only for debug
                for (int i = 0; i < 10; ++i) {
                    videos.addAll(Arrays.asList(
                            new Video(1, "VNqNnUJVcVs", "video 1", "image url 1",100, 0, "0", 4.5f, 10, false),
                            new Video(2, "CW5oGRx9CLM","video 2", "image url 2",200, 0, "0", 5.0f, 15, true),
                            new Video(3, "FBnAZnfNB6U","video 3", "image url 3",300, 0, "0", 1.488f, 1, false)));
                }

                bundle.putParcelableArrayList(VideoListFragment.VIDEOS_LIST_KEY, videos);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_drawer, fragment)
                        .commit();

            }
            /*case R.id.nav_api: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_drawer, new SingleVideoFragment())
                        .commit();
            }*/
            //TODO user profile fragment, add video
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switchToFragment(item.getItemId());
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
