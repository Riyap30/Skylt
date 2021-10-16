package com.example.audiotosign;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import android.os.Bundle;

import static com.example.audiotosign.R.raw.finalcsv;

public class audio extends AppCompatActivity {
    ArrayList<String> result;

    private ImageView iv_micc;
    private TextView tv_Speech_to_text;
    private ImageView nextt;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        iv_micc = findViewById(R.id.iv_mic);
        tv_Speech_to_text = findViewById(R.id.tv_speech_to_text);


        iv_micc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast
                            .makeText(audio.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                tv_Speech_to_text.setText(
                        Objects.requireNonNull(result).get(0));
                check(Objects.requireNonNull(result).get(0));
            }
        }
    }


    protected void check(String txt) {
        txt = txt.toLowerCase();

        String line = "";
        String splitBy = ",";
        String col1[];
        col1 = new String[76];
        String col2[];
        col2 = new String[76];
        String finarr[];
        finarr = new String[76];
        try {
//parsing a CSV file into BufferedReader class constructor

            BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(finalcsv)));
            int m = 0;
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] videos = line.split(splitBy);    // use comma as separator
                col1[m] = videos[0];
                col2[m] = videos[1];
                m++;
            }

            int t, g=0;
            for (t = 0; t < 76; t++) {
                if (col1[t].equals(txt)) {
                    String a = col1[t];
                    String b = col2[t];
                    g=1;
                    Intent intent = new Intent(audio.this, disvideo.class);
                    intent.putExtra("names", col1[t]);
                    intent.putExtra("url", col2[t]);
                    intent.putExtra("text", txt);
                    startActivity(intent);
                }
            }
            if (g==0){
                Intent intent = new Intent(audio.this, signlan.class);
                intent.putExtra("text", txt);
                startActivity(intent);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
