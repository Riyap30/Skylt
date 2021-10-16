package com.example.audiotosign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.audiotosign.R.raw.finalcsv;

public class signlan extends AppCompatActivity {
    private ImageView image1;
    private int c, d = 0;
    private TextView acqtext;
    ArrayList<String> linesList = new ArrayList<>();
    String csvFile = "raw\\transformed.csv";
    InputStream inputStream;
    String showtext;
    String arr[];
    String x, y, id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signlan);
        Intent i = getIntent();
        String st = i.getStringExtra("text");
        String temp = st;
        st = st.replaceAll(" ", "");
        image1 = (ImageView) findViewById(R.id.imageView1);
        acqtext = findViewById(R.id.actext);
        showtext = "Fingerspelling for '" + temp + "' is:";


            nextImage(st);

    }

    public void nextImage(String msg) {
        acqtext.setText(showtext);
        acqtext.setTextColor(Color.parseColor("#3edbf0"));
        String[] alphabet = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        List<String> list = Arrays.asList(alphabet);
        int a;
        a = msg.length();
        c = 0;
        if (d > c) {
            c = d;
        }
        String img = String.valueOf(msg.charAt(c));
        if (list.contains(img)) {
            String path = "@drawable/" + img;
            int imageResource = getResources().getIdentifier(path, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            image1.setImageDrawable(res);
            c++;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (c < a) {
                    nextImage(msg);
                    d = c;
                }
            }
        }, 650);

    }


}

