package com.rkgroup.videoconverter;

import androidx.annotation.NonNull;

/**
 * Instances of this class report information about videos size.
 *
 * @author Rufen Khokhar
 */
public class VideoSize {
    /**
     * The video height.
     */
    private int height;
    /**
     * The video width.
     */
    private int width;

    /**
     * It builds the bean.
     *
     * @param width  The video width.
     * @param height The video height.
     */
    public VideoSize(int width, int height) {
        this.height = height;
        this.width = width;
    }

    /**
     * Returns the video height.
     *
     * @return The video height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the video width.
     *
     * @return The video width.
     */
    public int getWidth() {
        return width;
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName() + " (width=" + width + ", height=" + height
                + ")";
    }
}
