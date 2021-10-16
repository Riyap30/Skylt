package com.example.audiotosign;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class signlanrec extends AppCompatActivity {
    private ImageView imgView, select;
    Button predict;
    private TextView tv;
    private Bitmap img;
    int c = 0, index;
    float max = 0;
    String fin;
    private int imgsize= 224;
    private String modelpath= "model_unquant.tflite";
    private String labelpath= "labels.txt";
    private Classifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signlanrec);


        imgView = (ImageView) findViewById(R.id.imtry);
        tv = (TextView) findViewById(R.id.conv);
        select = findViewById(R.id.buttonn);
        predict =  findViewById(R.id.buttonnn);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);

            }
        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    initClassifier();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                List<Classifier.Recognition> result = classifier.recognizeImage(img);
                tv.setText(result.get(0).toString());



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            imgView.setImageURI(data.getData());

            Uri uri = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initClassifier() throws IOException {
        classifier = new Classifier(getAssets(), modelpath, labelpath, imgsize);
    }



}
