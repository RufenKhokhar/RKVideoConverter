package com.rkgroup.rkvideoconverter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rkgroup.videoconverter.AudioInfo;
import com.rkgroup.videoconverter.FFMPEGMediaObject;
import com.rkgroup.videoconverter.FileManager;
import com.rkgroup.videoconverter.MediaInfo;
import com.rkgroup.videoconverter.VideoConverter;
import com.rkgroup.videoconverter.VideoInfo;
import com.rkgroup.videoconverter.listeners.EncoderProgressListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final int FILE_PICK_CODE = 142;
    private static final String TAG = "MainActivity";
    private File rootDirectory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VideoConverter.initialize(this, status -> Toast.makeText(this, String.valueOf(status), Toast.LENGTH_SHORT).show());
        rootDirectory = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        Button button = findViewById(R.id.btn_fab);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            //intent.setType("audio/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, FILE_PICK_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FILE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String lastPathSegment = uri.getLastPathSegment();
                int indexOf = lastPathSegment.indexOf(".");
                // String inputFileFormat = lastPathSegment.substring(indexOf);
                File inputFile = new File(getCacheDir(), "inputFile".concat(".mp4"));
                try {
                    FileManager.inputStreamToFile(getContentResolver().openInputStream(uri), inputFile);
                    FFMPEGMediaObject mediaObject = new FFMPEGMediaObject(MainActivity.this, inputFile);
                    MediaInfo info = mediaObject.getInfo();
                    VideoInfo videoInfo = info.getVideo();
                    AudioInfo audioInfo = info.getAudio();
                    String format = info.getFormat();
                    Log.d(TAG, "onActivityResult: videoInfo: ".concat(videoInfo.toString()));
                    Log.d(TAG, "onActivityResult: audioInfo: " + audioInfo.toString());
                    Log.d(TAG, "onActivityResult: inputFileFormat: " + format);
                    convertVideo(inputFile);
                } catch (Exception e) {
                    Log.e(TAG, "onActivityResult: ", e);
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void convertVideo(File inputFile) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Please Wait...");
        dialog.setMessage("converting...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        File outputFile = new File(rootDirectory, "outputFile.mp3");
        new VideoConverter.Builder(this)
                .setOutputFormat("mp3")
                .setFastStart(true)
                .setEncodingThreads(15)
                .setDecodingThreads(15)
                .build().convertVideo(inputFile, outputFile, new EncoderProgressListener() {
            @Override
            public void onStartEncoding(MediaInfo info) {
                dialog.show();
            }

            @Override
            public void onUpdateProgress(int progress) {
                dialog.setProgress(progress);

            }

            @Override
            public void onSendMassage(String message) {
                Log.d(TAG, "onSendMassage: ".concat(message));

            }

            @Override
            public void onCompleteEncoding(int completionCode) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d(TAG, "onCompleteEncoding: ".concat(String.valueOf(completionCode)));

            }

            @Override
            public void onReceivedError(Exception e) {
                Log.e(TAG, "onReceivedError: ", e);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });
    }
}