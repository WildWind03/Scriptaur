package ru.nsu.fit.scriptaur.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.acra.annotation.AcraDialog;
import org.acra.dialog.CrashReportDialog;
import pub.devrel.easypermissions.EasyPermissions;
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.common.DefaultObserver;
import ru.nsu.fit.scriptaur.common.PreferencesUtils;
import ru.nsu.fit.scriptaur.common.videos.AllVideosSource;
import ru.nsu.fit.scriptaur.common.videos.SearchQueryVideosSource;
import ru.nsu.fit.scriptaur.common.videos.UsersVideosSource;
import ru.nsu.fit.scriptaur.fragments.AddVideoFragment;
import ru.nsu.fit.scriptaur.fragments.InfiniteVideoListFragment;
import ru.nsu.fit.scriptaur.fragments.ProfileFragment;
import ru.nsu.fit.scriptaur.network.ApiHolder;
import ru.nsu.fit.scriptaur.network.entities.User;

import java.util.List;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EasyPermissions.PermissionCallbacks {


    private SearchView searchView;

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

        final TextView usernameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_username);
        final String username = getIntent().getStringExtra(LoginActivity.USERNAME_KEY);
        if (username != null) {
            usernameView.setText(username);
        } else {
            ApiHolder.getBackendApi().getUser(PreferencesUtils.getToken(this))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<User>() {
                        @Override
                        public void onNext(final User user) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    usernameView.setText(user.getUserName());
                                }
                            });
                        }
                    });

        }
        switchToFragment(R.id.nav_videos);
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
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchQueryVideosSource searchQueryVideosSource = new SearchQueryVideosSource(
                        PreferencesUtils.getToken(DrawerActivity.this), query);
                Bundle bundle = new Bundle();
                bundle.putParcelable(InfiniteVideoListFragment.VIDEOS_SOURCE_KEY,
                        searchQueryVideosSource);
                InfiniteVideoListFragment videoListFragment = new InfiniteVideoListFragment();
                videoListFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_drawer, videoListFragment)
                        .addToBackStack(null)
                        .commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private void switchToFragment(int fragmentId) {
        switch (fragmentId) {
            case R.id.nav_videos: {
                InfiniteVideoListFragment videoListFragment = new InfiniteVideoListFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(InfiniteVideoListFragment.VIDEOS_SOURCE_KEY,
                        new AllVideosSource(PreferencesUtils.getToken(this)));
                videoListFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_drawer, videoListFragment)
                        .commit();
                break;
            }
            case R.id.nav_profile: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_drawer, new ProfileFragment())
                        .commit();
                break;
            }
            case R.id.nav_add_video: {
                AppCompatDialogFragment dialog = new AddVideoFragment();
                dialog.show(getSupportFragmentManager(), "Add video");
                break;
            }
            case R.id.nav_my_videos: {
                InfiniteVideoListFragment videoListFragment = new InfiniteVideoListFragment();
                Bundle bundle = new Bundle();
                String token = PreferencesUtils.getToken(this);
                bundle.putParcelable(InfiniteVideoListFragment.VIDEOS_SOURCE_KEY, new UsersVideosSource(token));
                videoListFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_drawer, videoListFragment)
                        .commit();
                break;
            }
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
