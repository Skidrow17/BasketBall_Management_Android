package com.uowm.ekasdym;

import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView name, profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.fab));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.add_restriction,R.id.weekly_matches,R.id.rank,R.id.rules,R.id.clarification,R.id.about_ekasdym,
                R.id.about_us,R.id.announcements,R.id.received_messages,R.id.sent_messages,R.id.my_restrictions,
                R.id.make_announcement,R.id.my_matches,R.id.password_change,R.id.image_change)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);

        name = headerView.findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("name"));

        profile = headerView.findViewById(R.id.profile);
        profile.setText(getIntent().getStringExtra("profile"));

        ImageView iv = headerView.findViewById(R.id.profile_pic);

        Glide.with(getApplicationContext())
             .load(getIntent().getStringExtra("profile_pic"))
             .into(iv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
