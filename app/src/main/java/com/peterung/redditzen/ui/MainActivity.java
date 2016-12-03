package com.peterung.redditzen.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.f2prateek.rx.preferences.Preference;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.Tracker;
import com.peterung.redditzen.R;
import com.peterung.redditzen.RedditZenApplication;
import com.peterung.redditzen.data.annotations.AccessToken;
import com.peterung.redditzen.data.annotations.Username;
import com.peterung.redditzen.data.api.RedditService;
import com.peterung.redditzen.data.repository.AccountRepository;
import com.peterung.redditzen.ui.login.LoginActivity;
import com.peterung.redditzen.ui.messages.MessagesFragment;
import com.peterung.redditzen.ui.profile.ProfileFragment;
import com.peterung.redditzen.ui.subreddit.SubredditFragment;
import com.peterung.redditzen.ui.subscriptions.SubscriptionsFragment;
import com.peterung.redditzen.utils.Strings;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.adView) AdView adView;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @Inject RedditService redditService;
    @Inject @AccessToken Preference<String> accessToken;
    @Inject @Username Preference<String> username;
    @Inject AccountRepository accountRepository;
    View login;
    TextView usernameTv;
    Menu navMenu;
    CompositeSubscription compositeSubscription;
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        RedditZenApplication.getComponent().inject(this);
        compositeSubscription = new CompositeSubscription();
        toolbar.setTitle("Reddit Zen");

        setupNavigationDrawer();
        setupAccessTokenObserver();
        setupUsernameObserver();
        setupAnalytics();
        setupAdMob();


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new SubredditFragment())
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_frontpage) {
            fragment = new SubredditFragment();
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
        } else if (id == R.id.nav_messages) {
            fragment = new MessagesFragment();
        } else if (id == R.id.nav_subscriptions) {
            fragment = new SubscriptionsFragment();
        } else if (id == R.id.nav_logout) {
            logout();
        }


        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment);

            fragmentTransaction.commit();
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    public void setupNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navMenu = navigationView.getMenu();
        login = navigationView.getHeaderView(0).findViewById(R.id.login);
        usernameTv = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
        });

        navigationView.setCheckedItem(R.id.nav_frontpage);
    }

    public void setupAccessTokenObserver() {
        Subscription subscription = accessToken.asObservable()
                .filter(token -> token != null)
                .subscribeOn(Schedulers.io())
                .concatMap(token -> {
                    return redditService.getAccount().subscribeOn(Schedulers.io());
                })
                .subscribeOn(Schedulers.io())
                .concatMap(account -> {
                    username.set(account.name);
                    return accountRepository.addAccount(account);
                })
                .subscribe(
                        success -> {
                            Timber.d("Successfully retrieved account information");
                        },
                        error -> {
                            Timber.e(error, "Error retrieving account information");

                        }
                );

        compositeSubscription.add(subscription);

    }

    public void setupUsernameObserver() {
        Subscription subscription = username.asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        name -> {
                            if (!Strings.isBlank(name)) {
                                login.setVisibility(View.GONE);
                                usernameTv.setVisibility(View.VISIBLE);
                                usernameTv.setText(name);
                                Timber.d("Updating nav header with username");
                            }
                        },
                        error -> {
                            Timber.e("Unable to update nav header with username");
                        }
                );

        compositeSubscription.add(subscription);
    }

    public void setupAnalytics() {
        RedditZenApplication app = (RedditZenApplication) getApplication();
        tracker = app.getDefaultTracker();
    }

    public void setupAdMob() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-8408361950220436~4943337903");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EF8312AB8959353039C91E0DEED8481C")
                .build();

        adView.loadAd(adRequest);
    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    public void logout() {

    }
}
