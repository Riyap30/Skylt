package com.example.audiotosign;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Classifier {

    private Interpreter interpreter;
    private List<String> labellist;
    private int inputsize;
    private int pixelsize = 3;
    private int imagemean = 0;
    private float imagestd = 255.0f;
    private float maxresults = 26;
    private float threshhold = 0.4f;

    Classifier(AssetManager assetManager, String mmodelpath, String mlabelpath, int minputsize) throws IOException {
        inputsize = minputsize;
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(5);
        options.setUseNNAPI(true);
        interpreter = new Interpreter(loadModelFile(assetManager, mmodelpath), options);
        labellist = loadLabelList(assetManager, mlabelpath);
    }

    class Recognition {

        String id = "";
        String title = "";
        float confidence = 0F;

        public Recognition(String i, String s, float confidence) {
            id = i;
            title = s;
            this.confidence = confidence;
        }

        @NonNull
        @Override

        public String toString() {
            return "Letter Predicition = " + title ;
        }
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String MODEL_FILE) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);

    }

    private List<String> loadLabelList(AssetManager assetManager, String labpath) throws IOException {
        List<String> labellist = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(labpath)));
        String line;
        while ((line = reader.readLine()) != null) {
            labellist.add(line);
        }
        reader.close();
        return labellist;
    }

    List<Recognition> recognizeImage(Bitmap bitmap) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputsize, inputsize, false);
        ByteBuffer byteBuffer = convertBitmapToByteBuffer(scaledBitmap);
        float[][] result = new float[1][labellist.size()];
        interpreter.run(byteBuffer, result);
        return getSortedResultFloat(result);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer;
        byteBuffer = ByteBuffer.allocateDirect(4 * inputsize * inputsize * pixelsize);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intvalues = new int[inputsize * inputsize];
        bitmap.getPixels(intvalues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < inputsize; ++i) {
            for (int j = 0; j < inputsize; ++j) {
                final int val = intvalues[pixel++];

                byteBuffer.putFloat((((val >> 16) & 0xff) - imagemean) / imagestd);
                byteBuffer.putFloat((((val >> 8) & 0xff) - imagemean) / imagestd);
                byteBuffer.putFloat((((val) & 0xff) - imagemean) / imagestd);
            }
        }
        return byteBuffer;
    }

    private List<Recognition> getSortedResultFloat(float[][] labelprobarray) {

        PriorityQueue<Recognition> pq = new PriorityQueue<>((int) maxresults, new Comparator<Recognition>() {
            @Override
            public int compare(Recognition rhs, Recognition lhs) {
                return Float.compare(rhs.confidence, lhs.confidence);
            }
        });

        for (int i = 0; i < labellist.size(); ++i) {
            float confidence = labelprobarray[0][i];
            if (confidence > threshhold) {
                pq.add(new Recognition("" + i, labellist.size() > i ? labellist.get(i) : "unknown", confidence));
            }
        }


        final ArrayList<Recognition> recognitions = new ArrayList<>();
        int recognitionsize= (int) Math.min(pq.size(), maxresults);
        for(int i= 0; i<recognitionsize; ++i){
            recognitions.add(pq.poll());
        }
        return  recognitions;
    }
}