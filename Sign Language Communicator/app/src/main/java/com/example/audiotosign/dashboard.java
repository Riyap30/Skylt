package com.example.audiotosign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class dashboard extends AppCompatActivity {
    ImageView audio, sign, dict, rec;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawyerlayout;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        audio= findViewById(R.id.sp);
        sign= findViewById(R.id.si);
        dict= findViewById(R.id.di);
        rec= findViewById(R.id.re);

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this,audio.class);
                startActivity(intent);
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Intent intent = new Intent(dashboard.this, vidid.class);
                startActivity(intent);
            }
        });

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, signlanrec.class);
                startActivity(intent);
            }
        });

        dict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard.this, dict.class);
                startActivity(intent);

            }
        });

        toolbar= (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=(NavigationView)findViewById(R.id.navmenu);
        drawyerlayout =(DrawerLayout)findViewById(R.id.drawer);

        toggle= new ActionBarDrawerToggle(this,drawyerlayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawyerlayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.menu_med :
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.talkinghands.co.in/"));
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"opening",Toast.LENGTH_LONG).show();
                        drawyerlayout.closeDrawer(GravityCompat.START);
                        break;

                }
                return true;
            }
        });

    }
}