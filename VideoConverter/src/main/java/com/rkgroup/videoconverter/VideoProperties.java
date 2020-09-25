package com.rkgroup.videoconverter;

import com.rkgroup.videoconverter.videofilters.VideoFilter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Properties controlling the video encoding process.
 *
 * @author Rufen Khokhar
 */

class VideoProperties implements Serializable {
    /**
     * This value can be setted in the codec field to perform a direct stream
     * copy, without re-encoding of the audio stream.
     */
    public static final String COPY_ORIGINAL_STREAM = "copy";
    private static final long serialVersionUID = 1L;
    private final ArrayList<VideoFilter> videoFilters = new ArrayList<>();
    /**
     * The codec name for the encoding process. If null or not specified the
     * encoder will perform a direct stream copy.
     */
    private String codec = null;
    /**
     * The the forced tag/fourcc value for the video stream.
     */
    private String tag = null;
    /**
     * The bitrate value for the encoding process. If null or not specified a
     * default value will be picked.
     */
    private Integer bitRate = null;
    /**
     * The frame rate value for the encoding process. If null or not specified a
     * default value will be picked.
     */
    private Integer frameRate = null;
    /**
     * The video size for the encoding process. If null or not specified the
     * source video size will not be modified.
     */
    private VideoSize size = null;
    /**
     * The audio quality value for the encoding process. If null or not specified
     * the ffmpeg default will be used
     */
    private Integer quality = null;
    /**
     * Encode the video with faststart mode, default OFF
     * <p>
     * <p>
     * The mov/mp4/ismv muxer supports fragmentation. Normally, a MOV/MP4 file
     * has all the metadata about all packets stored in one location (written at
     * the end of the file, it can be moved to the start for better playback by
     * adding faststart to the movflags, or using the qt-faststart tool). A
     * fragmented file consists of a number of fragments, where packets and
     * metadata about these packets are stored together. Writing a fragmented
     * file has the advantage that the file is decodable even if the writing is
     * interrupted (while a normal MOV/MP4 is undecodable if it is not properly
     * finished), and it requires less memory when writing very long files
     * (since writing normal MOV/MP4 files stores info about every single packet
     * in memory until the file is closed). The downside is that it is less
     * compatible with other applications.
     */
    private boolean faststart = false;
    private X264_PROFILE x264Profile = null;

    /**
     * Returns the codec name for the encoding process.
     *
     * @return The codec name for the encoding process.
     */
    String getCodec() {
        return codec;
    }


    /**
     * Sets the codec name for the encoding process. If null or not specified
     * the encoder will perform a direct stream copy.
     * <p>
     * Be sure the supplied codec name is in the list returned by
     * {@link Encoder#getVideoEncoders()}.
     * <p>
     * A special value can be picked from
     * {@link VideoProperties#COPY_ORIGINAL_STREAM}.
     *
     * @param codec The codec name for the encoding process.
     * @return this instance
     */
    public VideoProperties setCodec(String codec) {
        this.codec = codec;
        return this;
    }

    /**
     * Returns the the forced tag/fourcc value for the video stream.
     *
     * @return The the forced tag/fourcc value for the video stream.
     */
    String getTag() {
        return tag;
    }

    /**
     * Sets the forced tag/fourcc value for the video stream.
     *
     * @param tag The the forced tag/fourcc value for the video stream.
     * @return this instance
     */
    public VideoProperties setTag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * Returns the bitrate value for the encoding process.
     *
     * @return The bitrate value for the encoding process.
     */
    Integer getBitRate() {
        return bitRate;
    }

    /**
     * Sets the bitrate value for the encoding process. If null or not specified
     * a default value will be picked.
     *
     * @param bitRate The bitrate value for the encoding process.
     * @return this instance
     */
    public VideoProperties setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    /**
     * Returns the frame rate value for the encoding process.
     *
     * @return The frame rate value for the encoding process.
     */
    Integer getFrameRate() {
        return frameRate;
    }

    /**
     * Sets the frame rate value for the encoding process. If null or not
     * specified a default value will be picked.
     *
     * @param frameRate The frame rate value for the encoding process.
     * @return this instance
     */
    public VideoProperties setFrameRate(Integer frameRate) {
        this.frameRate = frameRate;
        return this;
    }

    /**
     * Returns the video size for the encoding process.
     *
     * @return The video size for the encoding process.
     */
    VideoSize getSize() {
        return size;
    }

    /**
     * Sets the video size for the encoding process. If null or not specified
     * the source video size will not be modified.
     *
     * @param size he video size for the encoding process.
     * @return this instance
     */
    public VideoProperties setSize(VideoSize size) {
        this.size = size;
        return this;
    }

    /**
     * @return the faststart
     */
    public boolean isFaststart() {
        return faststart;
    }

    /**
     * @param faststart the faststart to set
     * @return this instance
     */
    public VideoProperties setFastStart(boolean faststart) {
        this.faststart = faststart;
        return this;
    }

    public void addFilter(VideoFilter videoFilter) {
        this.videoFilters.add(videoFilter);
    }

    public ArrayList<VideoFilter> getVideoFilters() {
        return this.videoFilters;
    }

    /**
     * @return the quality
     */
    public Integer getQuality() {
        return quality;
    }

    /**
     * The video quality value for the encoding process. If null or not specified
     * the ffmpeg default will be used
     *
     * @param quality the quality to set
     * @return this instance
     */
    public VideoProperties setQuality(Integer quality) {
        this.quality = quality;
        return this;
    }

    @Override
    public String toString() {
        return getClass().getName() + "(codec=" + codec
                + ", bitRate=" + bitRate + ", frameRate=" + frameRate
                + ", size=" + size + ", faststart=" + faststart
                + ", quality=" + quality + ")";
    }

    /**
     * @return the x264Profile
     */
    public X264_PROFILE getX264Profile() {
        return x264Profile;
    }

    /**
     * @param x264Profile the x264Profile to set
     * @return this instance
     */
    public VideoProperties setX264Profile(X264_PROFILE x264Profile) {
        this.x264Profile = x264Profile;
        return this;
    }

    public enum X264_PROFILE {
        BASELINE("baseline"), MAIN("main"), HIGH("high"),
        HIGH10("high10"), HIGH422("high422"), HIGH444("high444");
        private final String modeName;

        X264_PROFILE(String modeName) {
            this.modeName = modeName;
        }

        public String getModeName() {
            return modeName;
        }
    }

}
