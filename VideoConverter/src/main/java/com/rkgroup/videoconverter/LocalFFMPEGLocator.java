package com.rkgroup.videoconverter;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Used for to provide the Custom FFmeg binary file path
 *
 * @author Rufen Khokhar
 */
public class LocalFFMPEGLocator extends FFMPEGLocator {

    private String path;

    /**
     * @param mContext used for default ffmpeg file
     */
    public LocalFFMPEGLocator(@NonNull Context mContext) {
        this.path = FileManager.getFFmpeg(mContext).getAbsolutePath();
    }

    /**
     * @param path custom FFmpeg file path
     */
    public LocalFFMPEGLocator(String path) {
        this.path = path;
    }

    @Override
    protected String getFFMPEGExecutablePath() {
        return path;
    }
}
