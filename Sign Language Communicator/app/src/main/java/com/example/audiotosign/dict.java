package com.example.audiotosign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.example.audiotosign.R.raw.datasetisl1;
import static com.example.audiotosign.R.raw.finalcsv;
import static com.example.audiotosign.R.raw.trial;


public class dict extends AppCompatActivity {
    EditText textt;
    TextView err;
    ImageView but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);
        but = findViewById(R.id.searchic);
        textt = findViewById(R.id.input);
        err = findViewById(R.id.error);


        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texttt = textt.getText().toString().toLowerCase();
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                dictionaryhelper help = ds.getValue(dictionaryhelper.class);
                                String n = help.getwords();
                                String u = help.geturls();
                                if (n.equals(texttt)) {
                                    Intent intent = new Intent(dict.this, dictvideo.class);
                                    intent.putExtra("url", u);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}
