package com.rkgroup.videoconverter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileManager {
    public static final String FFMPEG_BINARY_FILE = "ffmpeg_binary_file";
    private static final String TAG = "FileManager";

    @NonNull
    public static File getFFmpeg(@NonNull Context context) {
        File folder = context.getFilesDir();
        return new File(folder, FFMPEG_BINARY_FILE);
    }

    public static boolean hasFFmpeg(@NonNull Context context) {
        File folder = context.getFilesDir();
        return new File(folder, FFMPEG_BINARY_FILE).exists();
    }

    public static boolean inputStreamToFile(InputStream stream, File file) {
        try {
            InputStream input = new BufferedInputStream(stream);
            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
            output.close();
            input.close();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "inputStreamToFile: error while writing ff binary file", e);

        }
        return false;
    }

    @NonNull
    private static File getAppStorage() {
        String packageName = BuildConfig.LIBRARY_PACKAGE_NAME;
        File storageDirectory = Environment.getExternalStorageDirectory();
        File appPath = new File(storageDirectory, packageName.substring(packageName.lastIndexOf(".") + 1));
        if (!appPath.exists()) {
            appPath.mkdir();
        }
        return appPath;
    }

    @NonNull
    public static List<File> getFilesFromAppStorage() {
        List<File> fileList = new ArrayList<>();
        File[] files = getAppStorage().listFiles();
        if (files != null) {
            Collections.addAll(fileList, files);
        }
        return fileList;

    }
}
