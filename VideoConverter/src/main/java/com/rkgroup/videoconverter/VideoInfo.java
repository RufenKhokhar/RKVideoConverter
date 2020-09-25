package com.rkgroup.videoconverter;

import androidx.annotation.NonNull;

/**
 * Instances of this class report informations about a video stream that can be
 * decoded.
 *
 * @author Rufen Khokhar
 */

public class VideoInfo {


    /**
     * The video stream decoder name.
     */
    private String decoder;

    /**
     * The video size. If null this information is not available.
     */
    private VideoSize size = null;

    /**
     * The video stream (average) bit rate. If less than 0, this information is
     * not available.
     */
    private int bitRate = -1;

    /**
     * The video frame rate. If less than 0 this information is not available.
     */
    private float frameRate = -1;

    /**
     * Returns the video stream decoder name.
     *
     * @return The video stream decoder name.
     */
    public String getDecoder() {
        return decoder;
    }

    /**
     * Sets the video stream decoder name.
     *
     * @param codec The video stream decoder name.
     * @return this instance
     */
    public VideoInfo setDecoder(String codec) {
        this.decoder = codec;
        return this;
    }

    /**
     * Returns the video size. If null this information is not available.
     *
     * @return the size The video size.
     */
    public VideoSize getSize() {
        return size;
    }

    /**
     * Sets the video size.
     *
     * @param size The video size.
     * @return this instance
     */
    public VideoInfo setSize(VideoSize size) {
        this.size = size;
        return this;
    }

    /**
     * Returns the video frame rate. If less than 0 this information is not
     * available.
     *
     * @return The video frame rate.
     */
    public float getFrameRate() {
        return frameRate;
    }

    /**
     * Sets the video frame rate.
     *
     * @param frameRate The video frame rate.
     * @return this instance
     */
    public VideoInfo setFrameRate(float frameRate) {
        this.frameRate = frameRate;
        return this;
    }

    /**
     * Returns the video stream (average) bit rate. If less than 0, this
     * information is not available.
     *
     * @return The video stream (average) bit rate.
     */
    public int getBitRate() {
        return bitRate;
    }

    /**
     * Sets the video stream (average) bit rate.
     *
     * @param bitRate The video stream (average) bit rate.
     * @return this instance
     */
    @NonNull
    public VideoInfo setBitRate(int bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName() + " (decoder=" + decoder + ", size=" + size
                + ", bitRate=" + bitRate + ", frameRate=" + frameRate + ")";
    }
}
