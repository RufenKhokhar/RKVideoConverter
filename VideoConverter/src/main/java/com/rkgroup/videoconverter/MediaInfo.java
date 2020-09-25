package com.rkgroup.videoconverter;

import androidx.annotation.NonNull;

/**
 * provides the input Media information.
 *
 * @author Rufen Khokhar
 */

public class MediaInfo {

    /**
     * The multimedia file format name.
     */
    private String format = null;

    /**
     * The stream duration in millis. If less than 0 this information is not
     * available.
     */
    private long duration = -1;

    /**
     * A set of audio-specific informations. If null, there's no audio stream in
     * the multimedia file.
     */
    private AudioInfo audio = null;

    /**
     * A set of video-specific informations. If null, there's no video stream in
     * the multimedia file.
     */
    private VideoInfo video = null;

    /**
     * Returns the multimedia file format name.
     *
     * @return The multimedia file format name.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the multimedia file format name.
     *
     * @param format The multimedia file format name.
     * @return this instance
     */
    public MediaInfo setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * Returns the stream duration in millis. If less than 0 this information is
     * not available.
     *
     * @return The stream duration in millis. If less than 0 this information is
     * not available.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Sets the stream duration in millis.
     *
     * @param duration The stream duration in millis.
     * @return this instance
     */
    public MediaInfo setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Returns a set of audio-specific informations. If null, there's no audio
     * stream in the multimedia file.
     *
     * @return A set of audio-specific informations.
     */
    public AudioInfo getAudio() {
        return audio;
    }

    /**
     * Sets a set of audio-specific informations.
     *
     * @param audio A set of audio-specific informations.
     * @return this instance
     */
    public MediaInfo setAudio(AudioInfo audio) {
        this.audio = audio;
        return this;
    }

    /**
     * Returns a set of video-specific informations. If null, there's no video
     * stream in the multimedia file.
     *
     * @return A set of audio-specific informations.
     */
    public VideoInfo getVideo() {
        return video;
    }

    /**
     * Sets a set of video-specific informations.
     *
     * @param video A set of video-specific informations.
     * @return this instance
     */
    public MediaInfo setVideo(VideoInfo video) {
        this.video = video;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getName() + " (format=" + format + ", duration="
                + duration + ", video=" + video + ", audio=" + audio + ")";
    }
}
